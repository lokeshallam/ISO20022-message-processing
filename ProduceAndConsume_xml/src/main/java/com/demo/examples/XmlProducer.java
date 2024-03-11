package com.demo.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.demo.examples.CustomKafkaSerializer;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import iso.std.iso._20022.tech.xsd.remt_001_001.Document;
import iso.std.iso._20022.tech.xsd.remt_001_001.ObjectFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

public class XmlProducer {

  public static final Logger logger = LoggerFactory.getLogger(XmlProducer.class);

  public static void main(final String[] args) throws IOException, SAXException, JAXBException {

    Properties props = new Properties();

    String xmlFile = null;

    if (args.length != 2) {

      System.out.println("Please provide the configuration file path as a command line argument");
      System.exit(1);

    } else {
      props = loadConfig(args[0]);
      xmlFile = args[1];
    }

    final String topic = props.getProperty("input.topic.name");

    File initialFile = new File(xmlFile);

    JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
    Schema xmlSchema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
        .newSchema(XmlProducer.class.getResource("/remittanceAdvice.xsd"));

    XmlSerializer<JAXBElement<Document>> xmlSerializer = new XmlSerializer<>(jaxbContext, xmlSchema);

    InputStream remittanceAdviceStream = new FileInputStream(initialFile);

    JAXBElement<Document> JaxbDocument = xmlSerializer.readFromXml(remittanceAdviceStream);

    props.put(ProducerConfig.RETRIES_CONFIG, 0);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomKafkaSerializer.class);
    props.put(AbstractKafkaSchemaSerDeConfig.NORMALIZE_SCHEMAS, true);
    props.put(KafkaAvroSerializerConfig.AVRO_REFLECTION_ALLOW_NULL_CONFIG, true);
    props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true); // set to false as best practice in prod

    try (KafkaProducer<String, Document> producer = new KafkaProducer<>(props)) {

      UUID uuid = UUID.randomUUID();
      final String keyId = uuid.toString();

      final Document document = JaxbDocument.getValue();

      final ProducerRecord<String, Document> record = new ProducerRecord<>(topic, keyId,
          document);
      producer.send(record);
      Thread.sleep(1000L);

      producer.flush();
      System.out.printf(
          "Successfully produced message to a topic called %s%n", topic);

    } catch (final SerializationException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  // We'll reuse this function to load properties from the Consumer as well
  public static Properties loadConfig(final String configFile) throws IOException {
    if (!Files.exists(Paths.get(configFile))) {
      throw new IOException(configFile + " not found.");
    }
    final Properties cfg = new Properties();
    try (InputStream inputStream = new FileInputStream(configFile)) {
      cfg.load(inputStream);
    }
    return cfg;
  }

}
