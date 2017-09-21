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

package app

// Java
import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.io.File

// Spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

// Config
import com.typesafe.config.{Config, ConfigFactory}

// This project
import model._

object SimpleApp {
  def main(args: Array[String]) {

    case class FileConfig(config: File = new File("."))
    val parser = new scopt.OptionParser[FileConfig](generated.Settings.name) {
      head(generated.Settings.name, generated.Settings.version)
      opt[File]("config").required().valueName("<filename>")
        .action((f: File, c: FileConfig) => c.copy(f))
        .validate(f =>
          if (f.exists) success
          else failure(s"Configuration file $f does not exist")
        )
    }

    val conf = parser.parse(args, FileConfig()) match {
      case Some(c) => ConfigFactory.parseFile(c.config).resolve()
      case None    => ConfigFactory.empty()
    }

    if (conf.isEmpty()) {
      System.err.println("Empty configuration file")
      System.exit(1)
    }

    val config = SimpleAppConfig(conf)

    val sparkConf = new SparkConf().setAppName(config.appName)
    val ssc = new StreamingContext(sparkConf, Seconds(config.batchDuration))

    val lines = ssc.receiverStream(new NsqReceiver(config.nsq))
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
