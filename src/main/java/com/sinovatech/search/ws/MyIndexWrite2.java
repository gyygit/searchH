package com.sinovatech.search.ws;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FieldDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class MyIndexWrite2 { 

	public static IndexWriter createIndexWriter(Map<String, String> configMap)
			throws CorruptIndexException, LockObtainFailedException,
			IOException {  
		//构造分词器                     
		Analyzer analyzer = new IKAnalyzer();
		//Analyzer analyzer = new CJKAnalyzer(Version.LUCENE_47); 
		//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);  
		//构造indexWrite相关配置
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_47 , analyzer);
		iwConfig.setOpenMode(OpenMode.CREATE);//直接创建
		Directory dir = FSDirectory.open(new File("C:\\sdb"));
		IndexWriter indexWriter = new IndexWriter(dir , iwConfig);  
        return indexWriter;
	}
	
	private static void createIndex()throws Exception 
	{
	    IndexWriter  indexWriter = createIndexWriter(new HashMap());
		 
		Document doc0  = createDoc("不错的小米手机","HTC","老香","2012-12-12","100","500");
		Document doc1  = createDoc("小辣椒手机public welcome","HTC","老香","2012-12-13","110","510");
		Document doc2  = createDoc("经济时尚大众手机漂亮 static void main ","SANXINGwelcome","老香","2014-12-14","120","520");
		Document doc3  = createDoc("经济实惠大众手机出色 string yellow","TCL,SANXING","老香","1985-12-15","130","530");
		Document doc4  = createDoc("经济实惠大众手机时尚","SANXING,KUPAI","小香","2012-12-16","150","550");
		Document doc5  = createDoc("经济实惠大众手机便宜","SANXING,KUPAI","小香","2012-12-17","100","500");
		Document doc6  = createDoc("经济实惠大众手机便宜","KUPAI,MMM","小香","1900-12-18","110","510");
		Document doc7  = createDoc("經濟實惠手機美觀騙子","KUPAI,MMM","小香","2012-12-19","120","520");
		Document doc8  = createDoc("中華人民恭賀我餓樂","MMM,HTC","小香","1200-12-21","130","530");
		Document doc9  = createDoc("經濟實惠手機美觀騙子","MMM,HTC","老香","3012-12-22","100","500");
		Document doc10  = createDoc("时尚手机","MMM,HTC","老香","3012-12-22","100","500");
		Document doc11  = createDoc("时尚迷你","MMM,HTC","老香","3012-12-22","100","500");
		indexWriter.addDocument(doc0);
		indexWriter.addDocument(doc1);
		indexWriter.addDocument(doc2);
		indexWriter.addDocument(doc3);
		indexWriter.addDocument(doc4);
		indexWriter.addDocument(doc5);
		indexWriter.addDocument(doc6);
		indexWriter.addDocument(doc7);
		indexWriter.addDocument(doc8);
		indexWriter.addDocument(doc9);
		indexWriter.addDocument(doc10);
		indexWriter.addDocument(doc11);
		indexWriter.commit(); 
		indexWriter.close();
	}
	public static void main(String args[]) throws Exception
	{ 
//		Analyzer analyzer = new IKAnalyzer(); 
//		Analyzer analyzer2 = new IKAnalyzer(); 
//		TokenStream ts2 = analyzer2.tokenStream("1111", "冬虫夏草北京人");
//		TokenStream ts = analyzer.tokenStream("1111", "冬虫夏草北京人");
//		 CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
//		 ts.reset(); 
//		 while (ts.incrementToken()) {
//			 System.out.println(term.toString());  
//		 }
//		 CharTermAttribute term1 = ts2.addAttribute(CharTermAttribute.class);
//		 ts2.reset(); 
//		 while (ts2.incrementToken()) {
//			 System.out.println(term1.toString());  
//		 }
		// getIndexSearcher("c:\\index1"); 
		   // createIndex(); 
		// searchOne("D:\\apache-tomcat-6.0.35\\webapps\\searchH\\db_lucene__tables\\SearchAppDAO", " (*:*)");
		 
//			Condition con = new Condition();
//			con.addCondition("name","手机",Condition.Logic.NOT,Condition.Opt.EQ);
//			con.addCondition("pinpai","HTC",Condition.Logic.AND,Condition.Opt.EQ);
//			System.out.println(con.builder());
		    //searchOne("c:\\sdb", con.builder());
			searchOne("D:\\linex\\iindex\\product\\data_a", "*:*");
		 
		  //searchOne( "C:\\oldsearch\\data_b" ,"(commandCode:oldsearch OR  1:2)  AND  ( (STATUS:00~0)  AND  ( ((SEARCHWORDNAME_ZH:流量包*)  OR (SEARCHWORDNAME_PY:流量包*)  OR (SEARCHWORDNAME_PINYIN:流量包*))  OR  ((SEARCHWORDNAME:流量包)  OR (SEARCHWORDNAME_PY_FC:流量包)  OR (SEARCHWORDNAME_PINYIN_FC:流量包))  OR  (  (SEARCHWORDNAME_PY_FC:llb)  OR (SEARCHWORDNAME_PINYIN_FC:liuliang bao))  OR  1:2)  )");
		  // searchOne("C:\\index1", " (name:经济  OR name:时尚   ) AND  name:迷你 ");
		 //   searchOne("C:\\index1", " name:经济 ");
		// searchOne("D:\\linex\\iindex\\product\\hotworddata", " (*:*)");
		 // searchOne( "E:\\oldsearch\\data_b" ,"(commandCode:oldsearch OR  1:2) AND (STATUS:01~0) AND  ( (((SEARCHWORDNAME_PINYIN_FC:iph) ) OR (SEARCHWORDNAME_PY_FC:iph) OR (SEARCHWORDNAME:iph) )   OR  ( ((SEARCHWORDNAME_ZH:iph*)  OR (SEARCHWORDNAME_PY:iph*)  OR (SEARCHWORDNAME_PINYIN:iph*))  OR  1:2)  )");
		 //group(getIndexSearcher("E:\\oldsearch\\data_b"),"BRAND_NAME", "GOODSNAME:千年 AND ((PROTYPE_CODE:10001_00001_00001~0) OR (PROTYPE_CODE:10001_00002_00001~0) OR (PROTYPE_CODE:10003_00001_00001~0) OR (PROTYPE_CODE:10003_00001_00002~0) OR (PROTYPE_CODE:10003_00001_00003~0) OR (PROTYPE_CODE:10003_00001_00004~0) OR (PROTYPE_CODE:10003_00001_00005~0) OR (PROTYPE_CODE:10003_00001_00006~0) OR (PROTYPE_CODE:10003_00001_00007~0) OR (PROTYPE_CODE:10003_00001_00008~0) OR (PROTYPE_CODE:10003_00001_00009~0) OR (PROTYPE_CODE:10003_00001_00010~0) OR (PROTYPE_CODE:10003_00002_00002~0) OR (PROTYPE_CODE:10003_00002_00003~0) OR (PROTYPE_CODE:10003_00002_00004~0) OR (PROTYPE_CODE:10003_00003_00001~0) OR (PROTYPE_CODE:10003_00003_00002~0) OR (PROTYPE_CODE:10003_00003_00003~0) OR (PROTYPE_CODE:10003_00004_00001~0) OR (PROTYPE_CODE:10003_00005_00001~0) OR (PROTYPE_CODE:10004_00001_00003~0) OR (PROTYPE_CODE:10004_00001_00004~0) OR (PROTYPE_CODE:10004_00002_00001~0) OR (PROTYPE_CODE:10004_00002_00002~0) OR (PROTYPE_CODE:10004_00002_00003~0) OR (PROTYPE_CODE:10005_00002_00001~0) OR (PROTYPE_CODE:10005_00002_00002~0) OR (PROTYPE_CODE:10005_00002_00003~0) OR (PROTYPE_CODE:10005_00002_00004~0) OR (PROTYPE_CODE:10005_00002_00005~0) OR (PROTYPE_CODE:10005_00002_00006~0) OR (PROTYPE_CODE:10005_00003_00001~0) OR (PROTYPE_CODE:10005_00004_00001~0) OR (PROTYPE_CODE:10005_00005_00001~0) OR (PROTYPE_CODE:10005_00005_00002~0) OR (PROTYPE_CODE:10005_00006_00001~0) OR (PROTYPE_CODE:10005_00007_00001~0) OR (PROTYPE_CODE:10005_00008_00001~0) OR (PROTYPE_CODE:10005_00008_00002~0) OR (PROTYPE_CODE:10005_00008_00003~0) OR (PROTYPE_CODE:10006_00001_00001~0) OR (PROTYPE_CODE:10006_00001_00002~0) OR (PROTYPE_CODE:10006_00001_00003~0) OR (PROTYPE_CODE:10006_00001_00004~0) OR (PROTYPE_CODE:10006_00001_00005~0) OR (PROTYPE_CODE:10006_00001_00006~0) OR (PROTYPE_CODE:10006_00001_00007~0) OR (PROTYPE_CODE:10006_00001_00008~0) OR (PROTYPE_CODE:10006_00001_00009~0) OR (PROTYPE_CODE:10006_00001_00010~0) OR (PROTYPE_CODE:10006_00003_00001~0) OR (PROTYPE_CODE:10006_00003_00002~0) OR (PROTYPE_CODE:10006_00003_00003~0) OR (PROTYPE_CODE:10006_00003_00004~0) OR (PROTYPE_CODE:10006_00003_00005~0) OR (PROTYPE_CODE:10006_00003_00006~0) OR (PROTYPE_CODE:10006_00003_00007~0) OR (PROTYPE_CODE:10006_00003_00008~0) OR (PROTYPE_CODE:10006_00003_00009~0) OR (PROTYPE_CODE:10006_00003_00010~0) OR (PROTYPE_CODE:10006_00004_00001~0) OR (PROTYPE_CODE:10006_00005_00001~0) OR (PROTYPE_CODE:10006_00006_00001~0) OR (PROTYPE_CODE:10006_00007_00001~0) OR (PROTYPE_CODE:10006_00008_00001~0) OR (PROTYPE_CODE:10006_00009_00001~0) OR (PROTYPE_CODE:10006_00010_00001~0) OR (PROTYPE_CODE:10006_00011_00001~0) OR (PROTYPE_CODE:10006_00012_00001~0) OR (PROTYPE_CODE:10006_00013_00001~0) OR (PROTYPE_CODE:10006_00014_00001~0) OR (PROTYPE_CODE:10006_00015_00001~0) OR (PROTYPE_CODE:10006_00016_00001~0) OR (PROTYPE_CODE:10006_00017_00001~0) OR (PROTYPE_CODE:10006_00018_00001~0) OR (PROTYPE_CODE:10006_00020_00001~0) ) "); 
		//Analyzer analyzer = new IKAnalyzer()
		//System.out.println( Double.valueOf(Math.round((Math.random()*10))).intValue()%5  );
		
		String s ="aaaa";
		System.out.println(s.split(";")[0]);
	
		
	}
	 
	public static  void group(IndexSearcher indexSearcher,String groupField,String content) throws IOException, ParseException {
        GroupingSearch groupingSearch = new GroupingSearch(groupField);
        groupingSearch.setGroupSort(new Sort(SortField.FIELD_SCORE));
        groupingSearch.setFillSortFields(true);
        groupingSearch.setCachingInMB(4.0, true);
        groupingSearch.setAllGroups(true);
        //groupingSearch.setAllGroupHeads(true);
        groupingSearch.setGroupDocsLimit(2);
        Analyzer language = new IKAnalyzer();
        QueryParser parser = new QueryParser(Version.LUCENE_47, "", language);
        Query query = parser.parse(content);
 
        TopGroups<BytesRef> result = groupingSearch.search(indexSearcher, query, 0, 6);
 
        System.out.println("搜索命中数：" + result.totalHitCount);
        System.out.println("搜索结果分组数：" + result.groups.length);
 
        Document document;
        for (GroupDocs<BytesRef> groupDocs : result.groups) {
            System.out.println("分组：" + groupDocs.groupValue.utf8ToString());
            System.out.println("组内记录：" + groupDocs .totalHits);
 
            //System.out.println("groupDocs.scoreDocs.length:" + groupDocs.scoreDocs.length);
            for (ScoreDoc scoreDoc : groupDocs.scoreDocs) {
                System.out.println(indexSearcher.doc(scoreDoc.doc));
            }
        }
    }
	
	private static FieldDoc getLastScoreDoc(int pageIndex, int pageSize,Query query,IndexSearcher searcher,Sort sort) throws IOException{
		if(pageIndex==1)  return null;
		int num = (pageIndex-1)*pageSize;
		//TopDocs tds = searcher.search(query, num);
		TopFieldDocs tds = searcher.search(query, null, num, sort);
		return (FieldDoc)tds.scoreDocs[num-1];
	}
	
	private static void setPageDate(TopDocs tds,IndexSearcher indexSearcher) throws IOException, ParseException{
		 List<IndexableField> fList = null;
		 List<Map<String,String>> rdate = new ArrayList<Map<String,String>>();
		 System.out.println("=================="+tds.totalHits);
		for(ScoreDoc sd: tds.scoreDocs){
			Document d = indexSearcher.doc(sd.doc);
			if(fList==null)
			{
				fList=d.getFields();
			}
			for(IndexableField indexableField:fList){
				System.out.print( indexableField.name()+":["+d.get(indexableField.name())+"]");
				
			}
			 System.out.println("==================");
		}
	}
	
	
	private static Document createDoc(String name,String pinpai,String area,String date,String salenum,String storeNum) throws UnsupportedEncodingException {  
        Document doc = new Document();  
         //就像有某个商品，查询结果列表要展示商品的名称，ID，和跳转链接地址，所以从数据库取出name,id,url字段   
        doc.add(new TextField("name", name, Field.Store.YES));
        doc.add(new StringField("pinpai", pinpai, Field.Store.YES));
        doc.add(new StringField("area", area, Field.Store.YES));
        doc.add(new StringField("date", date, Field.Store.YES));
        doc.add(new StringField("salenum",  salenum , Field.Store.YES)); 
        doc.add(new StoredField("storeNum",storeNum));  
    return doc;  
	}   


	private static IndexSearcher getIndexSearcher(String indexPath) {
		IndexSearcher search = null;
		try {
			FSDirectory directory = FSDirectory.open(new File(indexPath));// 打开索引库
			IndexReader reader = DirectoryReader.open(directory);// 流读取
			search = new IndexSearcher(reader);// 搜索
		} catch (Exception e) {
			e.printStackTrace();
		}
		return search;
	}

	public static void searchOne(String indexPath, String searchWords)
			throws ParseException, IOException {
		Analyzer language = new IKAnalyzer();
		//Analyzer language = new CJKAnalyzer(Version.LUCENE_47);
		//Analyzer language = new StandardAnalyzer(Version.LUCENE_47);  
		QueryParser parser = new QueryParser(Version.LUCENE_47, "", language);
		Query query = parser.parse(searchWords);
		IndexSearcher indexSearcher = getIndexSearcher(indexPath);
		 // Sort sort=new Sort(new SortField("QUESTION_TIME", SortField.Type.STRING,true));//true为降序排列
		 //Sort sort = new Sort(/*new SortField("TITLE", SortField.Type.SCORE), */new SortField("UPDATE_LAST_TIME", Type.STRING,true));
		Sort sort=new Sort();
		
		//TopDocs results = indexSearcher.search(query, 100);
		
		ScoreDoc lastSd = getLastScoreDoc(2,100, query, indexSearcher,sort);
		TopDocs tds = null;
		 if(lastSd==null)
		 {
			 tds = indexSearcher.search(query, 100,sort);//获取最高得分命中
		 }else{
			 tds = indexSearcher.searchAfter(lastSd, query, 100,sort);
		 }
		setPageDate(tds, indexSearcher);
	}
	public static void delfor(String s) throws  Exception
	{
		Analyzer language = new IKAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_47, "", language);
		Query query = parser.parse(s);
		IndexWriter  indexWriter = createIndexWriter(new HashMap());
		indexWriter.deleteDocuments(query);
		indexWriter.commit();
	}
 
 
}
