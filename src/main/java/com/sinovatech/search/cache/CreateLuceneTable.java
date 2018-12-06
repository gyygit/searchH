package com.sinovatech.search.cache;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.utils.FileUtil;
import com.sinovatech.search.utils.SpringContextHolder;

public class CreateLuceneTable {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(CreateLuceneTable.class);
	private static String dir = GlobalConfig.getProperty("searchh", "lucene_db_path");
	public static void createLuceneTableitem()
	{
		initLuceneIndexproductdemo();
		initLuceneIndexmap();
		 
	}
	private static void initLuceneIndexproductdemo()
	{
		try {
					
					dateTocatch();
					 SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
				        List<SearchAppDTO> list = searchAppService.getSearchAppList();
				        if (list != null && list.size() > 0) {
				            for (SearchAppDTO dto : list) {//检查是否已经存在原生appcode
				            	if("productdemo".equals(dto.getAppCode())){
				            	    log.info("appcode 'productdemo' already exist  return init");
				            		return;
				            	}
				            }
				        }
					log.info("CreateLuceneTable->createLuceneTableitem-productdemo begin");
					String appid =createSearchApp_productdemo();
					createSearchCommand_productdemo1(appid);
					createSearchCommand_productdemo2(appid);
					createSearchRuleDateDAO_productdemoList(appid);		
					log.info("CreateLuceneTable->createLuceneTableitem-productdemo end");
				} catch (AppException e) {
					//出错以后要删除掉
					boolean r =dellucenetable_productdemo();
					e.printStackTrace();
					log.error(r+"createLuceneTableitem-productdemo error",e);
				}
	}
	private static void initLuceneIndexmap()
	{
		try {
					
					 dateTocatchMap();
					 SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
				        List<SearchAppDTO> list = searchAppService.getSearchAppList();
				        if (list != null && list.size() > 0) {
				            for (SearchAppDTO dto : list) {//检查是否已经存在原生appcode
				            	if("mapdemo".equals(dto.getAppCode())){
				            	    log.info("appcode 'mapdemo' already exist  return init");
				            		return;
				            	}
				            }
				        }
					log.info("CreateLuceneTable->createLuceneTableitem-mapdemo begin");
					String appid =createSearchApp_mapdemo();
					createSearchCommand_mapdemo(appid);
					createSearchRuleDateDAO_mapdemoList(appid);		
					log.info("CreateLuceneTable->createLuceneTableitem-mapdemo end");
				} catch (AppException e) {
					//出错以后要删除掉
					boolean r =dellucenetable_mapdemo();
					e.printStackTrace();
					log.error(r+"createLuceneTableitem-mapdemo error",e);
				}
	}
	private static void dateTocatch()
	{
		String path=CreateLuceneTable.class.getClassLoader().getResource("").getPath()+"productdemo.csv";
		log.info("初始化文件位置为["+path+"]");
		List<String>  flist =FileUtil.readTxtFileByLine(path, "GBK");

		String[] ss;
		Map<String,String> itemmap =null;  
		for(String s :flist)
		{
			itemmap =new HashMap<String,String>();
			ss = s.split(",");
			//MARKET_PRICE PRODUCT_ID","MALL_PRICE","BRAND_ID","COLOR","COST","NAME
			//6              0           7            3            21      8   5
			itemmap.put("PRODUCT_ID", ss[0].replaceAll("\"", ""));
			itemmap.put("BRAND_ID", ss[3].replaceAll("\"", ""));
			itemmap.put("NAME", ss[5].replaceAll("\"", ""));
			itemmap.put("MARKET_PRICE", ss[6].replaceAll("\"", ""));
			itemmap.put("MALL_PRICE", ss[7].replaceAll("\"", ""));
			itemmap.put("COST", ss[8].replaceAll("\"", ""));
			itemmap.put("COLOR", ss[21].replaceAll("\"", ""));
			AppContext.demoall.add(itemmap);
			log.info("PRODUCT_ID["+ss[0]+"]"+"BRAND_ID["+ss[3]+"]"+"NAME["+ss[5]+"]"+"MARKET_PRICE["+ss[6]+"]"+"MALL_PRICE["+ss[7]+"]"+"COST["+ss[8]+"]"+"COLOR["+ss[21]+"]");
		}
	}
	private static void dateTocatchMap()
	{
		String path=CreateLuceneTable.class.getClassLoader().getResource("").getPath()+"map.csv";
		log.info("初始化文件位置为["+path+"]");
		List<String>  flist =FileUtil.readTxtFileByLine(path, "GBK");

		String[] ss;
		Map<String,String> itemmap =null;  
		for(String s :flist)
		{
			itemmap =new HashMap<String,String>();
			ss = s.split(",");
			//MARKET_PRICE PRODUCT_ID","MALL_PRICE","BRAND_ID","COLOR","COST","NAME
			//6              0           7            3            21      8   5
			itemmap.put("ID", ss[0].replaceAll("\"", ""));
			itemmap.put("NAME", ss[1].replaceAll("\"", ""));
			itemmap.put("ADDRESS", ss[2].replaceAll("\"", ""));
			itemmap.put("LONGITUDE", ss[3].replaceAll("\"", ""));
			itemmap.put("LATITUDE", ss[4].replaceAll("\"", ""));
			
			AppContext.demoall2.add(itemmap);
			log.info("ID["+ss[0]+"]"+"NAME["+ss[1]+"]"+"ADDRESS["+ss[2]+"]"+"LONGITUDE["+ss[3]+"]"+"LATITUDE["+ss[4]+"]");
		}
	}
	private static boolean dellucenetable_productdemo() 
	{
		boolean r = false;
			 try {
				   SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
				   SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
				   SearchRuleDateService searchRuleDateService = SpringContextHolder.getBean(SearchRuleDateService.class);
				   String del="appCode:productdemo~0";
				  
				   searchAppService.deleteSqlTX(del);
				
				   searchCommandService.deleteSqlTX(del);
				   searchRuleDateService.deleteSqlTX(del);
				   r=true;
			 	} catch (AppException e) {
					// TODO Auto-generated catch block
			 		log.error("dellucenetable_productdemo error",e);
					e.printStackTrace();
				}
				   return r;
		   
	}
	private static boolean dellucenetable_mapdemo() 
	{
		boolean r = false;
			 try {
				   SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
				   SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
				   SearchRuleDateService searchRuleDateService = SpringContextHolder.getBean(SearchRuleDateService.class);
				   String del="appCode:mapdemo~0";
				  
				   searchAppService.deleteSqlTX(del);
				
				   searchCommandService.deleteSqlTX(del);
				   searchRuleDateService.deleteSqlTX(del);
				   r=true;
			 	} catch (AppException e) {
					// TODO Auto-generated catch block
			 		log.error("dellucenetable_productdemo error",e);
					e.printStackTrace();
				}
				   return r;
		   
	}
	private static String createSearchApp_productdemo() throws AppException
	{
		String appName="商城系统demo";
        String appCode="productdemo";
        String currentDir="A";
        String shellPath="";
        String indexPath=dir+File.separator+"productdemo";
        long loopCindexTime=5;
        String IPS="*.*.*.*";
        String indexType="2";
        String analyzerType="1";
        String state="1";
        String isReset="1";
		return  createSearchApp(appName, appCode, currentDir, shellPath, indexPath, loopCindexTime, IPS, indexType, analyzerType, state, isReset);
				
	}
	private static String createSearchApp_mapdemo() throws AppException
	{
		String appName="地图demo";
        String appCode="mapdemo";
        String currentDir="A";
        String shellPath="";
        String indexPath=dir+File.separator+"mapdemo";
        long loopCindexTime=5;
        String IPS="*.*.*.*";
        String indexType="2";
        String analyzerType="1";
        String state="1";
        String isReset="1";
		return  createSearchApp(appName, appCode, currentDir, shellPath, indexPath, loopCindexTime, IPS, indexType, analyzerType, state, isReset);
				
	}
	private static String createSearchCommand_productdemo1(String appid) throws AppException{
		
		//{【passWord】[a123456]}
 	   //{【commandCode】[productdemoone]}
 	   //{【updateTime】[2018-05-21 15:59:50]}
 	   //{【linkAddress】[jdbc:oracle:thin:@192.168.244.3:1521:orcl]}
 	   //{【appCode】[productdemo]}
 	   //{【userName】[lzq]}
 	   //{【operator】[超级管理员]}
 	   //{【extend】[null]}
 	   //{【commandName】[商品业务一]}
 	   //{【viewName】[t_product]}
 	   //{【sqlWhere】[ ]}
 	   //{【createTime】[2018-05-21 00:00:00]}
 	   //{【port】[null]}
 	   //{【isExePro】[2]}
 	   //【localAddress】[null]}
 	   //{【appId】[93759744b13e4030b16dc36510b66608]}
 	   //{【id】[4c0e0bc7872c4c109de3bfca74f7d08d]}
 	   //{【proName】[]}
 	   //{【ftpType】[null]}
		   //{【linkDir】[null]}
			String commandName="商品业务一";
			String commandCode="productdemoone";
			String appCode="productdemo";
			String appId=appid;
			String viewName="t_product";
			String linkAddress="jdbc:oracle:thin:@192.168.244.3:1521:orcl";
			String userName="lzq";
			String passWord="a123456";
			String proName="";
			String sqlWhere="";
			String extend="";
			String port="";
			String ftpType="";
			String isExePro="2";
			String linkDir="";
			String localAddress="";
			return createSearchCommand(commandName, commandCode, appCode, appId, viewName, linkAddress, userName, passWord, proName, sqlWhere, extend, port, ftpType, isExePro, linkDir, localAddress);
					
	}
private static String createSearchCommand_mapdemo(String appid) throws AppException{
		
		 
			String commandName="逃脱室信息";
			String commandCode="taotuomapdemo";
			String appCode="mapdemo";
			String appId=appid;
			String viewName="t_map";
			String linkAddress="jdbc:oracle:thin:@192.168.244.3:1521:orcl";
			String userName="lzq";
			String passWord="a123456";
			String proName="";
			String sqlWhere="";
			String extend="2";
			String port="";
			String ftpType="";
			String isExePro="2";
			String linkDir="";
			String localAddress="";
			return createSearchCommand(commandName, commandCode, appCode, appId, viewName, linkAddress, userName, passWord, proName, sqlWhere, extend, port, ftpType, isExePro, linkDir, localAddress);
					
	}	
private static String createSearchCommand_productdemo2(String appid) throws AppException{
		
		//{【passWord】[a123456]}
	   //{【commandCode】[productdemotwo]}
	   //{【updateTime】[]}
	   //{【linkAddress】[jdbc:oracle:thin:@192.168.244.3:1521:orcl]}
	   //{【appCode】[productdemo]}
	   //{【userName】[lzq]}
	   //{【operator】[超级管理员]}
	   //{【extend】[null]}
	   //{【commandName】[商品业务二]}
	   //{【viewName】[t_product]}
	   //{【sqlWhere】[ ]}
	   //{【createTime】[2018-05-21 16:48:11]}
	   //{【port】[null]}
	   //{【isExePro】[2]}
	   //{【localAddress】[null]}
	   //{【appId】[93759744b13e4030b16dc36510b66608]}
	   //{【id】[8d1bb180a7934c178b96dc2d8c61960a]}
	   //{【proName】[]}
	   //{【ftpType】[null]}
	   //{【linkDir】[null]}-
			String commandName="商品业务二";
			String commandCode="productdemotwo";
			String appCode="productdemo";
			String appId=appid;
			String viewName="t_product";
			String linkAddress="jdbc:oracle:thin:@192.168.244.3:1521:orcl";
			String userName="lzq";
			String passWord="a123456";
			String proName="";
			String sqlWhere="";
			String extend="";
			String port="";
			String ftpType="";
			String isExePro="2";
			String linkDir="";
			String localAddress="";
			return createSearchCommand(commandName, commandCode, appCode, appId, viewName, linkAddress, userName, passWord, proName, sqlWhere, extend, port, ftpType, isExePro, linkDir, localAddress);
					
	}
	private static String createSearchApp(
			                              String appName,
			                              String appCode,
			                              String currentDir,
			                              String shellPath,
			                              String indexPath,
			                              long loopCindexTime,
			                              String IPS,
			                              String indexType,
			                              String analyzerType,
			                              String state,
			                              String isReset) throws AppException{
	    	 
	    	  SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
	         //{【currentDir】[A]}
	    	 // {【shellPath】[]}
	    	  //{【appName】[商城系统demo]}
	    	  //{【isDelete】[1]}
	    	  //{【updateTime】[2018-05-21 00:00:00]}
	    	  //{【appCode】[productdemo]}
	    	  //{【indexPath】[productdemo]}
	    	  //{【loopCindexTime】[1]}
	    	  //{【IPS】[*.*.*.*]}
	    	  //{【operator】[超级管理员]}
	    	  //{【indexType】[2]} 
	    	  //{【createTime】[2018-05-21 00:00:00]}
	    	  //{【analyzerType】[1]}
	    	  //{【id】[93759744b13e4030b16dc36510b66608]}
	    	  //{【state】[1]}
	    	  //{【isReset】[1]}-

	    	  SearchAppDTO searchAppDTO = new SearchAppDTO();
	    	  searchAppDTO.setCurrentDir(currentDir);
	    	  searchAppDTO.setShellPath(shellPath);
	    	  searchAppDTO.setAppName(appName);
	    	  searchAppDTO.setIsDelete("1");
	    	  searchAppDTO.setUpdateTime(new Date());
	    	  searchAppDTO.setAppCode(appCode);
	    	  searchAppDTO.setIndexPath(indexPath);
	    	  searchAppDTO.setLoopCindexTime(loopCindexTime);
	    	  searchAppDTO.setIPS(IPS);
	    	  searchAppDTO.setOperator("System");
	    	  searchAppDTO.setIndexType(indexType);
	    	  searchAppDTO.setCreateTime(new Date());
	    	  searchAppDTO.setAnalyzerType(analyzerType);
	    	  searchAppDTO.setState(state);
	    	  searchAppDTO.setIsReset(isReset);
	    	  searchAppService.saveTX(searchAppDTO);
	    	  LimitInfo limit = new LimitInfo();
	    	  SearchAppDTO  ss = new SearchAppDTO();
	    	  ss.setAppCode(searchAppDTO.getAppCode());
	    	  
	    	  ss.setIndexType("");
	    	  ss.setState("");
	    	  ss.setAppName("");
	    	  List<SearchAppDTO> list = searchAppService.list(limit, ss, null, null);
	    	  SearchAppDTO searchAppDTOupdate =null;
	    	  for(SearchAppDTO searchAppDTO1:list){
	    		  searchAppDTOupdate = searchAppDTO1;
	    	  }
	    	   String id = searchAppDTOupdate.getId();
	    	   
	    	   return id;
	    	  
	    	 
	    }
	   private static String createSearchCommand(
			   									String commandName,
			   									String commandCode,
			   									String appCode,
			   									String appId,
			   									String viewName,
			   									String linkAddress,
			   									String userName,
			   									String passWord,
			   									String proName,
			   									String sqlWhere,
			   									String extend,
			   									String port,
			   									String ftpType,
			   									String isExePro,
			   									String linkDir,
			   									String localAddress
			   									) throws AppException
	   {
		   //{【passWord】[a123456]}
    	   //{【commandCode】[productdemoone]}
    	   //{【updateTime】[2018-05-21 15:59:50]}
    	   //{【linkAddress】[jdbc:oracle:thin:@192.168.244.3:1521:orcl]}
    	   //{【appCode】[productdemo]}
    	   //{【userName】[lzq]}
    	   //{【operator】[超级管理员]}
    	   //{【extend】[null]}
    	   //{【commandName】[商品业务一]}
    	   //{【viewName】[t_product]}
    	   //{【sqlWhere】[ ]}
    	   //{【createTime】[2018-05-21 00:00:00]}
    	   //{【port】[null]}
    	   //{【isExePro】[2]}
    	   //【localAddress】[null]}
    	   //{【appId】[93759744b13e4030b16dc36510b66608]}
    	   //{【id】[4c0e0bc7872c4c109de3bfca74f7d08d]}
    	   //{【proName】[]}
    	   //{【ftpType】[null]}
		   //{【linkDir】[null]}
    	   //==================================================
    	   //{【passWord】[a123456]}
    	   //{【commandCode】[productdemotwo]}
    	   //{【updateTime】[]}
    	   //{【linkAddress】[jdbc:oracle:thin:@192.168.244.3:1521:orcl]}
    	   //{【appCode】[productdemo]}
    	   //{【userName】[lzq]}
    	   //{【operator】[超级管理员]}
    	   //{【extend】[null]}
    	   //{【commandName】[商品业务二]}
    	   //{【viewName】[t_product]}
    	   //{【sqlWhere】[ ]}
    	   //{【createTime】[2018-05-21 16:48:11]}
    	   //{【port】[null]}
    	   //{【isExePro】[2]}
    	   //{【localAddress】[null]}
    	   //{【appId】[93759744b13e4030b16dc36510b66608]}
    	   //{【id】[8d1bb180a7934c178b96dc2d8c61960a]}
    	   //{【proName】[]}
    	   //{【ftpType】[null]}
    	   //{【linkDir】[null]}-
		   
		   
		 
		   SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
    	   SearchCommandDTO searchCommandDTO1 =new SearchCommandDTO();
    	   searchCommandDTO1.setPassWord(passWord);
    	   searchCommandDTO1.setCommandCode(commandCode);
    	   searchCommandDTO1.setUpdateTime(new Date());
    	   searchCommandDTO1.setLinkAddress(linkAddress);
    	   searchCommandDTO1.setAppCode(appCode);
    	   searchCommandDTO1.setUserName(userName);
    	   searchCommandDTO1.setOperator("System");
    	   searchCommandDTO1.setExtend(extend);
    	   searchCommandDTO1.setCommandName(commandName);
    	   searchCommandDTO1.setViewName(viewName);
    	   searchCommandDTO1.setSqlWhere(sqlWhere);
    	   searchCommandDTO1.setCreateTime(new Date());
    	   searchCommandDTO1.setPort(port);
    	   searchCommandDTO1.setIsExePro(isExePro);
    	   searchCommandDTO1.setAppId(appId);
    	   searchCommandDTO1.setProName(proName);
    	   searchCommandDTO1.setFtpType(ftpType);
    	   searchCommandDTO1.setLinkDir(linkDir);
    	   searchCommandDTO1.setLocalAddress(localAddress);
    	   searchCommandService.saveOrUpdateTX(searchCommandDTO1);
    	   
    	      
	    	 return "";
	   }
	   
