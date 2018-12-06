package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.DictionaryindexDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.DictionaryindexDAO;
import com.sinovatech.search.services.DictionaryindexService;

/**
 * Dictionaryindex数据字典主表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:00<br />
 * @author  作者liuzhenquan
 */
public class DictionaryindexServiceImpl implements DictionaryindexService {
    @Autowired
    private DictionaryindexDAO dictionaryindexDAOL;

    /**
     * 获取所有数据
     */
    public List<DictionaryindexDTO> getAllDatas() {
        return dictionaryindexDAOL.getAllForT();
    }

    /**
     * 前台sql 不需要  “ where 1=1 ”
     * @param sqlwhere  直接是条件
     * @param limit
     * @param sqlwhere
     * @return
     */
    public List<DictionaryindexDTO> list(LimitInfo limit, String sqlwhere) {

        Page page = new Page();
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setPageNum(limit.getPageNum());
        page.setQueryStr(sqlwhere);
        page.addSortList("updatetime", Page.ORDER.DESC);
        List<DictionaryindexDTO> lst = dictionaryindexDAOL.listForT(page);
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
    public List<DictionaryindexDTO> list(LimitInfo limit, String hql, String as) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(hql);
        List<DictionaryindexDTO> lst = dictionaryindexDAOL.listForT(page);
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
    public List<DictionaryindexDTO> listForSql(LimitInfo limit, String sql, String as) {

        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sql);
        List<DictionaryindexDTO> lst = dictionaryindexDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    @Override
    public void saveTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
        dictionaryindexDAOL.add(dictionaryindexDTO);
    }

    @Override
    public void updateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
        dictionaryindexDAOL.update(dictionaryindexDTO);
    }

    @Override
    public void saveOrUpdateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
        dictionaryindexDAOL.update(dictionaryindexDTO);
    }

    @Override
    public void deleteTX(String ids) throws AppException {

        List<DictionaryindexDTO> list = listByIds(ids);
        for (DictionaryindexDTO dto : list) {
            dictionaryindexDAOL.del(dto);
        }
    }

    @Override
    public DictionaryindexDTO get(String id) throws AppException {
        return dictionaryindexDAOL.getById(id);
    }

    @Override
    public List<DictionaryindexDTO> listByIds(String ids) throws AppException {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        return dictionaryindexDAOL.listByIds(ids);
    }

    @Override
    public List<DictionaryindexDTO> list(LimitInfo limit, DictionaryindexDTO dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(" (*:*) ");
        if (!"".equals(dto.getIndexname()) && (null != dto.getIndexname())) {
            sb.append(" AND (indexname:").append(dto.getIndexname()).append("*) ");
        }
        if (!"".equals(dto.getIndexcode()) && (null != dto.getIndexcode())) {
            sb.append(" AND (indexcode:").append(dto.getIndexcode()).append("*) ");
        }
        if (!"".equals(dto.getStatus()) && (null != dto.getStatus())) {
            sb.append(" AND (status:").append(dto.getStatus()).append("~0) ");
        }
        return this.list(limit, sb.toString());
    }

    @Override
    public String save(DictionaryindexDTO dictionaryindexDTO) throws Exception {
        String flag = "OK";
        flag = this.validDate(dictionaryindexDTO, flag);
        if (flag != "OK")
            return flag;
        dictionaryindexDTO.setUpdatetime(new Date());
        this.saveTX(dictionaryindexDTO);
        return flag;
    }

    private String validDate(DictionaryindexDTO dto, String flag) {
        if ("".equals(dto.getIndexcode())) {
            return flag = "数据字典主表code不能为空!";
        }

        else if ("".equals(dto.getIndexname())) {
            return flag = "数据字典主表名称不能为空!";
        }

        String hql = "";
        if (dto.getId() != null && !dto.getId().equals(""))
            hql += " NOT (id:" + dto.getId() + ") ";
        LimitInfo limit = new LimitInfo();

        List<DictionaryindexDTO> list1 = this.list(limit, " (*:*) AND  (indexcode:'" + dto.getIndexcode() + "~0) " + hql);
        if (list1 != null && list1.size() > 0) {
            return flag = "数据字典主表code不能重复，请重新录入!";
        }
        List<DictionaryindexDTO> list2 = this.list(limit, " (*:*) AND (indexname:" + dto.getIndexname() + "~0) " + hql);
        if (list2 != null && list2.size() > 0) {
            return flag = "数据字典主表名称不能重复，请重新录入!";
        }
        return flag;
    }

    @Override
    public String update(DictionaryindexDTO dictionaryindexDTO) throws Exception {
        //根据id获得dictionaryindex，填充数据
//        DictionaryindexDTO m = this.get(dictionaryindexDTO.getId());
        String flag = "OK";
        flag = this.validDate(dictionaryindexDTO, flag);
        if (flag.equals("OK"))
            return flag;
        dictionaryindexDTO.setUpdatetime(new Date());
        this.updateTX(dictionaryindexDTO);
        return flag;
    }
}
