package com.sinovatech.search.utils.encode;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDATAAdapter extends XmlAdapter<String, String>
{

	@Override
	public String unmarshal(String v) throws Exception
	{
		// TODO Auto-generated method stub
		return v;

	}

	@Override
	public String marshal(String v) throws Exception
	{
		// TODO Auto-generated method stub
		return "<![CDATA[" + v + "]]>";
	}

//	public static void main(String[] args)
//	{
//		String str = "<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?><PackageChangeReq><UserNumber>15652837840</UserNumber><PackageCode>028527</PackageCode><OperCode>1</OperCode><ProcTime>20120330134708280</ProcTime></PackageChangeReq>]]>";
//		CDATAAdapter adapter = new CDATAAdapter();
//		String str1 = "";
//		try
//		{
//			str1 = adapter.unmarshal(str);
//		} catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(str1);
//	}
}