	   private static void createSearchRuleDateDAO_productdemoList(String appid) throws AppException
	   {
		        String[] fieldName={"MARKET_PRICE","PRODUCT_ID","MALL_PRICE","BRAND_ID","COLOR","COST","NAME","MARKET_PRICE","PRODUCT_ID","MALL_PRICE","BRAND_ID","COLOR","COST","NAME"};
				String[] commandCode={"productdemoone","productdemoone","productdemoone","productdemoone","productdemoone","productdemoone","productdemoone","productdemotwo","productdemotwo","productdemotwo","productdemotwo","productdemotwo","productdemotwo","productdemotwo"};
				String[] fieldStoreType={"1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
				String[] fileldDateType={"2","4","2","1","1","1","1","2","4","2","1","1","1","1"};
				String[] appCode={"productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo","productdemo"};
				String[] isIntelliSense={"2","2","2","2","2","2","1","2","2","2","2","2","2","1"};
				String[] commandName={"商品业务一","商品业务一","商品业务一","商品业务一","商品业务一","商品业务一","商品业务一","商品业务二","商品业务二","商品业务二","商品业务二","商品业务二","商品业务二","商品业务二"};
				String[] opt={"","","","","","","","","","","","","",""};
				String[] appId={appid,appid,appid,appid,appid,appid,appid,appid,appid,appid,appid,appid,appid,appid};
				String[] fieldIndexType={"2","2","2","2","2","2","3","2","2","2","2","2","2","3"};
				for(int i=0;i<14;i++){
					createSearchRuleDateDAO(fieldName[i], commandCode[i], fieldStoreType[i], fileldDateType[i], appCode[i], isIntelliSense[i], commandName[i], opt[i], appId[i], fieldIndexType[i]);
				}
		   
	   }
	   private static void createSearchRuleDateDAO_mapdemoList(String appid) throws AppException
	   {
		        String[] fieldName={"ID","NAME","ADDRESS","LONGITUDE","LATITUDE"};
				String[] commandCode={"taotuomapdemo","taotuomapdemo","taotuomapdemo","taotuomapdemo","taotuomapdemo"};
				String[] fieldStoreType={"1","1","1","1","1"};
				String[] fileldDateType={"4","1","1","1","1"};
				String[] appCode={"mapdemo","mapdemo","mapdemo","mapdemo","mapdemo"};
				String[] isIntelliSense={"2","1","2","2","2"};
				String[] commandName={"逃脱室信息","逃脱室信息","逃脱室信息","逃脱室信息","逃脱室信息"};
				String[] opt={"","","","","","","","","","","","","",""};
				String[] appId={appid,appid,appid,appid,appid};
				String[] fieldIndexType={"2","3","2","2","2"};
				for(int i=0;i<5;i++){
					createSearchRuleDateDAO(fieldName[i], commandCode[i], fieldStoreType[i], fileldDateType[i], appCode[i], isIntelliSense[i], commandName[i], opt[i], appId[i], fieldIndexType[i]);
				}
		   
	   }
	   private static void createSearchRuleDateDAO( String fieldName,
			   										String commandCode,
			   										String fieldStoreType,
			   										String fileldDateType,
			   										String appCode,
			   										String isIntelliSense,
			   										String commandName,
			   										String opt,
			   										String appId,
			   										String fieldIndexType
			   										) throws AppException{
		   
		    
		   //{【fieldName】[MARKET_PRICE]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[2]}{【updateTime】[]}
		   //{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】
		   //[2018-05-21 16:43:59]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[c8ad417437b34cd69f05710cbd2040db]}
		   //{【fieldIndexType】[2]}-
		   //{【fieldName】[PRODUCT_ID]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[4]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 15:58:42]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[c4ff95171db948d2835704f818ed1417]}{【fieldIndexType】[2]}-
		   //{【fieldName】[MALL_PRICE]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[2]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 16:46:22]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[9ddff39fca864448a1e37af4f3ace824]}{【fieldIndexType】[2]}-
		   //{【fieldName】[MALL_PRICE]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[2]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:48:53]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[c2cd338cb98240469cde82f65dce6ea7]}{【fieldIndexType】[2]}-
		   //{【fieldName】[BRAND_ID]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 16:45:54]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[56ea7d66b84e4fc2ad36ea20f7b2c66c]}{【fieldIndexType】[2]}-
		   //{【fieldName】[BRAND_ID]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:49:13]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[73d3f221d4464013a8f3ef5f01d77995]}{【fieldIndexType】[2]}-
		   //{【fieldName】[COLOR]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 16:45:26]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[afe220f1a7a1413ebab7f052afe5c9e0]}{【fieldIndexType】[2]}-
		   //{【fieldName】[COLOR]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:49:37]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[a41abc2923ee4a00a00f034529473be5]}{【fieldIndexType】[2]}-
		   //{【fieldName】[NAME]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[1]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 16:44:22]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[1f33293e4dfa4534996a4ec8a98e4e47]}{【fieldIndexType】[3]}-
		   //{【fieldName】[COST]}{【commandCode】[productdemoone]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务一]}{【opt】[null]}{【createTime】[2018-05-21 16:44:53]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[94c50466619447c3afeaa1be52cf2410]}{【fieldIndexType】[2]}-
		   //{【fieldName】[COST]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:49:58]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[f52fe0ba6f96498eb068f0e5acc676e5]}{【fieldIndexType】[2]}-
		   //{【fieldName】[NAME]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[1]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[1]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:50:22]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[5fdfd1e4c82e4b06b85116139b13d121]}{【fieldIndexType】[3]}-
		   //{【fieldName】[MARKET_PRICE]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[2]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:51:11]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[7baec51cd7ea42049032a97111b45ac8]}{【fieldIndexType】[2]}-
		   //{【fieldName】[PRODUCT_ID]}{【commandCode】[productdemotwo]}{【fieldStoreType】[1]}{【fileldDateType】[4]}{【updateTime】[]}{【appCode】[productdemo]}{【operator】[超级管理员]}{【isIntelliSense】[2]}{【commandName】[商品业务二]}{【opt】[null]}{【createTime】[2018-05-21 16:51:32]}{【appId】[93759744b13e4030b16dc36510b66608]}{【isUrl】[null]}{【id】[95c281b74bc141dba18208aa13cb4aef]}{【fieldIndexType】[2]}-
		   SearchRuleDateDTO searchRuleDateDTO = new SearchRuleDateDTO();
		   searchRuleDateDTO.setFieldName(fieldName);
		   searchRuleDateDTO.setCommandCode(commandCode);
		   searchRuleDateDTO.setFieldStoreType(fieldStoreType);
		   searchRuleDateDTO.setFileldDateType(fileldDateType);
		   searchRuleDateDTO.setUpdateTime(new Date());
		   searchRuleDateDTO.setAppCode(appCode);
		   searchRuleDateDTO.setOperator("System");
		   searchRuleDateDTO.setIsIntelliSense(isIntelliSense);
		   searchRuleDateDTO.setCommandName(commandName);
		   searchRuleDateDTO.setOpt(opt);
		   searchRuleDateDTO.setCreateTime(new Date());
		   searchRuleDateDTO.setAppId(appId);
		   searchRuleDateDTO.setIsUrl(null);
		   searchRuleDateDTO.setFieldIndexType(fieldIndexType);
		   SearchRuleDateService searchRuleDateService = SpringContextHolder.getBean(SearchRuleDateService.class);
		   searchRuleDateService.saveOrUpdateTX(searchRuleDateDTO);
	   }
}
