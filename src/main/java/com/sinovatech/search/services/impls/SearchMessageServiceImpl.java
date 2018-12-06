package com.sinovatech.search.services.impls;

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
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.daos.SearchMessageDAO;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.TuiHelpDTO;
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
private SearchMessageDAO searchMessageDAO; 

@Autowired
private JdbcTemplate jdbcTemplate;


/**
 * 获取所有数据
 */
public List<SearchMessageDTO> getAllDatas() {
  return searchMessageDAO.getAll();
}
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
	List lst = searchMessageDAO.list(limit, sqlwhere);    
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
	List lst = searchMessageDAO.list(limit,hql,as); 
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
   List lst = searchMessageDAO.listForSql(limit,sql,as);    
    return lst; 
}
/**
 * from table  where code=? and url=?
 * @param page
 * @param obj
 * @return
 */
public Page  page(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
 {
 hql = " from SearchMessageDTO Z";
 }
 return searchMessageDAO.findPage(page, hql, objWhere);
}

public Page  pageForSql(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
  {
   hql = " from SEARCH_MESSAGE  Z";
  }
  return searchMessageDAO.findPageForSql(page, hql, objWhere);
}
@Override
public void saveTX(SearchMessageDTO searchMessageDTO) throws AppException {
  searchMessageDAO.save(searchMessageDTO);
}
@Override
public void updateTX(SearchMessageDTO searchMessageDTO) throws AppException {
 searchMessageDAO.update(searchMessageDTO);
}
@Override
public void saveOrUpdateTX(SearchMessageDTO searchMessageDTO) throws AppException {
  searchMessageDAO.saveOrUpdate(searchMessageDTO);
}
@Override
public void deleteTX(String ids) throws AppException {
	ids = ids.replaceAll("'", "");
    String[] id = ids.split(",");
    for (int i = 0; i < id.length; i++)
    {
      searchMessageDAO.delete(id[i]);
    }
}
@Override
public SearchMessageDTO get(String id) throws AppException {
   return searchMessageDAO.get(id);
}
@Override
public List listByIds(String ids) throws AppException {
  ids = "'" + ids.replaceAll(",", "','")+ "'";
  return searchMessageDAO.listByIds(ids);
}
/**
 * 解析xml
 * @param datexml
 */
public TuiHelpDTO readXml(String datexml) {
	Document document = null;
	try {
		document = DocumentHelper.parseText(datexml);
	} catch (DocumentException e) {
		log.error("readXml  document is eror",e);
		e.printStackTrace();
		return null;
	}
	TuiHelpDTO dto = new TuiHelpDTO();
	Element root = document.getRootElement();
	dto.setAppCode( root.element("appcode").getText() == null ?  "" : root.element("appcode").getText());
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

