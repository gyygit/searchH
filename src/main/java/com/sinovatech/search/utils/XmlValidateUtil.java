package com.sinovatech.search.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;

/**
 * xml数据验证
 * 
 * @author duanws
 * 
 */
public class XmlValidateUtil {

	public static boolean validateXml(String xml, String xsd) {
		if (xml != null && !xml.equals("")) {
			try {
				String schemaLanguage = XMLConstants.W3C_XML_SCHEMA_NS_URI;
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance(schemaLanguage);
				Schema schema = schemaFactory.newSchema(new File(getBasePath()
						+ File.separator + "xsd" + File.separator + xsd));

				Validator validator = schema.newValidator();
				InputSource inputSource = new InputSource(
						new ByteArrayInputStream(xml.getBytes("UTF-8")));
				Source source = new SAXSource(inputSource);
				validator.validate(source);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public static String getBasePath() {
		URL url = XmlValidateUtil.class.getResource("XmlValidateUtil.class");
		String path = url.getPath();
		path = path.substring(0, path.lastIndexOf("classes"));
		return path + "classes";
	}
}
