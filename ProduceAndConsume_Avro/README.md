## Produce and Consume AVRO messages

In this step we will use Kafka client to produce and consume messages in avro format using the Avro Schema generated. 

We will use `avro-maven-plugin` to create java objects from the AVRO schema. `KafkaAvroSerializer` is used to serialize the objects and `KafkaAvroDeserializer` is used to deserialize the bytes into instances of the generated java objects.

Clone the project if you do not have it locally.

```sh
git clone https://github.com/lokeshallam/ISO20022-message-processing
```

Navigate to the project folder.

```sh
cd ProduceAndConsume_Avro
```

Copy the schema file generated to src/main/avro directory 

Run maven avro plugin to generate java class files.

```sh
mvn avro:schema
```


Prepare the required configuration file as below 

```
# Required connection configs for Kafka producer, consumer, and admin
    bootstrap.servers=<<BOOTSTRAP-URL>>
    security.protocol=SASL_SSL
    sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='<<kakfa-api-key>>' password='<<Kafka-api-secret>>';
    sasl.mechanism=PLAIN
    # Required for correctness in Apache Kafka clients prior to 2.6
    client.dns.lookup=use_all_dns_ips
    ?# Best practice for Kafka producer to prevent data loss
    acks=all
    ?# Required connection configs for Confluent Cloud Schema Registry
    schema.registry.url=<<SchemaRegistry-URL>>
    basic.auth.credentials.source=USER_INFO
    basic.auth.user.info=<<Schema-Registry-api-key>>:<<Schema-Registry-api-secret>>
```


Build the jar file using command

```sh
mvn clean compile package
```

Run the following command to execute the producer application, which will produce random events to the topic.

```java
java -cp target/produce-consume-avro-1.0.jar com.demo.examples.AvroProducer src/main/resources/cloud-config.properties
```



And consume events by running following command


```java
java -cp target/produce-consume-avro-1.0.jar com.demo.examples.AvroConsumer src/main/resources/cloud-config.properties
```
