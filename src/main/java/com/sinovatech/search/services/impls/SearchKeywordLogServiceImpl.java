
package com.sinovatech.search.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.daos.SearchKeywordLogDAO;
import com.sinovatech.search.entity.SearchKeywordLogDTO;
import com.sinovatech.search.services.SearchKeywordLogService;

/**
 * SearchKeywordLog搜索日志表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:08<br />
 * @author  作者liuzhenquan
 */
public class SearchKeywordLogServiceImpl implements SearchKeywordLogService {
@Autowired
private SearchKeywordLogDAO searchKeywordLogDAO; 

@Autowired
private JdbcTemplate jdbcTemplate;


/**
 * 获取所有数据
 */
public List<SearchKeywordLogDTO> getAllDatas() {
  return searchKeywordLogDAO.getAll();
}
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
	List lst = searchKeywordLogDAO.list(limit, sqlwhere);    
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
	List lst = searchKeywordLogDAO.list(limit,hql,as); 
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
   List lst = searchKeywordLogDAO.listForSql(limit,sql,as);    
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
 hql = " from SearchKeywordLogDTO Z";
 }
 return searchKeywordLogDAO.findPage(page, hql, objWhere);
}

public Page  pageForSql(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
  {
   hql = " from SEARCH_KEYWORD_LOG  Z";
  }
  return searchKeywordLogDAO.findPageForSql(page, hql, objWhere);
}
@Override
public void saveTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
  searchKeywordLogDAO.save(searchKeywordLogDTO);
}
@Override
public void updateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
 searchKeywordLogDAO.update(searchKeywordLogDTO);
}
@Override
public void saveOrUpdateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
  searchKeywordLogDAO.saveOrUpdate(searchKeywordLogDTO);
}
@Override
public void deleteTX(String ids) throws AppException {
	ids = ids.replaceAll("'", "");
    String[] id = ids.split(",");
    for (int i = 0; i < id.length; i++)
    {
      searchKeywordLogDAO.delete(id[i]);
    }
}
@Override
public SearchKeywordLogDTO get(String id) throws AppException {
   return searchKeywordLogDAO.get(id);
}
@Override
public List listByIds(String ids) throws AppException {
  ids = "'" + ids.replaceAll(",", "','")+ "'";
  return searchKeywordLogDAO.listByIds(ids);
}


@Override
public List list(LimitInfo limit ,SearchKeywordLogDTO dto){
	StringBuffer sb = new StringBuffer();
	if(!dto.getAppCode().equals("")){
		sb.append(" and appCode  like '").append(dto.getAppCode()).append("%'");
	}
	if(!dto.getCommandCode().equals("")){
		sb.append(" and commandCode  like '").append(dto.getCommandCode()).append("%'");
	}
	if(dto.getSearchPinyin() != null && !dto.getSearchPinyin().equals("")){
		sb.append(" and searchPinyin = '").append(dto.getSearchPinyin()).append("'");
	}
	if(dto.getSearchPy() != null && !dto.getSearchPy().equals("")){
		sb.append(" and searchPy = '").append(dto.getSearchPy()).append("'");
	}
	return this.list(limit, sb.toString());
	
}
}