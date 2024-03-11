package com.demo.remittance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector;

import iso.std.iso._20022.tech.xsd.remt_001_001.Document;
import jakarta.xml.bind.JAXBException;

public class XmlAvroSchemaConverter {

  public static void main(final String[] args) throws IOException, JAXBException, SAXException {
    if (args.length != 1) {
      System.out.println(
          "Please provide the file name and path to generate the avro schema file. eg: /targetlocation/myschemafile.avsc");
      System.exit(1);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.setAnnotationIntrospector(new JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance(), false));

    AvroSchemaGenerator avroSchemaGenerator = new AvroSchemaGenerator();
    avroSchemaGenerator.enableLogicalTypes();

    mapper.acceptJsonFormatVisitor(Document.class, avroSchemaGenerator);

    org.apache.avro.Schema avroSchema = avroSchemaGenerator.getGeneratedSchema().getAvroSchema();

    Path path = Paths.get(args[0]);

    Files.writeString(path, avroSchema.toString(), StandardOpenOption.CREATE);

  }

}
