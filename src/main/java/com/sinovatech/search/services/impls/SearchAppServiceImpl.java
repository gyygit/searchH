package com.sinovatech.search.services.impls;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.luceneindex.db.DataBaseManager;
import com.sinovatech.search.luceneindex.db.dao.SearchMessageDAO;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.daos.SearchAppDAO;
import com.sinovatech.search.daos.SearchCommandDAO;
import com.sinovatech.search.daos.SearchRuleDateDAO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchRuleDateService;


/**
 * SearchApp注册应用业务表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:04<br />
 * 
 * @author 作者liuzhenquan
 */
public class SearchAppServiceImpl implements SearchAppService {
	@Autowired
	private SearchAppDAO searchAppDAO;

	@Autowired
	private SearchRuleDateDAO searchRuleDateDAO;
	
	@Autowired
	private SearchCommandDAO searchCommandDAO;
	
	@Autowired
	private SearchMessageDAO searchMessageDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private SearchRuleDateService searchRuleDateService;

	private static final Log log = LogFactory.getLog(SearchAppServiceImpl.class);
	/**
	 * 获取所有数据
	 */
	public List<SearchAppDTO> getAllDatas() {
		return searchAppDAO.getAll();
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
		List lst = searchAppDAO.list(limit, sqlwhere);
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
		List lst = searchAppDAO.list(limit, hql, as);
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
		List lst = searchAppDAO.listForSql(limit, sql, as);
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
			hql = " from SearchAppDTO Z";
		}
		return searchAppDAO.findPage(page, hql, objWhere);
	}

	public Page pageForSql(Page page, String hql, Object[] objWhere) {
		if (hql == null || "".equals(hql)) {
			hql = " from SEARCH_APP  Z";
		}
		return searchAppDAO.findPageForSql(page, hql, objWhere);
	}

	@Override
	public void saveTX(SearchAppDTO searchAppDTO) throws AppException {
		searchAppDAO.save(searchAppDTO);
	}

	@Override
	public void updateTX(SearchAppDTO searchAppDTO) throws AppException {
		searchAppDAO.update(searchAppDTO);
	}

	@Override
	public void saveOrUpdateTX(SearchAppDTO searchAppDTO) throws AppException {
		searchAppDAO.saveOrUpdate(searchAppDTO);
	}

	@Override
	public void deleteTX(String ids) throws AppException {
		List<SearchAppDTO> list = listByIds(ids);
		for(SearchAppDTO dto : list){
			//删除前，先把App设置成停止状态
			if(dto.getState() == RedisKeyConst.Search.USERING)
				LuceneManager.optIndexWrite(dto.getAppCode(), RedisKeyConst.Search.SEARCH_SYS_STOP);
			dto.setIsDelete(RedisKeyConst.Search.IS_DELETE_HIDE);
			dto.setState(RedisKeyConst.Search.STOP);
			updateTX(dto);
		}
	}

	@Override
	public SearchAppDTO get(String id) throws AppException {
		return searchAppDAO.get(id);
	}

	@Override
	public List listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		return searchAppDAO.listByIds(ids);
	}
	public List listByLimitForSql(LimitInfo limit, String sql, String where, String as) throws AppException {
		return searchAppDAO.listByLimitForSql(limit, sql, where, as);
	} 
	public List listByLimitForSqlRmap(LimitInfo limit, String sql, String where, String as,SearchCommandDTO searchCommandDTO,DataBaseManager dataBaseManager,List<String> cnameList) throws AppException {
		return searchAppDAO.listByLimitForSqlRmap(limit, sql, where, as);
	} 
	
	public List<Map> getAllViewData(String appCode) 
	{
		  
	   
		String sql="";
	        ResultSet rs = null;//executeQueryRS(sql); 
	        ResultSetMetaData rsmd = null; 
	        int columnCount = 0; 
	        try { 
	            rsmd = rs.getMetaData(); 
	        } catch (SQLException e2) { 
	            System.out.println(e2.getMessage()); 
	        } 
	        try { 
	            columnCount = rsmd.getColumnCount(); 
	        } catch (SQLException e1) { 
	            System.out.println(e1.getMessage()); 
	        } 
	         
	        List<Object> list = new ArrayList<Object>(); 
	         
	        try { 
	            while(rs.next()) { 
	                Map<String, Object> map = new HashMap<String, Object>(); 
	                for(int i = 1; i <= columnCount; i++) { 
	                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));                
	                } 
	                list.add(map); 
	            } 
	        } catch (SQLException e) { 
	            System.out.println(e.getMessage()); 
	        } finally { 
	            //closeAll(); 
	        } 
	         
	        return null;//list; 
	     
	}

	@Override
	public List getSearchAppList() {
		// TODO Auto-generated method stub
		LimitInfo limit =new LimitInfo();
		String hql = "from SearchAppDTO t where 1=1 and isDelete = '"+ RedisKeyConst.Search.IS_DELETE_SHOW + "'";
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public List getSearchAppDTO(String hql) {
		LimitInfo limit =new LimitInfo();
		hql = "from SearchAppDTO t where 1=1 " + hql;
		List list = this.list(limit, hql, "");
		return list;
	}

	@Override
	public void updateCacheByAppCode(String appCode) {
		List list = this.getSearchAppDTO(" and appCode = '" + appCode + "'");
		if(list != null && list.size() > 0){
			SearchAppDTO searchAppDTO = (SearchAppDTO) list.get(0);
			//修改缓存(重新读取相关command和ruleDate)
			AppCacheDTO cacheDTO = AppContext.getAppCacheDTOMap().get(searchAppDTO.getAppCode());
			
			LimitInfo limit = new LimitInfo();
			List<SearchCommandDTO> commandList = searchCommandDAO.list(limit, " and appId = '" + searchAppDTO.getId() + "' ");//读取command
			
			List<SearchRuleDateDTO> ruleDateList = searchRuleDateDAO.list(limit, " and appId = '" + searchAppDTO.getId() + "' ");//读取RuleDate
			Map<String, SearchCommandDTO> commandDTOMap = new HashMap<String,SearchCommandDTO>();//<commandCode,SearchCommandDTO>
			Map<String, SearchRuleDateDTO> searchRuleDateDTOMap = new HashMap<String, SearchRuleDateDTO>();//<flied,searchRuleDateDTOMap>
			for(SearchCommandDTO commandDTO : commandList){
				commandDTOMap.put(commandDTO.getCommandCode(), commandDTO);
			}
			for(SearchRuleDateDTO ruleDateDTO : ruleDateList){
				searchRuleDateDTOMap.put(ruleDateDTO.getFieldName()+"_"+ruleDateDTO.getCommandCode(), ruleDateDTO);
			}
			cacheDTO.setSearchCommandDTOMap(commandDTOMap);
			cacheDTO.setSearchRuleDateDTOMap(searchRuleDateDTOMap);
			cacheDTO.setSearchAppDTO(searchAppDTO);
		}
	}

	@Override
	public List list(LimitInfo limit ,SearchAppDTO dto,String usercode,String appcode) {
		
		StringBuffer sb = new StringBuffer();
		if(!"".equals(dto.getAppCode())){
			sb.append(" and appCode like '%").append(dto.getAppCode()).append("%'");
		}
		if(!"".equals(dto.getAppName())){
			sb.append(" and appName like '%").append(dto.getAppName()).append("%'");
		}
		if(dto.getIndexType() != null && !dto.getIndexType().equals("")){
			sb.append(" and indexType = '").append(dto.getIndexType()).append("'");
		}
		if(dto.getState() != null && !dto.getState().equals("")){
			sb.append(" and state = '").append(dto.getState()).append("'");
		}
		sb.append(" and isDelete ='").append(RedisKeyConst.Search.IS_DELETE_SHOW).append("'");//显示
		return this.list(limit, sb.toString());
	}

	@Override
	public String save(SearchAppDTO searchAppDTO) throws Exception {
		
		String flag = this.validDate(searchAppDTO);
		if(!flag .equals("OK"))
			return flag;
		searchAppDTO.setCreateTime(new Date());
		searchAppDTO.setCurrentDir(RedisKeyConst.Search.A_DIR);
		//操作人，待修改
		searchAppDTO.setOperator("1");
		searchAppDTO.setState(RedisKeyConst.Search.STOP);//默认停用
		searchAppDTO.setIsDelete(RedisKeyConst.Search.IS_DELETE_SHOW);
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_NO);//默认不需要重启（因为已经停用）
		this.saveTX(searchAppDTO);
		return flag;
	}

	private String validDate(SearchAppDTO dto) {
		String flag = "OK";
		if("".equals(dto.getAppName())){
			return flag = "业务应用名称不能为空!";
		}else if("".equals(dto.getAppCode())){
			return flag = "应用编码不能为空!";
		}
		else if("".equals(dto.getIndexType())){
			return flag = "请选择索引方式!";
		}
		else if("".equals(dto.getIndexPath())){
			return flag = "索引路径不能为空!";
		}
		else if(dto.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI)){
		if(dto.getLoopCindexTime() == null || dto.getLoopCindexTime().equals("")){
			return flag = "定时重建索引不能为空!";
		}}
		String hql = " and isDelete = " + RedisKeyConst.Search.IS_DELETE_SHOW;
		if(dto.getId() != null && !dto.getId().equals(""))
			hql += " and t.id != '" + dto.getId()+"'";
		
		//业务应用名称唯一
		List list2 = this.getSearchAppDTO(" and t.appName = '" + dto.getAppName() + "'" + hql);
		if(list2 != null && list2.size()>0){
		return	flag = "业务应用名称重复，请重新录入！";
		}
		//appCode唯一
		List list1 = this.getSearchAppDTO(" and t.appCode = '" + dto.getAppCode() + "'" + hql);
		if(list1 != null && list1.size()>0){
		return	flag = "应用编码不能重复，请重新录入！";
		}
		//索引路径唯一
		List list = this.getSearchAppDTO(" and t.indexPath = '" + dto.getIndexPath() + "'" + hql);
		if(list != null && list.size()>0){
			return flag = "索引路径不能重复，请重新录入！";
		}
	
		return flag;
	}

	@Override
	public String update(SearchAppDTO searchAppDTO) throws Exception {
		//根据id获得searchapp，填充数据
		SearchAppDTO m = this.get(searchAppDTO.getId());
		searchAppDTO.setCreateTime(m.getCreateTime());
		searchAppDTO.setOperator(m.getOperator());
		searchAppDTO.setCurrentDir(m.getCurrentDir());
		searchAppDTO.setState(m.getState());
		searchAppDTO.setIsDelete(m.getIsDelete());
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_YES);
		
		searchAppDTO.setIndexType(m.getIndexType());
		searchAppDTO.setLoopCindexTime(m.getLoopCindexTime());
		searchAppDTO.setIndexPath(m.getIndexPath());
		searchAppDTO.setUpdateTime(new Date());
		String flag = this.validDate(searchAppDTO);
		if(flag.equals("OK"))
			this.updateTX(searchAppDTO);
		return flag;
	}
	


	/**
	 * 修改缓存
	 * 
	 * @param searchAppDTO
	 * @param commandList
	 * @param ruleDateList
	 * @throws AppException
	 */
	
	public void editCache(SearchAppDTO searchAppDTO,
			List<SearchCommandDTO> commandList,
			List<SearchRuleDateDTO> ruleDateList) throws AppException {
		AppCacheDTO cacheDTO = AppContext.getAppCacheDTOMap().get(
				searchAppDTO.getAppCode());
		if (cacheDTO == null)
			cacheDTO = new AppCacheDTO();
		searchAppDAO.update(searchAppDTO);
		Map<String, SearchCommandDTO> commandDTOMap = new HashMap<String, SearchCommandDTO>();// <commandCode,SearchCommandDTO>
		Map<String, SearchRuleDateDTO> searchRuleDateDTOMap = new HashMap<String, SearchRuleDateDTO>();// <flied,searchRuleDateDTOMap>
		for (SearchCommandDTO commandDTO : commandList) {
			commandDTOMap.put(commandDTO.getCommandCode(), commandDTO);
		}
		for (SearchRuleDateDTO ruleDateDTO : ruleDateList) {
			searchRuleDateDTOMap.put(ruleDateDTO.getFieldName()+"_"+ruleDateDTO.getCommandCode(), ruleDateDTO);
		}
		cacheDTO.setSearchCommandDTOMap(commandDTOMap);
		cacheDTO.setSearchRuleDateDTOMap(searchRuleDateDTOMap);
		cacheDTO.setSearchAppDTO(searchAppDTO);
		AppContext.getAppCacheDTOMap().put(searchAppDTO.getAppCode(), cacheDTO);
	
	}

	public String validStart(List<SearchCommandDTO> commandList,
			List<SearchRuleDateDTO> ruleDateList, String flag) {
		if (commandList == null || commandList.size() == 0) {
			return flag = "当前业务数据类型和索引数据配为空，无法启动该应用！";
		}
		String str = "";
		for (SearchCommandDTO b : commandList) {
			List<SearchRuleDateDTO> list =	searchRuleDateService.searchRuleDateBySql(" commandCode = '" + b.getCommandCode() + "'" );	
			int x = 0;
			for(SearchRuleDateDTO date : list){
				if(date.getFileldDateType().equals(RedisKeyConst.Search.RULE_DATE_TYPE_PK))
					x++;
			}
			if(x == 0){
				str += b.getCommandName();
			}
		}
		if(!str.equals(""))
			return flag = "业务数据类型编码\"" + str
				+ "\"下无对应索引数据或无主键索引数据，无法启用当前应用，请查看！";

		return "OK";
	}

	@Override
	public Map<String, AppCacheDTO> getReopen(String appCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 
	 * @param datexml
	 */
	public  boolean  saveSearchMessageDTOTX(String datexml) {
		String[] strs =readXmlCommandAndAppCode(datexml);
		if(strs==null)
		{
			log.error("saveSearchMessageDTOTX 	String[] strs is null");
			return false;
		}
		SearchMessageDTO aearchMessageDTO =new SearchMessageDTO();
		aearchMessageDTO.setAppcode(strs[0]);
		aearchMessageDTO.setCommandcode(strs[1]);
		aearchMessageDTO.setOpt(strs[2]);
		aearchMessageDTO.setFileinfo(datexml);
		this.searchMessageDAO.add(aearchMessageDTO);
		log.info("saveSearchMessageDTOTX 	is ok ");
		return true;
		  
	}
	public String retenRxml(String[] strs,String yuanyin,boolean isSu)
    {
		String rflag = isSu?"1":"2";
    	String  appcode = "";
    	String  commandCode = "";
    	String  optCode = "";
    	if(strs!=null)
    	{
    		appcode =strs[0];
    		commandCode =strs[1];
    		optCode =strs[2];
    	}
    	StringBuffer sb = new StringBuffer();
    	sb.append("<result>")
    	  .append("<appcode>").append(appcode).append("</appcode>")
    	  .append("<commandcode>").append(commandCode).append("</commandcode>")
    	  .append("<optcode>").append(optCode).append("</optcode>")
    	  .append("<res>").append(rflag).append("</res>")
    	  .append("<reszn>").append(yuanyin).append("</reszn>")
    	  .append("</result>");
    	return sb.toString();
    }
	 
	/**
	 * 解析xml
	 * @param datexml
	 */
	public String[] readXmlCommandAndAppCode(String datexml) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(datexml);
		} catch (DocumentException e) {
			log.error("readXmlCommandAndAppCode  document is eror",e);
			e.printStackTrace();
			return null;
		}
		Element root = document.getRootElement();
		String opt = "";
		String appCode = "";
		String commandCode = "";
		opt = root.element("opt").getText() == null ?  "" : root.element("opt").getText();
		appCode = root.element("appcode").getText() == null ?  "" : root.element("appcode").getText();
		commandCode = root.element("commandcode").getText() == null ?  "" : root.element("commandcode").getText();
		String[] rstrs =new String[3]; 
		rstrs[0]=appCode;
		rstrs[1]=commandCode;
		rstrs[2]=opt;
		return rstrs;
	}

	@Override
	public String getAnalyzerTypeByAppCode(String appCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSqlTX(String ids) throws AppException {
		// TODO Auto-generated method stub
		
	}
	 
}
