package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SynonymWordDTO;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月7日 上午10:07:18
 */
public interface SynonymWordService {

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 上午10:09:15
     * @param synonymWordDTO
     * @return
     * @throws AppException 
     */
    public String save(SynonymWordDTO synonymWordDTO) throws AppException;
    
    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 上午11:51:56
     * @param limit
     * @param sqlwhere
     * @return
     */
    public List<SynonymWordDTO> list(LimitInfo limit, String sqlwhere);

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 上午10:51:09
     * @param synonymWordDTO
     * @throws AppException
     */
    void saveTX(SynonymWordDTO synonymWordDTO) throws AppException;

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 上午11:47:20
     * @param limit
     * @param synonymWordDTO
     * @return
     */
    public List<SynonymWordDTO> list(LimitInfo limit, SynonymWordDTO synonymWordDTO);
    /***
     * 
    * @Title: list
    * @Description:
    * @author Ma Tengfei 
    * @param @param limit
    * @param @param synonymWordDTO
    * @param @param usercode
    * @param @param appcode
    * @param @return 
    * @return List<SynonymWordDTO>    返回类型
    * @throws
     */
    public List<SynonymWordDTO> list(LimitInfo limit, SynonymWordDTO synonymWordDTO,String usercode,String appcode);

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 下午2:47:41
     * @param id
     * @return
     */
    public SynonymWordDTO get(String id);

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 下午2:49:02
     * @param synonymWordDTO
     * @return
     */
    public String update(SynonymWordDTO synonymWordDTO);

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月7日 下午3:38:39
     * @param ids
     */
    public void deleteTX(String ids);

}
