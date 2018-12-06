
package com.sinovatech.search.services.impls;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.daos.DictionaryindexDAO;
import com.sinovatech.search.entity.DictionaryindexDTO;
import com.sinovatech.search.services.DictionaryindexService;

/**
 * Dictionaryindex数据字典主表 Service实现类
 * 
 * 创建: 2014-11-14 13:25:00<br />
 * @author  作者liuzhenquan
 */
public class DictionaryindexServiceImpl implements DictionaryindexService {
@Autowired
private DictionaryindexDAO dictionaryindexDAO; 

@Autowired
private JdbcTemplate jdbcTemplate;


/**
 * 获取所有数据
 */
public List<DictionaryindexDTO> getAllDatas() {
  return dictionaryindexDAO.getAll();
}
/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
	List lst = dictionaryindexDAO.list(limit, sqlwhere);    
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
	List lst = dictionaryindexDAO.list(limit,hql,as); 
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
   List lst = dictionaryindexDAO.listForSql(limit,sql,as);    
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
 hql = " from DictionaryindexDTO Z";
 }
 return dictionaryindexDAO.findPage(page, hql, objWhere);
}

public Page  pageForSql(Page page, String hql,Object[] objWhere){
 if(hql==null || "".equals(hql))
  {
   hql = " from DICTIONARYINDEX  Z";
  }
  return dictionaryindexDAO.findPageForSql(page, hql, objWhere);
}
@Override
public void saveTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
  dictionaryindexDAO.save(dictionaryindexDTO);
}
@Override
public void updateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
 dictionaryindexDAO.update(dictionaryindexDTO);
}
@Override
public void saveOrUpdateTX(DictionaryindexDTO dictionaryindexDTO) throws AppException {
  dictionaryindexDAO.saveOrUpdate(dictionaryindexDTO);
}
@Override
public void deleteTX(String ids) throws AppException {
    String[] id = ids.split(",");
    for (int i = 0; i < id.length; i++)
    {
      dictionaryindexDAO.delete(id[i]);
    }
}
@Override
public DictionaryindexDTO get(String id) throws AppException {
   return dictionaryindexDAO.get(id);
}
@Override
public List listByIds(String ids) throws AppException {
  ids = "'" + ids.replaceAll(",", "','")+ "'";
  return dictionaryindexDAO.listByIds(ids);
}

@Override
public List list(LimitInfo limit ,DictionaryindexDTO dto){
	StringBuffer sb = new StringBuffer();
	if (!dto.getIndexname().equals("")) {
		sb.append(" and indexname like '%").append(dto.getIndexname()).append("%'");
	}
	if (!dto.getIndexcode().equals("")) {
		sb.append(" and indexcode like '%").append(dto.getIndexcode())
				.append("%'");
	}
	if (!dto.getStatus().equals("")) {
		sb.append(" and status like '%").append(dto.getStatus())
				.append("%'");
	}
	return this.list(limit, sb.toString());
}

@Override
public String save(DictionaryindexDTO dictionaryindexDTO)throws Exception{
	String flag = "OK";
	flag = this.validDate(dictionaryindexDTO, flag);
	if (!flag.equals("OK")) 
		return flag;
		dictionaryindexDTO.setUpdatetime(new Date());
		this.saveTX(dictionaryindexDTO);
		return flag;
	}

	private String validDate(DictionaryindexDTO dto, String flag){
		if (dto.getIndexcode().equals("")) {
			return flag = "数据字典主表code不能为空!";
		}

		else if (dto.getIndexname().equals("")) {
			return flag = "数据字典主表名称不能为空!";
		} else if (dto.getDescription().equals("")) {
			return flag = "数据字典主表描述不能为空!";
		}

		String hql = "";
		if (dto.getId() != null
				&& !dto.getId().equals(""))
			hql += " and Z.id != '" + dto.getId() + "'";
		LimitInfo limit = new LimitInfo();

		List list1 =this.dictionaryindexDAO.list(limit, " and Z.indexcode = '"
				+ dto.getIndexcode() + "'" + hql);
		if (list1 != null && list1.size() > 0) {
			return flag = "数据字典主表code不能重复，请重新录入!";
		}
		List list2 = dictionaryindexDAO.list(limit, " and Z.indexname = '"
				+ dto.getIndexname() + "'" + hql);
		if (list2 != null && list2.size() > 0) {
			return flag = "数据字典主表名称不能重复，请重新录入!";
		}
		return flag;
	}
	@Override
	public String update(DictionaryindexDTO dictionaryindexDTO)throws Exception{
		//根据id获得dictionaryindex，填充数据
		DictionaryindexDTO m = this.get(dictionaryindexDTO.getId());
		String flag = "OK";
		flag = this.validDate(dictionaryindexDTO,flag);
		if(!flag.equals("OK"))
			return flag;
		dictionaryindexDTO.setUpdatetime(new Date());
		this.updateTX(dictionaryindexDTO);
		return flag;
	}
}


