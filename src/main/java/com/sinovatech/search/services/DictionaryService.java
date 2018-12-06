
package com.sinovatech.search.services;

import java.util.List;

 

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.entity.DictionaryDTO;


/**
 * Dictionary数据字典子表 Service接口
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * @author  作者liuzhenquan
 */
public interface DictionaryService {

/**
 * 获取所有数据
 */
public List<DictionaryDTO> getAllDatas();
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
 
 
public void saveTX(DictionaryDTO dictionaryDTO) throws AppException;
 
public void updateTX(DictionaryDTO dictionaryDTO) throws AppException;
 
public void saveOrUpdateTX(DictionaryDTO dictionaryDTO) throws AppException ;
 
public void deleteTX(String ids) throws AppException;
 
public DictionaryDTO get(String id) throws AppException;
 
public List listByIds(String ids) throws AppException;
public List list(LimitInfo limit, DictionaryDTO dto);
public String save(DictionaryDTO dictionaryDTO) throws Exception;
public String update(DictionaryDTO dictionaryDTO)throws Exception;
}

