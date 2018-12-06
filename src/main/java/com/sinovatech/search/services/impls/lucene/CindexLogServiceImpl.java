package com.sinovatech.search.services.impls.lucene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.entity.GroupBean;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.CreateIndexProcessLogDAO;
import com.sinovatech.search.services.CindexLogService;

/**
 *
 * @author Ma Tengfei
 * @date 2016年12月22日 下午4:56:54
 */
public class CindexLogServiceImpl implements CindexLogService {
    
    @Autowired
    private CreateIndexProcessLogDAO createIndexProcessLogDAOL;
    
    public List<CreateIndexProcessLogDTO> list(LimitInfo limit, String sqlwhere) {
        Page page = new Page();
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sqlwhere); 
        page.setPageNum(limit.getPageNum());
        page.addSortList("cdate", Page.ORDER.DESC);
        List<CreateIndexProcessLogDTO> lst = createIndexProcessLogDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SearchClickVisitLogService#getClickVisitLogList(com.sinovatech.search.ectable.limit.LimitInfo, com.sinovatech.search.entity.ClickVisitLogInfoclickDto)
     */
    @Override
    public List<CreateIndexProcessLogDTO> getCreateIndexProcessLogList(LimitInfo limit, CreateIndexProcessLogDTO clickDto) {
        StringBuffer sb = new StringBuffer("");
        String begintime="";
        String endtime="";
        sb.append(" (*:*) ");
         if (clickDto.getAppcode() != null && !"".equals(clickDto.getAppcode())) {
            sb.append(" AND (appcode:").append(clickDto.getAppcode()).append("*) ");
         }
         if (clickDto.getCommandcode() != null && !"".equals(clickDto.getCommandcode())) {
             sb.append(" AND (commandcode:").append(clickDto.getCommandcode()).append("*) ");
        }
        if (clickDto.getStartTime() != null && !"".equals(clickDto.getStartTime())) {
        	begintime=clickDto.getStartTime();
        }else{
        	begintime="2000-01-01";
        }  
        if (clickDto.getEndTime() != null && !"".equals(clickDto.getEndTime())) {
        	endtime=clickDto.getEndTime();
         }else{
        	endtime="2118-01-01";
        }  
        sb.append(" AND (cdate:[").append(begintime).append(" TO ").append(endtime).append("])");
        return this.list(limit, sb.toString());
    }
     
    @Override
    public void deleteTX(String ids) throws AppException {
    	List<CreateIndexProcessLogDTO> list = listByIds(ids);
    	for(CreateIndexProcessLogDTO li :list){
    		createIndexProcessLogDAOL.del(li);
    	}
    }
    
    public List<CreateIndexProcessLogDTO> listByIds(String ids) throws AppException {
    	 ids = "'" + ids.replaceAll(",", "','")+ "'";
    	return createIndexProcessLogDAOL.listByIds(ids);
    }
    
}
