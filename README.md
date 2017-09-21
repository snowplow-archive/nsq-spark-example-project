# NSQ Spark Example Project

## Introduction
This example project aims to giving an example how to integrate [NSQ][nsq] with [Spark][spark].

## Building
Assuming git, [Vagrant][vagrant-install] and [VirtualBox][virtualbox-install] installed:
```bash
 host> git clone https://github.com/snowplow/nsq-spark-example-project
 host> cd nsq-spark-example-project
 host> vagrant up && vagrant ssh
guest> cd /vagrant
guest> inv build_project
```
The 'fat jar' is now available as:
```
target/scala-2.10/nsq-spark-example-project-0.1.0.jar
```

## Starting the simple word count Spark job
You can start the word count Spark job with following command:
```
inv run_project
```
After run the command, you should see the outputs of the Spark.
You should see these lines a few lines below from the beginning of the output:
```
Created connection: nsq-spark:4150 - Connection.<init>
IdentifyResponse: ... - NSQFeatureDetectionHandler.channelRead0
11:38:02.856 INFO  reinstall LengthFieldBasedFrameDecoder - NSQFeatureDetectionHandler.eject
Server identification: ... - Connection.<init>
```
If you could see these lines, this means that Spark job is started correctly and communicating with NSQ topic successfully.

## Sending text to Spark job throuh NSQ
You can send text to NSQ topic which Spark job is waiting input. This can be done with two way:
1. Entering input manually
    ```
    inv send_to_spark
    ```
    After call this command, you will see a command line application which waiting input. You can enter some text to here and this text will be sent to the NSQ topic and Spark job will read text from NSQ topic.

2. Sending text file
    ```
    inv send_to_spark_file /file/path
    ```
    You can send content of the file to NSQ topic with this command.

3. If everything have worked correctly so far, you can see the outputs of the word counts of text which you sent, with `inv see_output` command.


## Copyright and license
Copyright 2017 Snowplow Analytics Ltd.

Licensed under the [Apache License, Version 2.0][license]; you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

[vagrant-install]: https://www.vagrantup.com/docs/installation/index.html
[virtualbox-install]: https://www.virtualbox.org/wiki/Downloads

[nsq]: nsq.io
[spark]: https://spark.apache.org/

[license]: http://www.apache.org/licenses/LICENSE-2.0
