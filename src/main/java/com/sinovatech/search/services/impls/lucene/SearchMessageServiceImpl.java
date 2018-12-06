package com.sinovatech.search.services.impls.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.TuiHelpDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SearchMessageDAO;
import com.sinovatech.search.services.SearchMessageService;

/**
 * SearchMessage推送信息表 Service实现类
 * 
 * 创建: 2014-12-18 14:06:04<br />
 * @author  作者liuzhenquan
 */
public class SearchMessageServiceImpl implements SearchMessageService {
	private static final Log log = LogFactory.getLog(SearchMessageServiceImpl.class);
@Autowired
private SearchMessageDAO searchMessageDAOL; 
 


/**
 * 获取所有数据
 */
public List<SearchMessageDTO> getAllDatas() {
  return searchMessageDAOL.getAllForT();
}
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
	Page page = new Page();
	page.setPageNum(limit.getPageNum());
	page.setRowDisplayed(limit.getRowDisplayed());
	page.setQueryStr(sqlwhere);
	List<SearchMessageDTO> lst = searchMessageDAOL.listForT(page);
	limit.setTotalNum(page.getTotalNum());
   return lst; 
}
/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List list(LimitInfo limit, String hql,String as) {
	List lst = this.list(limit,hql); 
    return lst; 
}

/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List  listForSql(
   LimitInfo limit, String sql,String as) {
	List lst = this.list(limit,sql);   
    return lst; 
}
 
@Override
public void saveTX(SearchMessageDTO searchMessageDTO) throws AppException {
  searchMessageDAOL.add(searchMessageDTO);
}
@Override
public void updateTX(SearchMessageDTO searchMessageDTO) throws AppException {
 searchMessageDAOL.update(searchMessageDTO);
}
@Override
public void saveOrUpdateTX(SearchMessageDTO searchMessageDTO) throws AppException {
  searchMessageDAOL.update(searchMessageDTO);
}
@Override
public void deleteTX(String ids) throws AppException {
	ids = ids.replaceAll("'", "");
    List<SearchMessageDTO> list = listByIds(ids);
    for(SearchMessageDTO dto : list){
    	searchMessageDAOL.del(dto);
	}
}
@Override
public SearchMessageDTO get(String id) throws AppException {
   return searchMessageDAOL.getById(id);
}
@Override
public List listByIds(String ids) throws AppException {
  ids = "'" + ids.replaceAll(",", "','")+ "'";
  return searchMessageDAOL.listByIds(ids);
}
/**
 * 解析xml
 * @param datexml
 */
public TuiHelpDTO readXml(String datexml) {
	Document document = null;
	try {
		log.info(datexml);
		document = DocumentHelper.parseText(datexml);
	} catch (DocumentException e) {
		log.error("readXml  document is eror",e);
		e.printStackTrace();
		return null;
	}
	TuiHelpDTO dto = new TuiHelpDTO();
	Element root = document.getRootElement();
	dto.setAppCode( root.element("appcode").getText() == null ?  "" : root.element("appcode").getText());
	if(root.element("totalNo") != null)
		dto.setTotalNo( root.element("totalNo").getText() == null ?  "" : root.element("totalNo").getText());
	log.info("opt=========================" + root.element("opt").getText());
	dto.setOpt(root.element("opt").getText() == null ?  "" : root.element("opt").getText());
	dto.setCommandCode(root.element("commandcode").getText() == null ?  "" : root.element("commandcode").getText());
	List list = new ArrayList();
	for (Iterator iter = root.elementIterator(); iter.hasNext();) {
		Element element = (Element) iter.next();
		Map map = new HashMap();
		for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
			Element element1 = (Element) iterInner.next();
			String fieldName = "", value = "";
			for (Iterator iterInner1 = element1.elementIterator(); iterInner1.hasNext();) {
				Element element2 = (Element) iterInner1.next();
				fieldName = element1.element("fieldName").getText();
				value = element1.element("value").getText();
			}
			map.put(fieldName, value);
		}
		if(map.size() > 0)
			list.add(map);
	}
	dto.setList(list);
	return dto;
}
public static void main(String[] args) {
	String xml = "<?xml version='1.0' encoding='UTF-8'?>	<infoDate>			   <opt>add</opt>				<appcode>ftp</appcode>				<commandcode>ftp</commandcode>				<list>				<fileInfo>				<fieldName>ID</fieldName				<value>4</value>   </fileInfo>				<fileInfo>				<fieldName>PNAME</fieldName>				<value>5</value>				</fileInfo>				<fileInfo>				<fieldName>CON</fieldName>	   <value>5</value>				</fileInfo>				<fileInfo>				<fieldName>SALENUM</fieldName>				<value>5</value>				</fileInfo>				<fileInfo>		   <fieldName>PINPAI</fieldName>				<value>5</value>				</fileInfo>				<fileInfo>				<fieldName>PRICE</fieldName>				<value>5</value>				</fileInfo></list>				<list>				<fileInfo>				<fieldName>ID</fieldName>		   <value>2</value>				</fileInfo>				<fileInfo>				<fieldName>PNAME</fieldName<value>5</value>				</fileInfo>				<fileInfo>				<fieldName>CON</fieldName> <value>5</value>				</fileInfo>				<fileInfo>				<fieldName>SALENUM</fieldName>				<value>5</value>				</fileInfo>				<fileInfo>		   <fieldName>PINPAI</fieldName>				<value>5</value>				</fileInfo>				<fileInfo>				<fieldName>PRICE</fieldName>				<value>5</value>				</fileInfo></list>				</infoDate>";
	readXml1(xml);
	
}
public static TuiHelpDTO readXml1(String datexml) {
	Document document = null;
	try {
		log.info(datexml);
		document = DocumentHelper.parseText(datexml);
	} catch (DocumentException e) {
		log.error("readXml  document is eror",e);
		e.printStackTrace();
		return null;
	}
	TuiHelpDTO dto = new TuiHelpDTO();
	Element root = document.getRootElement();
	dto.setAppCode( root.element("appcode").getText() == null ?  "" : root.element("appcode").getText());
	if(root.element("totalNo") != null)
		dto.setTotalNo( root.element("totalNo").getText() == null ?  "" : root.element("totalNo").getText());
	log.info("opt=========================" + root.element("opt").getText());
	dto.setOpt(root.element("opt").getText() == null ?  "" : root.element("opt").getText());
	dto.setCommandCode(root.element("commandcode").getText() == null ?  "" : root.element("commandcode").getText());
	List list = new ArrayList();
	for (Iterator iter = root.elementIterator(); iter.hasNext();) {
		Element element = (Element) iter.next();
		Map map = new HashMap();
		for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
			Element element1 = (Element) iterInner.next();
			String fieldName = "", value = "";
			for (Iterator iterInner1 = element1.elementIterator(); iterInner1.hasNext();) {
				Element element2 = (Element) iterInner1.next();
				fieldName = element1.element("fieldName").getText();
				value = element1.element("value").getText();
			}
			map.put(fieldName, value);
		}
		if(map.size() > 0)
			list.add(map);
	}
	dto.setList(list);
	return dto;
}
}

