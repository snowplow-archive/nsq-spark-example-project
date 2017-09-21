/**
 * Copyright (c) 2017 Snowplow Analytics Ltd.
 * All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache
 * License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the Apache License Version 2.0 for the specific language
 * governing permissions and limitations there under.
 */

package com.snowplowanalytics.spark

// Scala
import scala.io.Source

//Java
import java.nio.charset.StandardCharsets.UTF_8
import java.io.File

// NSQ
import com.snowplowanalytics.client.nsq.NSQProducer

// Specs2
import org.specs2.mutable.Specification

// Config
import com.typesafe.config.{Config, ConfigFactory}

// This project
import model._

class WordCountTest extends Specification {

  "A WordCount job" should {

    "count words correctly" in {

      var configStr = """appName: "simple-app"
                         |batchDuration: 5
                         |outputFile : /tmp/spark-output
                         |nsq {
                         | inTopicName: "nsq-spark-in"
                         | outTopicName: "nsq-spark-out"
                         | channelName = "nsq-spark-channel"
                         | host = "127.0.0.1"
                         | lookupPort = 4161
                         | port = 4150
                         | }""".stripMargin

      var config = ConfigFactory.parseString(configStr).resolve()
      val conf = SimpleAppConfig(config)

      val producer = new NSQProducer().addAddress(conf.nsq.host, conf.nsq.port).start()

      val input = "hack hack hack and hack"
      producer.produce(conf.nsq.inTopicName, input.getBytes(UTF_8))

      new File(conf.outputFile).delete()

      val ssc = WordCount.execute(
        master = Some("local[2]"),
        config = conf,
        jars = Nil
      )

      WordCount.awaitTerminationOrTimeout(ssc, 2*conf.batchDuration*1000)

      val actual = Source.fromFile(conf.outputFile).mkString
      actual must_== "(hack,4)\n(and,1)\n"
    }
  }
}