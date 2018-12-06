
package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;


/**
 * SearchRuleDate索引数据配置表 Service接口
 * 
 * 创建: 2014-11-14 13:25:11<br />
 * @author  作者liuzhenquan
 */
public interface SearchRuleDateService {

/**
 * 获取所有数据
 */
public List<SearchRuleDateDTO> getAllDatas();
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere);
/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List list(LimitInfo limit, String hql,String as);

/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List  listForSql(
		LimitInfo limit, String sql,String as);
 
public void saveTX(SearchRuleDateDTO searchRuleDateDTO) throws AppException;
 
public void updateTX(SearchRuleDateDTO searchRuleDateDTO) throws AppException;
 
public void saveOrUpdateTX(SearchRuleDateDTO searchRuleDateDTO) throws AppException ;
 
public void deleteTX(String ids) throws AppException;
public void deleteSqlTX(String ids) throws AppException; 
public void deleteTX(List<SearchRuleDateDTO> lstDto ) throws AppException; 
public SearchRuleDateDTO get(String id) throws AppException;
 
public List<SearchRuleDateDTO> listByIds(String ids) throws AppException;
public List<SearchRuleDateDTO> searchRuleDate(String appId);

public List<SearchRuleDateDTO> searchRuleDateBySql(String sql);
public List<SearchRuleDateDTO> list(LimitInfo limit, SearchRuleDateDTO dto);
public String save(SearchRuleDateDTO searchRuleDateDTO) throws AppException;
public String update(SearchRuleDateDTO searchRuleDateDTO)  throws Exception ;
public String updateSyncSearchRule() throws Exception ;

public void  updateListTX(List<SearchRuleDateDTO> lstDto,String appcode) throws Exception ;
	 
}

