
package com.sinovatech.search.services;

import java.util.List;

 

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;

import com.sinovatech.search.entity.SearchKeywordLogDTO;


/**
 * SearchKeywordLog搜索日志表 Service接口
 * 
 * 创建: 2014-11-14 13:25:08<br />
 * @author  作者liuzhenquan
 */
public interface SearchKeywordLogService {

/**
 * 获取所有数据
 */
public List<SearchKeywordLogDTO> getAllDatas();
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List<SearchKeywordLogDTO> list(LimitInfo limit,String sqlwhere);
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
public List list(LimitInfo limit , SearchKeywordLogDTO dto);
public List  listForSql(
		LimitInfo limit, String sql,String as);
 
 
public void saveTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException;
 
public void updateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException;
 
public void saveOrUpdateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException ;
 
public void deleteTX(String ids) throws AppException;
 
public SearchKeywordLogDTO get(String id) throws AppException;
 
public List listByIds(String ids) throws AppException;
}

