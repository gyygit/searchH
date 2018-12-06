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
import com.sinovatech.search.services.SearchCommandService;

/**
 * SearchCommand业务数据类型表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:06<br />
 * 
 * @author 作者liuzhenquan
 */
public class SearchCommandServiceImpl implements SearchCommandService {
	@Autowired
	private SearchAppDAO searchAppDAOL;

	@Autowired
	private SearchRuleDateDAO searchRuleDateDAOL;
	
	@Autowired
	private SearchCommandDAO searchCommandDAOL;

	/**
	 * 获取所有数据
	 */
	public List<SearchCommandDTO> getAllDatas() {
		return null;
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
	public List<SearchCommandDTO> list(LimitInfo limit, String sqlwhere) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sqlwhere);
		page.addSortList("createTime", Page.ORDER.DESC);
		List<SearchCommandDTO> lst = searchCommandDAOL.listForT(page);
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
	public List<SearchCommandDTO> list(LimitInfo limit, String hql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(hql);
		List<SearchCommandDTO> lst = searchCommandDAOL.listForT(page);
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
	public List<SearchCommandDTO> listForSql(LimitInfo limit, String sql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sql);
		List<SearchCommandDTO> lst = searchCommandDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	/**
	 * from table where code=? and url=?
	 * 
	 * @param page
	 * @param obj
	 * @return
	 */
//	public Page page(Page page, String hql, Object[] objWhere) {
//		if (hql == null || "".equals(hql)) {
//			hql = " from SearchCommandDTO Z";
//		}
//		return searchCommandDAOL.findPage(page, hql, objWhere);
//	}

//	public Page pageForSql(Page page, String hql, Object[] objWhere) {
//		if (hql == null || "".equals(hql)) {
//			hql = " from SEARCH_COMMAND  Z";
//		}
//		return searchCommandDAOL.findPageForSql(page, hql, objWhere);
//	}

	@Override
	public void saveTX(SearchCommandDTO searchCommandDTO) throws AppException {
		SearchAppDTO app = searchAppDAOL.getById(searchCommandDTO.getAppId());
		//把对应的searchApp修改为“需要重启”
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchCommandDTO.setAppCode(app.getAppCode());
		searchCommandDTO.setCreateTime(new Date());
//		searchCommandDTO.setOperator("1");
		searchAppDAOL.update(app);
		searchCommandDAOL.add(searchCommandDTO);
	}

	@Override
	public void updateTX(SearchCommandDTO searchCommandDTO) throws AppException {
		SearchAppDTO app = searchAppDAOL.getById(searchCommandDTO.getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchCommandDTO.setAppCode(app.getAppCode());
		searchAppDAOL.update(app);
		searchCommandDAOL.update(searchCommandDTO);
	}

	@Override
	public void saveOrUpdateTX(SearchCommandDTO searchCommandDTO)
			throws AppException {
		searchCommandDAOL.update(searchCommandDTO);
	}

	@Override
	public String deleteTX(String ids) throws AppException {
		String[] id = ids.split(",");
		//app设置成需要重新启动
		SearchAppDTO app = searchAppDAOL.getById(searchCommandDAOL.getById(id[id.length-1]).getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDAOL.update(app);
		for (int i = 0; i < id.length; i++) {
			//删除command的时候判断是否存在引用
			SearchCommandDTO dto = searchCommandDAOL.getById(id[i]);
			if(dto != null){
				//and  Z.commandCode = '" + searchCommandDTO.getCommandCode() +"' and appId = '" + searchCommandDTO.getAppId() +"'"
				Page page = new Page();
				page.setQueryStr("(commandCode:" + dto.getCommandCode() +"~0) AND (appId:" + dto.getAppId() +"~0)");
				List<SearchRuleDateDTO> ruleDateList = searchRuleDateDAOL.listForT(page);
				if(ruleDateList != null && ruleDateList.size() > 0){
					return "业务数据分类编码为\""+dto.getCommandCode()+"\"存在索引数据，请先删除相关索引数据!";
				}
				searchCommandDAOL.del(dto);
			}
		}
		return "OK";
	}
	
	@Override
	public void  deleteTX(List<SearchCommandDTO> lstDto) throws AppException {
		for (SearchCommandDTO searchCommandDTO: lstDto) {
				searchCommandDAOL.del(searchCommandDTO);
			}
	}
	
	@Override
	public void  updateListTX(List<SearchCommandDTO> lstDto,String appcode) throws AppException {
		for (SearchCommandDTO searchCommandDTO: lstDto) {
			searchCommandDTO.setAppCode(appcode);
				searchCommandDAOL.update(searchCommandDTO);
			}
	}

	@Override
	public SearchCommandDTO get(String id) throws AppException {
		return searchCommandDAOL.getById(id);
	}

	@Override
	public List<SearchCommandDTO> listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		return searchCommandDAOL.listByIds(ids);
	}

	@Override
	public List<SearchCommandDTO> searchCommand(String appId) {
		LimitInfo limit = new LimitInfo();
		String hql = " (appId:" + appId + ")";
		List<SearchCommandDTO> list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List<SearchCommandDTO> list(LimitInfo limit, SearchCommandDTO dto) {
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		if(dto.getViewName() != null && !dto.getViewName().equals("")){
			sb.append(" AND (viewName:").append(dto.getViewName()).append("*)");
		}
		if(dto.getCommandCode() != null && !dto.getCommandCode().equals("")){
			sb.append(" AND (commandCode:").append(dto.getCommandCode()).append("*)");
		}
		if(dto.getCommandName() != null && !dto.getCommandName().equals("")){
			sb.append(" AND (commandName:").append(dto.getCommandName()).append("*)");
		}
		
		sb.append("AND (appId:").append(dto.getAppId()).append("~0)");
		return this.list(limit, sb.toString());
	}

	@Override
	public String save(SearchCommandDTO searchCommandDTO) throws Exception {
 		String flag = validDate(searchCommandDTO);
		if(flag == "OK")
			this.saveTX(searchCommandDTO);
		return flag;
	}
	private String validDate(SearchCommandDTO dto) {
		String flag = "OK";
		if(dto.getCommandCode() == null || dto.getCommandCode().equals("")){
			return flag = "业务数据分类编码不能为空!";
		}
		if(dto.getSqlWhere() == null || dto.getSqlWhere().equals("")){
			dto.setSqlWhere(" ");
		}
		//业务数据分类编码不能重复
		String hql = " (commandCode:" + dto.getCommandCode() + "~0) AND (appId:" + dto.getAppId() + "~0) ";
		if(!dto.getId().equals("")){
			hql += " NOT (id:" + dto.getId() + "~0)";
		}
		LimitInfo limit = new LimitInfo();
		List<SearchCommandDTO> list = this.list(limit, hql);
		if(list != null && list.size() >0)
			return flag = "业务数据分类编码不能重复";
		//业务数据名称不能重复
		hql = " (commandName:" + dto.getCommandName() + "~0) AND (appId:" + dto.getAppId() + "~0)" ;
		if(!dto.getId().equals("")){
			hql += " NOT (id:" + dto.getId() + "~0)";
		}
		List<SearchCommandDTO> list1 = this.list(limit, hql);
		if(list1 != null && list1.size() >0) 
			return flag = "业务数据名称不能重复";
		
		SearchAppDTO app = searchAppDAOL.getById(dto.getAppId());
		//如果是推送
		if(app.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_TUI)) {
//			if(dto.getUserName().equals(""))
//				return flag = "用户名不能为空！";
//			if(dto.getPassWord().equals(""))
//				return flag = "密码不能为空！";
//			if(dto.getLinkAddress().equals(""))
//				return flag = "链接地址不能为空！";
		}
		//视图
		else if(app.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI)) {
			if(dto.getViewName().equals(""))
				return flag = "视图名称不能为空!";
		}
		//ftp
		else if(app.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_FTP)) {
			if(dto.getUserName().equals(""))
				return flag = "用户名不能为空！";
			if(dto.getPassWord().equals(""))
				return flag = "密码不能为空！";
			if(dto.getLinkAddress().equals(""))
				return flag = "链接地址不能为空！";
			if(dto.getPort().equals(""))
				return flag = "端口号不能为空！";
			if(dto.getLocalAddress().equals(""))
				return flag = "本地文件夹为空！";
			if(dto.getLinkAddress().equals(""))
				return flag = "链接地址不能为空！";
			if(dto.getLinkDir().equals(""))
				return flag = "远程文件夹不能为空！";
		}
		//服务
		else if(app.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE)) {
//			if(dto.getUserName().equals(""))
//				return flag = "用户名不能为空！";
//			if(dto.getPassWord().equals(""))
//				return flag = "密码不能为空！";
			if(dto.getLinkAddress().equals(""))
				return flag = "链接地址不能为空！";
		}
		return flag;
	}

	@Override
	public String update(SearchCommandDTO searchCommandDTO)  throws Exception{
		String flag = this.validDate(searchCommandDTO);
		if(flag == "OK")
			this.updateTX(searchCommandDTO);
		return flag;
	}

	@Override
	public void deleteSqlTX(String sql) throws AppException {
		searchCommandDAOL.del(sql);
		
	}
}
