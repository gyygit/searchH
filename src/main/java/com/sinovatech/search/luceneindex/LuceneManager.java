package com.sinovatech.search.luceneindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.constants.LoggerConstants;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchKeywordLogDTO;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.entity.TuiHelpDTO;
import com.sinovatech.search.luceneindex.db.DataBaseManager;
import com.sinovatech.search.luceneindex.task.IndexWriteTask;
import com.sinovatech.search.services.CollectInfoService;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SearchKeywordLogService;
import com.sinovatech.search.services.SearchMessageService;
import com.sinovatech.search.utils.HttpClientUtil;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.PinyinUtil;
import com.sinovatech.search.utils.SpringContextHolder;
import com.sinovatech.search.utils.Lucene.FTP;
import com.sinovatech.search.utils.Lucene.FTPAbstract;
import com.sinovatech.search.utils.Lucene.SFTP;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

/**
 * LuceneManager工具类
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * 
 * @author 作者liuzhenquan
 */
public class LuceneManager {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(LuceneManager.class);
	private static final String rs ="00000000000.00";
	private static final String rs1 ="00000000000";
	private static final String rs2 ="00"; 
   
	private static SpatialContext ctx;// "ctx" is the conventional variable name  
	  
	    /** 提供索引和查询模型的策略接口 */  
    private static SpatialStrategy strategy;  
    
    static{
    	//地图搜索
    	ctx = SpatialContext.GEO;  
        //网格最大11层, geohash的精度  
        int maxLevels = 11;  
        //Spatial Tiers  
        SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);  
        strategy = new RecursivePrefixTreeStrategy(grid, "myGeoField");  
    }

//	private  static Analyzer analyzer =  new IKAnalyzer(); 
	//private  static Analyzer analyzer =  new StandardAnalyzer(Version.LUCENE_47); 
	/**
	 * 自动获得使用那个IndexWrite的Map
	 * 
	 * @param appcode
	 * @return
	 */
	public static Map<String, IndexWriter> autoGetIndexWriteMap(String appcode) {
		String flagDir = AppContext.getAppCacheDTOMap().get(appcode)
				.getSearchAppDTO().getCurrentDir();
		Map<String, IndexWriter> rmap = null;
		if (RedisKeyConst.Search.A_DIR.equals(flagDir)) {
			rmap = AppContext.getaIndexWriterMap();
		}
		if (RedisKeyConst.Search.B_DIR.equals(flagDir)) {
			rmap = AppContext.getbIndexWriterMap();
		}
		 
		 log.info("autoGetIndexWriteMap AppCode["+appcode+"]getCurrentDir()["+flagDir+"]库");
		return rmap;
	}
	public static Map<String, IndexWriter> autoGetIndexWriteMapSearchUseCurrentIndexWrite(String appcode) {
		String flagDir = AppContext.getAppCacheDTOMap().get(appcode)
				.getSearchAppDTO().getCurrentDir();
		Map<String, IndexWriter> rmap = null;
		if (RedisKeyConst.Search.A_DIR.equals(flagDir)) {
			rmap = AppContext.getbIndexWriterMap();
		}
		if (RedisKeyConst.Search.B_DIR.equals(flagDir)) {
			rmap = AppContext.getaIndexWriterMap();
		}
		 
		 log.info("autoGetIndexWriteMapSearchUseCurrentIndexWrite AppCode["+appcode+"]getCurrentDir()["+flagDir+"]库");
		return rmap;
	}

	  /**
	    * 根据appCode和type获得搜索工具
	    * @param appCode
	    * @param type：1,3推送2,4视图hot热词
	    */
	   public static IndexSearcher autoGetIndexSearcher(String appCode,boolean ishot)
	   {
		   String a="a";String b="b"; String c="";
		   AppCacheDTO  appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode); 
		   String type =appCacheDTO.getSearchAppDTO().getIndexType();
		   IndexSearcher indexSearcher =null;
		   Map<String, IndexSearcher> tmpMapIndexSearch=null;
		   
		   SearchAppDTO searchAppDTOtmp =  appCacheDTO.getSearchAppDTO(); 
		   String lucenePath ="";
		   if(ishot)
		   {
			   tmpMapIndexSearch=AppContext.getHotIndexSearcherMap();
			   lucenePath = searchAppDTOtmp.getIndexPath()+"/"+RedisKeyConst.Search.HOT_DIR_VALUE;
		   }else {
			   //3ftp4服务
 
			   if(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_TUI.equals(type)||RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_FTP.equals(type))
			   {
				   tmpMapIndexSearch = AppContext.getaIndexSearcherMap();
				   lucenePath = searchAppDTOtmp.getIndexPath()+"/"+RedisKeyConst.Search.A_DIR_VALUE;
			   }else if(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(type) ||  RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(type)){
 
					   
					if (RedisKeyConst.Search.A_DIR.equals(appCacheDTO.getSearchAppDTO().getCurrentDir())) {
						tmpMapIndexSearch = AppContext.getbIndexSearcherMap();
						lucenePath = searchAppDTOtmp.getIndexPath()+"/"+RedisKeyConst.Search.B_DIR_VALUE;
						c=b;
					}
					if (RedisKeyConst.Search.B_DIR.equals(appCacheDTO.getSearchAppDTO().getCurrentDir())) {
						tmpMapIndexSearch = AppContext.getaIndexSearcherMap();
						lucenePath = searchAppDTOtmp.getIndexPath()+"/"+RedisKeyConst.Search.A_DIR_VALUE;
						c=a;
					}
			   }
		 
		   }
		 
		   if(tmpMapIndexSearch!=null){
			   indexSearcher = tmpMapIndexSearch.get(appCode);
			   if(indexSearcher!=null){
				   
				   indexSearcher =  reopen(indexSearcher,lucenePath);
				   tmpMapIndexSearch.put(appCode,indexSearcher);
				   log.info("最终获取IndexSearcher为reopen AppCode["+appCode+"]库："+c);
			   }else{
				   log.info("最终获取IndexSearcher为没有变化的 AppCode["+appCode+"]库："+c);
			   }
		   }
		   
		      
		   return indexSearcher;
	   }
	

		/**
		 * 刷新indexSearcher
		 * 
		 * @param indexSearcher
		 */
		private  static synchronized IndexSearcher reopen(IndexSearcher indexSearcher,String lucenePath) { 
				try {
					log.info("indexSearcher:" + indexSearcher.hashCode());
					IndexReader oldReader = indexSearcher.getIndexReader();

					// 可用状态
					//if (oldReader.getRefCount() > 0) {
						IndexReader newReader = DirectoryReader
								.openIfChanged((DirectoryReader) oldReader);// reader.reopen();
						// //
						// 读入新增加的增量索引内容，满足实时索引需求
						if (newReader != null) {
							log.info("newReader!= null重新刷新缓存reopen-IndexReader 重新构造IndexSearcher[app的]");
							IndexSearcher searcher2 = new IndexSearcher(newReader);
							log.info("旧IndesReader失效，将其关闭[app的]。");
							oldReader.close();
							return searcher2;

						} else {
							log.info("缓存无改动，沿用以前的IndexSearcher[app的]");

							return indexSearcher;
						}
//					} else {
//						log.info("IndexReader被关闭，创建一个新的。[app的]"+lucenePath);
//						//创建新的indexSearch
//						indexSearcher=LuceneManager.createIndexSearcher(lucenePath);
//						return indexSearcher;
//					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("刷新缓存失败[app的]：", e);

					return indexSearcher;
				}
				// return indexSearcher;
			}
