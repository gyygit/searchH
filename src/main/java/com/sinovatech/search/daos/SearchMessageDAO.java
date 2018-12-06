package com.sinovatech.search.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.orm.hibernate.HibernateDao;
/**
 * 泛型DAO类.
 * SearchMessage推送信息表 DAO实现类
 * 
 * 创建: 2014-12-18 14:06:04<br />
 * @author  作者liuzhenquan
 */
public class SearchMessageDAO extends HibernateDao<SearchMessageDTO, String> {
@Override
@Autowired
@Qualifier(value = "sessionFactory")
public void setSessionFactory(SessionFactory sessionFactory) {
   super.setSessionFactory(sessionFactory);
}

/**
 * 前台sql 不需要  “ where 1=1 ”
 * @param sqlwhere  直接是条件
 * @param limit
 * @param sqlwhere
 * @return
 */
public List list(LimitInfo limit,String sqlwhere) {
  String hql = " from SearchMessageDTO Z ";
  List lst = listByLimit(limit, hql,sqlwhere,"Z");
   return lst; 
}
public List listByIds(String ids) {
  LimitInfo limit =new LimitInfo();
  return this.list(limit, "    Z.Id in (" + ids + ")");
 }
}
