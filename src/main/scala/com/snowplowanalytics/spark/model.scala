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

import com.typesafe.config.Config

package model {

  case class NsqConfig(
    inTopicName: String,
    channelName: String,
    host: String,
    lookupPort: Int,
    port: Int
  )

  case class SimpleAppConfig(conf: Config) {
    val nsqConf = conf.getConfig("nsq")
    val nsq = NsqConfig(nsqConf.getString("inTopicName"),
                        nsqConf.getString("channelName"),
                        nsqConf.getString("host"),
                        nsqConf.getInt("lookupPort"),
                        nsqConf.getInt("port")
                       )
    val appName = conf.getString("appName")
    val batchDuration = conf.getLong("batchDuration")
    val outputFile = conf.getString("outputFile")
  }
}
