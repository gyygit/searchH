
package com.sinovatech.search.services.impls;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.daos.DictionaryDAO;
import com.sinovatech.search.entity.DictionaryDTO;
import com.sinovatech.search.services.DictionaryService;

/**
 * Dictionary数据字典子表 Service实现类
 * 
 * 创建: 2014-11-14 13:24:57<br />
 * @author  作者liuzhenquan
 */
public class DictionaryServiceImpl implements DictionaryService {
@Autowired
private DictionaryDAO dictionaryDAO; 

@Autowired
private JdbcTemplate jdbcTemplate;


/**
 * 获取所有数据
 */
public List<DictionaryDTO> getAllDatas() {
  return dictionaryDAO.getAll();
}
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
	List lst = dictionaryDAO.list(limit, sqlwhere);    
   return lst; 
}
/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List list(LimitInfo limit, String hql,String as) {
	List lst = dictionaryDAO.list(limit,hql,as); 
    return lst; 
}

/**
 * 前台sql必须加  “ where 1=1 ” 
 * @param limit
 * @param hql
 * @param as 主表别名 可以不传
 * @return
 */
public List  listForSql(
   LimitInfo limit, String sql,String as) {
   List lst = dictionaryDAO.listForSql(limit,sql,as);    
    return lst; 
}
/**
 * from table  where code=? and url=?
 * @param page
 * @param obj
 * @return
 */
public Page  page(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
 {
 hql = " from DictionaryDTO Z";
 }
 return dictionaryDAO.findPage(page, hql, objWhere);
}

public Page  pageForSql(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
  {
   hql = " from DICTIONARY  Z";
  }
  return dictionaryDAO.findPageForSql(page, hql, objWhere);
}
@Override
public void saveTX(DictionaryDTO dictionaryDTO) throws AppException {
  dictionaryDAO.save(dictionaryDTO);
}
@Override
public void updateTX(DictionaryDTO dictionaryDTO) throws AppException {
 dictionaryDAO.update(dictionaryDTO);
}
@Override
public void saveOrUpdateTX(DictionaryDTO dictionaryDTO) throws AppException {
  dictionaryDAO.saveOrUpdate(dictionaryDTO);
}
@Override
public void deleteTX(String ids) throws AppException {
    String[] id = ids.split(",");
    for (int i = 0; i < id.length; i++)
    {
      dictionaryDAO.delete(id[i]);
    }
}
@Override
public DictionaryDTO get(String id) throws AppException {
   return dictionaryDAO.get(id);
}
@Override
public List listByIds(String ids) throws AppException {
  ids = "'" + ids.replaceAll(",", "','")+ "'";
  return dictionaryDAO.listByIds(ids);
}
@Override
public List list(LimitInfo limit ,DictionaryDTO dto){
	
	StringBuffer sb = new StringBuffer();
	if (!dto.getName().equals("")) {
		sb.append(" and name like '%").append(dto.getName()).append("%'");
	}

	if (!dto.getCode().equals("")) {
		sb.append(" and code like '%").append(dto.getCode()).append("%'");
	}
	if (!dto.getStatus().equals("")) {
		sb.append(" and status like '%").append(dto.getStatus())
				.append("%'");
	}
	sb.append(" and indexId = '").append(dto.getIndexId()).append("'");
	return this.list(limit, sb.toString());
}
@Override
public String save(DictionaryDTO dictionaryDTO) throws Exception{
	String flag = "OK";
	flag = this.validDate(dictionaryDTO, flag);
	if (!flag.equals("OK")) 
		return flag;
		dictionaryDTO.setUpdatetime(new Date());
		this.saveTX(dictionaryDTO);
		return flag;
	}
private String validDate(DictionaryDTO dto,String flag) {
	if (dto.getName() != null
			&& dto.getName().equals("")) {
		flag = "名称不能为空!";
	}
	if (dto.getCode() != null
			&& dto.getCode().equals("")) {
		flag = "code不能为空!";
		}
	if (dto.getValue() != null
			&& dto.getValue().equals("")) {
		dto.setValue(" ");
	}


	String hql = "";
	if (dto.getId() != null && !dto.getId().equals(""))
		hql += " and Z.id != '" + dto.getId() + "'";
	LimitInfo limit = new LimitInfo();

	List list1 = this.dictionaryDAO.list(limit, " and Z.code = '"
			+ dto.getCode() + "' and Z.indexId = '" + dto.getIndexId() +"'"  + hql);
	if (list1 != null && list1.size() > 0) {
		return flag = "子表code不能重复，请重新录入!";
	}
	List list2 = this.dictionaryDAO.list(limit, " and Z.name = '"
			+ dto.getName() + "'" + hql);
	if (list2 != null && list2.size() > 0) {
		return flag = "名称不能重复，请重新录入!";
	}
	return flag ;
}
@Override
public String update(DictionaryDTO dictionaryDTO)throws Exception{
	// 根据id获得dictionary，填充数据
			DictionaryDTO m = this.get(dictionaryDTO.getId());
			dictionaryDTO.setUpdatetime(new Date());
			dictionaryDTO.setId(m.getId());
			dictionaryDTO.setIndexId(m.getIndexId());
			dictionaryDTO.setIndexcode(m.getIndexcode());
			// dictionaryDTO.setStatus(m.getStatus());
			String flag = "OK";
			flag = this.validDate(dictionaryDTO, flag);
			if (flag.equals("OK"))
				this.updateTX(dictionaryDTO);
			return flag;
}
}

