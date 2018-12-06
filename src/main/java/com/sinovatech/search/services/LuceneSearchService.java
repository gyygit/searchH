package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.LuceneSearchDTO;

public interface LuceneSearchService {

	public LuceneSearchDTO search(LuceneSearchDTO dto,LimitInfo limit);
	
	public List searchHot(String appcode,String commandcode, LimitInfo limit) ;
	
}
