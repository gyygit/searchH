package com.sinovatech.search.services.impls;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.daos.SearchAppDAO;
import com.sinovatech.search.daos.SearchCommandDAO;
import com.sinovatech.search.daos.SearchRuleDateDAO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
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
	private SearchRuleDateDAO searchRuleDateDAO;
	@Autowired
	private SearchCommandDAO searchCommandDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SearchAppDAO searchAppDAO;

	/**
	 * 获取所有数据
	 */
	public List<SearchRuleDateDTO> getAllDatas() {
		return searchRuleDateDAO.getAll();
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
		List lst = searchRuleDateDAO.list(limit, sqlwhere);
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
		List lst = searchRuleDateDAO.list(limit, hql, as);
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
		List lst = searchRuleDateDAO.listForSql(limit, sql, as);
		return lst;
	}

	/**
	 * from table where code=? and url=?
	 * 
	 * @param page
	 * @param obj
	 * @return
	 */
	public Page page(Page page, String hql, Object[] objWhere) {
		if (hql == null || "".equals(hql)) {
			hql = " from SearchRuleDateDTO Z";
		}
		return searchRuleDateDAO.findPage(page, hql, objWhere);
	}

	public Page pageForSql(Page page, String hql, Object[] objWhere) {
		if (hql == null || "".equals(hql)) {
			hql = " from SEARCH_RULE_DATE  Z";
		}
		return searchRuleDateDAO.findPageForSql(page, hql, objWhere);
	}

	@Override
	public void saveTX(SearchRuleDateDTO searchRuleDateDTO) throws AppException {

		// 把对应的searchApp修改为“需要重启”
		SearchAppDTO app = searchAppDAO.get(searchRuleDateDTO.getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchRuleDateDTO.setAppCode(app.getAppCode());
		searchAppDAO.update(app);
		searchRuleDateDAO.save(searchRuleDateDTO);
	}

	@Override
	public void updateTX(SearchRuleDateDTO searchRuleDateDTO)
			throws AppException {
		//把对应的searchApp修改为“需要重启”
		SearchAppDTO app = searchAppDAO.get(searchRuleDateDTO.getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDAO.update(app);
		searchRuleDateDAO.update(searchRuleDateDTO);
	}

	@Override
	public void saveOrUpdateTX(SearchRuleDateDTO searchRuleDateDTO)
			throws AppException {
		searchRuleDateDAO.saveOrUpdate(searchRuleDateDTO);
	}

	@Override
	public void deleteTX(String ids) throws AppException {
		String[] id = ids.split(",");
		//把对应的app设置成需要重新启动
		SearchAppDTO app = searchAppDAO.get(searchRuleDateDAO.get(id[id.length-1]).getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDAO.update(app);
		for (int i = 0; i < id.length; i++) {
			searchRuleDateDAO.delete(id[i]);
		}
	}

	@Override
	public SearchRuleDateDTO get(String id) throws AppException {
		return searchRuleDateDAO.get(id);
	}

	@Override
	public List listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		return searchRuleDateDAO.listByIds(ids);
	}

	@Override
	public List<SearchRuleDateDTO> searchRuleDate(String appId) {
		LimitInfo limit = new LimitInfo();
		String hql = "from SearchRuleDateDTO t where 1=1 and appId = '" + appId + "' ";
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List<SearchRuleDateDTO> searchRuleDateBySql(String sql) {
		LimitInfo limit = new LimitInfo();
		String hql = "from SearchRuleDateDTO t where 1=1 and " + sql;
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List list(LimitInfo limit, SearchRuleDateDTO dto) {
		StringBuffer sb = new StringBuffer();
		if (!dto.getCommandCode().equals("")) {//业务数据类型编码
			sb.append(" and commandCode like '%").append(dto.getCommandCode()).append("%'");
		}
		if (dto.getFieldIndexType() != null && !dto.getFieldIndexType().equals("")) {//字段索引类型
			sb.append(" and fieldIndexType = '").append(dto.getFieldIndexType()).append("'");
		}
		if (!dto.getFieldName().equals("")) {//字段名称
			sb.append(" and fieldName like '%").append(dto.getFieldName().toUpperCase()).append("%'");
		}
		if (dto.getFileldDateType() != null && !dto.getFileldDateType().equals("")) {//字段值类型
			sb.append(" and fileldDateType = '").append(dto.getFileldDateType()).append("'");
		}
		if (dto.getFieldStoreType() != null && !dto.getFieldStoreType().equals("")) {//字段值是否存储
			sb.append(" and fieldStoreType = '").append(dto.getFieldStoreType()).append("'");
		}
		if (dto.getIsIntelliSense() != null && !dto.getIsIntelliSense().equals("")) {//是否智能提示
			sb.append(" and isIntelliSense = '").append(dto.getIsIntelliSense()).append("'");
		}
		sb.append(" and appId = '").append(dto.getAppId()).append("'");
	
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
		//一个应用下，只能有一个智能提示
		if(dto.getIsIntelliSense().equals(RedisKeyConst.Search.IS_INTELLISENSE_TRUE)){
			//当前对象为智能提示时，判断当前应用下时候还有智能提示的字段
			String sql = " isIntelliSense = '" + RedisKeyConst.Search.IS_INTELLISENSE_TRUE +"' and commandCode = '" + dto.getCommandCode() +"'" ;
			if(!dto.getId().equals("")){
				sql += " and id != '" + dto.getId() + "'";
			}
			List list = this.searchRuleDateBySql(sql);
			if(list != null && list.size() > 0)
				flag = "选择的业务数据类型下已存在“智能提示”字段！";
		}
		if(dto.getFileldDateType().equals(RedisKeyConst.Search.RULE_DATE_TYPE_PK)){
			//判断主键只能存在一个
			String sql =  " fileldDateType = '" + RedisKeyConst.Search.RULE_DATE_TYPE_PK + "' and commandCode = '" + dto.getCommandCode() +"' and appId = '" + dto.getAppId()+ "'";
			if(!dto.getId().equals("")){
				sql += " and id != '" + dto.getId() + "'";
			}
			List list = this.searchRuleDateBySql(sql);
			if(list != null && list.size() > 0)
				flag = "索引数据配置模块中业务数据类型编码\"" + dto.getCommandCode() +"\"的索引数据主键配置规则不符合‘有且仅有一个’原则，无法启用当前应用，请查看！!";
		}
		//字段名字不能重复
		String sql = " fieldName = '" + dto.getFieldName().toUpperCase() + "' and appId = '" + dto.getAppId() +"' ";
		if(!dto.getId().equals("")){
			sql += " and id != '" + dto.getId() + "'";
		}
		List list = this.searchRuleDateBySql(sql);
		if(list != null && list.size() > 0) flag = "字段名字\"" + dto.getFieldName() +"\"已经存在，请重新录入!";
		dto.setFieldName(dto.getFieldName().toUpperCase());
		return flag;
	}
	private void setCommandName(SearchRuleDateDTO searchRuleDateDTO) {
		LimitInfo limit = new LimitInfo();
		List<SearchCommandDTO> list = this.searchCommandDAO.list(limit, " and commandCode ='"+searchRuleDateDTO.getCommandCode() 
				+ "' and appId = '" + searchRuleDateDTO.getAppId()+ "'");
		if(list != null && list.size() > 0)
			searchRuleDateDTO.setCommandName(list.get(0).getCommandName());
	}

	@Override
	public String save(SearchRuleDateDTO searchRuleDateDTO) throws AppException {
		String flag = this.validDate(searchRuleDateDTO);
		if(!flag.equals("OK")) return flag;
		searchRuleDateDTO.setCreateTime(new Date());
		searchRuleDateDTO.setOperator("1");
		//根据commandCode，查询commandName
		setCommandName(searchRuleDateDTO);
		//searchRuleDateDTO.getCommandCode();
	
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
	public void deleteTX(List<SearchRuleDateDTO> lstDto) throws AppException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String updateSyncSearchRule() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSqlTX(String ids) throws AppException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateListTX(List<SearchRuleDateDTO> lstDto, String appcode)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
}
