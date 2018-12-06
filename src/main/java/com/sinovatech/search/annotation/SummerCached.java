package com.sinovatech.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 缓存自定义注解，可加在需要增加或删除缓存的方法上<br>
 * 此注解中的两个枚举，如果为了代码简洁，可以写到枚举包中<br>
 * 
 * @since 2010-09-22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SummerCached {

	/**
	 * 操作涉及到的缓存组名称
	 */
	public enum RelatedItem {
		// 此处写约定的大key名
		USER, SOMETHINGELSE, NONE
		//
	}

	/**
	 * 此属性支持单个对象缓存<br>
	 * 更新此缓存对象要删除此缓存(方法体多参数的情况下，根据构造参数找到相应的类)<br>
	 */
	public enum SingleItem {
		NONE(Object.class);

		public Class<?> cls;

		private SingleItem(Class<?> cls) {
			this.cls = cls;
		}
	}

	/**
	 * 操作类型 查询\清除，这些操作是针对缓存的<br>
	 * SELECT:只针对DB的查询操作<br>
	 * DELETE:针对关联表的更新和删除<br>
	 * 
	 */
	public enum OperatorType {
		SELECT, DELETE
	}

	// 相关联的缓存组名称
	RelatedItem[] related() default RelatedItem.NONE;

	// 自定义key前辍
	String keyPrefix() default "DEFAULTKEY";

	// 操作(默认为SELECT)
	OperatorType action() default OperatorType.SELECT;

	// 缓存时间（单位：秒），默认半天
	int livingTime() default 43200;

	SingleItem single() default SingleItem.NONE;
}
