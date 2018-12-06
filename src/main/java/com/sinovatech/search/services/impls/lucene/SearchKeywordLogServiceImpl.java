package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchKeywordLogDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.SearchKeywordLogDAO;
import com.sinovatech.search.services.SearchKeywordLogService;
import com.sinovatech.search.utils.PinyinUtil;
 
/**
 * SearchKeywordLog搜索日志表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:08<br />
 * @author  作者liuzhenquan
 */
public class SearchKeywordLogServiceImpl implements SearchKeywordLogService {
    @Autowired
    private SearchKeywordLogDAO searchKeywordLogDAOL;

    /**
     * 获取所有数据
     */
    public List<SearchKeywordLogDTO> getAllDatas() {
        return searchKeywordLogDAOL.getAllForT();
    }

    /**
     * 前台sql 不需要  “ where 1=1 ”
     * @param sqlwhere  直接是条件
     * @param limit
     * @param sqlwhere
     * @return
     */
    public List<SearchKeywordLogDTO> list(LimitInfo limit, String sqlwhere) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sqlwhere);
        page.addSortList("createTime", Page.ORDER.DESC);
        List<SearchKeywordLogDTO> lst = searchKeywordLogDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    /**
     * 前台sql必须加  “ where 1=1 ” 
     * @param limit
     * @param hql
     * @param as 主表别名 可以不传
     * @return
     */
    public List<SearchKeywordLogDTO> list(LimitInfo limit, String hql, String as) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(hql);
        List<SearchKeywordLogDTO> lst = searchKeywordLogDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    /**
     * 前台sql必须加  “ where 1=1 ” 
     * @param limit
     * @param hql
     * @param as 主表别名 可以不传
     * @return
     */
    public List<SearchKeywordLogDTO> listForSql(LimitInfo limit, String sql, String as) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sql);
        List<SearchKeywordLogDTO> lst = searchKeywordLogDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    @Override
    public void saveTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
        if(!StringUtils.isEmpty(searchKeywordLogDTO.getSearchKeyword())){
            searchKeywordLogDTO.setSearchPinyin(PinyinUtil.cn2Spell(searchKeywordLogDTO.getSearchKeyword()));
            searchKeywordLogDTO.setSearchPy(PinyinUtil.cn2Spell(searchKeywordLogDTO.getSearchKeyword()));
        }
        searchKeywordLogDTO.setCreateTime(new Date());
        searchKeywordLogDAOL.add(searchKeywordLogDTO);
    }

    @Override
    public void updateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
        searchKeywordLogDAOL.update(searchKeywordLogDTO);
    }

    @Override
    public void saveOrUpdateTX(SearchKeywordLogDTO searchKeywordLogDTO) throws AppException {
        searchKeywordLogDAOL.update(searchKeywordLogDTO);
    }

    @Override
    public void deleteTX(String ids) throws AppException {
        List<SearchKeywordLogDTO> list = listByIds(ids);
        for (SearchKeywordLogDTO dto : list) {
            searchKeywordLogDAOL.del(dto);
        }
    }

    @Override
    public SearchKeywordLogDTO get(String id) throws AppException {
        return searchKeywordLogDAOL.getById(id);
    }

    @Override
    public List<SearchKeywordLogDTO> listByIds(String ids) throws AppException {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        return searchKeywordLogDAOL.listByIds(ids);
    }

    @Override
    public List<SearchKeywordLogDTO> list(LimitInfo limit, SearchKeywordLogDTO dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(" (*:*) ");
        if (!dto.getAppCode().equals("")) {
            sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
        }
        if (!dto.getCommandCode().equals("")) {
            sb.append(" AND (commandCode:").append(dto.getCommandCode()).append("*) ");
        }
        if (dto.getSearchPinyin() != null && !dto.getSearchPinyin().equals("")) {
            sb.append(" AND (searchPinyin:").append(dto.getSearchPinyin()).append("*) ");
        }
        if (dto.getSearchPy() != null && !dto.getSearchPy().equals("")) {
            sb.append(" AND (searchPy:").append(dto.getSearchPy()).append("*) ");
        }
        return this.list(limit, sb.toString());

    }

}