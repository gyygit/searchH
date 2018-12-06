/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: HibernateDao.java 1205 2010-09-09 15:12:17Z calvinxiu $
 */
package com.sinovatech.search.orm.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.orm.PropertyFilter;
import com.sinovatech.search.orm.PropertyFilter.MatchType;
import com.sinovatech.search.services.impls.SearchAppServiceImpl;
import com.sinovatech.search.utils.reflection.ReflectionUtils;

/**
 * 封装SpringSide扩展功能的Hibernat DAO泛型基类.
 * 
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 * 
 * @author calvin
 */
public class HibernateDao<T, PK extends Serializable> extends
		SimpleHibernateDao<T, PK> {
	/**
	 * 用于Dao层子类的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
	 * HibernateDao<User, Long>{ }
	 */
	public HibernateDao() {
		super();
	}
	private static final Log log = LogFactory.getLog(HibernateDao.class);
	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数. 在构造函数中定义对象类型Class. eg.
	 * HibernateDao<User, Long> userDao = new HibernateDao<User,
	 * Long>(sessionFactory, User.class);
	 */
	public HibernateDao(final SessionFactory sessionFactory,
			final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	// -- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final Page<T> page) {
		return findPage(page);
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数. 注意不支持其中的orderBy参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql,
			final Object... values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page
	 *            分页参数. 注意不支持其中的orderBy参数.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql,
			final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List<T> result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page
	 *            分页参数.
	 * @param criterions
	 *            数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);

		if (page.isAutoCount()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);

		List<T> result = c.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0,"Page Size must larger than zero");

		// hibernate的firstResult的序号从0开始
		q.setFirstResult(page.getFirst() - 1);
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c,
			final Page<T> page) {

		Assert
				.isTrue(page.getPageSize() > 0,
						"Page Size must larger than zero");

		// hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');

			Assert.isTrue(orderByArray.length == orderArray.length,
					"分页多重排序参数中,排序字段与排序方向的个数不相等");

			for (int i = 0; i < orderByArray.length; i++) {
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(Order.asc(orderByArray[i]));
				} else {
					c.addOrder(Order.desc(orderByArray[i]));
				}
			}
		}
		return c;
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);
		System.out.println("hql count ===============" + countHql);
		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:"
					+ countHql, e);
		}
	}
	/*
	 * 本函数只能自动处理简单的sql语句,复杂的sql查询请另行编写count语句查询.
	 */
	protected long countSqlResult(final String sql, final Map<String, ?> values) { 
		try { 
		    Query  query = createQueryForSql(sql,values);
		    Long count = ((java.math.BigDecimal) query.uniqueResult()).longValue();
			return count;
		} catch (Exception e) { 
			throw new RuntimeException("hql can't be auto count, sqlc is:"
					+ sql, e);
		}
	}
	 
	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings("unchecked")
	protected long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List<OrderEntry>) ReflectionUtils.getFieldValue(
					impl, "orderEntries");
			ReflectionUtils
					.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	// -- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 * 
	 * @param matchType
	 *            匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value,
			final MatchType matchType, Order... ooo) {
		Criterion criterion = buildCriterion(propertyName, value, matchType);
		return find(criterion);
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(criterions);
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPage(final Page<T> page,
			final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(page, criterions);
	}

	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	protected Criterion buildCriterion(final String propertyName,
			final Object propertyValue, final MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		// 根据MatchType构造criterion
		switch (matchType) {
		case EQ:
			criterion = Restrictions.eq(propertyName, propertyValue);
			break;
		case LIKE:
			criterion = Restrictions.like(propertyName, (String) propertyValue,
					MatchMode.ANYWHERE);
			break;

		case LE:
			criterion = Restrictions.le(propertyName, propertyValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, propertyValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, propertyValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, propertyValue);
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	protected Criterion[] buildCriterionByPropertyFilter(
			final List<PropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(),
						filter.getMatchValue(), filter.getMatchType());
				criterionList.add(criterion);
			} else {// 包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param, filter
							.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}

	/**
	 * 
	 * 基于与前台大数据量查询进行翻页 与ectable结合
	 * 
	 * @param limit
	 * @return
	 */
	public List<T> listByLimit(
			LimitInfo limit , String hql) {
		Object[] param = limit.getWhereHQL("Z");
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			hql += " where 1=1 " + param[0] + " " + limit.getOrder("Z");
			Query q = createQuery(hql, (Map) param[1]);
			return q.list();
		} else {// 分页查询
			hql += " where 1=1 " + param[0] + " " + limit.getOrder("Z");
			Query q = createQuery(hql, (Map) param[1]);
			long totalCount = countHqlResult(hql, (Map) param[1]);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
	}
	/**
	 * 
	 * 基于与前台大数据量查询进行翻页 与ectable结合
	 * 
	 * @param limit
	 * @return
	 */
	public List<T> listByLimit( 
			 LimitInfo limit, String hql,String where,String as) {
		Object[] param = limit.getWhereHQL(as);
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			hql += " where 1=1  "+where+" " + param[0] + " " + limit.getOrder(as);
			Query q = createQuery(hql, (Map) param[1]);
			return q.list();
		} else {// 分页查询
			hql += " where 1=1  "+where+" "  + param[0] + " " + limit.getOrder(as);
			Query q = createQuery(hql, (Map) param[1]);
			long totalCount = countHqlResult(hql, (Map) param[1]);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
	}
	
	public List<T> listByLimitForSql(
			LimitInfo limit, String sql,String where,String as) {
		Object[] param = limit.getWhereHQL(as);
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			sql += " where 1=1  "+where+" " + param[0] + " " + limit.getOrder(as);
			Query q = createSQLQuery(sql, (Map) param[1]);
			return q.list();
		} else {// 分页查询
			sql += " where 1=1  "+where+" "  + param[0] + " " + limit.getOrder(as);
			Query q = createSQLQuery(sql, (Map) param[1]);
			long totalCount = getRecordCountBySQL(sql, (Map) param[1]);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
	}
	public List<T> listByLimitForSqlRmap(
			LimitInfo limit, String sql,String where,String as) {
		Object[] param = limit.getWhereHQL(as);
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			sql += " where 1=1  "+where+" " + param[0] + " " + limit.getOrder(as);
			Query q = createSQLQuery(sql, (Map) param[1]);
			q.setResultTransformer(new ResultTransformer() {
		         
				   @Override
				   public Object transformTuple(Object[] values, String[] columns) {
				      Map<String, Object> map = new LinkedHashMap<String, Object>(1);
				      Object ov =null;
				      int i = 0;
				      for(String column : columns){
				    	  ov =values[i++];
				    	  if(ov==null)
				    	  {
				    		  ov="";
				    	  }
				         map.put(column, ov.toString());
				      }
				      return map;
				   }
				   
				   @Override
				   public List transformList(List list) {
				      return list;
				   }
				});
			return q.list();
		} else {// 分页查询
			sql += " where 1=1  "+where+" "  + param[0] + " " + limit.getOrder(as);
			Query q = createSQLQuery(sql, (Map) param[1]);
			q.setResultTransformer(new ResultTransformer() {
		         
				   @Override
				   public Object transformTuple(Object[] values, String[] columns) {
					      Map<String, Object> map = new LinkedHashMap<String, Object>(1);
					      Object ov =null;
					      int i = 0;
					      for(String column : columns){
					    	  ov =values[i++];
					    	  if(ov==null)
					    	  {
					    		  ov="";
					    	  }
					         map.put(column, ov.toString());
					      }
					      return map;
					   }
					   
				   
				   @Override
				   public List transformList(List list) {
				      return list;
				   }
				});
			long totalCount = getRecordCountBySQL(sql, null);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
	}
	
	/**
	 * 按SQL分页查询.
	 * 
	 * @param page
	 *            分页参数. 注意不支持其中的orderBy参数.
	 * @param sql
	 *            sql语句.
	 * @param values
	 *            数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageForSql(final Page<T> page, final String sql,
			final Object... values) {
		Assert.notNull(page, "page不能为空");

		SQLQuery q = createSQLQuery(sql, values);

		if (page.isAutoCount()) {
			long totalCount = getRecordCountBySQL(sql,values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List<T> result = q.list();
		page.setResult(result);
		return page;
	}
	
	public int getRecordCountBySQL(String sql) {
		Session session = null;
		sql = "select count(*) from (" + sql +")";
		int result = 0;
		try {
			session = super.getSession();
			logger.debug("sql = " + sql);
			Query qry = session.createSQLQuery(sql);
			result = ((Number) qry.uniqueResult()).intValue();
		} catch (Exception e) {
			logger.error("获取SQL查询行数异常！getRecordCountBySQL(String)中。");
		} finally {

		}
		return result;
	}
	public int getRecordCountBySQL(String sql ,final Object... values) {  
		Session session = null;
		sql = "select count(*) from (" + sql +")";
		int result = 0;
		try {
			session = super.getSession();
			logger.debug("sql = " + sql);
			Query qry = session.createSQLQuery(sql);
			
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					qry.setParameter(i, values[i]);
				}
			}
			result = ((Number) qry.uniqueResult()).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取SQL查询行数异常！getRecordCountBySQL(String)中。 加条件的");
		} finally {

		}
		return result;
	}
	/**
	 * 解析查询总数sql语句
	 * @param sql
	 * @return
	 */
	protected String parseCountSql(String sql) {
		if(!sql.matches(".* (count|COUNT)\\(.+\\) .+"))//如果语句中已包含count函数，则不作任务解析(当一条统计语句比较复杂时，可自定义统计语句)，否则，按一般规则解析统计语句
		{
			sql = sql.replaceAll("(\\s|　)+", " ");
			int startIndex = sql.indexOf("from ");
			if(startIndex==-1)
				startIndex = sql.indexOf("FROM ");
			int endIndex = sql.lastIndexOf(" group by ");
			if(endIndex == -1)
			{
				endIndex = sql.lastIndexOf(" GROUP BY ");
			}
			if(endIndex == -1)
			{
				endIndex = sql.lastIndexOf(" order by ");
			}
			if(endIndex == -1)
			{
				endIndex = sql.lastIndexOf(" ORDER BY ");
			}
			if(endIndex==-1)
				sql = sql.substring(startIndex);
			else
				sql = sql.substring(startIndex, endIndex);
			
			sql = "select count(*) "+sql;
		}
		return sql;
	}

	
	/**
	 * 根据查询SQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public SQLQuery createSQLQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = getSession().createSQLQuery(queryString);
		 if ((values != null) && (values.size() > 0)) {
				query.setProperties(values);
			}
		return query;
	}
	
	/**
	 * 根据查询SQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public SQLQuery createSQLQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = getSession().createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	/**
	 * 前台sql必须加  “ where 1=1 ” 
	 * @param limit
	 * @param hql
	 * @param as 主表别名 可以不传
	 * @return
	 */
	public List list(LimitInfo limit, String hql,String as) {
		Object[] param = null;
		if("".equals(as) || as==null)
		{
			param =new Object[]{"",new HashMap()};
		}else{
			param = limit.getWhereHQL(as);
		}
		
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			hql +=  " "  +   param[0] + " " + limit.getOrder(as);
			Query q = createQuery(hql, (Map) param[1]);
			return q.list();
		} else {// 分页查询
			hql += " "  +   param[0] + " " + limit.getOrder(as);
			Query q = createQuery(hql, (Map) param[1]);
			long totalCount = countHqlResult(hql, (Map) param[1]);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
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
		
		Object[] param = null;
		if("".equals(as) || as==null)
		{
			param =new Object[]{"",new HashMap()};
		}else{
			param = limit.getWhereHQL(as);
		}
		
		if (limit.getRowDisplayed() < 1) {// 非分页, 每页记录小于1时表示不进行分页
			sql += "   " + param[0] + " " + limit.getOrder(as);
			Query q = createSQLQuery(sql, (Map) param[1]);
			return q.list();
		} else {// 分页查询
			String sqlcount = " select count(*) from ( "+ sql + " "  + param[0] + " " + limit.getOrder(as)+" )";
			Query q = createSQLQuery(sql, (Map) param[1]);
			long totalCount = countSqlResult(sqlcount, (Map) param[1]);
			limit.setTotalNum((int) totalCount);
			q.setFirstResult(limit.getStartLineNum() - 1);
			q.setMaxResults(limit.getRowDisplayed());
			return q.list();
		}
	}
	/**
	 * from T_REQ_URL T where code=? and url=?
	 * @param page
	 * @param obj
	 * @return
	 */
	public Page  page(Page page, String hql,Object[] objWhere){
		return this.findPage(page, hql, objWhere);
	}
	
	public Page  pageForSql(Page page, String hql,Object[] objWhere){
		return this.findPageForSql(page, hql, objWhere);
	}
	 
	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteForSql(final String hql, final Object... values) {
		return createQueryForSql(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecuteForSql(final String hql, final Map<String, ?> values) {
		return createQueryForSql(hql, values).executeUpdate();
	}	 
}
