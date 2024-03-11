## xml-avro-schema-generator


We have used a combination of JAXB maven plugin to generate Java classes from XML Schema and Jackson dataformat xml extension along with JAXB annotation module to generate Avro Schema. 



To Generate Avro schema:


Clone the project if you do not have it locally.

```sh
git clone https://github.com/lokeshallam/ISO20022-message-processing
```

Navigate to schema generator Module.


```sh
cd xml-avro-schema-generator

```



> **Important**  
> Note on XSD schema and Java Type Bindings

The example xsd file has xs:any element to give flexibility to extend the schema. For this example we assumed a string element. 


    <xs:complexType name="SupplementaryDataEnvelope1">
        <xs:sequence>
            <xs:any namespace="##any" processContents="lax"/>
        </xs:sequence>
    </xs:complexType>

Replace with

    <xs:complexType name="SupplementaryDataEnvelope1">
        <xs:sequence>
            <xs:element name="supplEnvlpData" type="Max35Text"/>
        </xs:sequence>
    </xs:complexType>



Schema bindings are defined in `src/main/resources/xjb/global.xjb` to covert Date and Time elements to simplified String.
These bindings are used to covert XML schema datatypes to Java datatypes that are compatabile to Avro data types.

```xml
<jaxb:javaType name="java.lang.String" xmlType="xsd:dateTime" parseMethod="jakarta.xml.bind.DatatypeConverter.parseString" printMethod="jakarta.xml.bind.DatatypeConverter.printString" />
<jaxb:javaType name="java.lang.String" xmlType="xsd:date" parseMethod="jakarta.xml.bind.DatatypeConverter.parseString" printMethod="jakarta.xml.bind.DatatypeConverter.printString" />
<jaxb:javaType name="java.lang.String" xmlType="xsd:time" parseMethod="jakarta.xml.bind.DatatypeConverter.parseString" printMethod="jakarta.xml.bind.DatatypeConverter.printString" />            
<jaxb:javaType name="java.lang.String" xmlType="xsd:gYear" parseMethod="jakarta.xml.bind.DatatypeConverter.parseString" printMethod="jakarta.xml.bind.DatatypeConverter.printString" />     
```





Verify and update plugin configuration for `jaxb-maven-plugin` as needed.

```xml
<configuration>
    <debug>true</debug>
    <verbose>true</verbose>
    <bindingDirectory>src/main/resources/xjb</bindingDirectory>
    <bindingIncludes>
        <include>global.xjb</include>
    </bindingIncludes>  
    <sources>
        <source>src/main/resources/remittanceAdvice.xsd</source> 
    </sources>
    <debugMode>true</debugMode>
</configuration>
```

Compile the project to generate 
```sh
mvn clean package
```

Run the generated jar with argument as full path of the output file name to generate the avro schema file.

```java
java -jar target/xml-avro-schema-converter-1.0.jar ./remittanceAdvice.avsc
```

Review the generated avro schema file and register the schema in Schema Registry.


