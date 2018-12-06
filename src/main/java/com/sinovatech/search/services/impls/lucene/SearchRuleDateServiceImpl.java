package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SearchAppDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchCommandDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchRuleDateDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchRuleDateDAO1;
import com.sinovatech.search.services.SearchRuleDateService;


/**
 * SearchRuleDate索引数据配置表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:11<br />
 * 
 * @author 作者liuzhenquan
 */
public class SearchRuleDateServiceImpl implements SearchRuleDateService {
	@Autowired
	private SearchAppDAO searchAppDAOL;

	@Autowired
	private SearchRuleDateDAO searchRuleDateDAOL;
	
	@Autowired
	private SearchCommandDAO searchCommandDAOL;
	
	@Autowired
	private SearchRuleDateDAO1 searchRuleDateDAOL1;
	
	/**
	 * 获取所有数据
	 */
	public List<SearchRuleDateDTO> getAllDatas() {
		
		return searchRuleDateDAOL.getAllForT();
	}

	/**
	 * 前台sql 不需要 “ where 1=1 ”
	 * 
	 * @param sqlwhere
	 *            直接是条件
	 * @param limit
	 * @param sqlwhere
	 * @return
	 */
	public List list(LimitInfo limit, String sqlwhere) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sqlwhere);
		page.setPageNum(limit.getPageNum());
		page.addSortList("createTime", Page.ORDER.DESC);
		List<SearchRuleDateDTO> lst = searchRuleDateDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	/**
	 * 前台sql必须加 “ where 1=1 ”
	 * 
	 * @param limit
	 * @param hql
	 * @param as
	 *            主表别名 可以不传
	 * @return
	 */
	public List list(LimitInfo limit, String hql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(hql);
		List<SearchRuleDateDTO> lst = searchRuleDateDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	/**
	 * 前台sql必须加 “ where 1=1 ”
	 * 
	 * @param limit
	 * @param hql
	 * @param as
	 *            主表别名 可以不传
	 * @return
	 */
	public List listForSql(LimitInfo limit, String sql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sql);
		List<SearchRuleDateDTO> lst = searchRuleDateDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	@Override
	public void saveTX(SearchRuleDateDTO searchRuleDateDTO) throws AppException {

		// 把对应的searchApp修改为“需要重启”
		SearchAppDTO app = searchAppDAOL.getById(searchRuleDateDTO.getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchRuleDateDTO.setAppCode(app.getAppCode());
		searchAppDAOL.update(app);
		searchRuleDateDAOL.add(searchRuleDateDTO);
	}

	@Override
	public void updateTX(SearchRuleDateDTO searchRuleDateDTO)
			throws AppException {
		//把对应的searchApp修改为“需要重启”
		//SearchAppDTO app = searchAppDAOL.getById(searchRuleDateDTO.getAppId());
		//app.setIsReset(RedisKeyConst.Search.RESET_YES);
		//searchAppDAOL.update(app);
		searchRuleDateDAOL.update(searchRuleDateDTO);
	}

	@Override
	public void saveOrUpdateTX(SearchRuleDateDTO searchRuleDateDTO)
			throws AppException {
		searchRuleDateDAOL.update(searchRuleDateDTO);
	}

	@Override
	public void deleteTX(List<SearchRuleDateDTO> lstDto ) throws AppException {
	 
		for (SearchRuleDateDTO searchRuleDateDTO:lstDto) {
			searchRuleDateDAOL.del(searchRuleDateDTO);
		}
	}
	@Override
	public void deleteTX(String ids) throws AppException {
		String[] id = ids.split(",");
		//把对应的app设置成需要重新启动
		SearchAppDTO app = searchAppDAOL.getById(searchRuleDateDAOL.getById(id[id.length-1]).getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDAOL.update(app);
		for (int i = 0; i < id.length; i++) {
			SearchRuleDateDTO dto = searchRuleDateDAOL.getById(id[i]);
			searchRuleDateDAOL.del(dto);
		}
	}

	@Override
	public SearchRuleDateDTO get(String id) throws AppException {
		return searchRuleDateDAOL.getById(id);
	}

	@Override
	public List listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		return searchRuleDateDAOL.listByIds(ids);
	}

	@Override
	public List<SearchRuleDateDTO> searchRuleDate(String appId) {
		LimitInfo limit = new LimitInfo();
		String hql = " (appId:" + appId + "~0)";
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List<SearchRuleDateDTO> searchRuleDateBySql(String hql) {
		LimitInfo limit = new LimitInfo();
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List list(LimitInfo limit, SearchRuleDateDTO dto) {
		StringBuffer sb = new StringBuffer();
		sb.append(" where (*:*) ");
		if (!dto.getCommandCode().equals("")) {//业务数据类型编码
			sb.append(" AND (commandCode:").append(dto.getCommandCode()).append("*)");
		}
		if (dto.getFieldIndexType() != null && !dto.getFieldIndexType().equals("")) {//字段索引类型
			sb.append(" AND (fieldIndexType:").append(dto.getFieldIndexType()).append("*)");
		}
		if (!dto.getFieldName().equals("")) {//字段名称
			sb.append(" AND (fieldName:").append(dto.getFieldName().toUpperCase()).append("*)");
		}
		if (dto.getFileldDateType() != null && !dto.getFileldDateType().equals("")) {//字段值类型
			sb.append(" AND (fileldDateType:").append(dto.getFileldDateType()).append("*)");
		}
		if (dto.getFieldStoreType() != null && !dto.getFieldStoreType().equals("")) {//字段值是否存储
			sb.append(" AND (fieldStoreType:").append(dto.getFieldStoreType()).append("*)");
		}
		if (dto.getIsIntelliSense() != null && !dto.getIsIntelliSense().equals("")) {//是否智能提示
			sb.append(" AND (isIntelliSense:").append(dto.getIsIntelliSense()).append("*)");
		}
		sb.append(" AND (appId:").append(dto.getAppId()).append("~0)");
	
		return this.list(limit, sb.toString());
	}
	private String validDate(SearchRuleDateDTO dto) {
		String flag  = "OK";
		if (dto.getFieldName().equals("")) {
			return flag = "字段名称不能为空!";
		} else if (dto.getFileldDateType().equals("")) {
			return flag = "请选择是否为主键!";
		} else if (dto.getFieldIndexType().equals("")) {
			return flag = "请选择字段索引类型!";
		} else if (dto.getFieldStoreType().equals("")) {
			return flag = "请选择字段值是否存储!";
		} else if (dto.getCommandCode().equals("")) {
			return flag = "业务数据类型编码不能为空!";
		}else if (dto.getFileldDateType().equals("on")) {
			return flag = "字段值类型不能为空!";
		}
		//通过appId获取AppCode   
		List<SearchRuleDateDTO> searchRuleDateDTO = searchRuleDate(dto.getAppId());
		if(searchRuleDateDTO!=null && searchRuleDateDTO.size()>0){
			dto.setAppCode(searchRuleDateDTO.get(0).getAppCode());
		}
		//一个应用下，只能有一个智能提示
//		if(dto.getIsIntelliSense().equals(RedisKeyConst.Search.IS_INTELLISENSE_TRUE)){
//			//当前对象为智能提示时，判断当前应用下时候还有智能提示的字段
//			String sql = " (isIntelliSense:" + RedisKeyConst.Search.IS_INTELLISENSE_TRUE +") AND (commandCode:" + dto.getCommandCode() +"~0) AND (appCode:" + dto.getAppCode() + "~0)" ;
//			if(!dto.getId().equals("")){
//				sql += " NOT (id:" + dto.getId() + ")";
//			}
//			List list = this.searchRuleDateBySql(sql);
//			if(list != null && list.size() > 0)
//				return flag = "选择的业务数据类型下已存在“智能提示”字段！";
//		}
//		//一个应用下，只能有一个是url选项
//		if(dto.getIsUrl().equals(RedisKeyConst.Search.IS_URL_TRUE)){
//			String sql = " (isUrl:" + RedisKeyConst.Search.IS_URL_TRUE +") AND (commandCode:" + dto.getCommandCode() +"~0) AND (appCode:" + dto.getAppCode() + "~0)" ;
//			if(!dto.getId().equals("")){
//				sql += " NOT (id:" + dto.getId() + ")";
//			}
//			List list = this.searchRuleDateBySql(sql);
//			if(list != null && list.size() > 0)
//				return flag = "选择的业务数据类型下已存在“是url选项”字段！";
//		}
		
		if(dto.getFileldDateType().equals(RedisKeyConst.Search.RULE_DATE_TYPE_PK)){
			//判断主键只能存在一个
			String sql =  " (fileldDateType:" + RedisKeyConst.Search.RULE_DATE_TYPE_PK + "~0) AND (commandCode:" + dto.getCommandCode() +"~0) AND (appId:" + dto.getAppId()+ "~0)";
			if(!dto.getId().equals("")){
				sql += " NOT (id:" + dto.getId() + "~0)";
			}
			List list = this.searchRuleDateBySql(sql);
			if(list != null && list.size() > 0)
				return flag = "索引数据配置模块中业务数据类型编码\"" + dto.getCommandCode() +"\"的索引数据主键配置规则不符合‘有且仅有一个’原则，无法启用当前应用，请查看！!";
		}
		//字段名字不能重复
//		String sql = " (fieldName:" + dto.getFieldName().toUpperCase() + "~0) AND (appId:" + dto.getAppId() +"~0) ";
		String sql = " (fieldName:" + dto.getFieldName().toUpperCase() + "~0) AND (appId:" + dto.getAppId() +"~0)  AND (commandCode:" + dto.getCommandCode() +"~0) ";
		if(!dto.getId().equals("")){
			sql += " NOT (id:" + dto.getId() + "~0)";
		}
		List list = this.searchRuleDateBySql(sql);
		if(list != null && list.size() > 0) flag = "字段名字\"" + dto.getFieldName() +"\"已经存在，请重新录入!";
		dto.setFieldName(dto.getFieldName().toUpperCase());
		return flag;
	}
	private void setCommandName(SearchRuleDateDTO searchRuleDateDTO) {
		Page page = new Page();
		page.setQueryStr(" (commandCode:"+searchRuleDateDTO.getCommandCode() + "~0) AND (appId:" + searchRuleDateDTO.getAppId()+ "~0)");
		List<SearchCommandDTO> list = this.searchCommandDAOL.listForT(page);
		if(list != null && list.size() > 0)
			searchRuleDateDTO.setCommandName(list.get(0).getCommandName());
	}

	@Override
	public String save(SearchRuleDateDTO searchRuleDateDTO) throws AppException {
		String flag = this.validDate(searchRuleDateDTO);
		if(!flag.equals("OK")) return flag;
		searchRuleDateDTO.setCreateTime(new Date());
//		searchRuleDateDTO.setOperator("1");
		//根据commandCode，查询commandName
		setCommandName(searchRuleDateDTO);
		this.saveTX(searchRuleDateDTO);
		return flag;
	}

	@Override
	public String update(SearchRuleDateDTO searchRuleDateDTO) throws Exception {
		String flag = this.validDate(searchRuleDateDTO);
		if(!flag.equals("OK")) return flag;
		setCommandName(searchRuleDateDTO);
		searchRuleDateDTO.setUpdateTime(new Date());
		this.updateTX(searchRuleDateDTO);
		return flag;
	}

	@Override
	public String updateSyncSearchRule() throws Exception {
		List<SearchRuleDateDTO> serchRules = getAllDatas();
		List<SearchRuleDateDTO> serchRule1 = getSearchRule1AllData();
		if(serchRules!=null){
			if(serchRule1!=null){
				for(SearchRuleDateDTO sr : serchRule1){
					searchRuleDateDAOL1.del(sr);
				}
			}
			for(SearchRuleDateDTO sr : serchRules){
				searchRuleDateDAOL1.add(sr);
			}
			return "OK";
		}else{
			return "对不起，索引数据配置为空，无法更新!";
		}
	}
	
	private List<SearchRuleDateDTO> getSearchRule1AllData() {
		return searchRuleDateDAOL1.getAllForT();
	}

	@Override
	public void deleteSqlTX(String sql) throws AppException {
		searchRuleDateDAOL.del(sql);
		
	}
	
	@Override
	public void  updateListTX(List<SearchRuleDateDTO> lstDto,String appcode) throws Exception {
		for (SearchRuleDateDTO searchRuleDateDTO: lstDto) {
			    searchRuleDateDTO.setAppCode(appcode);
			    searchRuleDateDAOL.update(searchRuleDateDTO);
			}
	}
}
