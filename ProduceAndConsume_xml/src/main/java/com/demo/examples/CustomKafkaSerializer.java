package com.demo.examples;

import java.io.IOException;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


import org.apache.avro.Schema;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

public class CustomKafkaSerializer extends AbstractCustomKafkaAvroSerializer implements Serializer<Object> {

    /**
     * Constructor used by Kafka producer.
     */
    public CustomKafkaSerializer() {

    }

    public CustomKafkaSerializer(SchemaRegistryClient client) {
        this.schemaRegistry = client;
        this.ticker = ticker(client);
    }

    public CustomKafkaSerializer(SchemaRegistryClient client, Map<String, ?> props) {
        this.schemaRegistry = client;
        this.ticker = ticker(client);
        configure(serializerConfig(props));
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.isKey = isKey;
        configure(new KafkaAvroSerializerConfig(configs));
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        return this.serialize(topic, null, data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object record) {
        if (record == null) {
            return null;
        }

        // Use jackson extension along with JAXB introspector to generate avro schema 
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(
                new JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance(), false));
        AvroSchemaGenerator generator = new AvroSchemaGenerator();
        generator.enableLogicalTypes();
        try {
            mapper.acceptJsonFormatVisitor(record.getClass(), generator);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Exception while mapping object", e);
        }
        com.fasterxml.jackson.dataformat.avro.AvroSchema schemas = generator.getGeneratedSchema();
        Schema schemaavro = schemas.getAvroSchema();
/*
        Path path = Path.of("../remittanceAdvice.avsc");

        try {
            Files.writeString(path, schemaavro.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 */

        AvroSchema avroSchema = new AvroSchema(schemaavro);

        return serializeImpl(
                getSubjectName(topic, isKey, record, avroSchema), topic, headers, record, avroSchema);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException e) {
            throw new RuntimeException("Exception while closing serializer", e);
        }
    }

}
