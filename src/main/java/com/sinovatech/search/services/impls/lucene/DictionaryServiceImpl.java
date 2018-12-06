package com.sinovatech.search.services.impls.lucene;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.DictionaryDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.DictionaryDAO;
import com.sinovatech.search.services.DictionaryService;

/**
 * Dictionary数据字典子表 Service实现类
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * @author  作者liuzhenquan
 */
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryDAO dictionaryDAOL;

    /**
     * 获取所有数据
     */
    public List<DictionaryDTO> getAllDatas() {
        return dictionaryDAOL.getAllForT();
    }

    /**
     * 前台sql 不需要  “ where 1=1 ”
     * @param sqlwhere  直接是条件
     * @param limit
     * @param sqlwhere
     * @return
     */
    public List<DictionaryDTO> list(LimitInfo limit, String sqlwhere) {
        Page page = new Page();
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sqlwhere);
        page.addSortList("updatetime", Page.ORDER.DESC);
        List<DictionaryDTO> lst = dictionaryDAOL.listForT(page);
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
    public List<DictionaryDTO> list(LimitInfo limit, String hql, String as) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(hql);
        List<DictionaryDTO> lst = dictionaryDAOL.listForT(page);
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
    public List<DictionaryDTO> listForSql(LimitInfo limit, String sql, String as) {
        Page page = new Page();
        page.setPageNum(limit.getPageNum());
        page.setRowDisplayed(limit.getRowDisplayed());
        page.setQueryStr(sql);
        List<DictionaryDTO> lst = dictionaryDAOL.listForT(page);
        limit.setTotalNum(page.getTotalNum());
        return lst;
    }

    @Override
    public void saveTX(DictionaryDTO dictionaryDTO) throws AppException {
        dictionaryDAOL.add(dictionaryDTO);
    }

    @Override
    public void updateTX(DictionaryDTO dictionaryDTO) throws AppException {
        dictionaryDAOL.update(dictionaryDTO);
    }

    @Override
    public void saveOrUpdateTX(DictionaryDTO dictionaryDTO) throws AppException {
        dictionaryDAOL.update(dictionaryDTO);
    }

    @Override
    public void deleteTX(String ids) throws AppException {

        List<DictionaryDTO> list = listByIds(ids);
        for (DictionaryDTO dto : list) {
            dictionaryDAOL.del(dto);
        }
    }

    @Override
    public DictionaryDTO get(String id) throws AppException {
        return dictionaryDAOL.getById(id);
    }

    @Override
    public List<DictionaryDTO> listByIds(String ids) throws AppException {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        return dictionaryDAOL.listByIds(ids);
    }

    @Override
    public List<DictionaryDTO> list(LimitInfo limit, DictionaryDTO dto) {

        StringBuffer sb = new StringBuffer();
        sb.append(" (*:*) ");
        if (!"".equals(dto.getName()) && (null != dto.getName())) {
            sb.append(" AND (name:").append(dto.getName()).append("*) ");
        }

        if (!"".equals(dto.getCode()) && (null != dto.getCode())) {
            sb.append(" AND (code:").append(dto.getCode()).append("~0) ");
        }
        if (!"".equals(dto.getStatus()) && (null != dto.getStatus())) {
            sb.append(" AND (status:").append(dto.getStatus()).append("~0) ");
        }
        sb.append(" AND (indexId:").append(dto.getIndexId()).append("~0) ");
        return this.list(limit, sb.toString());
    }

    @Override
    public String save(DictionaryDTO dictionaryDTO) throws Exception {
        String flag = "OK";
        flag = this.validDate(dictionaryDTO, flag);
        if (!flag.equals("OK"))
            return flag;
        dictionaryDTO.setUpdatetime(new Date());
        this.saveTX(dictionaryDTO);
        return flag;
    }

    private String validDate(DictionaryDTO dto, String flag) {
        if (dto.getName() != null && dto.getName().equals("")) {
            flag = "名称不能为空!";
        }
        if (dto.getCode() != null && dto.getCode().equals("")) {
            flag = "code不能为空!";
        }
        if (dto.getValue() != null && dto.getValue().equals("")) {
            dto.setValue(" ");
        }

        String hql = "";
        if (dto.getId() != null && !dto.getId().equals(""))
            hql += " NOT (id:" + dto.getId() + "~0) ";
        LimitInfo limit = new LimitInfo();

        List<DictionaryDTO> list1 = this.list(limit, " (*:*) AND (code:'" + dto.getCode() + "~0) AND (indexId:" + dto.getIndexId() + "~0) "
                + hql);
        if (list1 != null && list1.size() > 0) {
            return flag = "子表code不能重复，请重新录入!";
        }
        List<DictionaryDTO> list2 = this.list(limit, " (*:*) AND (name:'" + dto.getName() + "*) " + hql);
        if (list2 != null && list2.size() > 0) {
            return flag = "名称不能重复，请重新录入!";
        }
        return flag;
    }

    @Override
    public String update(DictionaryDTO dictionaryDTO) throws Exception {
        // 根据id获得dictionary，填充数据
        DictionaryDTO m = this.get(dictionaryDTO.getId());
        dictionaryDTO.setUpdatetime(new Date());
        dictionaryDTO.setId(m.getId());
        dictionaryDTO.setIndexId(m.getIndexId());
        dictionaryDTO.setIndexcode(m.getIndexcode());
        // dictionaryDTO.setStatus(m.getStatus());
        String flag = "OK";
        flag = this.validDate(dictionaryDTO, flag);
        if (!flag.equals("OK"))
            return flag;
        this.updateTX(dictionaryDTO);
        return flag;
    }
}
