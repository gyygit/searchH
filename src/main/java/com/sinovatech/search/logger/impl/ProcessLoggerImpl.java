package com.sinovatech.search.logger.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.search.entity.LogInfoDto;
import com.sinovatech.search.entity.abstractdto.LogInfoAbstractDto;
import com.sinovatech.search.logger.ProcessLogger;
import com.sinovatech.search.utils.JsonSerializeUtil;

/**
 * 处理日志信息实现类
 * @author ChenZhuo
 * @date 2016年12月12日 下午2:05:21
 */
public class ProcessLoggerImpl implements ProcessLogger {
    
    Logger logger = LoggerFactory.getLogger(ProcessLoggerImpl.class);

    /* (non-Javadoc)
     * @see com.sinovatech.search.logger.ProcessLogger#process(com.sinovatech.search.entity.LogBaseDto)
     */
    @Override
    public void processLog(LogInfoAbstractDto dto) {
        logger.info(dto.toString());
    }
    
    public void processLogOfJson(LogInfoAbstractDto dto) throws Exception{
//        logger.info(dto.toJsonString());
        logger.info(JsonSerializeUtil.serializeBeanToJson(dto));
    }

}
