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

import sbt._

object Dependencies {
  object V {
    val sparkSql       = "2.2.0"
    val sparkStreaming = "2.2.0"
    val nsqClient      = "1.1.0-rc1"
    val scopt          = "3.7.0"
    val config         = "1.3.1"
    val pureconfig     = "0.8.0"
  }
  object Libraries {
    val sparkSql       = "org.apache.spark"      %% "spark-sql"            % V.sparkSql
    val sparkStreaming = "org.apache.spark"      %% "spark-streaming"      % V.sparkStreaming
    val nsqClient      = "com.snowplowanalytics" %  "nsq-java-client_2.10" % V.nsqClient
    val scopt          = "com.github.scopt"      %% "scopt"                % V.scopt
    val config         = "com.typesafe"          %  "config"               % V.config
    val pureconfig     = "com.github.pureconfig" %% "pureconfig"           % V.pureconfig
  }
}
