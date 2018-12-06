package com.sinovatech.search.luceneindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
/**
 * AppContext工具类
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * 
 * @author 作者liuzhenquan
 */
public class AppContext {
	private static final Log log = org.apache.commons.logging.LogFactory.getLog(AppContext.class);
	public static  List<Map<String,String>> demoall = new ArrayList<Map<String,String>>();
	public static  List<Map<String,String>> demoall2 = new ArrayList<Map<String,String>>();
	//创建内容索引库的IndexWrite
	private static Map<String,IndexWriter> aIndexWriterMap = new HashMap<String,IndexWriter>();
	private static Map<String,IndexWriter> bIndexWriterMap = new HashMap<String,IndexWriter>();
	//创建热词索引库的IndexWrite
    private static Map<String,IndexWriter> hotWordIdexWriterMap = new HashMap<String,IndexWriter>();
    
    //创建业务查询search
    private static Map<String,IndexSearcher> aIndexSearcherMap = new HashMap<String,IndexSearcher>();
    private static Map<String,IndexSearcher> bIndexSearcherMap = new HashMap<String,IndexSearcher>();
    private static Map<String,IndexSearcher> hotIndexSearcherMap = new HashMap<String,IndexSearcher>();
	//注册业务缓存
	private static Map<String,AppCacheDTO> appCacheDTOMap = new HashMap<String,AppCacheDTO>();
	//定时器操作句柄
	private static Map<String,ScheduledFuture<?>> scheduledFutureMap = new HashMap<String,ScheduledFuture<?>>();
    //创建索引定时器线程池
	private static ScheduledExecutorService	executor = null;
	//创建热词索引定时器线程池
	private static ScheduledExecutorService	hotExecutor = null;
	//创建推送索引定时器线程池
	private static ScheduledExecutorService	tuiExecutor = null;
	//点击日志保存定时器线程池
	private static ScheduledExecutorService clicklogExecutor = null;
	
	//定时备份lucene_tables 索引库
	private static ScheduledExecutorService luceneTablesExecutor = null;
	
	public  static ArrayBlockingQueue<CreateIndexProcessLogDTO>  cindexlogBlockingQueue= new ArrayBlockingQueue<CreateIndexProcessLogDTO>(100000);
	
	private  static Map<String,Map<String,CreateIndexProcessLogDTO>> cindexProcessMap = new HashMap<String,Map<String,CreateIndexProcessLogDTO>>();

	public static ScheduledExecutorService getLuceneTablesExecutor() {
		return luceneTablesExecutor;
	}



	public static void setLuceneTablesExecutor(
			ScheduledExecutorService luceneTablesExecutor) {
		AppContext.luceneTablesExecutor = luceneTablesExecutor;
	}
	//"appcode"-<commandcode>map
	public static Map<String, Map<String, CreateIndexProcessLogDTO>> getCindexProcessMap() {
		return cindexProcessMap;
	}



	public static Map<String, IndexWriter> getHotWordIdexWriterMap() {
		return hotWordIdexWriterMap;
	}



	public static ScheduledExecutorService getExecutor() {
		return executor;
	}



	public static void setExecutor(ScheduledExecutorService executor) {
		AppContext.executor = executor;
	}



	public static Map<String, ScheduledFuture<?>> getScheduledFutureMap() {
		return scheduledFutureMap;
	}


	public static Map<String, IndexWriter> getaIndexWriterMap() {
		return aIndexWriterMap;
	}



	public static Map<String, IndexWriter> getbIndexWriterMap() {
		return bIndexWriterMap;
	}



	public static Map<String, AppCacheDTO> getAppCacheDTOMap() {
		return appCacheDTOMap;
	}
	
	public static ScheduledExecutorService getHotExecutor() {
		return hotExecutor;
	}



	public static void setHotExecutor(ScheduledExecutorService hotExecutor) {
		AppContext.hotExecutor = hotExecutor;
	}

	public static ScheduledExecutorService getTuiExecutor() {
		return tuiExecutor;
	}



	public static void setTuiExecutor(ScheduledExecutorService tuiExecutor) {
		AppContext.tuiExecutor = tuiExecutor;
	}

	public static ScheduledExecutorService getClicklogExecutor() {
		return clicklogExecutor;
	}



	public static void setClicklogExecutor(ScheduledExecutorService clicklogExecutor) {
		AppContext.clicklogExecutor = clicklogExecutor;
	}



	public static Map<String, IndexSearcher> getaIndexSearcherMap() {
		return aIndexSearcherMap;
	}



	public static Map<String, IndexSearcher> getbIndexSearcherMap() {
		return bIndexSearcherMap;
	}



	public static Map<String, IndexSearcher> getHotIndexSearcherMap() {
		return hotIndexSearcherMap;
	}
}
