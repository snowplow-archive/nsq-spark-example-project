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

from invoke import run, task

jar_file = "nsq-spark-example-project-0.1.0.jar"
executables_dir = "/home/vagrant/snowplow/bin"
config_path = "resources/simple_app.hocon"
nsq_topic = "nsq-spark-topic"
nsqd_tcp_address = "127.0.0.1:4150"

@task
def build_project():
  """
  build nsq-spark-example-project
  and package into "fat jar" ready for spark-submit
  """
  run("sbt assembly", pty=True)

@task
def run_project():
  """
  Submits the compiled "fat jar" to Apache Spark and
  starts Spark Streaming based on project settings
  """
  run("{}/spark-master/bin/spark-submit \
      --class app.SimpleApp \
      --master local[4] \
      target/scala-2.10/{} \
      --config {}".format(executables_dir, jar_file, config_path),
      pty=True)

@task
def send_to_spark():
  """
  Start the to_nsq application for sending
  data to nsq topic which spark is waiting
  input in
  """
  run("{}/to_nsq \
      --topic {} \
      --nsqd-tcp-address {} \
      --rate 1".format(executables_dir, nsq_topic, nsqd_tcp_address),
      pty=True)

@task
def send_to_spark_file(file_location):
  """
  Send content of the file to nsq topic
  which spark is waiting input in
  """
  run("cat {} | {}/to_nsq \
      --topic {} \
      --nsqd-tcp-address {} \
      --rate 1".format(file_location, executables_dir, nsq_topic, nsqd_tcp_address),
      pty=True)