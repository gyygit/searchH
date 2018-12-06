package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SynonymWordDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SynonymWordDAO;
import com.sinovatech.search.services.SynonymWordService;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月7日 上午10:08:41
 */
public class SynonymWordServiceImpl implements SynonymWordService {

    @Autowired
    private SynonymWordDAO synonymWordDAOL;

    public void saveTX(SynonymWordDTO synonymWordDTO) throws AppException {
        synonymWordDAOL.add(synonymWordDTO);
    }

    public List<SynonymWordDTO> list(LimitInfo limit, String sqlwhere) {
        Page page = new Page();
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sqlwhere);
        page.setPageNum(limit.getPageNum());
        page.addSortList("createTime", Page.ORDER.DESC);
        List<SynonymWordDTO> lst = synonymWordDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SynonymWordService#save(com.sinovatech.search.entity.SynonymWordDTO)
     */
    @Override
    public String save(SynonymWordDTO synonymWordDTO) throws AppException {
        String flag = this.validate(synonymWordDTO);
        if (!"OK".equals(flag))
            return flag;
        synonymWordDTO.setCreateTime(new Date());
        synonymWordDTO.setCurrentDir(RedisKeyConst.Search.A_DIR);
        //操作人，待修改
//        synonymWordDTO.setOperator("1");
//        synonymWordDTO.setState(RedisKeyConst.Search.USERING);//默认启用
        synonymWordDTO.setIsDelete(RedisKeyConst.Search.IS_DELETE_SHOW);
        this.saveTX(synonymWordDTO);
        return flag;
    }

    /**
     *
     * @author Ma
     * @date 2016年12月7日 上午10:41:52
     * @param synonymWordDTO
     * @return
     */
    private String validate(SynonymWordDTO synonymWordDTO) {
        String flag = "OK";
        if ("".equals(synonymWordDTO.getAppCode())) {
            return flag = "应用编码不能为空!";
        } else if ("".equals(synonymWordDTO.getKeyWord())) {
            return flag = "同义词关键词不能为空!";
        } else if ("".equals(synonymWordDTO.getSynonymArray())) {
            return flag = "同义词词组不能为空!";
        }
        String hql = " AND (isDelete:" + RedisKeyConst.Search.IS_DELETE_SHOW + ")";
        if (synonymWordDTO.getId() != null && !"".equals(synonymWordDTO.getId())){
            hql += " NOT (id:" + synonymWordDTO.getId() + ")";
        }
        hql += " AND (appCode:" + synonymWordDTO.getAppCode() + ")";
        
        hql += " AND (commandCode:" + synonymWordDTO.getCommandCode() + ")";    
        //业务应用名称 数据类型下唯一
        List<SynonymWordDTO> synonymList = this.getSynonymWordDTO("  (keyWord:" + synonymWordDTO.getKeyWord() + "~0)" + hql);

        if (synonymList != null && synonymList.size() > 0) {
            return flag = "该业务应用-数据分类下已存在 '"+synonymWordDTO.getKeyWord()+"' 该关键词,请重新录入！";
        }
        return flag;
    }

