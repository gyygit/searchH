package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.entity.GroupBean;

/**
 *
 * @author   liuzhenquan
 * @date 2018年5月3日 下午4:56:07
 */
public interface CindexLogService {

    /**
     *
     * @author liuzhenquan
     * @date 2018年5月3日 下午3:18:28
     * @param limit
     * @param cindexLogDto
     * @return
     */
    List<CreateIndexProcessLogDTO> getCreateIndexProcessLogList(LimitInfo limit, CreateIndexProcessLogDTO cindexLogDto);
     
    void deleteTX(String ids) throws AppException;
}
