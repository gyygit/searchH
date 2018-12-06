/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sinovatech.search.utils.encode;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.Assert;

import com.sinovatech.search.utils.reflection.Reflections;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

/**
 * 使用Jaxb2.0实现XML<->Java Object的Mapper.
 * 
 * 在创建时需要设定所有需要序列化的Root对象的Class. 特别支持Root对象是Collection的情形.
 * 
 * @author elvman
 */
public class JaxbBinder
{

	private static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

	/**
	 * Java Object->Xml without encoding.
	 */
	public static String toXml(Object root)
	{
		Class clazz = Reflections.getUserClass(root);
		return toXml(root, clazz, null);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, String encoding)
	{
		Class clazz = Reflections.getUserClass(root);
		return toXml(root, clazz, encoding);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, Class clazz, String encoding)
	{
		try
		{
			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e)
		{
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
	 */
	public static String toXml(Collection<?> root, String rootName, Class clazz)
	{
		return toXml(root, rootName, clazz, null);
	}

	/**
	 * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
	 */
	public static String toXml(Collection<?> root, String rootName,
	        Class clazz, String encoding)
	{
		try
		{
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
			        new QName(rootName), CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e)
		{
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * Xml->Java Object.
	 */
	public static <T> T fromXml(String xml, Class<T> clazz)
	{
		try
		{
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(clazz).unmarshal(reader);
		} catch (JAXBException e)
		{
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 创建Marshaller, 设定encoding(可为Null), 设定declaration(可为Null).
	 */
	public Marshaller createMarshaller(Class clazz, String encoding,
	        String declaration, Boolean formatted)
	{
		JAXBContext jaxbContext = getJaxbContext(clazz);
		try
		{
			Marshaller marshaller = jaxbContext.createMarshaller();

			// 是否格式化XML
			if (formatted)
			{
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				        Boolean.TRUE);
			}

			// 设置编码方式
			if (null != encoding && !"".equals(encoding))
			{
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}

			// 设置XML声明
			if (null != declaration && !"".equals(declaration))
			{
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration",
				        Boolean.FALSE);
			}
			return marshaller;
		} catch (JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建Marshaller并设定encoding(可为null). 线程不安全，需要每次创建或pooling。
	 */
	public static Marshaller createMarshaller(Class clazz, String encoding)
	{
		try
		{
			JAXBContext jaxbContext = getJaxbContext(clazz);

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			        Boolean.TRUE);
			// marshaller.setProperty("com.sun.xml.bind.xmlDeclaration",
			// Boolean.FALSE);
			// marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			if (StringUtils.isNotBlank(encoding))
			{
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}
			marshaller.setProperty(CharacterEscapeHandler.class.getName(),
			        new CharacterEscapeHandler()
			        {
				        @Override
				        public void escape(char[] ac, int i, int j,
				                boolean flag, Writer writer) throws IOException
				        {
					        writer.write(ac, i, j);
				        }
			        });
			return marshaller;
		} catch (JAXBException e)
		{
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 创建UnMarshaller. 线程不安全，需要每次创建或pooling。
	 */
	public static Unmarshaller createUnmarshaller(Class clazz)
	{
		try
		{
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e)
		{
			throw Exceptions.unchecked(e);
		}
	}

	protected static JAXBContext getJaxbContext(Class clazz)
	{
		Assert.notNull(clazz, "'clazz' must not be null");
		JAXBContext jaxbContext = jaxbContexts.get(clazz);
		if (jaxbContext == null)
		{
			try
			{
				jaxbContext = JAXBContext.newInstance(clazz,
				        CollectionWrapper.class);
				jaxbContexts.putIfAbsent(clazz, jaxbContext);
			} catch (JAXBException ex)
			{
				throw new HttpMessageConversionException(
				        "Could not instantiate JAXBContext for class [" + clazz
				                + "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}

	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper
	{

		@XmlAnyElement
		protected Collection<?> collection;
	}

	public static void main(String[] args)
	{
		// JaxbBinder jaxb1 = new JaxbBinder();
		// UniBSS bss = new UniBSS();
		// bss.setActionCode("0");
		// bss.setActionRelation("0");
		// bss.setActivityCode("T2000101");
		// bss.setbIPCode("BIP2A001");
		// bss.setbIPVer("0100");
		// bss.setHomeDomain("UCRM");
		// bss.setMsgReceiver("1101");
		// bss.setMsgSender("1100");
		// bss.setOrigDomain("ECIP");
		// bss.setProcessTime("20120330134708");
		// bss.setProcID("0520120330134708280");
		// Routing routing = new Routing();
		// routing.setRouteType("01");
		// routing.setRouteValue("15652837840");
		// bss.setRouting(routing);
		// SPReserve spReserve = new SPReserve();
		// spReserve.setCutOffDay("20120330");
		// spReserve.sethSNDUNS("0002");
		// spReserve.setoSNDUNS("1100");
		// spReserve.setTransIDC("ECIP00020520120330134708280");
		// bss.setsPReserve(spReserve);
		// PackageChangeReq req = new PackageChangeReq();
		// req.setOperCode("1");
		// req.setPackageCode("028527");
		// req.setProcTime("20120330134708280");
		// req.setUserNumber("15652837840");
		// String svnContent = JaxbBinder.toXml(req, "UTF-8");
		// // System.out.println(svnContent);
		// // OutputFormat of = new OutputFormat();
		// // of.setCDataElements(new String[] { svnContent });
		// //
		// bss.setSvcCont("<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><PackageChangeReq><UserNumber>15652837840</UserNumber><PackageCode>028527</PackageCode><OperCode>1</OperCode><ProcTime>20120330134708280</ProcTime></PackageChangeReq>]]>");
		//
		// // String st =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PackageChangeReq><UserNumber>15652837840</UserNumber><PackageCode>028527</PackageCode><OperCode>1</OperCode><ProcTime>20120330134708280</ProcTime></PackageChangeReq>";
		// // PackageChangeReq req1 = JaxbBinder.fromXml(st,
		// PackageChangeReq.class);
		// // System.out.println(req1.getOperCode());
		// // System.out.println(req1.getPackageCode());
		// // System.out.println(req1.getProcTime());
		// // System.out.println(req1.getUserNumber());
		//
		// bss.setSvcCont(svnContent);
		// bss.setSvcContVer("0100");
		// bss.setTestFlag("1");
		// bss.setTransIDH("");
		// bss.setTransIDO("0520120330134708280");
		// System.out.println(JaxbBinder.toXml(bss, "UTF-8"));

		// String str =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><UniBss><actionCode>0</actionCode><actionRelation>0</actionRelation><activityCode>T2000101</activityCode><homeDomain>UCRM</homeDomain><msgReceiver>1101</msgReceiver><msgSender>1100</msgSender><OrigDomain>ECIP</OrigDomain><procID>0520120330134708280</procID><processTime>20120330134708</processTime><routing><routeType>01</routeType><routeValue>15652837840</routeValue></routing><svcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><packageChangeReq><operCode>1</operCode><packageCode>028527</packageCode><procTime>20120330134708280</procTime><userNumber>15652837840</userNumber></packageChangeReq>]]></svcCont><svcContVer>0100</svcContVer><testFlag>1</testFlag><transIDH></transIDH><transIDO>0520120330134708280</transIDO><bIPCode>BIP2A001</bIPCode><bIPVer>0100</bIPVer><sPReserve><cutOffDay>20120330</cutOffDay><transIDC>ECIP00020520120330134708280</transIDC><hSNDUNS>0002</hSNDUNS><oSNDUNS>1100</oSNDUNS></sPReserve></UniBss>";
		//
		//
		// //String
		// packstr="<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><packageChangeReq><operCode>1</operCode><packageCode>028527</packageCode><procTime>20120330134708280</procTime><userNumber>13011255555</userNumber></packageChangeReq>]]>";
		// UniBSS bss = JaxbBinder.fromXml(str, UniBSS.class);
		// System.out.println(bss.getbIPCode());
		// System.out.println(bss.getMsgReceiver());
		// System.out.println(bss.getHomeDomain());
		// System.out.println(bss.getActionCode());
		// String content = bss.getSvcCont();
		// System.out.println(content);
		// PackageChangeReq req1 = JaxbBinder.fromXml(content,
		// PackageChangeReq.class);
		// System.out.println(req1.getUserNumber());
		//
		// System.out.println(bss.getRouting().getRouteType());

//		Uniss uniss = new Uniss();
//		uniss.setReqbusicode("cu.query.pukcode");
//		uniss.setReqchannelcode("111000001");
//		uniss.setReqformat("");
//		uniss.setReqmock("");
//		uniss.setReqsign("");
//		uniss.setReqts("20130219092015390");
//
//		Eop_cipher eop_cipher = new Eop_cipher();
//		eop_cipher.setUsernumber("13011255558");
//		uniss.setEop_cipher(eop_cipher);
//		Pub_params pub_params = new Pub_params();
//		pub_params.setBusinesscode("11012000007");
//		pub_params.setChannelcode("111000001");
//		pub_params.setCitycode("994");
//		pub_params.setCustomid("3512092124552663");
//		pub_params.setEoptransid("678772023843204747014414636046");
//		pub_params.setNettype("01");
//		pub_params.setPaytype("2");
//		pub_params.setProvincecode("097");
//		pub_params.setTransid("1110000011101200000720130219092015015863353");
//		uniss.setPub_params(pub_params);
//		System.out.println(JaxbBinder.toXml(uniss));
//
//		String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Uniss><reqts>20130219092015390</reqts><reqmock>null</reqmock><eop_cipher><usernumber>13091799551</usernumber></eop_cipher><pub_params><citycode>994</citycode><eoptransid>678772023843204747014414636046</eoptransid><provincecode>097</provincecode><nettype>01</nettype><customid>3512092124552663</customid><businesscode>11012000007</businesscode><paytype>2</paytype><channelcode>111000001</channelcode><transid>1110000011101200000720130219092015015863353</transid></pub_params><reqformat>null</reqformat><reqchannelcode>111000001</reqchannelcode><reqsign>null</reqsign><reqbusicode>cu.query.pukcode</reqbusicode></Uniss>";
//
//		Uniss nss = JaxbBinder.fromXml(xml1, Uniss.class);
//		System.out.println("------------"+nss.getEop_cipher().getUsernumber());

	}
}
