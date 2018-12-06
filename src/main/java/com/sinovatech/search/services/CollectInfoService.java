package com.sinovatech.search.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月12日 上午11:40:44
 */
public interface CollectInfoService {
    
    /**
     *
     * @author ChenZhuo
     * @date 2016年12月12日 下午1:33:18
     * @param request
     * @param response
     * @throws Exception 
     */
    public void recordLogInfo(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月15日 下午4:25:31
     * @param request
     * @param response 
     * @throws AppException 
     */
    public void recordLogInfoToDocument(HttpServletRequest request, HttpServletResponse response) throws AppException;

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月22日 下午3:18:25
     * @param clickDTO
     */
    public void intoClickVisitLogInfo(ClickVisitLogInfoDTO clickDTO);

}
