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
	private SearchCommandDAO searchCommandDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SearchAppDAO searchAppDAO;
	
	@Autowired
	private SearchRuleDateDAO searchRuleDateDAO;

	/**
	 * 获取所有数据
	 */
	public List<SearchCommandDTO> getAllDatas() {
		return searchCommandDAO.getAll();
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
		List lst = searchCommandDAO.list(limit, sqlwhere);
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
		List lst = searchCommandDAO.list(limit, hql, as);
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
		List lst = searchCommandDAO.listForSql(limit, sql, as);
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
			hql = " from SearchCommandDTO Z";
		}
		return searchCommandDAO.findPage(page, hql, objWhere);
	}

	public Page pageForSql(Page page, String hql, Object[] objWhere) {
		if (hql == null || "".equals(hql)) {
			hql = " from SEARCH_COMMAND  Z";
		}
		return searchCommandDAO.findPageForSql(page, hql, objWhere);
	}

	@Override
	public void saveTX(SearchCommandDTO searchCommandDTO) throws AppException {
		searchCommandDAO.save(searchCommandDTO);
		SearchAppDTO app = searchAppDAO.get(searchCommandDTO.getAppId());
		//把对应的searchApp修改为“需要重启”
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchCommandDTO.setCreateTime(new Date());
		searchCommandDTO.setAppCode(app.getAppCode());
		searchCommandDTO.setOperator("1");
		searchAppDAO.update(app);
	}

	@Override
	public void updateTX(SearchCommandDTO searchCommandDTO) throws AppException {
		SearchAppDTO app = searchAppDAO.get(searchCommandDTO.getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchCommandDTO.setAppCode(app.getAppCode());
		searchAppDAO.update(app);
		searchCommandDAO.update(searchCommandDTO);
	}

	@Override
	public void saveOrUpdateTX(SearchCommandDTO searchCommandDTO)
			throws AppException {
		searchCommandDAO.saveOrUpdate(searchCommandDTO);
	}

	@Override
	public String deleteTX(String ids) throws AppException {
		String[] id = ids.split(",");
		//app设置成需要重新启动
		SearchAppDTO app = searchAppDAO.get(searchCommandDAO.get(id[id.length-1]).getAppId());
		app.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDAO.update(app);
		for (int i = 0; i < id.length; i++) {
			//删除command的时候判断是否存在引用
			SearchCommandDTO dto = searchCommandDAO.get(id[i]);
			if(dto != null){
				List ruleDateList = searchRuleDateDAO.ListByCommandCode(dto);
				if(ruleDateList != null && ruleDateList.size() > 0){
					return "业务数据分类编码为\""+dto.getCommandCode()+"\"存在索引数据，请先删除相关索引数据!";
				}
				searchCommandDAO.delete(id[i]);
			}
		}
		return "OK";
	}

	@Override
	public SearchCommandDTO get(String id) throws AppException {
		return searchCommandDAO.get(id);
	}

	@Override
	public List listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		return searchCommandDAO.listByIds(ids);
	}

	@Override
	public List searchCommand(String appId) {
		LimitInfo limit = new LimitInfo();
		String hql = "from SearchCommandDTO t where 1=1 and appId = '" + appId + "' ";
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List list(LimitInfo limit, SearchCommandDTO dto) {
		StringBuffer sb = new StringBuffer();
		if(dto.getViewName() != null && !dto.getViewName().equals("")){
			sb.append(" and viewName like '%").append(dto.getViewName()).append("%'");
		}
		if(dto.getCommandCode() != null && !dto.getCommandCode().equals("")){
			sb.append(" and commandCode like '%").append(dto.getCommandCode()).append("%'");
		}
		if(dto.getCommandName() != null && !dto.getCommandName().equals("")){
			sb.append(" and commandName like '%").append(dto.getCommandName()).append("%'");
		}
		
		sb.append(" and appId = '").append(dto.getAppId()).append("'");
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
		if(dto.getCommandCode() != null && dto.getCommandCode().equals("")){
			return flag = "业务数据分类编码不能为空!";
		}
		if(dto.getSqlWhere() != null && dto.getSqlWhere().equals("")){
			dto.setSqlWhere(" ");
		}
		//业务数据分类编码不能重复
		String hql = " and commandCode = '" + dto.getCommandCode() + "' and appId = '" + dto.getAppId() + "' ";
		if(!dto.getId().equals("")){
			hql += " and id != '" + dto.getId() + "'";
		}
		LimitInfo limit = new LimitInfo();
		List<SearchCommandDTO> list = this.list(limit, hql);
		if(list != null && list.size() >0)
			return flag = "业务数据分类编码不能重复";
		//业务数据名称不能重复
		hql = "and commandName = '" + dto.getCommandName() + "'and appId = '" + dto.getAppId() + "'" ;
		if(!dto.getId().equals("")){
			hql += " and id != '" + dto.getId() + "'";
		}
		List<SearchCommandDTO> list1 = this.list(limit, hql);
		if(list1 != null && list1.size() >0)
			return flag = "业务数据名称不能重复";
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
	public void deleteTX(List<SearchCommandDTO> lstDto) throws AppException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateListTX(List<SearchCommandDTO> lstDto, String appcode)
			throws AppException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSqlTX(String ids) throws AppException {
		// TODO Auto-generated method stub
		
	}
}
