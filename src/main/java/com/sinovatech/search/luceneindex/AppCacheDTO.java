package com.sinovatech.search.luceneindex;

import java.util.Map;

import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SearchRuleDateDTO;

public class AppCacheDTO {
	private SearchAppDTO searchAppDTO;
	private Map<String,SearchCommandDTO> searchCommandDTOMap;//<commandCode,SearchCommandDTO>
	private Map<String,SearchRuleDateDTO> searchRuleDateDTOMap;//<flied,searchRuleDateDTOMap>
	
	public SearchAppDTO getSearchAppDTO() {
		return searchAppDTO;
	}
	public void setSearchAppDTO(SearchAppDTO searchAppDTO) {
		this.searchAppDTO = searchAppDTO;
	}
	public Map<String, SearchCommandDTO> getSearchCommandDTOMap() {
		return searchCommandDTOMap;
	}
	public void setSearchCommandDTOMap(
			Map<String, SearchCommandDTO> searchCommandDTOMap) {
		this.searchCommandDTOMap = searchCommandDTOMap;
	}
	public Map<String, SearchRuleDateDTO> getSearchRuleDateDTOMap() {
		return searchRuleDateDTOMap;
	}
	public void setSearchRuleDateDTOMap(
			Map<String, SearchRuleDateDTO> searchRuleDateDTOMap) {
		this.searchRuleDateDTOMap = searchRuleDateDTOMap;
	}
	
}
