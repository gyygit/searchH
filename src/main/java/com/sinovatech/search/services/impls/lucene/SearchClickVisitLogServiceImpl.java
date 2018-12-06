package com.sinovatech.search.services.impls.lucene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.GroupBean;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.ClickVisitLogInfoDAO;
import com.sinovatech.search.services.SearchClickVisitLogService;

/**
 *
 * @author Ma Tengfei
 * @date 2016年12月22日 下午4:56:54
 */
public class SearchClickVisitLogServiceImpl implements SearchClickVisitLogService {
    
    @Autowired
    private ClickVisitLogInfoDAO clickVisitLogInfoDAOL;
    
    public List<ClickVisitLogInfoDTO> list(LimitInfo limit, String sqlwhere) {
        Page page = new Page();
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sqlwhere);
        page.setPageNum(limit.getPageNum());
        page.addSortList("visitDate", Page.ORDER.DESC);
        List<ClickVisitLogInfoDTO> lst = clickVisitLogInfoDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SearchClickVisitLogService#getClickVisitLogList(com.sinovatech.search.ectable.limit.LimitInfo, com.sinovatech.search.entity.ClickVisitLogInfoclickDto)
     */
    @Override
    public List<ClickVisitLogInfoDTO> getClickVisitLogList(LimitInfo limit, ClickVisitLogInfoDTO clickDto) {
        StringBuffer sb = new StringBuffer("");
        sb.append(" (*:*) ");
        if (clickDto.getAppCode() != null && !"".equals(clickDto.getAppCode())) {
            sb.append(" AND (appCode:").append(clickDto.getAppCode()).append("*) ");
        }
        if (clickDto.getSearchKeyWord() != null && !"".equals(clickDto.getSearchKeyWord())) {
            sb.append(" AND (searchKeyWord:").append(clickDto.getSearchKeyWord()).append("*) ");
        }
        /*if (clickDto.getState() != null && !"".equals(clickDto.getState())) {
            sb.append(" AND (state:").append(clickDto.getState()).append("~0) ");
        }*/

        return this.list(limit, sb.toString());
    }
    
    
    public List<GroupBean> group(String groupField) throws Exception{
    	Page page = new Page();
    	page.setRowDisplayed(5);//设置页面显示多少组
    	page.setQueryStr(" (*:*) ");
    	page.setGroup_groupField(groupField);//设置分组字段
    	page.setGroup_zuSize(1000000);
    	clickVisitLogInfoDAOL.group(page);
    	System.out.println("命中结果为：["+page.getTotalNum()+"]本次返回["+page.getRowDisplayed()+"]");
    	page.setSortList(null);
    	List<GroupBean> listBean = new ArrayList<GroupBean>();
    	Map<String, List<Map<String, String>>> groupData = page.getListGroupData();
    	for(Map.Entry<String, List<Map<String, String>>> entry : groupData.entrySet()){
    		System.out.println(entry.getKey());
    		System.out.println(entry.getValue().size());
    		GroupBean bean = new GroupBean();
    		bean.setGroupKey(entry.getKey());
    		bean.setGroupNum(entry.getValue().size());
    		listBean.add(bean);
    	}
//    	String rstr = LuceneJsonUtil.getJSONString(page);
//    	System.out.println(rstr);
    	return listBean;
    }
    
    @Override
    public void deleteTX(String ids) throws AppException {
    	List<ClickVisitLogInfoDTO> list = listByIds(ids);
    	for(ClickVisitLogInfoDTO li :list){
    		clickVisitLogInfoDAOL.del(li);
    	}
    }
    
    public List<ClickVisitLogInfoDTO> listByIds(String ids) throws AppException {
    	 ids = "'" + ids.replaceAll(",", "','")+ "'";
    	return clickVisitLogInfoDAOL.listByIds(ids);
    }
    
}