//	   /**
//		 * 刷新indexSearcher
//		 * 
//		 * @param indexSearcher
//		 */
//		public static IndexSearcher reopen(IndexSearcher indexSearcher,String lucenePath) {
//			try {
//				log.info("indexSearcher:" + indexSearcher.hashCode());
//				IndexReader oldReader = indexSearcher.getIndexReader();
//				IndexReader newReader = DirectoryReader
//						.openIfChanged((DirectoryReader) oldReader);// reader.reopen();
//																	// //
//																	// 读入新增加的增量索引内容，满足实时索引需求
//				if (newReader != null) {
//					log.info("newReader!= null重新刷新缓存reopen-IndexReader 重新构造IndexSearcher");
//					indexSearcher = new IndexSearcher(newReader);
//					oldReader.close();
//					System.out.println("oldReader==newReader:"
//							+ (oldReader == newReader));
//
//				} else {
//					log.info("newReader == null 刷新缓存为null，则沿用以前的IndexSearcher");
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("刷新缓存失败：", e);
//			}
//			return indexSearcher;
//		}
	/**
	 * 获得索引库方式
	 * 
	 * @param openModeStr
	 * @return
	 */
	private static OpenMode getOpenModeByString(String openModeStr) {
		OpenMode openMode = OpenMode.CREATE_OR_APPEND;
		if ("1".equals(openModeStr)) {
			openMode = OpenMode.CREATE;
		}
		if ("2".equals(openModeStr)) {
			openMode = OpenMode.APPEND;
		}
		if ("3".equals(openModeStr)) {
			openMode = OpenMode.CREATE_OR_APPEND;
		}
		return OpenMode.CREATE_OR_APPEND;
		//	return openMode;
	}

	/**
	 * 创建IndexWrite
	 * 
	 * @param configMap
	 *            configMap<"path","xxxx/xxxx/xxxx/xxx"><"openMode",
	 *            "1:create,2append,3create_append">
	 * @return
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public static IndexWriter createIndexWriter(Map<String, String> configMap)
			throws Exception {
		// 构造分词器
		String path = configMap.get("path");
		String openMode = configMap.get("openMode");
		String appCode = configMap.get("appCode");
//		Analyzer analyzer = new IKAnalyzer();
		// 构造indexWrite相关配置
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
//		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47, analyzerType(appCode));
		iwConfig.setOpenMode(getOpenModeByString(openMode));// 设置索引存储方式
		Directory dir = FSDirectory.open(new File(path));
		IndexWriter indexWriter = new IndexWriter(dir, iwConfig);
		return indexWriter;
	}

	/**
	 * 创建一个索引工具到缓存
	 * 
	 * @param appCode
	 *            接入业务应用代码
	 * @param path
	 *            索引路径
	 * @param openMode
	 *            1=create;2=append;3=create_append
	 * @param isRplace
	 *            true强制替换false如果存在就不创建
	 * @param type 1a视图a库1b视图b库 2推送3热词
	 */
	public synchronized static void addIndexWriterForMap(String appCode,
			String path, String openMode, boolean isRplace, String type) {
		log.info("创建一个IndexWrite到AppContext，appCode[" + appCode + "]path["
				+ path + "]isRplace[" + isRplace + "]=========>Begin");
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.put("path", path);
		configMap.put("openMode", openMode);
		configMap.put("appCode", appCode);
		IndexWriter indexWriter_tmp = null;
		
		if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_HOT.equals(type)) {
			indexWriter_tmp = AppContext.getHotWordIdexWriterMap().get(appCode);
		} 
		if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_TUI_A.equals(type)) {
			indexWriter_tmp = AppContext.getaIndexWriterMap().get(appCode);
		}  
		if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_A.equals(type)) {
			indexWriter_tmp = AppContext.getaIndexWriterMap().get(appCode);
		}
		if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_B.equals(type)) {
			indexWriter_tmp = AppContext.getbIndexWriterMap().get(appCode);
		}
		boolean isCreateSuccess = true;
		IndexWriter indexWriter = null;
		//IndexSearcher indexSearcher = null;
		Map<String, IndexSearcher> tmpMap = null;
		if (indexWriter_tmp == null) {
			log.info("AppContext没有发现IndexWrite ，appCode[" + appCode + "]path["
					+ path + "]");
			try {
				indexWriter = LuceneManager.createIndexWriter(configMap);
			} catch (Exception e) {
				e.printStackTrace();
				isCreateSuccess = false;
				log.error("非强制创建索引工具类出错", e);
			}
			if (isCreateSuccess) {
				if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_HOT.equals(type)) {
					log.info("HotWord非强制替换索引工具IndexWrite到AppContext，appCode["
							+ appCode + "]path[" + path + "]");
					AppContext.getHotWordIdexWriterMap().put(appCode,
							indexWriter);
					tmpMap=AppContext.getHotIndexSearcherMap();
				} 
				if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_TUI_A.equals(type)) {
					log.info("内容非强制替换索引工具IndexWrite到AppContext，appCode["
							+ appCode + "]path[" + path + "]");

					AppContext.getaIndexWriterMap().put(appCode, indexWriter);
					tmpMap=AppContext.getaIndexSearcherMap();
				}  
				if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_A.equals(type)) {
					log.info("内容非强制替换索引工具IndexWrite到AppContext，appCode["
							+ appCode + "]path[" + path + "]");

					AppContext.getaIndexWriterMap().put(appCode, indexWriter);
					tmpMap=AppContext.getaIndexSearcherMap();
				}
				if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_B.equals(type)) {
					log.info("内容非强制替换索引工具IndexWrite到AppContext，appCode["
							+ appCode + "]path[" + path + "]");

					AppContext.getbIndexWriterMap().put(appCode, indexWriter);
					tmpMap=AppContext.getbIndexSearcherMap();
				}
			}

		} else {
			log.info("AppContext发现存在IndexWrite ，appCode[" + appCode + "]path["
					+ path + "]");
			if (isRplace) {
				log.info("强制替换索引工具IndexWrite到AppContext，appCode[" + appCode
						+ "]path[" + path + "]=======>Begin");
				try {
					indexWriter_tmp.close();
					//autoGetIndexWriteMap(appCode).remove(appCode);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("indexWriter_tmp.close()失败", e);
				}
				try {
					indexWriter = LuceneManager.createIndexWriter(configMap);
				} catch (Exception e) {
					e.printStackTrace();
					isCreateSuccess = false;
					log.error("强制创建索引工具类出错", e);
				}
				if (isCreateSuccess) {
					if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_HOT.equals(type)) {
						log.info(type+"HotWord强制替换索引工具IndexWrite到AppContext，appCode["
								+ appCode + "]path[" + path + "]");
						AppContext.getHotWordIdexWriterMap().put(appCode,
								indexWriter);
						tmpMap=AppContext.getHotIndexSearcherMap();
					} 
					if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_TUI_A.equals(type)) {
						log.info(type+"内容强制替换索引工具IndexWrite到AppContext，appCode["
								+ appCode + "]path[" + path + "]");

						AppContext.getaIndexWriterMap().put(appCode, indexWriter);
						tmpMap=AppContext.getaIndexSearcherMap();
					}  
					if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_A.equals(type)) {
						log.info(type+"内容强制替换索引工具IndexWrite到AppContext，appCode["
								+ appCode + "]path[" + path + "]");

						AppContext.getaIndexWriterMap().put(appCode, indexWriter);
						tmpMap=AppContext.getaIndexSearcherMap();
					}
					if (RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_B.equals(type)) {
						log.info(type+"内容强制替换索引工具IndexWrite到AppContext，appCode["
								+ appCode + "]path[" + path + "]");

						AppContext.getbIndexWriterMap().put(appCode, indexWriter);
						tmpMap=AppContext.getaIndexSearcherMap();
					}
					 
				}
				log.info("强制替换索引工具IndexWrite到AppContext，appCode[" + appCode
						+ "]path[" + path + "]=======>end");
			} else {
				log.info("索引工具存在AppContext，appCode[" + appCode + "]path["
						+ path + "]isRplace[" + isRplace + "]不创建索引");
			}
		}
		Document doc = new Document();
		doc.add(new StringField("test", "test",  Store.YES));
		try {
			if(indexWriter!=null){
				indexWriter.addDocument(doc);//创建一个索引防止前台服务不能打开reader
				
				Query q = new TermQuery(new Term("test", "test"));
				indexWriter.deleteDocuments(q);
				indexWriter.commit(); 
				IndexSearcher  indexSearcher001 = tmpMap.get(appCode);
				if(indexSearcher001!=null)
				{
					IndexReader indexReader001 = indexSearcher001.getIndexReader();
					if(indexReader001.getRefCount()>0)
					{
						indexReader001.close();
					}
					indexSearcher001 = null;
				} 
				IndexSearcher  indexSearcher =  LuceneManager.createIndexSearcher((String)configMap.get("path"));
				tmpMap.put(appCode, indexSearcher); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("new StringField(test,  test ,  Store.YES)索引工具存在AppContext，appCode[" + appCode + "]path["
					+ path + "]isRplace[" + isRplace + "] 创建test索引");
		}
	}

	public IndexReader createIndexReader(Map<String, String> configMap)
			throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据路径创建IndexSearch
	 * @param indexPath 路径
	 * @return
	 */
	public static IndexSearcher  createIndexSearcher(String indexPath) 
	{
		   IndexSearcher search = null;
		   FSDirectory directory;
		   IndexReader reader =null;
		try {
			directory = FSDirectory.open(new File(indexPath));
			reader=DirectoryReader.open(directory);//流读t取
			search=new IndexSearcher(reader);//搜
			log.info("创建搜索indexsearcher成功path["+indexPath+"]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("创建搜索indexsearcher失败",e);
			throw  new IllegalStateException("创建搜索indexsearcher失败",e);
			
		}//打开索引库索
		
		   return  search;
	}

	public static synchronized void closeIndexWriter(String appCode)
			throws IOException {

		IndexWriter indexWriter = autoGetIndexWriteMap(appCode).get(appCode);
		autoGetIndexWriteMap(appCode).remove(appCode);
		indexWriter.close();
	}

	public void closeIndexReader(String appCode) throws IOException {
		// TODO Auto-generated method stub

	}

	public static IndexWriter getIndexWriter(String appCode) {
		IndexWriter indexWriter = autoGetIndexWriteMap(appCode).get(appCode);
		// 以后若果为空就 同步的创建一个IndexWrite
		// if(indexWriter==null){
		// synchronized(){
		// }
		// }
		return indexWriter;
	}
	public static IndexWriter getIndexWriterOpt(String appCode) {
		IndexWriter indexWriter = autoGetIndexWriteMapSearchUseCurrentIndexWrite(appCode).get(appCode);
		// 以后若果为空就 同步的创建一个IndexWrite
		// if(indexWriter==null){
		// synchronized(){
		// }
		// }
		return indexWriter;
	}
	

	public IndexReader getIndexReader(String appCode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void closeAll(String command) throws IOException {
		// TODO Auto-generated method stub

	}
	private static boolean sqlHaveNo(String sql) {
		Pattern pattern = Pattern.compile("^[a-zA-Z]*$");
		return pattern.matcher(sql).matches();
	}
	/**
	 * 创建索引调用方法
	 * 
	 * @param appCode
	 *            注册的appcode
	 */
	public static void doIndexWriteWorkView(String appCode) {
		if("productdemo".equals(appCode) || "mapdemo".equals(appCode) )
		{
			doIndexWriteWorkViewTest(appCode);
			return;
		}
		 
		long dobegin=System.currentTimeMillis();
		boolean isreaddb =false;//是否能读取数据内容
		boolean issendurl =false;//是否通知url切换索引库
		if(checkAppCodeIsStop(appCode))
		{
			log.info("doIndexWriteWorkView的appCode["+appCode+"]已停用");
			return;
		}
		int count =500;
		try{
			count =Integer.parseInt(GlobalConfig.getProperty("searchh", "readviewcount"));
		}catch(Exception e)
		{
			e.printStackTrace();
			log.info("doIndexWriteWorkView-error 的count =Integer.parseInt:readviewcount appCode["+appCode+"]",e);
		}
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		if (appCacheDTO != null) { // 如果是视图方式创建索引
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(appCacheDTO
					.getSearchAppDTO().getIndexType())) {
				Map<String, SearchCommandDTO> searchCommandDTOMap = appCacheDTO
						.getSearchCommandDTOMap();
				Iterator<String> itCommand = searchCommandDTOMap.keySet()
						.iterator();
				while (itCommand.hasNext())// 循环子业务规则表
				{
					
					String command = itCommand.next();
					SearchCommandDTO searchCommandDTO = searchCommandDTOMap
							.get(command);
					
					log.info("XXXXXXXXXXXXXXXXXXXXXXXXXgetAppCode[" + searchCommandDTO.getAppCode()
							+ "] getCommandCode"+ searchCommandDTO.getCommandCode());
					if (searchCommandDTO == null) {
						log.info("searchCommandDTO [" + appCode
								+ "] 为空，跳过改command数据，");
						continue;
					}
					boolean ismap =false;
					if("2".equals(searchCommandDTO.getExtend()))
					{
						ismap=true;
					}
					
					
					Map<String,SearchRuleDateDTO> ruleDateDTOMap =filterCommandcode(command,appCode);
					StringBuffer sb =new StringBuffer();
					List<String> cnameList = new ArrayList<String>();
					
					for(String sf:ruleDateDTOMap.keySet())
					{
						if(!sqlHaveNo(sf)) {
							sf = "\"" + sf + "\"";  //todo 解决索引字段才数字的情况，需要在sql中添加引号
						}
						sb.append(sf).append(",");
						
						cnameList.add(sf);
					}
					String sqlf=sb.substring(0, sb.lastIndexOf(","));
					String viewName = searchCommandDTO.getViewName();
					String sqlWhere = searchCommandDTO.getSqlWhere() == null ? "" :searchCommandDTO.getSqlWhere();
					SearchAppService searchAppService = SpringContextHolder
							.getBean(SearchAppService.class);
					LimitInfo limit = new LimitInfo();
					limit.setRowDisplayed(count);
					IndexWriter indexWriter = getIndexWriter(appCode);
					long dbbegin,dbend,indexbegin,indexend ,indexcommit;
					
					String user=searchCommandDTO.getUserName();
					String pwd = searchCommandDTO.getPassWord();
					String url = searchCommandDTO.getLinkAddress();
					DataBaseManager   dataBaseManager = new DataBaseManager("oracle.jdbc.driver.OracleDriver",url,user, pwd);
					try{
						//调用存储过程
						if("1".equals(searchCommandDTO.getIsExePro())){
							callPrc(dataBaseManager,searchCommandDTO.getProName());
						}
						do {
							try {
								dbbegin=System.currentTimeMillis();
								List<Map<String, String>> rlst = searchAppService
										.listByLimitForSqlRmap(limit,
												"select  "+sqlf+" from " + viewName, sqlWhere,viewName,searchCommandDTO,dataBaseManager,cnameList);
								
								if(!isreaddb && rlst.size()>0) {//判断数据库第一次是否能正确读取到数据，如果能则删除旧的索引，如果不能读到数据则保留老索引
									clearIndex(indexWriter);
									issendurl =true;//成功调用清除索引了 就代表正常创建了
									String flagDir = AppContext.getAppCacheDTOMap().get(appCode)
											.getSearchAppDTO().getCurrentDir();
									log.info("成功删除旧索引库:["+appCode+"]库["+flagDir+"]issendurl =true");
								}
								isreaddb =true;
								if(!issendurl)
								{
									//第一次不成功则break跳出循环
									log.info("第一次读取数据库失败则跳出循环BREAK！！！！:["+appCode+"]issendurl =false");
									break;
									
								}
								
								dbend=System.currentTimeMillis();
								indexbegin= System.currentTimeMillis();
								Document doc = null;
								
								for (Map<String, String> rmap : rlst) {
									try {
										doc = createDoc(command, rmap, ruleDateDTOMap,indexWriter,ismap);
										indexWriter.addDocument(doc);// 增加一个方法commit
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										log.error("doIndexWriteWorkView make doc error appCode [" + appCode+ "] -commandcode:["+command+"]创建doc失败原因为" + e.getMessage(),
												e);
									}
								}
								indexend= System.currentTimeMillis();
								indexWriter.commit();
								indexcommit=System.currentTimeMillis();
								log.info("doIndexWriteWorkView create index appcode:["+appCode+"]-commandcode:["+command+"]耗时统计：第:["+limit.getPageNum()+"]次.数据库查询耗时["+((dbend-dbbegin)/1000)+"秒]"
										          +"索引创建耗时["+((indexend-indexbegin)/1000)+"秒]"
										          +"索引提交耗时["+((indexcommit-indexend)/1000)+"秒]");
								log.info("doIndexWriteWorkView create index page appcode:["+appCode+"]-commandcode:["+command+"] current page-size:["+limit.getPageNum()+"]"+"total-page-size["+limit.getTotalPage()+"]");
								intoqueuelog(appCode, command, limit.getTotalPage()+"", limit.getPageNum()+"", dobegin,"1","ok"); 
								limit.setPageNum(limit.getPageNum() + 1);
							} catch (Exception e) {
								intoqueuelog(appCode, command, limit.getTotalPage()+"", limit.getPageNum()+"", dobegin,"2",extostring(e)); 
								limit.setPageNum(limit.getPageNum() + 1);//防止死循环
								e.printStackTrace();
								log.error("doIndexWriteWorkView create index error appCode [" + appCode + "] -commandcode:["+command+"];"
										+ e.getMessage(), e);
							}
							
						} while (limit.getPageNum() <= limit.getTotalPage());
					}catch(Exception e)
					{
						e.printStackTrace();
					}finally{
						dataBaseManager.closeConnection();//关闭数据库连接
					}
					
				}
			}
			//更改数据库cdir，并且通知前台（servlet）更改查询索引库
			if(issendurl){
				log.info("doIndexWriteWorkView 成功重新创建完本次索引库issendurl:["+issendurl+"]appCode:["+appCode+"] 进行通知changeCDirSearch(appCode);");
				//clearClogP(appCode,"old");
				changeCDirSearch(appCode);
				clearClogP(appCode,"new");//qing kong jisuan  cpage totalpage
			}else{
				log.info("doIndexWriteWorkView 没有创建本次索引库issendurl:["+issendurl+"]appCode:["+appCode+"] NOT！！！不通知changeCDirSearch(appCode);");
				
			}
			
		} else {
			log.info("doIndexWriteWorkView appCode [" + appCode
					+ "] appCacheDTO 为null，请重新引用该配置或者该appCode不存在");
		}
	}
    //当到达100%时候A库创建完毕后切换车B库 则清空B库统计信息
	private static void  clearClogP(String  appCode,String old)
	{
		try{
	     Map<String, Map<String, CreateIndexProcessLogDTO>> tmap =  AppContext.getCindexProcessMap();
	     AppCacheDTO cacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
	     String dir = cacheDTO.getSearchAppDTO().getCurrentDir();
	     Map<String, CreateIndexProcessLogDTO> itemmap= tmap.get(dir+appCode); 
	     log.info("dir+appCode [" + dir+appCode
					+ "] old["+old+"]");
	     Iterator  it =itemmap.keySet().iterator();
	     while(it.hasNext())
	     {
	    	 CreateIndexProcessLogDTO  createIndexProcessLogDTO =itemmap.get(it.next());
	    	 createIndexProcessLogDTO.setOld(old) ;
	    	 createIndexProcessLogDTO.setCpage("0");
	    	 createIndexProcessLogDTO.setTotalpage("0");
	     }
		}catch(Exception e)
		{
			
			e.printStackTrace();
		}
	     
	     
	}
	private static void callPrc(DataBaseManager dataBaseManager,String prcName) throws Exception
	{
		java.sql.CallableStatement  statement = null;
		java.sql.Connection  conn = dataBaseManager.getConn();
		log.info("================prcName============..."+prcName);
		log.info("================conn============..."+conn);
		log.info("================conn.isClosed()============..."+conn.isClosed());
		String sql = " {call "+prcName+" } ";
		try {
			log.info("=进入存储过程执行方法...");
			 
			statement =  conn.prepareCall(sql);
			log.info("开始调用存储过程....");
			statement.executeUpdate();
			log.info("正常调用结束...");
		} catch (Exception e) {
			log.error("调用存储过程出错，异常原因：", e);
			e.printStackTrace();
			throw   e;
		} finally {
			log.info("结束调用，关闭连接");
			try{
				conn.commit();
				log.info("提交事物...");
			}catch(Exception e){
				log.error("提交事物失败:",e);
			}
 
			log.info("=退出存储过程执行方法...");
		}
	}
	/**
	 * 创建索引调用方法-调用服务v
	 * 
	 * @param appCode
	 *            注册的appcode
	 */
	public static void doIndexWriteWorkWs(String appCode) {
		long dobegin=System.currentTimeMillis();
		if(checkAppCodeIsStop(appCode))
		{
			log.info("doIndexWriteWorkWs 的appCode["+appCode+"]已停用");
			return;
		}

		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		if (appCacheDTO != null) { // 如果是视图方式创建索引
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(appCacheDTO
					.getSearchAppDTO().getIndexType())) {
				Map<String, SearchCommandDTO> searchCommandDTOMap = appCacheDTO
						.getSearchCommandDTOMap();
				Iterator<String> itCommand = searchCommandDTOMap.keySet()
						.iterator();
				while (itCommand.hasNext())// 循环子业务规则表
				{
					String command = itCommand.next();
					SearchCommandDTO searchCommandDTO = searchCommandDTOMap
							.get(command);
					if (searchCommandDTO == null) {
						log.info("doIndexWriteWorkWs searchCommandDTO [" + appCode
								+ "] 为空，跳过改command数据，");
						continue;
					}
					boolean ismap =false;
					if("2".equals(searchCommandDTO.getExtend()))
					{
						ismap=true;
					}
					Map<String,SearchRuleDateDTO> ruleDateDTOMap =filterCommandcode(command,appCode);
					StringBuffer sb =new StringBuffer();
					List<String> cnameList = new ArrayList<String>();
					
					for(String sf:ruleDateDTOMap.keySet())
					{
						cnameList.add(sf);
					}
				 
					SearchMessageService searchMessageService = SpringContextHolder
							.getBean(SearchMessageService.class);
					IndexWriter indexWriter = getIndexWriter(appCode);
					long dbbegin,dbend,indexbegin,indexend ,indexcommit;
					
					String user=searchCommandDTO.getUserName();
					String pwd = searchCommandDTO.getPassWord();
					String url = searchCommandDTO.getLinkAddress();
					int pageNo =1,totalNo=1;
					do {
						try {
							dbbegin=System.currentTimeMillis();
							TuiHelpDTO tuiHelpDTO =callws(searchMessageService,url,user,pwd,pageNo);
							totalNo = Integer.parseInt(tuiHelpDTO.getTotalNo());
							List<Map<String, String>> rlst =  tuiHelpDTO.getList();//httpclient 调用，然后分析xml得到页码和总页数去判断循环
							dbend=System.currentTimeMillis();
							indexbegin= System.currentTimeMillis();
							Document doc = null;
							for (Map<String, String> rmap : rlst) {
								try {
									doc = createDoc(command, rmap, ruleDateDTOMap,indexWriter,ismap);
									
									indexWriter.addDocument(doc);// 增加一个方法commit
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									log.error("doIndexWriteWorkWs make doc error appCode [" + appCode+ "] -commandcode:["+command+"]创建doc失败原因为" + e.getMessage(),
											e);

								}
							}
							indexend= System.currentTimeMillis();
							indexWriter.commit();
							indexcommit=System.currentTimeMillis();
							
							log.info("doIndexWriteWorkWs create index appcode:["+appCode+"]-commandcode:["+command+"]耗时统计：第:["+pageNo+"]次.数据库查询耗时["+((dbend-dbbegin)/1000)+"秒]"
					          +"索引创建耗时["+((indexend-indexbegin)/1000)+"秒]"
					          +"索引提交耗时["+((indexcommit-indexend)/1000)+"秒]");                                   
							log.info("doIndexWriteWorkWs create index page appcode:["+appCode+"]-commandcode:["+command+"] current page-size:["+pageNo+"]"+"total-page-size["+totalNo+"]");
							intoqueuelog(appCode, command, totalNo+"", pageNo+"",dobegin,"1","ok"); 
							pageNo++;
						} catch (Exception e) {
							intoqueuelog(appCode, command, 1+"", 1+"", dobegin,"2",extostring(e)); 
							e.printStackTrace();
							log.error("doIndexWriteWorkWs create index error appCode [" + appCode + "] -commandcode:["+command+"];"
									+ e.getMessage(), e);
						}
					} while (pageNo<totalNo);
				}
			}
			//更改数据库cdir，并且通知前台（servlet）更改查询索引库
			clearClogP(appCode,"old");
			changeCDirSearch(appCode);
			clearClogP(appCode,"new");//qing kong jisuan  cpage totalpage
		} else {
			log.info("appCode [" + appCode
					+ "] appCacheDTO 为null，请重新引用该配置或者该appCode不存在");
		}
	}
	/**
	 * 获得调用服务数据信息
	 * @param url
	 * @param pm
	 * @return
	 */
	private static TuiHelpDTO callws(SearchMessageService searchMessageService,String url, String username,String pwd,int pageNo  )
	{
		Map<String,String> pm = new HashMap<String,String>();
		pm.put(RedisKeyConst.Search.CALL_WS_USERNAME, username);
		pm.put(RedisKeyConst.Search.CALL_WS_PWD, pwd);
		pm.put(RedisKeyConst.Search.CALL_WS_PAGENO, String.valueOf(pageNo));
		log.info("正在调用服务获得数据信息["+url+"]开始");
		String xml=HttpClientUtil.sendHttpPostRequest(url,pm, null) ;
		log.info("正在调用服务获得数据信息["+url+"]结束 ");
		TuiHelpDTO tuiHelpDTO = searchMessageService.readXml(xml);
		return tuiHelpDTO;
	}
	/**
	 * ftp获取资源
	 * @param appCode
	 */
	public static void doIndexWriteWorkFtp(String appCode) {
		Long a = System.currentTimeMillis();
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		if(appCacheDTO != null){
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_FTP.equals(appCacheDTO.getSearchAppDTO().getIndexType())) {
				Map<String, SearchCommandDTO> searchCommandDTOMap = appCacheDTO.getSearchCommandDTOMap();
				Iterator<String> itCommand = searchCommandDTOMap.keySet().iterator();
				SearchMessageService searchMessageService = SpringContextHolder.getBean(SearchMessageService.class);
				while (itCommand.hasNext()) {// 循环子业务规则表
					Map<String,String> mapOpt = new HashMap<String,String>();
					String command = itCommand.next();
					SearchCommandDTO searchCommandDTO = searchCommandDTOMap.get(command);
					if(searchCommandDTO.getFtpType().equals(RedisKeyConst.Search.SEARCH_COMMAND_FTP)) {//如果是FTP
						FTPAbstract ftp = new FTP();
						//登录
						FTPClient client = ftp.login(searchCommandDTO);
						//下载
						ftp.down(client, searchCommandDTO);
						//备份
						ftp.rename(client, searchCommandDTO.getLinkDir());
						chanelFTP(searchMessageService, mapOpt, searchCommandDTO);
					}else if(searchCommandDTO.getFtpType().equals(RedisKeyConst.Search.SEARCH_COMMAND_SFTP)) {
						FTPAbstract sftp  = new SFTP();
						sftp.login(searchCommandDTO);
						sftp.down(new FTPClient(), searchCommandDTO);
						chanelFTP(searchMessageService, mapOpt, searchCommandDTO);
					}
				}
			}
		}
		long b = System.currentTimeMillis();
		log.info("ftp 1W 条数据共耗时：" + (a-b));
	}

	private static void chanelFTP(SearchMessageService searchMessageService,
			Map<String, String> mapOpt, SearchCommandDTO searchCommandDTO) {
		File file=new File(searchCommandDTO.getLocalAddress());
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			try {
				log.info("解压文件" + tempList[i].getName());
				unzip(tempList[i].getName(), searchCommandDTO.getLocalAddress());
				log.info("解压成功" + tempList[i].getName());
			} catch (IOException e1) {
				e1.printStackTrace();
				log.error("解压出错");
			}
		}
		tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				File myFile = new File(tempList[i].getName());
				if(myFile.getName().endsWith(".xml")) {
					try {
						BufferedReader in = new BufferedReader(new FileReader(searchCommandDTO.getLocalAddress() + System.getProperty("file.separator") + myFile));
						StringBuffer xml = new StringBuffer("");
						String str;
			            while ((str = in.readLine()) != null) 
			            {
			            	xml = xml.append(str);
			            }
			            in.close();
			            log.info("解析xml");
			            TuiHelpDTO tuiHelpDTO = searchMessageService.readXml(xml.toString());
			            mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_OPT, tuiHelpDTO.getOpt());
			            mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_COMMANDCODE, tuiHelpDTO.getCommandCode());
			            mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_APPCODE, tuiHelpDTO.getAppCode());
			    		optIndexTui(mapOpt,tuiHelpDTO.getList());
			    		
					} catch (Exception e) {
						log.error("ftp出错:", e);
						e.printStackTrace();
					}
				}
			}
			//删除文件
			tempList[i].delete();
		}
	}
	public synchronized static void unzip(String zipFilename,
			String outputDirectory) throws IOException {
		File outFile = new File(outputDirectory);
		if (!outFile.exists()) {
			outFile.mkdirs();
		}

		ZipFile zipFile = new ZipFile(outputDirectory + File.separator
				+ zipFilename);
		Enumeration en = zipFile.entries();
		ZipEntry zipEntry = null;
		while (en.hasMoreElements()) {
			zipEntry = (ZipEntry) en.nextElement();
			if (zipEntry.isDirectory()) {
				// / mkdir directory
				String dirName = zipEntry.getName();
				// /System.out.println("=dirName is:=" + dirName + "=end=");
				dirName = dirName.substring(0, dirName.length() - 1);
				File f = new File(outFile.getPath() + File.separator + dirName);
				f.mkdirs();
			} else {
				// /unzip file
				String strFilePath = outFile.getPath() + File.separator
						+ zipEntry.getName();
				File f = new File(strFilePath);

				// /the codes remedified by can_do on 2010-07-02 =begin=
				// //////begin/////
				// /判断文件不存在的话，就创建该文件所在文件夹的目录
				if (!f.exists()) {
					String[] arrFolderName = zipEntry.getName().split("/");
					String strRealFolder = "";
					for (int i = 0; i < (arrFolderName.length - 1); i++) {
						strRealFolder += arrFolderName[i] + File.separator;
					}
					strRealFolder = outFile.getPath() + File.separator
							+ strRealFolder;
					File tempDir = new File(strRealFolder);
					// /此处使用.mkdirs()方法，而不能用.mkdir()
					tempDir.mkdirs();
				}
				// ////end///
				// / the codes remedified by can_do on 2010-07-02 =end=
				f.createNewFile();
				InputStream in = zipFile.getInputStream(zipEntry);
				FileOutputStream out = new FileOutputStream(f);
				try {
					int c;
					byte[] by = new byte[1024];
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					// /out.flush();
				} catch (IOException e) {
					throw e;
				} finally {
					out.close();
					in.close();
				}
			}
		}
	}
	/**
	 * 根据坐标加入lucene索引记录字段
	 * @param doc   lucene document
	 * @param longitude   横在坐标({"longitude":121.4217,"latitude":31.215496})
	 * @param latitude    纵坐标   ({"longitude":121.4217,"latitude":31.215496})
	 * @return  lucene document
	 */
	private static Document makeMapfiled(Document doc,double longitude,double latitude ) {  
    	{  
    		Shape  shape =ctx.makePoint(longitude,latitude);
            for (Field f : strategy.createIndexableFields(shape)) {  
                doc.add(f);  
            }  
           
            Point pt = (Point) shape;  
            doc.add(new StoredField(strategy.getFieldName(), pt.getX() + " " + pt.getY()));  
        }  
  
        return doc;  
    }  
	/**
	 * 构造docment
	 * 
	 * @param mapVale
	 * @param mapFieldRule
	 * @return
	 * @throws Exception
	 */
	private static Document createDoc(String commandCode,
			Map<String, String> mapVale,
			Map<String, SearchRuleDateDTO> mapFieldRule,IndexWriter indexWriter,boolean ismap) throws Exception {
		Document doc = new Document();
		Iterator<String> it = mapVale.keySet().iterator();
		String field = "", value = "";
		SearchRuleDateDTO searchRuleDateDTO = null;
		Store store = null;
		while (it.hasNext()) {
			field = it.next();
			value = mapVale.get(field).toString();
			searchRuleDateDTO = mapFieldRule.get(field);
			if (searchRuleDateDTO == null) {
				//log.error("field [" + field + "] 字段没有配置被配置请检查,此条记录不会被索引");
				continue;
			}
			if (RedisKeyConst.Search.RULE_STORE_TYPE_YES
					.equals(searchRuleDateDTO.getFieldStoreType())) {
				store = Store.YES;
			} else {
				store = Store.NO;
			}
			//主键
			if (RedisKeyConst.Search.RULE_DATE_TYPE_PK.equals(searchRuleDateDTO.getFileldDateType())) {
					doc.add(new StringField(field, value.toString(),  Store.YES));
			}//字符串
			if (RedisKeyConst.Search.RULE_DATE_TYPE_STRING
					.equals(searchRuleDateDTO.getFileldDateType())) {

				if (RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_INDEX
						.equals(searchRuleDateDTO.getFieldIndexType())) {
					doc.add(new StoredField(field, value.toString()));
				} 
				if (RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED
						.equals(searchRuleDateDTO.getFieldIndexType())) {
					doc.add(new StringField(field, value.toString(), store));
				}
				if (RedisKeyConst.Search.RULE_INDEX_TYPE_TOKENIZED
						.equals(searchRuleDateDTO.getFieldIndexType())) {
					doc.add(new TextField(field, value.toString(), store));
				}
			}
			if (RedisKeyConst.Search.RULE_DATE_TYPE_INT
					.equals(searchRuleDateDTO.getFileldDateType())) {
					doc.add(new StringField(field, buzero(value.toString()), Store.YES));
				 
			}
			if (RedisKeyConst.Search.IS_INTELLISENSE_TRUE
					.equals(searchRuleDateDTO.getIsIntelliSense())) {
				//要创建三个字段
				//智能提示的自动加入三个后缀
				String py ="",pinyin="",vzn="";
				try{
					vzn=value.toString().toLowerCase();
					py =PinyinUtil.cn2FirstSpell(vzn);
					pinyin =PinyinUtil.cn2Spell(vzn);
				}catch(Exception e){
					log.error("拼音转换错误["+value.toString()+"]",e);
				}
				py=py.toLowerCase();
				doc.add(new StringField(field+RedisKeyConst.Search.INTELLISENSE_ZH, vzn, Store.YES));
				doc.add(new StringField(field+RedisKeyConst.Search.INTELLISENSE_PY,py, Store.YES));
				doc.add(new StringField(field+RedisKeyConst.Search.INTELLISENSE_PINYIN,pinyin, Store.YES));
				creatTokenizedDoc(doc, indexWriter, field, vzn);//增加创建拼音索引分词
			}
			
			
			
//			if (RedisKeyConst.Search.RULE_DATE_TYPE_FLOAT
//					.equals(searchRuleDateDTO.getFileldDateType())) {
//				if (RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_INDEX
//						.equals(searchRuleDateDTO.getFieldIndexType())) {
//					doc.add(new StoredField(field, Float.parseFloat(value)));
//				}
//				if (RedisKeyConst.Search.RULE_INDEX_TYPE_NOT_TOKENIZED
//						.equals(searchRuleDateDTO.getFieldIndexType())) {
//					doc.add(new FloatField(field, Float.parseFloat(value),
//							store));
//				}
//				if (RedisKeyConst.Search.RULE_INDEX_TYPE_TOKENIZED
//						.equals(searchRuleDateDTO.getFieldIndexType())) {
//					doc.add(new FloatField(field, Float.parseFloat(value),
//							store));
//				}
//			}
 		}
		doc.add(new StringField("commandCode", commandCode, store));// 业务分类编码
		//地图索引字段
		double longitude,latitude;
		if(ismap){
			longitude = Double.parseDouble(mapVale.get("LONGITUDE").toString());
			latitude = Double.parseDouble(mapVale.get("LATITUDE").toString());
			makeMapfiled(doc,longitude,latitude);
		}
		return doc;
	}
	private static void creatTokenizedDoc(Document doc,IndexWriter indexWriter,String fieldName,String text)
	{
		List<String> lst = new ArrayList<String>();
		try {
				 TokenStream  ts = indexWriter.getAnalyzer().tokenStream(fieldName, text);
				 CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
				 ts.reset(); 
				 while (ts.incrementToken()) {
					 lst.add(term.toString());  
				 }
					//关闭TokenStream（关闭StringReader）
				 ts.end();
				 ts.close();
				
			
				StringBuffer sb =new StringBuffer();
				StringBuffer sb1 =new StringBuffer();
				if(lst.size()>0)
				{
					 for(String s:lst)
					 {
						 s=s.toString().toLowerCase();
						//log.info("s:"+s);
						 sb.append(PinyinUtil.cn2Spell(s)).append(" ");
						 sb1.append(PinyinUtil.cn2FirstSpell(s)).append(" ");
					 }
				}
				if(sb.length()>0)
				{
					Field f =new TextField(fieldName+RedisKeyConst.Search.INTELLISENSE_PINYIN_FC,sb.toString().trim(), Store.YES);
					f.setBoost(-1);
					doc.add(f);
				} 
				if(sb1.length()>0)
				{
					doc.add(new TextField(fieldName+RedisKeyConst.Search.INTELLISENSE_PY_FC,sb1.toString().trim(), Store.YES));
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("分词的拼音索引错误["+e.getMessage()+"]");
		}
	}

	private static String buzero(String s)
	{
	    String rstr=null;
		if(s==null || "".equals(s.trim()) || "0".equals(s.trim())){
			return rs;
		}
		 try{
			 	Double.valueOf(s);
			 }catch(Exception e)
			 {
				 log.info("buzero 转换错误:"+s+"所以返回"+rs);
				 return    rs;
			 }
         String	ss[]=s.split("\\.");
         if(ss.length==1)
         {
        	 //rstr =rs1.substring(ss[0].length())+ss[0]+"."+rs2;
        	 rstr =rs1.substring(ss[0].length())+ss[0]+"."+rs2;
        	 
         }else{
        	 if(ss[1].length()<2)
        	 {
        		 rstr =rs1.substring(ss[0].length())+ss[0]+"."+ss[1]+rs2.substring(ss[1].length());
        	 }else{
        		 rstr =rs1.substring(ss[0].length())+ss[0]+"."+ss[1];
        	 }
         }
         
		return rstr;
	}
	  
	/**
	 * 创建索引调用方法--热词索引
	 * 
	 * @param appCode 注册的appcode
	 *  
	 */
	public static void doHotWordIndexWrite() {
		SearchKeywordLogService searchKeywordLogService = SpringContextHolder
				.getBean(SearchKeywordLogService.class);
		int count =500;
		try{
			count =Integer.parseInt(GlobalConfig.getProperty("searchh", "readlogcount"));
			if(count>999)
			{
				count =999;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			log.info("doHotWordIndexWrite-error 的count =Integer.parseInt:readlogcount",e);
		}
		LimitInfo limit = new LimitInfo();
		limit.setRowDisplayed(count);
		List<SearchKeywordLogDTO> rlst = searchKeywordLogService.list(limit, " (*:*) ");
		 
		rlst = transformHotList(rlst);
		for (SearchKeywordLogDTO searchKeywordLogDTO : rlst) {
			try {
				if(updateHotWord(searchKeywordLogDTO)){
					try {
						log.info("delete hotWord is begin");
						log.info("delete hotWord id is ["+searchKeywordLogDTO.getId().toString()+"]");
						searchKeywordLogService.deleteTX(searchKeywordLogDTO.getId());
						log.info("delete hotWord is end");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error("delete hotWord[" + searchKeywordLogDTO.getId().toString() + "] error",e);
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("updateHotWord[" + searchKeywordLogDTO + "] error",e);
			}
		}
		
		
	}

	/**
	 * 转换热词list：从热词list分组按照AppCode，然后把相同的热词直接算出数量，节省更新操作次数
	 * 
	 * @param lst
	 * @return
	 */
	private static List<SearchKeywordLogDTO> transformHotList(
			List<SearchKeywordLogDTO> lst) {
		Map<String, SearchKeywordLogDTO> rmap = new HashMap<String, SearchKeywordLogDTO>();
		List<SearchKeywordLogDTO> rlst = new ArrayList<SearchKeywordLogDTO>();
		String appCode = "";
		String keyWord = "";
		SearchKeywordLogDTO searchKeywordLogDTOtmp = null;
		for (SearchKeywordLogDTO searchKeywordLogDTO : lst) {
			appCode = searchKeywordLogDTO.getAppCode();
			keyWord = searchKeywordLogDTO.getSearchKeyword();
			searchKeywordLogDTOtmp = rmap.get(appCode + keyWord);
			if (searchKeywordLogDTOtmp == null) {
				rmap.put(appCode + keyWord, searchKeywordLogDTO);
			} else {
				searchKeywordLogDTOtmp.setId(searchKeywordLogDTOtmp.getId()+","+ searchKeywordLogDTO.getId());
				searchKeywordLogDTOtmp
						.setOnce(searchKeywordLogDTOtmp.getOnce() + 1);
			}
//			AppContext.getHotWordIdexWriterMap().get(appCode);
			// hotIindexWriter.updateDocument(term, doc);
		}

		rlst.addAll(rmap.values());
		return rlst;
	}
/**
 * 
 * @param searchKeywordLogDTO
 */
	public static boolean  updateHotWord(SearchKeywordLogDTO searchKeywordLogDTO) throws Exception{
		IndexWriter indexWriterHot =null;
		IndexReader reader=null;
		FSDirectory directory =null;
		try{
			String appCode = searchKeywordLogDTO.getAppCode();
			indexWriterHot =AppContext.getHotWordIdexWriterMap().get(appCode);
			
			SearchAppDTO searchAppDTO = AppContext.getAppCacheDTOMap().get(appCode).getSearchAppDTO();
			if(checkAppCodeIsStop(appCode))
			{
				log.info("updateHotWord的appCode["+appCode+"]已停用");
				return false;
			}
			if(indexWriterHot==null)
			{
				log.info("获得indexWriterHot为空appCode["+appCode+"]不存在或者已停用");
				return false ;
			}
			IndexSearcher search = LuceneManager.autoGetIndexSearcher(appCode, true);
			//Query q = new TermQuery(new Term(RedisKeyConst.Search.HOT_FILED_SEARCHKEYWORD, searchKeywordLogDTO.getSearchKeyword()));
//			QueryParser parser = new QueryParser(Version.LUCENE_47, "",analyzer );
			QueryParser parser = new QueryParser(Version.LUCENE_47, "",analyzerType(appCode));
			parser.setLowercaseExpandedTerms(false);
			String searchKeyword = searchKeywordLogDTO.getSearchKeyword();
			if(StringUtils.isEmpty(searchKeyword)){
			    //searchKeyword = "";
			    return true;
			}else{
			    searchKeyword += "~0";
			}
			Query q =parser.parse(RedisKeyConst.Search.HOT_FILED_SEARCHKEYWORD+":"+searchKeyword);
			
			// q.add();
			Document dt = null;
			Document d =null;
			TopDocs td = search.search(q, Integer.MAX_VALUE);// 获取最高得分命中
			int intOnce =0;
			if(td.scoreDocs.length>0){
				for(int i=0;i<td.scoreDocs.length;i++){
					 ScoreDoc doc = null;
					 doc=td.scoreDocs[i];
					 dt = search.doc(doc.doc);
					 String once =dt.get(RedisKeyConst.Search.HOT_FILED_ONCE);//次数
					 intOnce += stringTranInt(once);
				}
				
				 indexWriterHot.deleteDocuments(q);
				 
				 d =  new Document();
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_COMMAND,searchKeywordLogDTO.getCommandCode(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHKEYWORD,searchKeywordLogDTO.getSearchKeyword(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHPINYIN,searchKeywordLogDTO.getSearchPinyin(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHPY,searchKeywordLogDTO.getSearchPy(),Store.YES));
				 d.add(new IntField(RedisKeyConst.Search.HOT_FILED_ONCE, intOnce+searchKeywordLogDTO.getOnce(),Store.YES));
				 indexWriterHot.addDocument(d);
			}else{
				 d =  new Document();
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_COMMAND,searchKeywordLogDTO.getCommandCode(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHKEYWORD,searchKeywordLogDTO.getSearchKeyword(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHPINYIN,searchKeywordLogDTO.getSearchPinyin(),Store.YES));
				 d.add(new StringField(RedisKeyConst.Search.HOT_FILED_SEARCHPY,searchKeywordLogDTO.getSearchPy(),Store.YES));
				 d.add(new IntField(RedisKeyConst.Search.HOT_FILED_ONCE,searchKeywordLogDTO.getOnce(),Store.YES));
				 indexWriterHot.addDocument(d);
			}
			return true ;
		}finally{
			if(indexWriterHot!=null)
			{
				indexWriterHot.commit();
			}
			if(reader!=null){
				reader.close();// 关闭读取流
			}
			if(directory!=null)
			{
				directory.close();// 文件夹
			}
		}
			
	}

	private static int stringTranInt(String once)
	{
		int rint =1;
		try{
		  rint =Integer.parseInt(once);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return rint;
	}
    /**
     * 
     * @param appCode 注册的appCode
     * @param flag    操作方式 stop:停用 start
     */
     
	public static void  optIndexWrite(String appCode,String flag) {
		if(RedisKeyConst.Search.SEARCH_SYS_START.equals(flag))
		{
			//新增的和启用的
			//增加a和b索引或者是a索引
			AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(appCacheDTO.getSearchAppDTO().getIndexType()) || RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(appCacheDTO.getSearchAppDTO().getIndexType()))
			{
				//视图
				LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(),
                        appCacheDTO.getSearchAppDTO().getIndexPath()+"/"+RedisKeyConst.Search.A_DIR_VALUE, 
                        "1", true,RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_A);
				LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(),
						appCacheDTO.getSearchAppDTO().getIndexPath()+"/"+RedisKeyConst.Search.B_DIR_VALUE, 
						"1", true,RedisKeyConst.Search.SEARCH_INDEX_TYPE_VIEW_B);
				//增加定时起:IndexWriteTask创建索引
			
				AppContext.getScheduledFutureMap().put(appCode, AppContext.getExecutor().scheduleAtFixedRate(new IndexWriteTask(appCode), 0, appCacheDTO.getSearchAppDTO().getLoopCindexTime(), TimeUnit.MINUTES));
				log.info("start  timetask appcode["+appCode+"]增加定时起:IndexWriteTask创建索引 ");
			}else{
				//推送
				LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(),
						  appCacheDTO.getSearchAppDTO().getIndexPath()+"/"+RedisKeyConst.Search.A_DIR_VALUE, 
						  "3", true,RedisKeyConst.Search.SEARCH_INDEX_TYPE_TUI_A);
			}
			//增加indexWriteHot
			 LuceneManager.addIndexWriterForMap(appCacheDTO.getSearchAppDTO().getAppCode(),
	                 appCacheDTO.getSearchAppDTO().getIndexPath()+"/"+RedisKeyConst.Search.HOT_DIR_VALUE, 
	                 "3", true,RedisKeyConst.Search.SEARCH_INDEX_TYPE_HOT);
			
		}else if(RedisKeyConst.Search.SEARCH_SYS_STOP.equals(flag))
		{
			//删除的和停用的
			AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
			//删除a和b索引或者是a索引
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(appCacheDTO.getSearchAppDTO().getIndexType()) || RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE.equals(appCacheDTO.getSearchAppDTO().getIndexType()) )
			{
				//视图删除
				 IndexWriter  indexWriterA  =AppContext.getaIndexWriterMap().get(appCode);
				 IndexWriter  indexWriterB  =AppContext.getbIndexWriterMap().get(appCode);
				 AppContext.getaIndexWriterMap().remove(appCode);
				 AppContext.getbIndexWriterMap().remove(appCode);
				 //AppContext.getaIndexSearcherMap().remove(appCode);//不清除方便后台查
				// AppContext.getbIndexSearcherMap().remove(appCode);//不清除方便后台查
				 if(indexWriterA!=null)
				 {
					 try {
						indexWriterA.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error("stop timetask appcode["+appCode+"] indexWriterA close is error  ["+e+"]",e);
					}
				 }
				 if(indexWriterB!=null)
				 {
					 try {
						indexWriterB.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.error("stop timetask appcode["+appCode+"] indexWriterB close is error  ["+e+"]",e);
					}
				 }
				 ScheduledFuture<?> future = AppContext.getScheduledFutureMap().get(appCode);
				 if(future!=null)
				 {
					 boolean s = future.cancel(true);//删除定时器
					 log.info("stop timetask appcode["+appCode+"] rs is ["+s+"]");
					 if(s)
					 {
						 AppContext.getScheduledFutureMap().remove(appCode);
						 future=null;
					 }
				 }
			}else{
				//推送删除
				 IndexWriter  indexWriterA  =AppContext.getaIndexWriterMap().get(appCode);
				 AppContext.getaIndexWriterMap().remove(appCode);
				 //AppContext.getaIndexSearcherMap().remove(appCode);//不清除方便后台查
				 if(indexWriterA!=null)
				 {
					 try {
						indexWriterA.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			}
			//删除indexWriteHot
			 IndexWriter  indexWriterH  =AppContext.getHotWordIdexWriterMap().get(appCode);
			 AppContext.getHotWordIdexWriterMap().remove(appCode);
			 //AppContext.getHotIndexSearcherMap().remove(appCode);//不清除方便后台查
			 
			 if(indexWriterH!=null)
			 {
				 try {
					 indexWriterH.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		}
	}
	/**
	 * 判断是否停用
	 * @param appCode
	 * @return
	 */
	public static boolean  checkAppCodeIsStop(String appCode) {
		AppCacheDTO  appCacheDTO =AppContext.getAppCacheDTOMap().get(appCode);
		
		boolean ris=false;
		if(appCacheDTO!=null){
			SearchAppDTO searchAppDTO = appCacheDTO.getSearchAppDTO();
			if(searchAppDTO!=null)
			{
				if(RedisKeyConst.Search.STOP.equals(searchAppDTO.getState())){
					ris=true;
				}else if (RedisKeyConst.Search.USERING.equals(searchAppDTO.getState())){

					ris=false;
				}
			}else{
				 ris=true;
			}
		}else{
			 ris=true;
		}
		if(ris){
			log.info("appCode["+appCode+"]业务已停用");
		}else{
			log.info("appCode["+appCode+"]正在启用");
		}
		return ris;
	}
	/**
	 * 推送操作类增删改
	 * @param mapOpt RedisKeyConst.Search.SEARCH_TUI_OPT，SEARCH_TUI_APPCODE SEARCH_TUI_COMMANDCODE
	 * @param listMapVale 存放传过来的值list值
	 * @return
	 */
	public static boolean optIndexTui(Map<String, String> mapOpt ,List<Map<String, String>> listMapVale){
		String opt =mapOpt.get(RedisKeyConst.Search.SEARCH_TUI_OPT);
		String appCode = mapOpt.get(RedisKeyConst.Search.SEARCH_TUI_APPCODE);
		String commandCode = mapOpt.get(RedisKeyConst.Search.SEARCH_TUI_COMMANDCODE);
		boolean ris=false;
		if(checkAppCodeIsStop(appCode))
		{
			log.info("optIndex的appCode["+appCode+"]已停用");
			return false;
		}
		IndexWriter indexWriter = getIndexWriterOpt(appCode);//获得opt的indexWrite
		if(RedisKeyConst.Search.SEARCH_TUI_OPT_ADD.equals(opt))
		 {
			ris =tuiAdd(appCode,commandCode,listMapVale,indexWriter);
		 }else if(RedisKeyConst.Search.SEARCH_TUI_OPT_UPDATE.equals(opt))
		 {
			 ris =tuiDel(appCode,commandCode,listMapVale,indexWriter);
			 ris =tuiAdd(appCode,commandCode,listMapVale,indexWriter);
		 }else if(RedisKeyConst.Search.SEARCH_TUI_OPT_DELTET.equals(opt))
		 {
			 
			 ris =tuiDel(appCode,commandCode,listMapVale,indexWriter);
		 }
		try {
			indexWriter.commit();
			log.info("optIndex-opt:"+opt+"-list-commit-ok [" + appCode+ "]");
			ris=true;
		} catch (IOException e) {
			ris=false;
			e.printStackTrace();
			log.error("optIndex-error-indexWriter.commit();-opt:"+opt+"appcode: [" + appCode+ "] 创建doc失败原因为" + e.getMessage(),e);
		}
		
		return ris;
	}	
	/**
	 * 根据commandCode过滤改业务类型字段
	 * @param commandCode
	 * @param appCode
	 * @return
	 */
	public static  Map<String, SearchRuleDateDTO> filterCommandcode(String commandCode,String appCode){
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		Map<String, SearchRuleDateDTO> rmap_tmp =null;
		Map<String, SearchRuleDateDTO> rmap = new HashMap<String, SearchRuleDateDTO>();
		SearchRuleDateDTO searchRuleDateDTO = null;
		rmap_tmp = appCacheDTO.getSearchRuleDateDTOMap();
		
		for(Entry<String, SearchRuleDateDTO> entry:rmap_tmp.entrySet()){
			searchRuleDateDTO =entry.getValue(); 
			if(commandCode.equals(searchRuleDateDTO.getCommandCode()))
			 {
				rmap.put(searchRuleDateDTO.getFieldName(), searchRuleDateDTO);
			 }
		}
	    return rmap;
	}
	/**
	 * 添加方法
	 * @param appCode
	 * @param commandCode
	 * @param listMapVale
	 * @param indexWriter
	 * @return
	 */
	private static  boolean tuiAdd(String appCode,String commandCode,List<Map<String, String>> listMapVale,IndexWriter indexWriter){
		boolean ris=false;
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		SearchCommandDTO searchCommandDTO =appCacheDTO.getSearchCommandDTOMap().get(commandCode);
		boolean ismap =false;
		if("2".equals(searchCommandDTO.getExtend()))
		{
			ismap=true;
		}
		
		Document doc = null;
		Map<String,SearchRuleDateDTO> ruleDateDTOMap =filterCommandcode(commandCode,appCode);
		for (Map<String, String> rmap : listMapVale) {
			try {
				doc = createDoc(commandCode, rmap,ruleDateDTOMap,indexWriter,ismap);
				indexWriter.addDocument(doc);// 增加一个方法commit
				log.info("optIndex-add-ok [" + appCode+ "]rmap:"+rmap.toString());
				ris=true;
			} catch (Exception e) {
				ris=false;
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("optIndex-appCode [" + appCode+ "] 创建doc失败原因为" + e.getMessage(),e);
			}
		}
		return ris;
	}
	/**
	 * 删除
	 * @param appCode
	 * @param commandCode
	 * @param listMapVale
	 * @param indexWriter
	 * @return
	 */
	private static  boolean tuiDel(String appCode,String commandCode,List<Map<String, String>> listMapVale,IndexWriter indexWriter){
		boolean ris=false;
		String pkf=getLucenePk(appCode,commandCode);
		for (Map<String, String> rmap : listMapVale) {
			try {
				Query q = new TermQuery(new Term(pkf, rmap.get(pkf)));
				indexWriter.deleteDocuments(q);//删除
				log.info("tuiDel-del-ok [" + appCode+ "]rmap:"+rmap.toString());
				ris=true;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("tuiDel-del-error [" + appCode+ "] 删除doc失败原因为" + e.getMessage(),e);
				ris=false;
			}
		}
		 
		return ris;
	}
	/**
	 * 根据appcode获得主键字段名称
	 * @param appCode
	 * @return
	 */
	public static String getLucenePk(String appCode,String commandCode){
		String spk=null;
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		Map<String, SearchRuleDateDTO>  map =appCacheDTO.getSearchRuleDateDTOMap();
		for (SearchRuleDateDTO searchRuleDateDTO : map.values()) {
				if(RedisKeyConst.Search.RULE_DATE_TYPE_PK.equals(searchRuleDateDTO.getFileldDateType()) && 
						commandCode.equals(searchRuleDateDTO.getCommandCode())){
					spk=searchRuleDateDTO.getFieldName();
				}
		}
		return spk;
	}
	
	@SuppressWarnings("unused")
	private static  boolean changeCDirSearch(String appCode)
	{
		SearchAppService searchAppService = SpringContextHolder.getBean(SearchAppService.class);
		LimitInfo limit = new LimitInfo();
		limit.setRowDisplayed(1);
		List<SearchAppDTO> lst = searchAppService.list(limit, " (appCode:"+appCode+"~0) ");
		SearchAppDTO searchAppDTO = lst.get(0);
		String cdir =searchAppDTO.getCurrentDir();
		if(RedisKeyConst.Search.A_DIR.equals(cdir))
		{
			searchAppDTO.setCurrentDir(RedisKeyConst.Search.B_DIR);
		}
		if(RedisKeyConst.Search.B_DIR.equals(cdir))
		{
			searchAppDTO.setCurrentDir(RedisKeyConst.Search.A_DIR);
		}
		try {
			searchAppService.updateTX(searchAppDTO);
			//改缓存
			searchAppService.updateCacheByAppCode(searchAppDTO.getAppCode());
			//通知搜索前台切换搜索路径
			String url=null;
			String urls[]=null ;
			url = GlobalConfig.getProperty("searchh", "sendurl");
			if(url!=null && !"".equals(url))
			{
				urls = extracted(url);
			}
		   
		    if(urls!=null)
		    {
		    	 log.info("tongzhi-url-begin:"+url);
				extracted(url, urls,appCode);
				 log.info("tongzhi-url-end:"+url);
		    }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}


	private static  void extracted(String url, String[] urls,String appCode) {
		{
			Map<String,String> pm =  new HashMap<String,String>();
			pm.put("appCode", appCode);
		    for(String urltmp:urls)
		    {
		    	HttpClientUtil.sendHttpPostRequest(urltmp+"Onlyrp",pm, null) ;
		    	 log.info("正在通知节点url:"+url+"|切换索引库位置并reopen");
		    }
		}
	}


	private  static String[] extracted(String url) {
		//QueryParser parser = new QueryParser(Version.LUCENE_47, "", null);
		String[] urls;
		{
			urls = url.split(",");
		}
		return urls;
	}
	
	/**
	 * 定时读取数据库创建推送索引库 
	 */
	public static void doTuiIndexWrite() {
		SearchMessageService searchMessageService = SpringContextHolder
				.getBean(SearchMessageService.class);
		Map<String,String> appcodeMap = new HashMap<String,String>();// 存放要调用的appcode
		int count =50;
		try{
			count =Integer.parseInt(GlobalConfig.getProperty("searchh", "readlogcount"));
			if(count>999)
			{
				count =999;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			log.info("doTuiIndexWrite-error 的count =Integer.parseInt:readlogcount",e);
		}
		LimitInfo limit = new LimitInfo();
		limit.setRowDisplayed(count);
		List<SearchMessageDTO> rlst = searchMessageService.list(limit, " (*:*) ");
		
		//log.info("=======>"+rlst.size());
		//读取
		StringBuffer sb =new StringBuffer();
		Map<String,String> mapOpt = new HashMap<String,String>();
		boolean r;
		for (SearchMessageDTO searchMessageDTO : rlst) {
			log.info("===searchMessageDTO.getId()====>"+searchMessageDTO.getId());
			appcodeMap.put(searchMessageDTO.getAppcode(), searchMessageDTO.getAppcode());
			mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_OPT, searchMessageDTO.getOpt());
			mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_COMMANDCODE, searchMessageDTO.getCommandcode());
			mapOpt.put(RedisKeyConst.Search.SEARCH_TUI_APPCODE, searchMessageDTO.getAppcode());
			TuiHelpDTO tuiHelpDTO = searchMessageService.readXml(searchMessageDTO.getFileinfo());
			r=optIndexTui(mapOpt,tuiHelpDTO.getList());
			if(r){
				sb.append("'").append(searchMessageDTO.getId()).append("',");
			}
		}
		//sb.append("1");
		try {
			if(sb.length()>0){
				log.info("delete doTuiIndexWrite is begin");
				log.info("delete doTuiIndexWrite id is ["+sb.toString()+"]"); 
				searchMessageService.deleteTX(sb.toString());
				
				String url=null;
				String urls[]=null ;
				url = GlobalConfig.getProperty("searchh", "sendurl");
				if(url!=null && !"".equals(url))
				{
					urls = extracted(url);
				}
			  Iterator<String> itappcods = appcodeMap.keySet().iterator();
			  while(itappcods.hasNext())
			  {
				  String toappcode=itappcods.next();
			    if(urls!=null)
			    {
			    	 log.info("tui-send call searchweb url:["+url+"]appcode:["+toappcode+"] begin");
					 extracted(url, urls,toappcode);
					 log.info("tui-send call searchweb url:["+url+"]appcode:["+toappcode+"] end");
			    }
			  }
			}
			//log.info("delete doTuiIndexWrite is end，sb["+sb.toString()+"]");
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("delete doTuiIndexWrite [" + sb.toString() + "] error",e);
		}
	}
	
	private static void clearIndex(IndexWriter indexWriter) throws IOException
	{
		long a =System.currentTimeMillis();
		indexWriter.deleteAll();
 		indexWriter.commit(); 
 		long b =System.currentTimeMillis();
 		log.info("删除索引总共花费 ["+((b-a)/1000)+"秒]");
	}
	
	public static void saveClickLogByCallbackSearchweb(){
		CollectInfoService clicklogService = SpringContextHolder.getBean(CollectInfoService.class);
		String url = GlobalConfig.getProperty("searchh", "clicklogurl");
		if(url!=null && !"".equals(url)){
			//如果是配置多节点
			if(url.indexOf("_~_")!=-1){
				String[] URL = url.split("_~_");
				for(int i=0;i<URL.length;i++){
					String result = HttpClientUtil.sendHttpPostRequest(URL[i],null, null) ;
					if(StringUtils.isNotBlank(result) && !"null".equals(result)){
						String[] jsonArray = result.split(LoggerConstants.NULL_PLACEHOLDER);
						for(String json : jsonArray){
							ClickVisitLogInfoDTO clickDTO = JsonUtil.getDTO(json, ClickVisitLogInfoDTO.class);
							clicklogService.intoClickVisitLogInfo(clickDTO);;
						}
					}
				}
			}else{
				//如果是配置单节点（默认单节点）
				String result = HttpClientUtil.sendHttpPostRequest(url,null, null) ;
				if(StringUtils.isNotBlank(result) && !"null".equals(result)){
					String[] jsonArray = result.split(LoggerConstants.NULL_PLACEHOLDER);
					for(String json : jsonArray){
						ClickVisitLogInfoDTO clickDTO = JsonUtil.getDTO(json, ClickVisitLogInfoDTO.class);
						clicklogService.intoClickVisitLogInfo(clickDTO);;
					}
				}
			}
		}
	}
	
    private static Analyzer analyzerType(String appCode){
    	Analyzer analyzer = new IKAnalyzer();
    	if(StringUtils.isNotBlank(appCode)){
    		SearchAppService searchAppService = SpringContextHolder
    				.getBean(SearchAppService.class);
    		String analyzer_Type = searchAppService.getAnalyzerTypeByAppCode(appCode);
    		if(StringUtils.isNotBlank(analyzer_Type)){
    			if("1".equals(analyzer_Type)){
    				return analyzer;
    			}
    			Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_47);
    			if("2".equals(analyzer_Type)){
    				return standardAnalyzer;
    			}
    		}
    	}
    	return analyzer;
    }
    
    public static String datestring(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat();
        
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public static String extostring(Exception e)
    {
		 String err="";
		 try{
	    	StringWriter writer = new StringWriter();
	        PrintWriter printWriter= new PrintWriter(writer);
	        e.printStackTrace(printWriter);
	        err =writer.toString();
		 }catch(Exception ee)
		 {
			 ee.printStackTrace();
		 }
	        return err;
    }
    public static void  intoqueuelog(String appcode,String commandcode,String totalpage,String cpage,long begintime,String flag,String errorinfo)
    {
    	String ku = AppContext.getAppCacheDTOMap().get(appcode).getSearchAppDTO().getCurrentDir();
    	CreateIndexProcessLogDTO createIndexProcessLogDTO =new CreateIndexProcessLogDTO();
    	createIndexProcessLogDTO.setId(getUUID());
    	createIndexProcessLogDTO.setAppcode(appcode);
    	createIndexProcessLogDTO.setCommandcode(commandcode);
    	createIndexProcessLogDTO.setCpage(cpage);
    	createIndexProcessLogDTO.setTotalpage(totalpage);
    	createIndexProcessLogDTO.setKu(ku);
    	createIndexProcessLogDTO.setDobegintime(begintime);
    	createIndexProcessLogDTO.setDoendtime(System.currentTimeMillis());
    	createIndexProcessLogDTO.setFlag(flag);
    	createIndexProcessLogDTO.setErrinfo(errorinfo);
    	System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"+datestring(new Date()));
    	createIndexProcessLogDTO.setCdate(datestring(new Date()));
    	try {
			AppContext.cindexlogBlockingQueue.offer(createIndexProcessLogDTO, 100, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("进入日志队列intoqueuelog error：intoqueuelog[appcode:"+appcode+"|+commandcode:"+commandcode+"|cpage:"+cpage+"|totalpage:"+totalpage+"|ku:"+ku+"]",e);
			return ;
		}
    	log.info("进入日志队列：intoqueuelog ok [appcode:"+appcode+"|+commandcode:"+commandcode+"|cpage:"+cpage+"|totalpage:"+totalpage+"|ku:"+ku+"]");
    }
    private static String getUUID() {
		return StringUtils.replace(UUID.randomUUID().toString().trim(), "-",
				"");
	}
    
	/**
	 * 创建索引调用方法
	 * 
	 * @param appCode
	 *            注册的appcode
	 */
	public static void doIndexWriteWorkViewTest(String appCode) {
		long dobegin=System.currentTimeMillis();
		boolean isreaddb =false;//是否能读取数据内容
		boolean issendurl =false;//是否通知url切换索引库
		if(checkAppCodeIsStop(appCode))
		{
			log.info("doIndexWriteWorkView的appCode["+appCode+"]已停用");
			return;
		}
		int count =500;
		 
		AppCacheDTO appCacheDTO = AppContext.getAppCacheDTOMap().get(appCode);
		if (appCacheDTO != null) { // 如果是视图方式创建索引
			if (RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI.equals(appCacheDTO
					.getSearchAppDTO().getIndexType())) {
				Map<String, SearchCommandDTO> searchCommandDTOMap = appCacheDTO
						.getSearchCommandDTOMap();
				Iterator<String> itCommand = searchCommandDTOMap.keySet()
						.iterator();
				while (itCommand.hasNext())// 循环子业务规则表
				{
					
					String command = itCommand.next();
			 
					SearchCommandDTO searchCommandDTO =appCacheDTO.getSearchCommandDTOMap().get(command);
					boolean ismap =false;
					if("2".equals(searchCommandDTO.getExtend()))
					{
						ismap=true;
					}
					Map<String,SearchRuleDateDTO> ruleDateDTOMap =filterCommandcode(command,appCode);
				 
					LimitInfo limit = new LimitInfo();
					limit.setRowDisplayed(500);
					if("productdemo".equals(appCode))
					{
						limit.setTotalNum(AppContext.demoall.size());
					}else if ("mapdemo".equals(appCode))
					{
						limit.setTotalNum(AppContext.demoall2.size());
					}
					
					IndexWriter indexWriter = getIndexWriter(appCode);
					long dbbegin,dbend,indexbegin,indexend ,indexcommit;
					try{
						 
						do {
							try {
								dbbegin=System.currentTimeMillis();
								List<Map<String, String>> rlst= null;
								if("productdemo".equals(appCode))
								{
									rlst= AppContext.demoall.subList(limit.getStartLineNum()-1, limit.getEndLineNumOk());
								}else if ("mapdemo".equals(appCode))
								{
									rlst= AppContext.demoall2.subList(limit.getStartLineNum()-1, limit.getEndLineNumOk());
								}
								if(!isreaddb && rlst.size()>0) {//判断数据库第一次是否能正确读取到数据，如果能则删除旧的索引，如果不能读到数据则保留老索引
									clearIndex(indexWriter);
									issendurl =true;//成功调用清除索引了 就代表正常创建了
									String flagDir = AppContext.getAppCacheDTOMap().get(appCode)
											.getSearchAppDTO().getCurrentDir();
									log.info("成功删除旧索引库:["+appCode+"]库["+flagDir+"]issendurl =true");
								}
								isreaddb =true;
								if(!issendurl)
								{
									//第一次不成功则break跳出循环
									log.info("第一次读取数据库失败则跳出循环BREAK！！！！:["+appCode+"]issendurl =false");
									break;
									
								}
								
								dbend=System.currentTimeMillis();
								indexbegin= System.currentTimeMillis();
								Document doc = null;
								
								for (Map<String, String> rmap : rlst) {
									try {
										doc = createDoc(command, rmap, ruleDateDTOMap,indexWriter,ismap);
										indexWriter.addDocument(doc);// 增加一个方法commit
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										log.error("doIndexWriteWorkView make doc error appCode [" + appCode+ "] -commandcode:["+command+"]创建doc失败原因为" + e.getMessage(),
												e);
									}
								}
								indexend= System.currentTimeMillis();
								indexWriter.commit();
								indexcommit=System.currentTimeMillis();
								log.info("doIndexWriteWorkView create index appcode:["+appCode+"]-commandcode:["+command+"]耗时统计：第:["+limit.getPageNum()+"]次.数据库查询耗时["+((dbend-dbbegin)/1000)+"秒]"
										          +"索引创建耗时["+((indexend-indexbegin)/1000)+"秒]"
										          +"索引提交耗时["+((indexcommit-indexend)/1000)+"秒]");
								log.info("doIndexWriteWorkView create index page appcode:["+appCode+"]-commandcode:["+command+"] current page-size:["+limit.getPageNum()+"]"+"total-page-size["+limit.getTotalPage()+"]");
								intoqueuelog(appCode, command, limit.getTotalPage()+"", limit.getPageNum()+"", dobegin,"1","ok");
								limit.setPageNum(limit.getPageNum() + 1);
							} catch (Exception e) {
								intoqueuelog(appCode, command, limit.getTotalPage()+"", limit.getPageNum()+"", dobegin,"2",extostring(e)); 
								limit.setPageNum(limit.getPageNum() + 1);//防止死循环
								e.printStackTrace();
								log.error("doIndexWriteWorkView create index error appCode [" + appCode + "] -commandcode:["+command+"];"
										+ e.getMessage(), e);
							}
							
						} while (limit.getPageNum() <= limit.getTotalPage());
					}catch(Exception e)
					{
						e.printStackTrace();
					} 
				}
			}
			//更改数据库cdir，并且通知前台（servlet）更改查询索引库
			if(issendurl){
				log.info("doIndexWriteWorkView 成功重新创建完本次索引库issendurl:["+issendurl+"]appCode:["+appCode+"] 进行通知changeCDirSearch(appCode);");
				//clearClogP(appCode,"old");
				changeCDirSearch(appCode);
				clearClogP(appCode,"new");//qing kong jisuan  cpage totalpage
			}else{
				log.info("doIndexWriteWorkView 没有创建本次索引库issendurl:["+issendurl+"]appCode:["+appCode+"] NOT！！！不通知changeCDirSearch(appCode);");
				
			}
			
		} else {
			log.info("doIndexWriteWorkView appCode [" + appCode
					+ "] appCacheDTO 为null，请重新引用该配置或者该appCode不存在");
		}
	}
}
