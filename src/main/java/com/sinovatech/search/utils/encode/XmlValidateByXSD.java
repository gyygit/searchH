package com.sinovatech.search.utils.encode;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;

public class XmlValidateByXSD

{

	public static void main(String[] args)

	{

		try

		{

			// JAXBContext jc = JAXBContext.newInstance(REQUEST.class);

			// Unmarshaller unmarshaller = jc.createUnmarshaller();

			String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;

			SchemaFactory schemaFactory = SchemaFactory
			        .newInstance(schemaLanguage);

			Schema schema = schemaFactory.newSchema(new File(
			        "D:/projects/realNameSystem/src/main/resources/xsd/customer.xsd"));

			// unmarshaller.setSchema(schema);

			Validator validator = schema.newValidator();

			InputSource inputSource = new InputSource(
			        new FileInputStream(
			                new File(
			                        "D:/projects/realNameSystem/src/main/resources/xsd/Customer.xml")));

			Source source = new SAXSource(inputSource);

			validator.validate(source);

			System.out.println("success");

		}

		catch (Exception e)

		{

			e.printStackTrace();

		}

	}

}