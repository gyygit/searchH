package com.sinovatech.search.services.impls.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.lucene.search.IndexSearcher;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.LuceneSearchDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.Condition;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.luceneindex.db.LuceneQueryUtil;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.services.LuceneSearchService;
import com.sinovatech.search.services.SearchRuleDateService;

public class LuceneSearchServiceImpl implements LuceneSearchService {
	private static final Log log = org.apache.commons.logging.LogFactory
	.getLog(LuceneSearchServiceImpl.class);
	
	@Autowired
	private SearchRuleDateService searchRuleDateService;
	
	@Override
	public LuceneSearchDTO search(LuceneSearchDTO dto, LimitInfo limit) {
		String max = "9999999999";
		String min = "0";
		Condition con = new Condition();
		List<Condition> s = new ArrayList<Condition>();
		dto.getLogic().add(dto.getLogic().size(), "1");
		if(dto.getLogic().size() == 1){
			dto.getLogic().add(0, "1");
		}
		List<Condition> orWhere= new ArrayList<Condition>();
		Map<Integer, List<Condition>> map = new HashMap<Integer, List<Condition>>();
		String queryStr = "";
		if(dto.getQuerys() != null && dto.getQuerys().size() > 0){
			for (int i = 0; i < dto.getFileds().size(); i++) {
				if(!dto.getQuerys().get(i).equals("") && !dto.getFileds().get(i).equals("")) {
					// 拼接条件Conditions:1.等于2.like 3.大于 4.小于   5alike
					if(dto.getConditions().get(i).equals("1") || dto.getConditions().get(i).equals("2")|| dto.getConditions().get(i).equals("5")){
						String filed = dto.getFileds().get(i);
						String ruleDateSql = "(appCode:"+dto.getAppCode()+ "~0) AND (commandCode:" + dto.getCommandCode() + "~0) AND (fieldName:" + filed + ")";
						SearchRuleDateDTO searchRuleDate = searchRuleDateService.searchRuleDateBySql(ruleDateSql).get(0);
						if(dto.getLogic().get(i).equals("2")) { // 如果是or的关系
							
							if(dto.getConditions().get(i).equals("1"))
							{
								orWhere.add(new Condition(dto.getFileds().get(i),dto.getQuerys().get(i), Condition.Opt.EQ , Condition.Logic.OR));
							}
							if(dto.getConditions().get(i).equals("2"))
							{
								orWhere.add(new Condition(dto.getFileds().get(i),dto.getQuerys().get(i), Condition.Opt.LIKE , Condition.Logic.OR));
							}
							if(dto.getConditions().get(i).equals("5"))
							{
								orWhere.add(new Condition(dto.getFileds().get(i),dto.getQuerys().get(i), Condition.Opt.LIKE_ANALYZER , Condition.Logic.OR));
							}
							//orWhere.add(new Condition(dto.getFileds().get(i),dto.getQuerys().get(i), dto.getConditions().get(i).equals("1") ? Condition.Opt.EQ : Condition.Opt.LIKE
							//		, Condition.Logic.OR));
						}
						else {
							if(orWhere.size() >= 1) {
								List<Condition> conditionList = new ArrayList<Condition>();
								for(Condition con1 : orWhere) {
									conditionList.add(con1);
								}
								con.addCondition(conditionList,Condition.Logic.OR);
							}
							orWhere.clear();
							
							if(dto.getConditions().get(i).equals("1"))
							{
								con.addCondition(dto.getFileds().get(i), dto.getQuerys().get(i),
										Condition.Opt.EQ, 
											dto.getLogic().get(i).equals("1") ? Condition.Logic.AND : Condition.Logic.OR, 
													searchRuleDate.getFieldIndexType() == RedisKeyConst.Search.RULE_DATE_TYPE_INT ? Condition.StringOrInt.INT : Condition.StringOrInt.STRING);
				
							}
							if(dto.getConditions().get(i).equals("2"))
							{
								con.addCondition(dto.getFileds().get(i), dto.getQuerys().get(i),
										Condition.Opt.LIKE, 
											dto.getLogic().get(i).equals("1") ? Condition.Logic.AND : Condition.Logic.OR, 
													searchRuleDate.getFieldIndexType() == RedisKeyConst.Search.RULE_DATE_TYPE_INT ? Condition.StringOrInt.INT : Condition.StringOrInt.STRING);
		
							}
							if(dto.getConditions().get(i).equals("5"))
							{
								con.addCondition(dto.getFileds().get(i), dto.getQuerys().get(i),
										Condition.Opt.LIKE_ANALYZER, 
											dto.getLogic().get(i).equals("1") ? Condition.Logic.AND : Condition.Logic.OR, 
													searchRuleDate.getFieldIndexType() == RedisKeyConst.Search.RULE_DATE_TYPE_INT ? Condition.StringOrInt.INT : Condition.StringOrInt.STRING);
							}
						}
					}else if(dto.getConditions().get(i).equals("3")){
						con.addCondition(dto.getFileds().get(i), dto.getQuerys().get(i) + Condition.BETWEEN_SPLIT_STR + max,
								Condition.Opt.BETWEEN_CLOSE, dto.getLogic().get(i+1).equals("1") ? Condition.Logic.AND : Condition.Logic.OR, 
										Condition.StringOrInt.INT);
					}else if(dto.getConditions().get(i).equals("4")){
						con.addCondition(dto.getFileds().get(i), min + Condition.BETWEEN_SPLIT_STR + dto.getQuerys().get(i),
								Condition.Opt.BETWEEN_CLOSE, dto.getLogic().get(i+1).equals("1") ? Condition.Logic.AND : Condition.Logic.OR, 
										Condition.StringOrInt.INT);
					}
				}
			}
		}
		if(orWhere.size() > 0 ) {
			con.addCondition(orWhere,Condition.Logic.AND);
		}
		con.addCondition("commandCode", dto.getCommandCode(), Condition.Opt.EQ, Condition.Logic.AND);
		//log.info("LuceneSearchServiceImpl-query: " + queryStr);
		IndexSearcher indexSearch = LuceneManager.autoGetIndexSearcher(dto.getAppCode(), false);
		Page page = new Page();
		queryStr = con.builder();
		String finallyStr = "";
		if(!queryStr.equals("")) {
//			//按照 空格 分组
//			String [] splitStr = queryStr.split(" ");
//			// 循环分组
//			for(int x = 0; x < splitStr.length; x++) {
//				String sp = splitStr[x];
//				if(sp.indexOf("(") >=0) {
//					//截取字段名称
//					String fileName = sp.substring(sp.indexOf("(") + 1, sp.indexOf(":"));
//					fileName = fileName.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");
//					SearchRuleDateDTO ruleDTO = AppContext.getAppCacheDTOMap().get(dto.getAppCode()).getSearchRuleDateDTOMap().get(fileName+"_"+dto.getCommandCode());
//					if(ruleDTO != null) {
//						//如果是分次索引
//						if(ruleDTO.getFieldIndexType().equals(RedisKeyConst.Search.RULE_INDEX_TYPE_TOKENIZED)){
//							finallyStr += sp.replaceAll("~0", "").replaceAll("\\*", ""); 
//						}else {
//							finallyStr += sp;
//						}
//					}
//				}else {
//					finallyStr += sp;
//				}
//			}
		} else {
			queryStr = "(*:*)";
		}
		finallyStr =queryStr;
		//finallyStr += " (commandCode:"+ dto.getCommandCode() +"~0) ";
		page.setQueryStr(finallyStr);
		page.setCommandCode(new String[]{dto.getCommandCode()});
 
		page.setAppCode(dto.getAppCode());
		page.setPageNum(limit.getPageNum());
		page.setRowDisplayed(10);
		page = LuceneQueryUtil.queryIk(page, indexSearch);
//		limit.setPageNum(page.getPageNum());
		limit.setTotalNum(page.getTotalNum());
		limit.setRowDisplayed(10);
		//把数据封装至对象
		List<Map<String, String>> list = page.getListData();
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				Map<String, String> m = (Map<String, String>) list.get(i);
				if( i != 0 )
					dto.getValues().add("table_tr");
				for(int j = 0; j < dto.getNames().size(); j++){
					dto.getValues().add(m.get(dto.getNames().get(j)));
				}
			}
		}
		return dto;
	}
	
	
	@Override
	public List searchHot(String appcode,String commandcode, LimitInfo limit) {
		
		IndexSearcher indexSearch = LuceneManager.autoGetIndexSearcher(appcode, true);
		Condition con = new Condition();
		con.addCondition("commandCode", commandcode, Condition.Opt.EQ, Condition.Logic.AND);
		//log.info("LuceneSearchServiceImpl-query: " + queryStr);
		//SortField sortField= new SortField(RedisKeyConst.Search.HOT_FILED_ONCE, SortField.Type.INT,true);
		//Sort sort = new Sort(sortField);
		
		IndexSearcher indexSearch1 = LuceneManager.autoGetIndexSearcher(appcode, false);
		Page page = new Page();
		page.addSortList(RedisKeyConst.Search.HOT_FILED_ONCE, Page.ORDER.INT_DESC);
		String queryStr = con.builder();
		page.setQueryStr(queryStr);
		page.setCommandCode(new String[]{commandcode});
 
		page.setAppCode(appcode);
		page.setPageNum(limit.getPageNum());
		page.setRowDisplayed(10);
		page = LuceneQueryUtil.queryIk(page, indexSearch);
		limit.setTotalNum(page.getTotalNum());
		limit.setRowDisplayed(10);
		//把数据封装至对象
		List list = page.getListData();
		return list;
	}
	public static void main(String[] args) {
		String x = "((x:a~0) and (name:b~0)) and (name:c*)  and (name:y~0)  or   (ww:d*) ";
		System.out.println(x.indexOf("("));
		System.out.println(x.substring(x.indexOf("(")+1, x.indexOf(":")));
		String[] x1 = x.split(" ");
		for(int i=0;i<x1.length;i++){
			System.out.println(x1[i]);
		}
	}
}