    public List<SynonymWordDTO> getSynonymWordDTO(String hql) {
        System.out.println("hql --> " + hql);
        Page page = new Page();
        if (hql != null && !"".equals(hql)) {
            page.setQueryStr(hql);
        } else {
            page.setQueryStr("*:* AND " + "isDelete:" + RedisKeyConst.Search.IS_DELETE_SHOW);
        }
        page.setRowDisplayed(Integer.MAX_VALUE);
        List<SynonymWordDTO> lst = synonymWordDAOL.listForT(page);
        return lst;
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SynonymWordService#list(com.sinovatech.search.ectable.limit.LimitInfo, com.sinovatech.search.entity.SynonymWordDTO)
     */
    @Override
    public List<SynonymWordDTO> list(LimitInfo limit, SynonymWordDTO dto) {
        StringBuffer sb = new StringBuffer("");
        sb.append(" (*:*) ");
        if (!"".equals(dto.getAppCode())  && (null != dto.getAppCode())) {
            sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
        }
        if (dto.getKeyWord() != null && !"".equals(dto.getKeyWord())) {
            sb.append(" AND (keyWord:").append(dto.getKeyWord()).append("*) ");
        }
        if (dto.getState() != null && !"".equals(dto.getState())) {
            sb.append(" AND (state:").append(dto.getState()).append("~0) ");
        }
        return this.list(limit, sb.toString());
    }
    
    
    @Override
    public List<SynonymWordDTO> list(LimitInfo limit, SynonymWordDTO dto,String usercode,String appcode) {
    	StringBuffer sb = new StringBuffer("");
    	sb.append(" (*:*) ");
    	if (!"".equals(dto.getAppCode())  && (null != dto.getAppCode())) {
    		sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
    	}
    	if (dto.getKeyWord() != null && !"".equals(dto.getKeyWord())) {
    		sb.append(" AND (keyWord:").append(dto.getKeyWord()).append("*) ");
    	}
    	if (dto.getState() != null && !"".equals(dto.getState())) {
    		sb.append(" AND (state:").append(dto.getState()).append("~0) ");
    	}
    	if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
    		if(!"admin".equals(usercode)){
    			if(appcode.indexOf(",")!=-1){
    				String[] app = appcode.split(",");
    				sb.append(" AND (");
    				for(int i=0;i<app.length;i++){
    					sb.append("(appCode:").append(app[i]).append("*) OR ");
    				}
    				String cssb = sb.substring(0,sb.length()-3)+")";
    				sb = new StringBuffer();
    				sb.append(cssb);
    			}else{
    				sb.append(" AND (appCode:").append(appcode).append("*) ");
    			}
    		}
    	}
    	return this.list(limit, sb.toString());
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SynonymWordService#get(java.lang.String)
     */
    @Override
    public SynonymWordDTO get(String id) {
        return synonymWordDAOL.getById(id);
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SynonymWordService#update(com.sinovatech.search.entity.SynonymWordDTO)
     */
    @Override
    public String update(SynonymWordDTO synonymWordDTO) {
        SynonymWordDTO m = this.get(synonymWordDTO.getId());
        synonymWordDTO.setCreateTime(m.getCreateTime());
//        synonymWordDTO.setOperator(m.getOperator());
        synonymWordDTO.setCurrentDir(m.getCurrentDir());
//        synonymWordDTO.setState(m.getState());
        synonymWordDTO.setIsDelete(m.getIsDelete());
        synonymWordDTO.setUpdateTime(new Date());
        if (m.getIndexType() == null || "".equals(m.getIndexType()))
            synonymWordDTO.setIndexType(m.getIndexType());
        if (m.getLoopCindexTime() == null && "".equals(m.getLoopCindexTime()))
            synonymWordDTO.setLoopCindexTime(m.getLoopCindexTime());
        if (m.getIndexPath() == null || "".equals(m.getIndexPath()))
            synonymWordDTO.setIndexPath(m.getIndexPath());

        String flag = this.validate(synonymWordDTO);
        if (flag == "OK")
            this.updateTX(synonymWordDTO);
        return flag;
    }

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 下午2:53:37
     * @param synonymWordDTO
     */
    private void updateTX(SynonymWordDTO synonymWordDTO) {
        synchronized (RedisKeyConst.synObject) {
            synonymWordDAOL.update(synonymWordDTO);
        }
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.SynonymWordService#deleteTX(java.lang.String)
     */
    @Override
    public void deleteTX(String ids) {
        List<SynonymWordDTO> list = listByIds(ids);
        for (SynonymWordDTO dto : list) {
            //删除前，先把App设置成停止状态
            //            if (dto.getState() == RedisKeyConst.Search.USERING) {
            //                LuceneManager.optIndexWrite(dto.getAppCode(), RedisKeyConst.Search.SEARCH_SYS_STOP);
            //            }
            synonymWordDAOL.del(dto);
        }
    }

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 下午3:40:58
     * @param ids
     * @return
     */
    private List<SynonymWordDTO> listByIds(String ids) {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        return synonymWordDAOL.listByIds(ids);
    }

}
