package com.sinovatech.search.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.web.init.IAppInitJob;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.luceneindex.task.CindexLogTread;
import com.sinovatech.search.luceneindex.task.CpLuceneTablesTask;
import com.sinovatech.search.luceneindex.task.IndexWriteTask;
import com.sinovatech.search.luceneindex.task.TuiIndexWriteTask;
import com.sinovatech.search.luceneindex.task.hotWordIndexWriteTask;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchCommandService;
import com.sinovatech.search.services.SearchRuleDateService;
import com.sinovatech.search.utils.SpringContextHolder;

public class InitSearchApp implements IAppInitJob {
    
    private static final Logger log = LoggerFactory.getLogger(InitSearchApp.class);

    /**
     * 执行初始化，系统调用
     */
    public void doJob(ServletContext paramServletContext) {
        this.refreshAllCache(paramServletContext); //werwer

    }

    /**
     * 初始化其他杂项，如环境路径等
     * 
     * @param context
     */
    @SuppressWarnings("unchecked")
    private void refreshAllCache(ServletContext context) {
        try {
        	log.info("initZero(context) begin ====");
        	initZero(context);
        	log.info("initZero(context) end ====");
            log.info("initOne(context) begin ====");
            initOne(context);
            log.info("initOne(context) end ====");
            log.info("initTwo(context); begin ====");
            initTwo(context);
            log.info("initTwo(context); end ====");
            log.info("initThree(context); begin ====");
            initThree(context);
            log.info("initThree(context); end ====");
            
            log.info("initFour(context); begin ====");
            initFour(context);
            log.info("initFour(context); end ====");
        } catch (Exception e) {
            log.error("init InitLucenBank search error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    //创建最初的搜索配置信息
    private void initZero(ServletContext context) {
    	CreateLuceneTable.createLuceneTableitem();
        
    }
  

    //初始化缓存
    private void initOne(ServletContext context) {
        SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
        SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
        SearchRuleDateService searchRuleDateService = SpringContextHolder.getBean(SearchRuleDateService.class);
        List<SearchAppDTO> list = searchAppService.getSearchAppList();
        if (list != null && list.size() > 0) {
            for (SearchAppDTO dto : list) {
                AppCacheDTO cacheDTO = new AppCacheDTO();
                cacheDTO.setSearchAppDTO(dto);
                List<SearchCommandDTO> commandList = searchCommandService.searchCommand(dto.getId());//读取command
                List<SearchRuleDateDTO> ruleDateList = searchRuleDateService.searchRuleDate(dto.getId());//读取RuleDate
                Map<String, SearchCommandDTO> commandDTOMap = new HashMap<String, SearchCommandDTO>();//<commandCode,SearchCommandDTO>
                Map<String, SearchRuleDateDTO> searchRuleDateDTOMap = new HashMap<String, SearchRuleDateDTO>();//<flied,searchRuleDateDTOMap>
                for (SearchCommandDTO commandDTO : commandList) {
                    commandDTOMap.put(commandDTO.getCommandCode(), commandDTO);
                }
                for (SearchRuleDateDTO ruleDateDTO : ruleDateList) {
                    searchRuleDateDTOMap.put(ruleDateDTO.getFieldName()+"_"+ruleDateDTO.getCommandCode(), ruleDateDTO);
                }
                cacheDTO.setSearchCommandDTOMap(commandDTOMap);
                cacheDTO.setSearchRuleDateDTOMap(searchRuleDateDTOMap);
                AppContext.getAppCacheDTOMap().put(dto.getAppCode(), cacheDTO);
            }
        }
    }

    //初始化indexWrite
    private void initTwo(ServletContext context) {
        AppCacheDTO appCacheDTO = null;
        for (Entry<String, AppCacheDTO> entry : AppContext.getAppCacheDTOMap().entrySet()) {
            appCacheDTO = entry.getValue();
            //    		if(LuceneManager.checkAppCodeIsStop(appCacheDTO.getSearchAppDTO().getAppCode()))
            //			{ 
            //				log.info("initTwo的appCode["+appCacheDTO.getSearchAppDTO().getAppCode()+"]已停用");
            //				continue;
            //			}
            //创建视图索引工具到缓存
            if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(appCacheDTO.getSearchAppDTO().getIndexType())
                    || RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(appCacheDTO.getSearchAppDTO().getIndexType())) {
                LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(), appCacheDTO.getSearchAppDTO()
                        .getIndexPath() + "/" + RedisKeyConst.Search.A_DIR_VALUE, "1", true,
                        RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_A);
                LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(), appCacheDTO.getSearchAppDTO()
                        .getIndexPath() + "/" + RedisKeyConst.Search.B_DIR_VALUE, "1", true,
                        RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_B);
            } else {
                //创建推送索引工具到缓存
                LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(), appCacheDTO.getSearchAppDTO()
                        .getIndexPath() + "/" + RedisKeyConst.Search.A_DIR_VALUE, "3", true,
                        RedisKeyConst.Search.SEARCH_INDEX_TYPE_TUI_A);
                System.out.println("appCacheDTO.getSearchAppDTO().getAppCode():" + appCacheDTO.getSearchAppDTO().getAppCode());
            }
            //创建热词索引工具到缓存
            LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(), appCacheDTO.getSearchAppDTO()
                    .getIndexPath() + "/" + RedisKeyConst.Search.HOT_DIR_VALUE, "3", true,
                    RedisKeyConst.Search.SEARCH_INDEX_TYPE_HOT);
        }
    }

    //初始化定时任务
    private void initThree(ServletContext context) {

        int poolSize = AppContext.getAppCacheDTOMap().size();
        AppContext.setExecutor(Executors.newScheduledThreadPool(poolSize + 1));
        Map<String, IndexWriteTask> indexWriteTaskMap = new HashMap<String, IndexWriteTask>();

        for (Entry<String, AppCacheDTO> entry : AppContext.getAppCacheDTOMap().entrySet()) {
            AppCacheDTO appCacheDTO = entry.getValue();
            if (appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI)
                    || appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE)
                    || appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_FTP)) {
                indexWriteTaskMap.put(appCacheDTO.getSearchAppDTO().getAppCode(), new IndexWriteTask(appCacheDTO
                        .getSearchAppDTO().getAppCode()));
            }
        }
        for (Entry<String, IndexWriteTask> entry : indexWriteTaskMap.entrySet()) {
            IndexWriteTask indexWriteTask = entry.getValue();
            String appcode = entry.getKey();
            log.info("初始化定时任务indexWriteTask-appcode[" + appcode + "]begin");
            AppContext.getScheduledFutureMap().put(
                    appcode,
                    AppContext.getExecutor().scheduleAtFixedRate(indexWriteTask, 0,
                            AppContext.getAppCacheDTOMap().get(appcode).getSearchAppDTO().getLoopCindexTime(), TimeUnit.MINUTES));
            log.info("初始化定时任务indexWriteTask-appcode[" + appcode + "]end");
        }

        int time = 10;
        try {
            time = Integer.parseInt(GlobalConfig.getProperty("searchh", "hottasktime"));

        } catch (Exception e) {
            e.printStackTrace();
            log.info("initThree-error 的time =Integer.parseInt:hottasktime", e);
        }
        AppContext.setHotExecutor(Executors.newScheduledThreadPool(1));
        log.info("初始化定时任务hotWordIndexWriteTask-begin");
        AppContext.getHotExecutor().scheduleAtFixedRate(new hotWordIndexWriteTask(), 0, time, TimeUnit.MINUTES);
        log.info("初始化定时任务hotWordIndexWriteTask-end");

        int tuitasktime = 5;
        try {
            tuitasktime = Integer.parseInt(GlobalConfig.getProperty("searchh", "tuitasktime"));

        } catch (Exception e) {
            e.printStackTrace();
            log.info("initThree-error 的tuitasktime =Integer.parseInt:tuitasktime", e);
        }
        AppContext.setTuiExecutor(Executors.newScheduledThreadPool(1));
        log.info("初始化定时任务TuiIndexWriteTask-begin");
        AppContext.getTuiExecutor().scheduleAtFixedRate(new TuiIndexWriteTask(), 0, tuitasktime, TimeUnit.SECONDS);
        log.info("初始化定时任务TuiIndexWriteTask-end");
        
        Thread cindexLogTread  = new Thread(new CindexLogTread());
        try{
        log.info("初始化定时任务cindexLogTread-begin");
        	cindexLogTread.start();
        log.info("初始化定时任务cindexLogTread-end");
        }catch(Exception e)
        {
        	e.printStackTrace();
           log.error("初始化定时任务cindexLogTread-error",e);
        }
        try{
        AppContext.setLuceneTablesExecutor(Executors.newScheduledThreadPool(1));
        log.info("初始化定时备份配置索引库lucene_tables任务LuceneTablesTask-begin");
        AppContext.getLuceneTablesExecutor().scheduleAtFixedRate(new CpLuceneTablesTask(), 0, 1, TimeUnit.DAYS);
        log.info("初始化定时备份配置索引库lucene_tables任务LuceneTablesTask-end");
        }catch(Exception e)
        {
        	e.printStackTrace();
           log.error("初始化定时备份配置索引库lucene_tables任务-error",e);
        }
    }
  
    private void initFour(ServletContext context){
    log.info("-----------initFour 索引创建速度监控缓存begin---------------");
    try {
     Map<String, Map<String, CreateIndexProcessLogDTO>> tmap =  AppContext.getCindexProcessMap();
     Map<String, CreateIndexProcessLogDTO> itemMapA =null;
     Map<String, CreateIndexProcessLogDTO> itemMapB =null;
     for (Entry<String, AppCacheDTO> entry : AppContext.getAppCacheDTOMap().entrySet()) {
         AppCacheDTO appCacheDTO = entry.getValue();
         if(appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI)
        		 || appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE)){
        	 itemMapA =tmap.get("A"+appCacheDTO.getSearchAppDTO().getAppCode());
	         if(itemMapA==null)
	         {
	        
	        	 itemMapA =new HashMap<String, CreateIndexProcessLogDTO>();
	        	 tmap.put("A"+appCacheDTO.getSearchAppDTO().getAppCode(), itemMapA);
	         }
	         itemMapB =tmap.get("B"+appCacheDTO.getSearchAppDTO().getAppCode());
	         if(itemMapB==null)
	         {
	        	 itemMapB =new HashMap<String, CreateIndexProcessLogDTO>();
	        	 tmap.put("B"+appCacheDTO.getSearchAppDTO().getAppCode(), itemMapB);
	         }//A+appcode and B+appcode 前台请求的时候根据库和appcode直接找到对应map
         }
         // a,b 库放入相应的map
         SearchCommandService searchCommandService = SpringContextHolder.getBean(SearchCommandService.class);
         List<SearchCommandDTO> commandList = searchCommandService.searchCommand(appCacheDTO.getSearchAppDTO().getId());//读取command
         for (SearchCommandDTO commandDTO : commandList) {
        	 CreateIndexProcessLogDTO acreateIndexProcessLogDTO=new CreateIndexProcessLogDTO();
        	 acreateIndexProcessLogDTO.setAppcode(appCacheDTO.getSearchAppDTO().getAppCode());
        	 acreateIndexProcessLogDTO.setCpage("0");
        	 acreateIndexProcessLogDTO.setTotalpage("0");
        	 acreateIndexProcessLogDTO.setCommandcode(commandDTO.getCommandCode());
        	 acreateIndexProcessLogDTO.setFlag("1");
        	 acreateIndexProcessLogDTO.setErrinfo("ok");
        	 acreateIndexProcessLogDTO.setKu("A");
        	 itemMapA.put(commandDTO.getCommandCode(), acreateIndexProcessLogDTO);
        	 
        	 CreateIndexProcessLogDTO bcreateIndexProcessLogDTO=new CreateIndexProcessLogDTO();
        	 bcreateIndexProcessLogDTO.setAppcode(appCacheDTO.getSearchAppDTO().getAppCode());
        	 bcreateIndexProcessLogDTO.setCpage("0");
        	 bcreateIndexProcessLogDTO.setTotalpage("0");
        	 bcreateIndexProcessLogDTO.setCommandcode(commandDTO.getCommandCode());
        	 bcreateIndexProcessLogDTO.setFlag("1");
        	 bcreateIndexProcessLogDTO.setErrinfo("ok");
        	 bcreateIndexProcessLogDTO.setKu("B");
        	 itemMapB.put(commandDTO.getCommandCode(), bcreateIndexProcessLogDTO);
         }
        
     }
     log.info("-----------initFour 索引创建速度监控缓存end---------------");
    } catch (Exception e) {
		e.printStackTrace();
		log.info("initFour-error init初始化索引创建监控缓存",	e);
	}
	 
 }
//    private void initfive(ServletContext context){
//		int clicklogtasktime = 10;
//		try {
//			clicklogtasktime = Integer.parseInt(GlobalConfig.getProperty(
//					"searchh", "clicklogtasktime"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.info(
//					"initFour-error 的clicklogtasktime =Integer.parseInt:clicklogtasktime",
//					e);
//		}
//		AppContext.setClicklogExecutor(Executors.newScheduledThreadPool(1));
//		log.info("------初始化定时任务ClickVisitlogTask-begin--------");
//		AppContext.getClicklogExecutor().scheduleAtFixedRate(
//				new ClickVisitlogTask(), 0, clicklogtasktime, TimeUnit.MINUTES);
//		log.info("-----------初始化定时任务ClickVisitlogTask-end---------------");
    	
 //   }
}
