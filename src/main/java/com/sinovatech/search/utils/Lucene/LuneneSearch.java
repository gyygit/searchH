package com.sinovatech.search.utils.Lucene;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.IndexSearcher;


public class LuneneSearch {
	private String indexPathString=null;
	private IndexSearcher indexSearcher=  null;
	
	public LuneneSearch(String indexPathString,IndexSearcher indexSearcher)
	{
		this.indexSearcher = indexSearcher;
	}
	 /** 
     * 搜索 
     */  
    public List<String> serach(String queryString) throws Exception {  
    	 //Analyzer analyzer=new SmartChineseAnalyzer(Version.LUCENE_33);  
        //1 把要搜素的文本解析为QUery  
        String [] fields = {"content"};  
       // QueryParser queryParser =new MultiFieldQueryParser(Version.LUCENE_33, fields, analyzer);  
        //Query query =queryParser.parse(queryString);  
        //File indexPath=new File(queryString);//存放索引文件目录  
        // Directory fsDir =FSDirectory.open(indexPath);  
  
         //IndexSearcher indexSearcher= new IndexSearcher(fsDir);  
        // SerachResult serachResult=SerachUtil.serach(1, 4, query,analyzer,indexSearcher,null);   
        // System.out.println("总共有[ "+serachResult.getTotalCount()+" ]条匹配结果");  
         //3 打印结果  
         List<String> rsltList = new ArrayList<String>();
//         for (Document doc : (List<Document>)serachResult.getRecords()) { 
//        	 
//        	 rsltList.add(doc.get("content"));
//             System.out.println("content:"+doc.get("content"));  
//         }  
         return rsltList;  
    }  
}
