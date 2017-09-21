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

// NSQ
import com.snowplowanalytics.client.nsq.NSQConsumer
import com.snowplowanalytics.client.nsq.lookup.DefaultNSQLookup
import com.snowplowanalytics.client.nsq.NSQMessage
import com.snowplowanalytics.client.nsq.NSQConfig
import com.snowplowanalytics.client.nsq.callbacks.NSQMessageCallback
import com.snowplowanalytics.client.nsq.callbacks.NSQErrorCallback
import com.snowplowanalytics.client.nsq.exceptions.NSQException

// Java
import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket
import java.nio.charset.StandardCharsets

// Spark
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver

// This project
import model._

class NsqReceiver(nsqConfig: NsqConfig)
  extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2) with Logging {

  def onStart() {
    val errorCallback = new NSQErrorCallback {
      override def error(e: NSQException) =
        log.error(s"Exception while consuming topic $nsqConfig.inTopicName", e)
    }

    val nsqCallback = new  NSQMessageCallback {
      override def message(msg: NSQMessage): Unit = {
        val validMsg = new String(msg.getMessage)
        store(validMsg)
        msg.finished()
      }
    }

    val lookup = new DefaultNSQLookup
    // use NSQLookupd
    lookup.addLookupAddress(nsqConfig.host, nsqConfig.lookupPort)
    val consumer = new NSQConsumer(lookup,
                                   nsqConfig.inTopicName,
                                   nsqConfig.channelName,
                                   nsqCallback,
                                   new NSQConfig(),
                                   errorCallback)

    consumer.start()
  }

  def onStop() {}
}
