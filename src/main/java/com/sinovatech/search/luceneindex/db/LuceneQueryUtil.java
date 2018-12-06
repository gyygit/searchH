package com.sinovatech.search.luceneindex.db;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
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
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppCacheDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.utils.SpringContextHolder;

public class LuceneQueryUtil {
 
//	private  static Analyzer analyzer =  new IKAnalyzer(); 
 
//	private  static Analyzer analyzerIK =  new IKAnalyzer(); 
	//private  static Analyzer analyzerIK =new StandardAnalyzer(Version.LUCENE_47);
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(LuceneQueryUtil.class);
 
	/**
	 * 分页用
	 * @param pageIndex
	 * @param pageSize
	 * @param query
	 * @param searcher
	 * @param sort
	 * @return
	 * @throws IOException
	 */
	private static FieldDoc getLastScoreDoc(int pageIndex, int pageSize,Query query,IndexSearcher searcher,Sort sort) throws IOException{
		if(pageIndex==1)  return null;
		int num = (pageIndex-1)*pageSize;
		//TopDocs tds = searcher.search(query, num);
		TopFieldDocs tds = searcher.search(query, null, num, sort);
		return (FieldDoc)tds.scoreDocs[num-1];
	}
	  
	/**
	 * 查询方法
	 * @param page
	 * @return
	 */
	public static Page query(Page page,IndexSearcher indexSearcher)
	{
		log.info("执行query");
		 
		 if(indexSearcher==null)
		 {
			 return page;
		 }
		 try {
			page = getPageQuery(page,indexSearcher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			page.setErrorMsg(e.getMessage());
			log.error("查询错误，请检查！page.getAooCode:"+page.getAppCode()+"|page.getQueryStr():"+page.getQueryStr()+"|page.getCommandCode():"+page.getCommandCode(),e);
		}
		return page;
	}
	 
	/**
	 * 查询方法
	 * @param page
	 * @return
	 */
	public static Page queryIk(Page page,IndexSearcher indexSearcher)
	{
		log.info("执行query");
		 
		 if(indexSearcher==null)
		 {
			 return page;
		 }
		 try {
			page = getPageQueryIk(page,indexSearcher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			page.setErrorMsg(e.getMessage());
			log.error("查询错误，请检查！page.getAooCode:"+page.getAppCode()+"|page.getQueryStr():"+page.getQueryStr()+"|page.getCommandCode():"+page.getCommandCode(),e);
		}
		return page;
	}
	private  static Page getPageQueryIk(Page page,IndexSearcher indexSearcher)throws ParseException, IOException{
//		QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzerIK);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzerType(page));
		parser.setLowercaseExpandedTerms(false);
		//构造查询语句
		String query_str="";
		 query_str=page.getQueryStr();
		page.setQueryStr(query_str);
		log.info("[getPageQuery-page.getQueryStr()]"+page.getQueryStr());
		Query query =parser.parse(query_str);
		//构造排序
		List<String[]> sortList=page.getSortList();
		Sort sort = new Sort();
		if(sortList.size()>0){
			SortField [] sortField = new SortField[page.getSortList().size()];
			for(int i=0;i<sortList.size();i++)
			{
				String [] strs_tmp = sortList.get(i);
				boolean issort=true;
				if("ASC".equals(strs_tmp[1]))
				{
					issort =false;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.STRING,issort);
				}if("DESC".equals(strs_tmp[1]))
				{
					issort =true;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.STRING,issort);
				}
				if("INT_DESC".equals(strs_tmp[1]))
				{
					issort =true;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.INT,issort);
				}
				if("INT_ASC".equals(strs_tmp[1]))
				{
					issort =false;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.INT,issort);
				}
				if("SCORE".equals(strs_tmp[1]))
				{
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.SCORE);
				}
				
			}
			sort = new Sort(sortField);
		}
		int rowDisplayed;
		if(page.getRowDisplayed()==0)
		{
			rowDisplayed =Integer.MAX_VALUE;
		}else{
			rowDisplayed =page.getRowDisplayed();
		}
		ScoreDoc lastSd = getLastScoreDoc(page.getPageNum(),rowDisplayed, query, indexSearcher,sort);
		TopDocs tds = null;
		 if(lastSd==null)
		 {
			 tds = indexSearcher.search(query, rowDisplayed,sort);//获取最高得分命中
		 }else{
			 tds = indexSearcher.searchAfter(lastSd, query, rowDisplayed,sort);
		 }
		 page.setTotalNum(tds.totalHits);
		 setPageDate(page,tds,indexSearcher,parser);
		return page;
	}
	private  static Page getPageQuery(Page page,IndexSearcher indexSearcher)throws ParseException, IOException{
//		QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzerIK);
		QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzerType(page));
		parser.setLowercaseExpandedTerms(false);
		//构造查询语句
		String query_str="";
		 query_str=page.getQueryStr();
		page.setQueryStr(query_str);
		log.info("[getPageQuery-page.getQueryStr()]"+page.getQueryStr());
		Query query =parser.parse(query_str);
		//构造排序
		List<String[]> sortList=page.getSortList();
		Sort sort = new Sort();
		if(sortList.size()>0){
			SortField [] sortField = new SortField[page.getSortList().size()];
			for(int i=0;i<sortList.size();i++)
			{
				String [] strs_tmp = sortList.get(i);
				boolean issort=true;
				if("ASC".equals(strs_tmp[1]))
				{
					issort =false;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.STRING,issort);
				}if("DESC".equals(strs_tmp[1]))
				{
					issort =true;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.STRING,issort);
				}
				if("INT_DESC".equals(strs_tmp[1]))
				{
					issort =true;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.INT,issort);
				}
				if("INT_ASC".equals(strs_tmp[1]))
				{
					issort =false;
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.INT,issort);
				}
				if("SCORE".equals(strs_tmp[1]))
				{
					sortField[i]= new SortField(strs_tmp[0], SortField.Type.SCORE);
				}
			}
			sort = new Sort(sortField);
		}
		int rowDisplayed;
		if(page.getRowDisplayed()==0)
		{
			rowDisplayed =Integer.MAX_VALUE;
		}else{
			rowDisplayed =page.getRowDisplayed();
		}
		ScoreDoc lastSd = getLastScoreDoc(page.getPageNum(),rowDisplayed, query, indexSearcher,sort);
		TopDocs tds = null;
		 if(lastSd==null)
		 {
			 tds = indexSearcher.search(query, rowDisplayed,sort);//获取最高得分命中
		 }else{
			 tds = indexSearcher.searchAfter(lastSd, query, rowDisplayed,sort);
		 }
		 page.setTotalNum(tds.totalHits);
		 setPageDate(page,tds,indexSearcher,parser);
		return page;
	}
	/**
	 * 设置值到page
	 * @param page
	 * @param tds
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private static void setPageDate(Page page,TopDocs tds,IndexSearcher indexSearcher,QueryParser parser) throws IOException, ParseException{
 
		 String appcode;
		 appcode=page.getAppCode();
		 Map<String, SearchRuleDateDTO> intDataMap  =new  HashMap<String, SearchRuleDateDTO>();
		 if(appcode!=null)
		 {
			 intDataMap = getRuleData(appcode, page.getCommandCode(), "3");//int类型数据字段
		 }
		 List<IndexableField> fList = null;
		 List<Map<String,String>> rdate = new ArrayList<Map<String,String>>();
		 Map<String,String> dataMap = null;
		 SearchRuleDateDTO searchRuleDateDTOTmp;
		for(ScoreDoc sd: tds.scoreDocs){
			Document d = indexSearcher.doc(sd.doc);
			dataMap = new HashMap<String,String>();
			if(fList==null)
			{
				fList=d.getFields();
			}
			for(IndexableField indexableField:fList){
				searchRuleDateDTOTmp = intDataMap.get(indexableField.name());
				if(searchRuleDateDTOTmp!=null){
					//System.out.println("indexableField.name():"+indexableField.name()+"|value:"+d.get(indexableField.name()));
					dataMap.put(indexableField.name(), tranNum(d.get(indexableField.name())));
				}else{
					dataMap.put(indexableField.name(), d.get(indexableField.name()));
				}
				
			}
			rdate.add(dataMap);
		}
		page.setListData(rdate);
		setHighlighterWord(page,parser);
	}
 
	private  static void  setHighlighterWord(Page page, QueryParser parser) throws IOException, ParseException
	{
		 if(page.getHighLighterWord() ==null || "".equals(page.getHighLighterWord()))
		 {
			 return;
		 }
		 if(page.getHighlighterMap().size()==0)
		 {
			 return;
		 }
		 if(page.getListData().size()==0)
		 {
			 return;
		 }
		 //构造查询语句
		 Query query =parser.parse(page.getHighLighterWord());
		/*Begin:开始关键字高亮*/
         SimpleHTMLFormatter formatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
         Highlighter highlighter=new Highlighter(formatter, new QueryScorer(query));
         highlighter.setTextFragmenter(new SimpleFragmenter(500));
          
         for(Map<String,String> dmap:page.getListData()){
        	 for(String highword :page.getHighlighterMap().keySet())
        	 {
        		 if(dmap.get(highword)!=null){
        			 //执行高亮显示
//        			 TokenStream tokenstream=analyzerIK.tokenStream(null, new StringReader((String)dmap.get(highword)));
        			 TokenStream tokenstream=analyzerType(page).tokenStream(null, new StringReader((String)dmap.get(highword)));
        	         String value = dmap.get(highword);
        			 try {
    	        		 dmap.put(highword, highlighter.getBestFragment(tokenstream, (String)dmap.get(highword)));
        	          } catch (InvalidTokenOffsetsException e) {
        	        	  log.error("setHighlighterWord: is error:",e);
        	            e.printStackTrace();
        	          }
        	          if(dmap.get(highword) == null || dmap.get(highword).trim().equals("")) {
        	        	  dmap.put(highword, value);
        	          }
        		 }
        	  
        	 }
        	
         }
	}
 
	private static String tranNum(String sint)
	{
		if(sint==null){
			return "";
		}
		String rstr="";
		try{
 		 BigDecimal dd =new BigDecimal(sint);
 		 rstr =dd.toString();
		}catch(NumberFormatException e)
		{
			rstr =sint;
			//e.printStackTrace();
		}
		return rstr;
	} 
	/**
	 * 
	 * @param appcode 
	 * @param commandCode
	 * @param flag 1:全部,2String,3int,4智能提示
	 * @return
	 */
 
    public static Map<String,SearchRuleDateDTO> getRuleData(String appcode,String[] commandCode,String flag){
    	
    	 Map<String,String> ccommandMap = new HashMap<String,String>();
    	 for(String ccode:commandCode){
    		 ccommandMap.put(ccode, ccode);
    	 }
    	AppCacheDTO  appCacheDTO = AppContext.getAppCacheDTOMap().get(appcode); 
 
    	 Map<String, SearchRuleDateDTO> rmap = new HashMap<String, SearchRuleDateDTO>();
    	 if(appCacheDTO==null){return rmap;}
		 Map<String, SearchRuleDateDTO>  searchRuleDateMap  = appCacheDTO.getSearchRuleDateDTOMap();
		 
		 for(Entry<String, SearchRuleDateDTO> searchRuleDateDTO:searchRuleDateMap.entrySet()){
 
			 if(ccommandMap.get(searchRuleDateDTO.getValue().getCommandCode())!=null){
				 if("1".equals(flag)){
					 rmap.put(searchRuleDateDTO.getKey(), searchRuleDateDTO.getValue());
				 }else{
					 if("2".equals(flag)){
						 if(RedisKeyConst.Search.RULE_DATE_TYPE_STRING.equals(searchRuleDateDTO.getValue().getFileldDateType()))
						 {
							 rmap.put(searchRuleDateDTO.getKey(), searchRuleDateDTO.getValue());
						 }
					 }
					 if("3".equals(flag)){
						 if(RedisKeyConst.Search.RULE_DATE_TYPE_INT.equals(searchRuleDateDTO.getValue().getFileldDateType()))
						 {
							 rmap.put(searchRuleDateDTO.getKey(), searchRuleDateDTO.getValue());
						 }
					 }
					 if("4".equals(flag)){
						 if(RedisKeyConst.Search.IS_INTELLISENSE_TRUE.equals(searchRuleDateDTO.getValue().getIsIntelliSense()))
						 {
							 //System.out.println("===========================================>>>>"+searchRuleDateDTO.getValue().getAppCode()+"|"
						      //                  +searchRuleDateDTO.getValue().getCommandCode()+"|"+
						      //                  searchRuleDateDTO.getValue().getFieldName());
							 rmap.put(searchRuleDateDTO.getKey(), searchRuleDateDTO.getValue());
						 }
					 }
				 }
			 }
		 }
		 
		 return rmap;
    }
    
    public static void group(Page page,IndexSearcher indexSearcher) throws IOException, ParseException {
    	Map<String, List<Map<String, String>>> listGroupData = new HashMap<String, List<Map<String, String>>>();
    	GroupingSearch groupingSearch = new GroupingSearch(page.getGroup_groupField());
    	//排序
        Sort sort = makeSort(page);
        groupingSearch.setGroupSort(sort);//排序
        groupingSearch.setFillSortFields(true);
        groupingSearch.setCachingInMB(4.0, true);
        groupingSearch.setAllGroups(true);
        groupingSearch.setGroupDocsLimit(page.getGroup_zuSize());
        
//        QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzer);
        QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzerType(page));
        parser.setLowercaseExpandedTerms(false);
        Query query = parser.parse(page.getQueryStr());
        
        TopGroups<BytesRef> result = null;
		try {
			 log.info("==group begin==>>：" + page.getQueryStr());
			result = groupingSearch.search(indexSearcher, query,
					page.getStartLineNum(), page.getEndLineNum());
			log.info("==group end==>>：" + page.getQueryStr());
		} catch (Exception e) {
			log.info("== group Exception=="+e.getLocalizedMessage());
			e.printStackTrace();
			log.error(e);
		}
        log.info("分组搜索命中数：" + result.totalHitCount);
        log.info("搜索结果分组数：" + result.groups.length);
        
        for(GroupDocs<BytesRef> groupDocs : result.groups){
        	listGroupData.put(groupDocs.groupValue.utf8ToString(), makeResultList(page, indexSearcher, groupDocs.scoreDocs));
        }
        page.setListGroupData(listGroupData);
    }
    
    private static Sort makeSort(Page page) {
        //构造排序
        List<String[]> sortList = page.getSortList();
        Sort sort = new Sort();
        if (sortList.size() > 0) {
            SortField[] sortField = new SortField[page.getSortList().size()];
            for (int i = 0; i < sortList.size(); i++) {
                String[] strs_tmp = sortList.get(i);
                boolean issort = true;
                if ("ASC".equals(strs_tmp[1])) {
                    issort = false;
                    sortField[i] = new SortField(strs_tmp[0], SortField.Type.STRING, issort);
                }
                if ("DESC".equals(strs_tmp[1])) {
                    issort = true;
                    sortField[i] = new SortField(strs_tmp[0], SortField.Type.STRING, issort);
                }
                if ("INT_DESC".equals(strs_tmp[1])) {
                    issort = true;
                    sortField[i] = new SortField(strs_tmp[0], SortField.Type.INT, issort);
                }
                if ("INT_ASC".equals(strs_tmp[1])) {
                    issort = false;
                    sortField[i] = new SortField(strs_tmp[0], SortField.Type.INT, issort);
                }
                if ("SCORE".equals(strs_tmp[1])) {
                    sortField[i] = new SortField(strs_tmp[0], SortField.Type.SCORE);
                }
            }
            sort = new Sort(sortField);
        }
        return sort;
    }
    
    private static List<Map<String, String>> makeResultList(Page page, IndexSearcher indexSearcher, ScoreDoc[] scoreDocs) throws IOException {
    	 List<IndexableField> fList = null;
         List<Map<String, String>> rdate = new ArrayList<Map<String, String>>();
         Map<String, String> dataMap = null;
         for(ScoreDoc sd : scoreDocs){
        	 Document d = indexSearcher.doc(sd.doc);
        	 dataMap = new HashMap<String, String>();
        	 if (fList == null) {
                 fList = d.getFields();
             }
        	for(IndexableField indexableField : fList){
        		
        		 dataMap.put(indexableField.name(), d.get(indexableField.name()));
        	}
        	rdate.add(dataMap);
         }
    	return rdate;
    }
    
    private static Analyzer analyzerType(Page page){
    	Analyzer analyzer = new IKAnalyzer();
    	if(page!=null){
    		String appCode = page.getAppCode();
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
    	}
    	return analyzer;
    }
}
