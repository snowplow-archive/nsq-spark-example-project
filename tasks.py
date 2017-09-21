from invoke import run, task

jar_file = "nsq-spark-example-project-0.1.0.jar"
executables_dir = "/home/vagrant/snowplow/bin"
config_path = "resources/simple_app.hocon"
nsq_topic = "nsq-spark-in"
nsqd_tcp_address = "127.0.0.1:4150"
output_file = "/tmp/spark-output"

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
      --class com.snowplowanalytics.spark.WordCountJob \
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

@task
def see_output():
  """
  Open the output file with "tail"
  """
  run("tail -f {}".format(output_file), pty=True)
