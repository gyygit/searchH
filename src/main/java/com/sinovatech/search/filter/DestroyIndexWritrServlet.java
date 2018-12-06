package com.sinovatech.search.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.db.DbLuceneCon;
import com.sinovatech.search.luceneindex.db.InitDbLuceneConnection;

public class DestroyIndexWritrServlet extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DestroyIndexWritrServlet.class);
	public void init() throws ServletException
	  {
	      // 执行必需的初始化
		log.info("init servlet DestroyIndexWritrServlet no nothing worke");
	  }

	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	            throws ServletException, IOException
	  {
			log.info(" DestroyIndexWritrServlet doGet() no nothing worke");
	  }
	  
	  public void destroy()
	  {
		  log.info(" 发现被销毁 卸载程序或者重启   restart destroy  kill indexWrite beigin ");
		  
		  Map<String, IndexWriter>   hotWordIdexWriterMap =  AppContext.getHotWordIdexWriterMap();
		  for (Entry<String, IndexWriter> entry : hotWordIdexWriterMap.entrySet()) {
			  try{
				  log.info(" destroy  kill hotWordIdexWrite appcode  is ["+entry.getKey()+"]");
				  entry.getValue().close();
			  }catch(Exception e)
			  {
				  log.error(" destroy  kill hotWordIdexWrite appcode  is ["+entry.getKey()+"] shibai ",e);
			  }
			   
		  }
		  Map<String, IndexWriter>   aIndexWriterMap =  AppContext.getaIndexWriterMap();
		  for (Entry<String, IndexWriter> entry : aIndexWriterMap.entrySet()) {
			  try{
				  log.info(" destroy  kill aIndexWriterMap appcode  is ["+entry.getKey()+"]");
				  entry.getValue().close();
			  }catch(Exception e)
			  {
				  log.error(" destroy  kill aIndexWriterMap appcode  is ["+entry.getKey()+"] shibai ",e);
			  }
			   
		  }
		  
		  Map<String, IndexWriter>   bIndexWriterMap =   AppContext.getbIndexWriterMap();
		  for (Entry<String, IndexWriter> entry : bIndexWriterMap.entrySet()) {
			  try{
				  log.info(" destroy  kill bIndexWriterMap appcode  is ["+entry.getKey()+"]");
				  entry.getValue().close();
			  }catch(Exception e)
			  {
				  log.error(" destroy  kill bIndexWriterMap appcode  is ["+entry.getKey()+"] shibai ",e);
			  }
			   
		  }
		  
		  Map<String, DbLuceneCon> dbLuceneConMap = InitDbLuceneConnection.getDbLuceneOptMap();
		  
		  for (Entry<String, DbLuceneCon> entry : dbLuceneConMap.entrySet()) {
			  try{
				  log.info(" destroy  kill dbLuceneConMap appcode  is ["+entry.getKey()+"]");
				  entry.getValue().getIndexWriter().close();
			  }catch(Exception e)
			  {
				  log.error(" destroy  kill dbLuceneConMap appcode  is ["+entry.getKey()+"] shibai ",e);
			  }
			   
		  }
		 
		  log.info(" 发现被销毁 卸载程序或者重启   restart destroy kill indexWrite end ");
	  }
}
