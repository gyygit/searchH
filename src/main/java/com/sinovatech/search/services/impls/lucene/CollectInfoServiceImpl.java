package com.sinovatech.search.services.impls.lucene;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.entity.ClickVisitLogInfoDTO;
import com.sinovatech.search.entity.LogInfoDto;
import com.sinovatech.search.entity.abstractdto.LogInfoAbstractDto;
import com.sinovatech.search.logger.ProcessLogger;
import com.sinovatech.search.luceneindex.db.dao.ClickVisitLogInfoDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchLoggerDAO;
import com.sinovatech.search.services.CollectInfoService;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月12日 上午11:41:47
 */
public class CollectInfoServiceImpl implements CollectInfoService {

    @Autowired
    private ProcessLogger processLogger;
    
    @Autowired
    private SearchLoggerDAO searchLoggerDAOL;
    
    @Autowired
    private ClickVisitLogInfoDAO clickVisitLogInfoDAOL;
    
    private String clazzName;
    
    public void saveTX(LogInfoDto dto) throws AppException {
        searchLoggerDAOL.add(dto);
    }
    
    /* (non-Javadoc)
     * @see com.sinovatech.search.services.CollectInfoService#recordLogInfo()
     */
    @Override
    public void recordLogInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogInfoAbstractDto dto = (LogInfoAbstractDto) Class.forName(clazzName).newInstance();
        dto.init(request, response);
//        processLogger.processLog(dto);
        processLogger.processLogOfJson(dto);
    }

    /**  
     * 获取clazzName  
     * @return clazzName clazzName  
     */
    public String getClazzName() {
        return clazzName;
    }

    /**
     * 设置clazzName
     * @param clazzName the clazzName to set
     */
    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.CollectInfoService#recordLogInfoToDocument(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void recordLogInfoToDocument(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LogInfoDto dto = new LogInfoDto();
        dto.init(request, response);
        this.saveTX(dto);
    }

    /* (non-Javadoc)
     * @see com.sinovatech.search.services.CollectInfoService#intoClickVisitLogInfo(com.sinovatech.search.entity.ClickVisitLogInfoDTO)
     */
    @Override
    public void intoClickVisitLogInfo(ClickVisitLogInfoDTO clickDTO) {
        clickVisitLogInfoDAOL.add(clickDTO);
    }
    
    

}
