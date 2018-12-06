package com.sinovatech.search.luceneindex.task;

import java.util.Map;

import org.apache.commons.logging.Log;

import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.LuceneManager;

public class IndexWriteTask implements Runnable {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(IndexWriteTask.class);
	private String appCode;
	public IndexWriteTask(String appCode)
	{
		this.appCode=appCode;
	}
	@Override
	public void  run()  {
		// TODO Auto-generated method stub
		Map<String,  AppCacheDTO >  appCacheDTOMap = AppContext.getAppCacheDTOMap();
		try{
		if(appCacheDTOMap!=null)
		{
			 AppCacheDTO  appCacheDTO  = appCacheDTOMap.get(appCode);
			 if(appCacheDTO!=null)
			 {
				 //视图
				 if(appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SHI))
				 {
					 LuceneManager.doIndexWriteWorkView(appCode);
				 }//服务
				 else if(appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_SERVICE))
				 {
					 LuceneManager.doIndexWriteWorkWs(appCode);
				 }//FTP
				 else if(appCacheDTO.getSearchAppDTO().getIndexType().equals(RedisKeyConst.Search.SEARCHAPP_INDEXTYPE_FTP))
				 {
					 LuceneManager.doIndexWriteWorkFtp(appCode);
				 } 
			 } 
		}
		}catch(Exception e){
			log.error("IndexWriteTask-run-error", e);
		  e.printStackTrace();
		}
	}
}
