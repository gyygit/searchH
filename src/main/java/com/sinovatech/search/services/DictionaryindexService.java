package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.DictionaryindexDTO;

/**
 * Dictionaryindex数据字典主表 Service接口
 * 
 * 创建: 2014-11-14 13:25:00<br />
 * @author  作者liuzhenquan
 */
public interface DictionaryindexService {

    /**
     * 获取所有数据
     */
    public List<DictionaryindexDTO> getAllDatas();

    /**
     * 前台sql 不需要  “ where 1=1 ”
     * @param sqlwhere  直接是条件
     * @param limit
     * @param sqlwhere
     * @return
     */
    public List<DictionaryindexDTO> list(LimitInfo limit, String sqlwhere);

    public List<DictionaryindexDTO> list(LimitInfo limit, DictionaryindexDTO dto);

    /**
     * 前台sql必须加  “ where 1=1 ” 
     * @param limit
     * @param hql
     * @param as 主表别名 可以不传
     * @return
     */
    public List<DictionaryindexDTO> list(LimitInfo limit, String hql, String as);

    /**
     * 前台sql必须加  “ where 1=1 ” 
     * @param limit
     * @param hql
     * @param as 主表别名 可以不传
     * @return
     */
    public List<DictionaryindexDTO> listForSql(LimitInfo limit, String sql, String as);

    public void saveTX(DictionaryindexDTO dictionaryindexDTO) throws AppException;

    public void updateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException;

    public void saveOrUpdateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException;

    public void deleteTX(String ids) throws AppException;

    public DictionaryindexDTO get(String id) throws AppException;

    public List<DictionaryindexDTO> listByIds(String ids) throws AppException;

    public String save(DictionaryindexDTO dictionaryindexDTO) throws Exception;

    public String update(DictionaryindexDTO dictionaryindexDTO) throws Exception;
}
