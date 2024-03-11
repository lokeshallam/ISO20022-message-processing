package com.demo.examples;

import java.io.InputStream;

import javax.xml.validation.Schema;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlSerializer<T> {
    private final JAXBContext jaxbContext;
    private final Schema xmlSchema;

    public XmlSerializer(JAXBContext jaxbContext, Schema xmlSchema) {
        this.xmlSchema = xmlSchema;
        this.jaxbContext = jaxbContext;
    }

    private Unmarshaller createJaxbUnmarshaller() throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(xmlSchema);
        return unmarshaller;
    }

    @SuppressWarnings("unchecked")
    public T readFromXml(InputStream is) throws JAXBException {

        Unmarshaller jaxbUnmarshaller = createJaxbUnmarshaller();
        Object unmarshalledObj = jaxbUnmarshaller.unmarshal(is);
        return (T) unmarshalledObj;
    }

}
