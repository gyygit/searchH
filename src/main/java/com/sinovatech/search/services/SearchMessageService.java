package com.sinovatech.search.services;
import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.TuiHelpDTO;


/**
 * SearchMessage推送信息表 Service接口
 * 
 * 创建: 2014-12-18 14:06:04<br />
 * @author  作者liuzhenquan
 */
public interface SearchMessageService {

/**
 * 获取所有数据
 */
public List<SearchMessageDTO> getAllDatas();
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
 
public void saveTX(SearchMessageDTO searchMessageDTO) throws AppException;
 
public void updateTX(SearchMessageDTO searchMessageDTO) throws AppException;
 
public void saveOrUpdateTX(SearchMessageDTO searchMessageDTO) throws AppException ;
 
public void deleteTX(String ids) throws AppException;
 
public SearchMessageDTO get(String id) throws AppException;
 
public List listByIds(String ids) throws AppException;
/**
 * 解析xml
 * @param datexml
 */
public TuiHelpDTO readXml(String datexml) ;
}
