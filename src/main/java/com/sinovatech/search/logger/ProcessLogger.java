package com.sinovatech.search.logger;

import com.sinovatech.search.entity.LogInfoDto;
import com.sinovatech.search.entity.abstractdto.LogInfoAbstractDto;

/**
 * 处理日志信息接口类
 * @author ChenZhuo
 * @date 2016年12月12日 下午2:04:40
 */
public interface ProcessLogger {

    /**
     *
     * @author ChenZhuo
     * @date 2016年12月12日 下午2:11:32
     * @param dto
     */
    public void processLog(LogInfoAbstractDto dto);
    
    /**
     * 
     *
     * @author ChenZhuo
     * @date 2016年12月14日 下午2:27:11
     * @param dto
     * @throws Exception
     */
    public void processLogOfJson(LogInfoAbstractDto dto) throws Exception;

}
