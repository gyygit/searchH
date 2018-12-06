package com.sinovatech.search.services.impls.lucene;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.common.util.StringUtils;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.RecommendDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.luceneindex.db.Page;
import com.sinovatech.search.luceneindex.db.dao.RecommendDAO;
import com.sinovatech.search.luceneindex.db.dao.SearchCommandDAO;
import com.sinovatech.search.services.RecommendService;
/**
 * Recommend推荐 Service实现类
 * @author Ma Tengfei
 *
 */
public class RecommendServiceImpl implements RecommendService{

	@Autowired
	private RecommendDAO recommendDAO;
	
	@Autowired
	private SearchCommandDAO searchCommandDAOL;
	
	@Override
	public List list(LimitInfo limit, String sqlwhere) {
		Page page = new Page();
		page.setRowDisplayed(limit.getRowDisplayed());
		page.setPageNum(limit.getPageNum());
		page.setQueryStr(sqlwhere);
		List<RecommendDTO> lst = recommendDAO.listForT(page);
		limit.setTotalNum(page.getTotalNum());
		return lst;
	}

	@Override
	public List list(LimitInfo limit, RecommendDTO dto) {
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		if (!"".equals(dto.getAppCode()) && (null != dto.getAppCode())) {
			sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
		}
		if (!"".equals(dto.getSearchKeyword()) && (null != dto.getSearchKeyword())) {
			sb.append(" AND (searchKeyword:").append(dto.getSearchKeyword()).append("*) ");
		}
		if (!"".equals(dto.getTitle()) && (null != dto.getTitle())) {
			sb.append(" AND (title:").append(dto.getTitle()).append("*) ");
		}
		if (!"".equals(dto.getStatus()) && (null!=dto.getStatus()) ) {
			sb.append(" AND (status:").append(dto.getStatus())
					.append("~0) ");
		}
		return this.list(limit, sb.toString());
	}
	
	@Override
	public List list(LimitInfo limit, RecommendDTO dto,String usercode,String appcode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		if (!"".equals(dto.getAppCode()) && (null != dto.getAppCode())) {
			sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
		}
		if (!"".equals(dto.getSearchKeyword()) && (null != dto.getSearchKeyword())) {
			sb.append(" AND (searchKeyword:").append(dto.getSearchKeyword()).append("*) ");
		}
		if (!"".equals(dto.getTitle()) && (null != dto.getTitle())) {
			sb.append(" AND (title:").append(dto.getTitle()).append("*) ");
		}
		if (!"".equals(dto.getStatus()) && (null!=dto.getStatus()) ) {
			sb.append(" AND (status:").append(dto.getStatus())
			.append("~0) ");
		}
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if(!"admin".equals(usercode)){
				if(appcode.indexOf(",")!=-1){
					String[] app = appcode.split(",");
					sb.append(" AND (");
					for(int i=0;i<app.length;i++){
						sb.append("(appCode:").append(app[i]).append("*) OR ");
					}
					String cssb = sb.substring(0,sb.length()-3)+")";
					sb = new StringBuffer();
					sb.append(cssb);
				}else{
					sb.append(" AND (appCode:").append(appcode).append("*) ");
				}
			}
		}
		return this.list(limit, sb.toString());
	}

	@Override
	public String save(RecommendDTO dto) throws Exception {
		String flag = "OK";
		flag = this.checkFieldISValid(dto);
		if (flag != "OK"){
			return flag;
		}
//		RecommendDTO.setCreateTime(new Date());
		this.saveTX(dto);
		return flag;
	}

	private String checkFieldISValid(RecommendDTO dto){
		String flag = "OK";
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		if (!"".equals(dto.getAppCode()) && (null != dto.getAppCode())) {
			sb.append(" AND (appCode:").append(dto.getAppCode()).append("*) ");
		}
		if(!"".equals(dto.getCommandCode()) &&(null != dto.getCommandCode())){
			sb.append(" AND (commandCode:").append(dto.getCommandCode()).append("*) ");
		}
		if (!"".equals(dto.getSearchKeyword()) && (null != dto.getSearchKeyword())) {
			sb.append(" AND (searchKeyword:").append(dto.getSearchKeyword()).append("*) ");
		}
//		if (!"".equals(dto.getStatus()) && (null!=dto.getStatus()) ) {
//			sb.append(" AND (status:").append(dto.getStatus())
//					.append("~0) ");
//		}
		if (!"".equals(dto.getId()) && (null!=dto.getId()) ) {
			sb.append( " NOT (id:" + dto.getId() + "~0)");
		}
		LimitInfo limit = new LimitInfo();
		List<RecommendDTO> lst =  this.list(limit, sb.toString());
		if(lst!=null && lst.size()>0){
			flag = "该业务应用-数据分类下已存在 '"+dto.getSearchKeyword()+"' 该关键词,请重新录入！";
		}
		return flag;
	}
	
	@Override
	public void saveTX(RecommendDTO dto) throws AppException {
		recommendDAO.add(dto);
	}

	@Override
	public RecommendDTO get(String id) throws AppException {
		return recommendDAO.getById(id);
	}

	@Override
	public String update(RecommendDTO dto) throws Exception {
		String flag = "OK";
		flag = this.checkFieldISValid(dto);
		if (flag != "OK"){
			return flag;
		}
		this.updateTX(dto);
		return flag;
	}

	@Override
	public void updateTX(RecommendDTO dto) throws AppException {
		recommendDAO.update(dto);
	}

	@Override
	public void deleteTX(String ids) throws AppException {
		List<RecommendDTO> list = listByIds(ids);
		for(RecommendDTO dto : list){
			recommendDAO.del(dto);
		}
	}

	public List listByIds(String ids) throws AppException {
	    ids = "'" + ids.replaceAll(",", "','")+ "'";
	    return recommendDAO.listByIds(ids);
	}
	
	@Override
	public List<SearchCommandDTO> getListCommendByAppCode(String code){
		Page page = new Page();
		StringBuffer sb = new StringBuffer();
		sb.append(" (*:*) ");
		sb.append(" AND (appCode:").append(code).append("~0) ");
		page.setQueryStr(sb.toString());
		page.setRowDisplayed(Integer.MAX_VALUE);
		List<SearchCommandDTO> lists = searchCommandDAOL.listForT(page);
		return lists;
	}	
	
}
