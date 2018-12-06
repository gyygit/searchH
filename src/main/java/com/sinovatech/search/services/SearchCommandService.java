
package com.sinovatech.search.services;

import java.util.List;

 

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.entity.SearchCommandDTO;


/**
 * SearchCommand业务数据类型表 Service接口
 * 
 * 创建: 2014-11-14 13:25:06<br />
 * @author  作者liuzhenquan
 */
public interface SearchCommandService {

/**
 * 获取所有数据
 */
public List<SearchCommandDTO> getAllDatas();
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
/**
 * from table  where code=? and url=?
 * @param page
 * @param obj
 * @return
 */
public void saveTX(SearchCommandDTO searchCommandDTO) throws AppException;
 
public void updateTX(SearchCommandDTO searchCommandDTO) throws AppException;
 
public void saveOrUpdateTX(SearchCommandDTO searchCommandDTO) throws AppException ;
 
public String deleteTX(String ids) throws AppException;
public void deleteSqlTX(String ids) throws AppException;
public void  deleteTX(List<SearchCommandDTO> lstDto) throws AppException ;
 
public SearchCommandDTO get(String id) throws AppException;
 
public List<SearchCommandDTO> listByIds(String ids) throws AppException;

public List<SearchCommandDTO> searchCommand(String appId);
public List<SearchCommandDTO> list(LimitInfo limit, SearchCommandDTO dto);
public String save(SearchCommandDTO searchCommandDTO) throws Exception;
public String  update(SearchCommandDTO searchCommandDTO) throws Exception;
public void  updateListTX(List<SearchCommandDTO> lstDto,String appcode) throws AppException ;
}

