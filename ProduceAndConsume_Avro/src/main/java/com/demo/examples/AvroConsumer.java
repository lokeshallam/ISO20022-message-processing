package com.demo.examples;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.demo.examples.msg.definitions.RemittanceAdviceV05;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;

public class AvroConsumer {

    public static void main(final String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Please provide the configuration file path as a command line argument");
            System.exit(1);
        }

        final Properties props = AvroProducer.loadConfig(args[0]);

        final String topic = props.getProperty("input.topic.name");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payments-consumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        try (final KafkaConsumer<String, RemittanceAdviceV05> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                final ConsumerRecords<String, RemittanceAdviceV05> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, RemittanceAdviceV05> record : records) {
                    final String key = record.key();
                    final RemittanceAdviceV05 value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }

        }
    }
}
