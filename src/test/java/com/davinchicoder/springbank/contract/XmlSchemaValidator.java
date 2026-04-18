package com.davinchicoder.springbank.contract;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

public class XmlSchemaValidator {

    private final Schema schema;

    public XmlSchemaValidator(String xsdPath) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            this.schema = factory.newSchema(
                    new StreamSource(
                            getClass().getClassLoader().getResourceAsStream(xsdPath)
                    )
            );
        } catch (Exception e) {
            throw new IllegalStateException("Error loading XSD", e);
        }
    }

    public void validate(String xmlPath) {
        try (InputStream xmlStream =
                     getClass().getClassLoader().getResourceAsStream(xmlPath)) {

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlStream));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
