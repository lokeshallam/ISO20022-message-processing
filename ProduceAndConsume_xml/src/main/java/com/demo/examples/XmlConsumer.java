package com.demo.examples;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.xml.sax.SAXException;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import jakarta.xml.bind.JAXBException;

public class XmlConsumer {

    public static void main(final String[] args) throws IOException, JAXBException, SAXException {

        if (args.length != 1) {
            System.out.println("Please provide the configuration file path as a command line argument");
            System.exit(1);
        }

        final Properties props = XmlProducer.loadConfig(args[0]);
        final String topic = props.getProperty("input.topic.name");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payments-consumer-xml");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        try (final KafkaConsumer<String, event.iso.std.iso._20022.tech.xsd.remt_001_001.Document> consumer = new KafkaConsumer<>(
                props)) {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                final ConsumerRecords<String, event.iso.std.iso._20022.tech.xsd.remt_001_001.Document> records = consumer
                        .poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, event.iso.std.iso._20022.tech.xsd.remt_001_001.Document> record : records) {
                    final String key = record.key();

                    GenericRecord genericRecord = record.value();

                    final event.iso.std.iso._20022.tech.xsd.remt_001_001.Document value = (event.iso.std.iso._20022.tech.xsd.remt_001_001.Document) SpecificData
                            .get()
                            .deepCopy(event.iso.std.iso._20022.tech.xsd.remt_001_001.Document.SCHEMA$, genericRecord);
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }
        }
    }
}
