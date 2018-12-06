package com.sinovatech.search.services.impls;

import java.util.LinkedList;
import java.util.Queue;

import com.sinovatech.search.services.CallwsService;

public class CallwsServiceImpl implements CallwsService {

	@Override
	public String getXml() {
		String xml = "<infoDate><opt>add</opt><totalNo>1</totalNo><appcode>sc002</appcode><commandcode>sc001-on1</commandcode><list><fileInfo><fieldName>PRODUCT_ID</fieldName><value>4</value></fileInfo><fileInfo><fieldName>NAME</fieldName><value>4</value></fileInfo><fileInfo><fieldName>SPECIFICATION</fieldName><value>4</value></fileInfo><fileInfo><fieldName>COLOR</fieldName><value>4</value></fileInfo><fileInfo><fieldName>DESCS</fieldName><value>4</value></fileInfo><fileInfo><fieldName>MALL_PRICE</fieldName><value>4</value></fileInfo></list><list><fileInfo><fieldName>PRODUCT_ID</fieldName><value>4</value></fileInfo><fileInfo><fieldName>NAME</fieldName><value>4</value></fileInfo><fileInfo><fieldName>SPECIFICATION</fieldName><value>4</value></fileInfo><fileInfo><fieldName>COLOR</fieldName><value>4</value></fileInfo><fileInfo><fieldName>DESCS</fieldName><value>4</value></fileInfo><fileInfo><fieldName>MALL_PRICE</fieldName><value>4</value></fileInfo></list></infoDate>";
		return xml;
	}

    @Override
    public String getXml(int pageNo) {
        return  getInfoDate(pageNo);
    }

	public String getInfoDate(int pageNo){
	    String xml = "<infoDate><opt>add</opt><totalNo>100</totalNo><appcode>fuwu</appcode><commandcode>fw</commandcode>";
	    int z = 0;
	    if(pageNo==1){
	        z=1;
	    }else{
	        z = (pageNo - 1) * 10 + 1;
	    }
	    for(int i=z;i<=pageNo*10;i++){
	        xml += "<list><fileInfo><fieldName>PRODUCT_ID</fieldName><value>"+i+"</value></fileInfo>"
	                        +"<fileInfo><fieldName>NAME</fieldName><value>"+i+"</value></fileInfo>"
	                        +"<fileInfo><fieldName>SPECIFICATION</fieldName><value>"+i+"</value></fileInfo>"
	                        +"<fileInfo><fieldName>COLOR</fieldName><value>"+i+"</value></fileInfo>"
	                        +"<fileInfo><fieldName>DESCS</fieldName><value>"+i+"</value></fileInfo>"
	                        +"<fileInfo><fieldName>MALL_PRICE</fieldName><value>"+i+"</value></fileInfo></list>";
	    }
	    xml += "</infoDate>";
	    return xml;
	}
	
}
