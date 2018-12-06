package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.RecommendDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
/**
 * Recommend推荐 Service接口
 * @author Ma Tengfei
 *
 */
public interface RecommendService {

	public List<RecommendDTO> list(LimitInfo limit,String sqlwhere);
	
	public List<RecommendDTO> list(LimitInfo limit, RecommendDTO dto);
	public List<RecommendDTO> list(LimitInfo limit, RecommendDTO dto,String usercode,String appcode);
	
	public String save(RecommendDTO dto) throws Exception;
	
	public void saveTX(RecommendDTO dto) throws AppException;
	
	public RecommendDTO get(String id) throws AppException;
	
	public String update(RecommendDTO dto) throws Exception;
	
	public void updateTX(RecommendDTO dto) throws AppException;
	
	public void deleteTX(String ids) throws AppException;
	
	public List<SearchCommandDTO> getListCommendByAppCode(String code);
}
