package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.GroupBean;

/**
 *
 * @author Ma Tengfei
 * @date 2016年12月22日 下午4:56:07
 */
public interface SearchClickVisitLogService {

    /**
     *
     * @author Ma Tengfei
     * @date 2016年12月22日 下午5:02:28
     * @param limit
     * @param clickDto
     * @return
     */
    List<ClickVisitLogInfoDTO> getClickVisitLogList(LimitInfo limit, ClickVisitLogInfoDTO clickDto);
    /**
     * 
    * @Title: group
    * @Description: 分组查询
    * @param @return
    * @param @throws Exception   设定文件
    * @return List<GroupBean>    返回类型
    * @throws
     */
    List<GroupBean> group(String groupField) throws Exception;
    
    void deleteTX(String ids) throws AppException;
}
