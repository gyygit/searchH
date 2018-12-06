
package com.sinovatech.search.services;
import java.util.List;
import java.util.Map;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.db.DataBaseManager;



/**
 * SearchApp注册应用业务表 Service接口
 * 
 * 创建: 2014-11-14 13:25:04<br />
 * @author  作者liuzhenquan
 */
public interface SearchAppService {

/**
 * 获取所有数据
 */
public List<SearchAppDTO> getAllDatas();
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere);
public List list(LimitInfo limit , SearchAppDTO dto,String usercode,String appcode);
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
 
 
public void saveTX(SearchAppDTO searchAppDTO) throws AppException;
 
public void updateTX(SearchAppDTO searchAppDTO) throws AppException;
 
public void saveOrUpdateTX(SearchAppDTO searchAppDTO) throws AppException ;
 
public void deleteTX(String ids) throws AppException;
public void deleteSqlTX(String ids) throws AppException;
 
public SearchAppDTO get(String id) throws AppException;
 
public List listByIds(String ids) throws AppException;
 
public List listByLimitForSql(LimitInfo limit, String sql, String where, String as) throws AppException ;
public List listByLimitForSqlRmap(LimitInfo limit, String sql, String where, String as,SearchCommandDTO searchCommandDTO,DataBaseManager   dataBaseManager,List<String> cname)
		throws AppException;
 
public String save(SearchAppDTO searchAppDTO) throws Exception; 

public List<SearchAppDTO> getSearchAppList();

public List<SearchAppDTO> getSearchAppDTO(String hql);
 
public void updateCacheByAppCode(String appCode);
public String update(SearchAppDTO searchAppDTO) throws Exception;
//public void editCache(SearchAppDTO searchAppDTO,
//		List<SearchCommandDTO> commandList, List<SearchRuleDateDTO> ruleDateList)throws AppException;
public String validStart(List<SearchCommandDTO> commandList,
		List<SearchRuleDateDTO> ruleDateList,
		String flag)throws AppException;
public void editCache(SearchAppDTO searchAppDTO,
		List<SearchCommandDTO> commandList, List<SearchRuleDateDTO> ruleDateList) throws AppException;
public Map<String, AppCacheDTO> getReopen(String appCode);
	 
public String[] readXmlCommandAndAppCode(String datexml);
public String retenRxml(String[] strs,String yuanyin,boolean isSu);
public  boolean  saveSearchMessageDTOTX(String datexml);
public String getAnalyzerTypeByAppCode(String appCode);
}
