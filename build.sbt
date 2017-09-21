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

lazy val root = project.in(file("."))
  .settings(
    name        := "nsq-spark-example-project",
    version     := "0.1.0",
    description := "Example project for NSQ-Spark integration"
  )
  .settings(BuildSettings.buildSettings)
  .settings(BuildSettings.sbtAssemblySettings)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.Libraries.sparkSql,
      Dependencies.Libraries.sparkStreaming,
      Dependencies.Libraries.nsqClient,
      Dependencies.Libraries.scopt,
      Dependencies.Libraries.config,
      Dependencies.Libraries.pureconfig,
      Dependencies.Libraries.specs2,
      Dependencies.Libraries.jacksonCore,
      Dependencies.Libraries.jacksonDatabind
    )
  )

shellPrompt := { _ => "nsq-spark-example-project> " }
