package com.sinovatech.search.services.impls.lucene;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.luceneindex.db.DataBaseManager;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SearchAppDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchMessageDAO;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.utils.SpringContextHolder;



/**
 * SearchApp注册应用业务表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:04<br />
 * 
 * @author 作者liuzhenquan
 */
public class SearchAppServiceImpl implements SearchAppService {
	@Autowired
	private SearchAppDAO searchAppDAOL;
	@Autowired
	private SearchMessageDAO searchMessageDAOL;

	@Autowired
	private SearchRuleDateService searchRuleDateService;
	
	@Autowired
	private SearchCommandService  searchCommandService;
	

	private static final Log log = LogFactory.getLog(SearchAppServiceImpl.class);
	/**
	 * 获取所有数据
	 */
	public List<SearchAppDTO> getAllDatas() {
//		return null;//searchAppDAOL.getAll();
		return searchAppDAOL.getAllForT();
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
	public List<SearchAppDTO> list(LimitInfo limit, String sqlwhere) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sqlwhere);
		page.setPageNum(limit.getPageNum());
		page.addSortList("createTime", Page.ORDER.DESC);
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page);
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
	public List<SearchAppDTO> list(LimitInfo limit, String hql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(hql +" AND isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		page.setPageNum(limit.getPageNum());
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page);
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
	public List<SearchAppDTO> listForSql(LimitInfo limit, String sql, String as) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setQueryStr(sql+" AND isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		page.setPageNum(limit.getPageNum());
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	 

	@Override
	public void saveTX(SearchAppDTO searchAppDTO) throws AppException {
		searchAppDAOL.add(searchAppDTO);
	}

	@Override
	public  void updateTX(SearchAppDTO searchAppDTO) throws AppException {
		synchronized(RedisKeyConst.synObject)
		{
			searchAppDAOL.update(searchAppDTO);
		}
	}

	@Override
	public void saveOrUpdateTX(SearchAppDTO searchAppDTO) throws AppException {
		searchAppDAOL.update(searchAppDTO);
	}

	@Override
	public void deleteTX(String ids) throws AppException {
		List<SearchAppDTO> list = listByIds(ids);
		for(SearchAppDTO dto : list){
			//删除前，先把App设置成停止状态
			if(dto.getState() == RedisKeyConst.Search.USERING){
				LuceneManager.optIndexWrite(dto.getAppCode(), RedisKeyConst.Search.SEARCH_SYS_STOP);
			}
			
			searchAppDAOL.del(dto);
			
			LimitInfo limit2 = new LimitInfo();
			List<SearchRuleDateDTO> delDtoLst=searchRuleDateService.list(limit2, "appId:"+dto.getId());
			searchRuleDateService.deleteTX(delDtoLst);
			
			
			LimitInfo limit1 = new LimitInfo();
			List<SearchCommandDTO> delDtoLst1 = searchCommandService.list(limit1, "appId:"+dto.getId());
			searchCommandService.deleteTX(delDtoLst1);
			
		}
	}

	@Override
	public SearchAppDTO get(String id) throws AppException {
		return searchAppDAOL.getById(id);
	}

	@Override
	public List<SearchAppDTO> listByIds(String ids) throws AppException {
		ids = "'" + ids.replaceAll(",", "','") + "'";
		 return searchAppDAOL.listByIds(ids);
	}
	public List<SearchAppDTO> listByLimitForSql(LimitInfo limit, String sql, String where, String as) throws AppException {
		Page page = new Page();
		page.setQueryStr(sql+where+" AND isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	} 
	
	private static Map<String  ,Integer  > getCounmBy(ResultSet rs) throws SQLException
	{
		Map<String  ,Integer  > rmap = new HashMap<String  ,Integer  >();
		ResultSetMetaData rsM =rs.getMetaData();
		int ccount = rsM.getColumnCount();
		for(int i=1;i<=ccount;i++)
		{
			rmap.put(rsM.getColumnName(i), rsM.getColumnType(i)); 
		}
		return rmap;
	}
	 public static String getClobString( Clob c) { 
	        try { 
	            Reader reader=c.getCharacterStream();
	            if (reader == null) { 
	                return null; 
	            } 
	            StringBuffer sb = new StringBuffer(); 
	            char[] charbuf = new char[4096]; 
	            for (int i = reader.read(charbuf); i > 0; i = reader.read(charbuf)) { 
	                sb.append(charbuf, 0, i); 
	            } 
	            return sb.toString(); 
	        } catch (Exception e) { 
	        	log.error("getClobString:["+e.getMessage()+"]");
	            return ""; 
	        } 
	    }
	 public static String getBlobString( Blob c) { 
		 InputStream  ins=null;
	        try { 
	        	ins=c.getBinaryStream((long)1,(int)c.length()); 
	             
	            StringBuffer sb = new StringBuffer(); 
	            byte[] b = new byte[1024];
	            int len = 0;
	            while ( (len = ins.read(b)) != -1) {
	            	sb.append(b);
	            } 
	            return sb.toString(); 
	        } catch (Exception e) { 
	        	e.printStackTrace();
	        	log.error("getBlobStringError:["+e.getMessage()+"]");
	            return ""; 
	        } 
	    }
//	 public static void main(String[] args) {
//		 List<Map<String,String>> all =new ArrayList<Map<String,String>>();
//		 for(int i=0;i<67;i++)
//		 {
//			 Map<String,String> ramp = new HashMap<String,String>();
//			 ramp.put("t", i+"");
//			 all.add(ramp);
//		 }
//		 LimitInfo limit = new LimitInfo();
//		 limit.setRowDisplayed(10);
//		 limit.setTotalNum(67);
//		 do{
//			 System.out.println("limit.getPageNum()["+limit.getPageNum()+"]limit.getStartLineNum()-1["+(limit.getStartLineNum()-1)+"]limit.getEndLineNum()["+limit.getEndLineNum()+"]");
//
//			 List<Map<String, String>> subl= all.subList(limit.getStartLineNum()-1, limit.getEndLineNumOk());
//			  for(Map<String, String> m:subl)
//			 {
//				 System.out.println("neirong["+m.get("t")+"]");
//			 }
//			 limit.setPageNum(limit.getPageNum() + 1);
//		 }while (limit.getPageNum() <= limit.getTotalPage());
//		 
//	}
	 
	public List listByLimitForSqlRmap(LimitInfo limit, String sql, String where, String as,SearchCommandDTO searchCommandDTO,DataBaseManager dataBaseManager,List<String> cnameList) throws AppException {
		List<Map<String,String>> rlst =new ArrayList<Map<String,String>>();
		String user=searchCommandDTO.getUserName();
		String pwd = searchCommandDTO.getPassWord();
		String url = searchCommandDTO.getLinkAddress();
		 
		String queryString_oracle = " select * from ( select row_.*, rownum rownum_ from ( "+sql+where+") row_ where rownum <= "+((limit.getStartLineNum() - 1)+limit.getRowDisplayed())+") where rownum_ > "+(limit.getStartLineNum() - 1);
		String sqlcount__oracle = " select count(*) from  "+as+" "+where;
		
		String queryString_mysql = " select * from ("+sql+where+" limit "+(limit.getStartLineNum() - 1)+","+limit.getRowDisplayed()+" ) as t";
		String sqlcount_mysql = " select count(*) from  "+as+" "+where;
 
		
		Map<String,SearchRuleDateDTO> map1 = AppContext.getAppCacheDTOMap().get(searchCommandDTO.getAppCode()).getSearchRuleDateDTOMap();
		String zhujian  = "";
		Iterator it = map1.entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry entry = (Map.Entry) it.next();
		    String key = (String)entry.getKey();
		    SearchRuleDateDTO value = (SearchRuleDateDTO) entry.getValue();
		    if(value.getFileldDateType().equals("1") && value.getCommandCode().equals(searchCommandDTO.getCommandCode())) { //如果字段为主键
		    	zhujian = value.getFieldName();
		    	break;
		    }
	    }
		
		String queryString_sqlserver = " select top " + limit.getRowDisplayed() * limit.getPageNum() + " * from ( " + sql + where + " ) z"+
				" where  ("+ zhujian +" not in ( select top " + limit.getRowDisplayed() * (limit.getPageNum()-1) +" "+ zhujian + 
				" from ( " + sql + where + ") o order by " + zhujian + ")) order by " + zhujian ;
		String sqlcount_sqlserver = " select count(*) from  "+as+" "+where;
		
		String queryString = " select * from ( select row_.*, rownum rownum_ from ( "+sql+where+") row_ where rownum <= "+((limit.getStartLineNum() - 1)+limit.getRowDisplayed())+") where rownum_ > "+(limit.getStartLineNum() - 1);
		String sqlcount = " select count(*) from  "+as+" "+where;
		
		
		if(dataBaseManager.getDbType().equals(DataBaseManager.DbType.ORACLE))
		{
			queryString = queryString_oracle;
			sqlcount = sqlcount__oracle;
		}else if(dataBaseManager.getDbType().equals(DataBaseManager.DbType.MYSQL)){
			queryString = queryString_mysql;
			sqlcount = sqlcount_mysql;
		} else if(dataBaseManager.getDbType().equals(DataBaseManager.DbType.SQLSERVER)){ //sqlserver
			queryString = queryString_sqlserver;
			sqlcount = sqlcount_sqlserver;
		}
		//SELECT name FROM userinfo1 WHERE 1 = 1 LIMIT 100,20;  
		try{
			log.info("ResultSet rscount =dataBaseManager.getResultSet(sqlcount) is begin");
			log.info("sqlcount:["+sqlcount+"]");
			ResultSet rscount =dataBaseManager.getResultSet(sqlcount);
			rscount.next();
			Integer count = rscount.getBigDecimal(1).intValue();
			log.info("size:["+count+"]");
			limit.setTotalNum((int) count);
			//int cc=1;
			if(count>0){
				
				log.info("ResultSet rs =dataBaseManager.getResultSet(queryString); is begin");
				log.info("queryString["+queryString+"]");
				ResultSet rs =dataBaseManager.getResultSet(queryString);
				Map<String,Integer> rsmap = getCounmBy(rs);
				
				
				  while(rs.next())
				  {
					  Map<String,String> map =new HashMap<String,String>();
					  for(String c:cnameList)
					  {
						  c = c.replaceAll("\"", "");  //todo 解决索引字段才数字的情况，需要在sql中添加引号
						  Integer type = rsmap.get(c);
						  if (type == null ){type=-1; } //throw new RuntimeException();
						  
						  // get value.
						  Object v;
						  if (type == Types.CLOB){
							  v = null;
							  v =getClobString(rs.getClob(c));
						  } else if (type == Types.BLOB){
							  v = null;
							  v = getBlobString((rs.getBlob(c)));
						  } else {
							  v = rs.getObject(c);
						  }
						  
						  // convert as string
						  String v2;
						  if (v == null)
							  v2 = "";
						  else if (v instanceof String)
							  v2 = (String)v;
						  else if (v instanceof Date){
							  v2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
						  } else 
							  v2 = String.valueOf(v);
						  
						  map.put(c, v2);
					  }
					  rlst.add(map);
				  }
				  
			}
			 
			 
		}catch(Exception e){
			LuceneManager.intoqueuelog(searchCommandDTO.getAppCode(), searchCommandDTO.getCommandCode(), "2", "3", System.currentTimeMillis(),"2",LuceneManager.extostring(e)); 
			e.printStackTrace();
			log.info("Exception["+e.getMessage()+"]");
			//dataBaseManager.closeConnection();
		}
		return  rlst;// 
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
	public List<SearchAppDTO> getSearchAppList() {
		// TODO Auto-generated method stub
		LimitInfo limit =new LimitInfo();
		Page page = new Page();
		//String hql = "from SearchAppDTO t where 1=1 and isDelete = '"+ RedisKeyConst.Search.IS_DELETE_SHOW + "'";
		String hql = "isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW;
		page.setQueryStr(hql);
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page);
		return lst;
	}

	@Override
	public List<SearchAppDTO> getSearchAppDTO(String hql) {
	    System.out.println("hql --> "+hql);
		Page page = new Page();
		if(hql!=null && !"".equals(hql))
		{
			page.setQueryStr(hql);
		}else{
			page.setQueryStr("*:* AND "+"isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		}
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchAppDTO> lst = searchAppDAOL.listForT(page); 
		return lst;
	}

	@Override
	public void updateCacheByAppCode(String appCode) {
		List list = this.getSearchAppDTO(" appCode : " + appCode +" AND "+"isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		if(list != null && list.size() > 0){
			SearchAppDTO searchAppDTO = (SearchAppDTO) list.get(0); 
			//修改缓存(重新读取相关command和ruleDate)
			AppCacheDTO cacheDTO = AppContext.getAppCacheDTOMap().get(searchAppDTO.getAppCode());
			
			LimitInfo limit = new LimitInfo(); 
			List<SearchCommandDTO> commandList =  searchCommandService.searchCommand(searchAppDTO.getId());//读取command  
			
			List<SearchRuleDateDTO> ruleDateList =  searchRuleDateService.searchRuleDate(searchAppDTO.getId());//读取RuleDate
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
		sb.append(" (*:*) ");
		if(dto.getAppCode() != null && !dto.getAppCode().equals("")){
			sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
		}
		if(dto.getAppName() != null && !dto.getAppName().equals("")){
			sb.append(" AND (appName:").append(dto.getAppName()).append("*) ");
		}
		if(dto.getIndexType() != null && !dto.getIndexType().equals("")){
			sb.append(" AND (indexType:").append(dto.getIndexType()).append("~0) ");
		}
		if(dto.getState() != null && !dto.getState().equals("")){
			sb.append(" AND (state:").append(dto.getState()).append("~0) ");
		}
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if(!"admin".equals(usercode)){
				if(appcode.indexOf(",")!=-1){
					String[] app = appcode.split(",");
					sb.append(" AND (");
					for(int i=0;i<app.length;i++){
						sb.append("(appCode:").append(app[i]).append("*) OR ");
					}
					String cssb = sb.substring(0,sb.length()-3)+")";
					sb = new StringBuffer();
					sb.append(cssb);
				}else{
					sb.append(" AND (appCode:").append(appcode).append("*) ");
				}
			}
		}
		//sb.append(" AND (isDelete:").append(RedisKeyConst.Search.IS_DELETE_SHOW).append("~0) ");//显示 
		return this.list(limit, sb.toString());
	}

	@Override
	public String save(SearchAppDTO searchAppDTO) throws Exception {
		
		String flag = this.validDate(searchAppDTO);
		if(!flag.equals("OK"))
			return flag;
		searchAppDTO.setCreateTime(new Date());
		searchAppDTO.setCurrentDir(RedisKeyConst.Search.A_DIR);
		//操作人，待修改
//		searchAppDTO.setOperator("1");
		searchAppDTO.setState(RedisKeyConst.Search.STOP);//默认停用
		searchAppDTO.setIsDelete(RedisKeyConst.Search.IS_DELETE_SHOW);
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_NO);//默认不需要重启（因为已经停用）
		this.saveTX(searchAppDTO);
		return flag;
	}

	private String validDate(SearchAppDTO dto) {
		String flag = "OK";
		if(dto.getAppName().equals("")){
			return flag = "业务应用名称不能为空!";
		}else if(dto.getAppCode().equals("")){
			return flag = "应用编码不能为空!";
		}
		else if(dto.getIndexType().equals("")){
			return flag = "请选择索引方式!";
		}
		else if(dto.getIndexPath().equals("")){
			return flag = "索引路径不能为空!";
		}
		else if(!dto.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_TUI)){
    		if(dto.getLoopCindexTime() == null || dto.getLoopCindexTime().equals("")){
    			return flag = "定时重建索引不能为空!";
    		}
		}else if(dto.getIPS().equals("")){
		    return "IP规则不能为空!";
		}
		String hql = " AND (isDelete:" + RedisKeyConst.Search.IS_DELETE_SHOW + ")";
		if(dto.getId() != null && !dto.getId().equals(""))
			hql += " NOT (id:" + dto.getId()+")";
		
		//业务应用名称唯一
		List list2 = this.getSearchAppDTO("  (appName:" +dto.getAppName() +"~0"+ ")" + hql);
		
		if(list2 != null && list2.size()>0){
		return	flag = "业务应用名称重复，请重新录入！";
		}
		
		List list1 = this.getSearchAppDTO("  (appCode:" + dto.getAppCode() + "~0)" + hql);
		if(list1 != null && list1.size()>0){
		return	flag = "应用编码不能重复，请重新录入！";
		}
		
		//索引路径唯一
		List list = this.getSearchAppDTO("  (indexPath:" + escape(dto.getIndexPath()) + "~0)" + hql);
		if(list != null && list.size()>0){
			return flag = "索引路径不能重复，请重新录入！";
		}
		//appCode唯一
	
		return flag;
	}

	 public static String escape(String s) {
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < s.length(); i++) {
		      char c = s.charAt(i);
		      // These characters are part of the query syntax and must be escaped
		      if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
		        || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
		        || c == '*' || c == '?' || c == '|' || c == '&' || c == '/') {
		        sb.append('\\');
		      }
		      sb.append(c);
		    }
		    return sb.toString();//.toLowerCase();
		  }
	@Override
	public String update(SearchAppDTO searchAppDTO) throws Exception {
		//根据id获得searchapp，填充数据
		SearchAppDTO m = this.get(searchAppDTO.getId());
		searchAppDTO.setCreateTime(m.getCreateTime());
//		searchAppDTO.setOperator(m.getOperator());
		searchAppDTO.setCurrentDir(m.getCurrentDir());
		searchAppDTO.setState(m.getState());
		searchAppDTO.setIsDelete(m.getIsDelete());
		searchAppDTO.setIsReset(RedisKeyConst.Search.RESET_YES);
		searchAppDTO.setUpdateTime(new Date());
		if(m.getIndexType() == null || "".equals(m.getIndexType()))
			searchAppDTO.setIndexType(m.getIndexType());
		if(m.getLoopCindexTime() == null && "".equals(m.getLoopCindexTime()))
			searchAppDTO.setLoopCindexTime(m.getLoopCindexTime());
		if(m.getIndexPath() == null || "".equals(m.getIndexPath()))
			searchAppDTO.setIndexPath(m.getIndexPath());
		
		String flag = this.validDate(searchAppDTO);
		if(flag == "OK"){
			this.updateTX(searchAppDTO);
			if(!searchAppDTO.getAppCode().equals(m.getAppCode()))
			{
				LimitInfo limit1 = new LimitInfo();
				List<SearchCommandDTO> delDtoLst1 = searchCommandService.list(limit1, "appId:"+searchAppDTO.getId());
				searchCommandService.updateListTX(delDtoLst1,searchAppDTO.getAppCode());
				
				
				
				List<SearchRuleDateDTO> delDtoLst2 = searchRuleDateService.list(limit1,"appId:"+searchAppDTO.getId());
				searchRuleDateService.updateListTX(delDtoLst2,searchAppDTO.getAppCode());
				
				List<SearchCommandDTO> commandList = searchCommandService
						.searchCommand(searchAppDTO.getId());// 读取command
				List<SearchRuleDateDTO> ruleDateList = searchRuleDateService
						.searchRuleDate(searchAppDTO.getId());// 读取RuleDate
				
				this.editCache(searchAppDTO, commandList, ruleDateList);
			}
		}
		
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
		boolean isNew=false;
		if (cacheDTO == null)
		{
			cacheDTO = new AppCacheDTO();
			isNew =true;
			
		}
		 updateTX(searchAppDTO);
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
		
	
		//if(isNew){
				addJisuanCatch(searchAppDTO);
		//	}
		}
	private  void addJisuanCatch(SearchAppDTO searchAppDTO)
	{
		try{
				 Map<String, CreateIndexProcessLogDTO> itemMapA =null;
			     Map<String, CreateIndexProcessLogDTO> itemMapB =null;
				//把新加app放入计算log进度放入缓存
					if(searchAppDTO.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI)
			        		 || searchAppDTO.getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE)){
					 Map<String, Map<String, CreateIndexProcessLogDTO>> tmap =  AppContext.getCindexProcessMap();
				    
				     
				     
				     itemMapA =tmap.get("A"+searchAppDTO.getAppCode());
			         if(itemMapA==null)
			         { 
			        	 itemMapA =new HashMap<String, CreateIndexProcessLogDTO>();
			        	 tmap.put("A"+searchAppDTO.getAppCode(), itemMapA);
			         }
			         itemMapB =tmap.get("B"+searchAppDTO.getAppCode());
			         if(itemMapB==null)
			         { 
			        	 itemMapB =new HashMap<String, CreateIndexProcessLogDTO>();
			        	 tmap.put("B"+searchAppDTO.getAppCode(), itemMapB);
			         }//A+appcode and B+appcode 前台请求的时候根据库和appcode直接找到对应map
		        }
		        // a,b 库放入相应的map
		        SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
		        List<SearchCommandDTO> commandList11 = searchCommandService.searchCommand(searchAppDTO.getId());//读取command
		        for (SearchCommandDTO commandDTO : commandList11) {
		       	 CreateIndexProcessLogDTO acreateIndexProcessLogDTO=new CreateIndexProcessLogDTO();
		       	 acreateIndexProcessLogDTO.setAppcode(searchAppDTO.getAppCode());
		       	 acreateIndexProcessLogDTO.setCommandcode(commandDTO.getCommandCode());
		       	 acreateIndexProcessLogDTO.setCpage("0");
		       	 acreateIndexProcessLogDTO.setTotalpage("0");
		       	 acreateIndexProcessLogDTO.setFlag("1");
		       	 acreateIndexProcessLogDTO.setErrinfo("ok");
		       	 acreateIndexProcessLogDTO.setKu("A");
		       	 itemMapA.put(commandDTO.getCommandCode(), acreateIndexProcessLogDTO);
		       	 
		       	 CreateIndexProcessLogDTO bcreateIndexProcessLogDTO=new CreateIndexProcessLogDTO();
		       	 bcreateIndexProcessLogDTO.setAppcode(searchAppDTO.getAppCode());
		       	 bcreateIndexProcessLogDTO.setCommandcode(commandDTO.getCommandCode());
		       	 bcreateIndexProcessLogDTO.setCpage("0");
		       	 bcreateIndexProcessLogDTO.setTotalpage("0");
		       	 acreateIndexProcessLogDTO.setFlag("1");
		       	 acreateIndexProcessLogDTO.setErrinfo("ok");
		       	 bcreateIndexProcessLogDTO.setKu("B");
		       	 itemMapB.put(commandDTO.getCommandCode(), bcreateIndexProcessLogDTO);
		        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public String validStart(List<SearchCommandDTO> commandList,
			List<SearchRuleDateDTO> ruleDateList, String flag) {
		flag = "OK";
		// 判断业务数据是否存在
	//	List<SearchCommandDTO> commandList = new ArrayList<SearchCommandDTO>();
	//	List<SearchRuleDateDTO> ruleDateList = new ArrayList<SearchRuleDateDTO>();
		if (commandList == null || commandList.size() == 0) {
			return flag = "当前业务数据类型和索引数据配为空，无法启动该应用！";
		}

		String str = "";
		// SearchRuleDateDTO searchRuleDateDTO = new SearchRuleDateDTO();
		for (SearchCommandDTO b : commandList) {
			List<SearchRuleDateDTO> list =	searchRuleDateService.searchRuleDateBySql(" (commandCode:" + b.getCommandCode() + "~0)" );	
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

		return flag;
	}

	@Override
	public Map<String, AppCacheDTO> getReopen(String appCode) {
		 
		   
			SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
			SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
			SearchRuleDateService searchRuleDateService = SpringContextHolder.getBean(SearchRuleDateService.class);
			Map<String, AppCacheDTO> rmap = new HashMap<String, AppCacheDTO>() ;
			
			List<SearchAppDTO> list = null;
			if(appCode!=null && !"".equals(appCode.trim())){
				StringBuffer sb = new StringBuffer();
				sb.append("   (appCode:").append(appCode).append("~0) ");
				sb.append(" AND (isDelete:").append(RedisKeyConst.Search.IS_DELETE_SHOW).append("~0) "); 
				list =  searchAppService.getSearchAppDTO(sb.toString());
			}else
			{
				list = searchAppService.getSearchAppList();
			}
			
			if(list!=null && list.size()>0){
				List<AppCacheDTO> appCacheDTO = new ArrayList<AppCacheDTO>();
				for(SearchAppDTO dto : list){
					AppCacheDTO cacheDTO = new AppCacheDTO();
					cacheDTO.setSearchAppDTO(dto);
					List<SearchCommandDTO> commandList = searchCommandService.searchCommand(dto.getId());//读取command
					List<SearchRuleDateDTO> ruleDateList = searchRuleDateService.searchRuleDate(dto.getId());//读取RuleDate
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
					rmap.put(dto.getAppCode(), cacheDTO);
					
				}
			}
//			String sjson = LuceneJsonUtil.getJSONString(AppContext.getAppCacheDTOMap());
//			System.out.println("==========================================================>>>>>>>>>>"+sjson);
//			Map smap =LuceneJsonUtil.getDTOforme(sjson, Map.class);
//			AppCacheDTO appCacheDTO =LuceneJsonUtil.getDTOforme(smap.get("333").toString(), AppCacheDTO.class);
//			System.out.println("====================rrrrrrrrrrrrrr=============== =>>>>>>>>>>"+appCacheDTO.getSearchAppDTO().getAppCode());
//		 
		return rmap;
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
		this.searchMessageDAOL.add(aearchMessageDTO);
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
	public String getAnalyzerTypeByAppCode(String appCode){
		List list = this.getSearchAppDTO(" appCode : " + appCode +" AND "+"isDelete:"+ RedisKeyConst.Search.IS_DELETE_SHOW);
		if(list!=null && list.size()>0){
			SearchAppDTO searchAppDTO = (SearchAppDTO) list.get(0); 
			return searchAppDTO.getAnalyzerType();
		}
		return null;
	}

	@Override
	public void deleteSqlTX(String Sql) throws AppException {
		searchAppDAOL.del(Sql);
		
	}

}
