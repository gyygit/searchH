 package com.sinovatech.search.entity;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.SearchRuleDateAbstractDTO;
/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，索引数据配置表， 对应表:SEARCH_RULE_DATE可在本类中过客户化操作
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建: 2014-11-14 13:25:11<br />
 * @author  作者liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
public class  SearchRuleDateDTO extends SearchRuleDateAbstractDTO{
	public   SearchRuleDateDTO()
	{
	      super();
	}


	public  SearchRuleDateDTO(java.lang.String id)
	{
	        super(id);
	}


	public boolean equals(Object other)
	{	
                                     if ( !(other instanceof  SearchRuleDateDTO) )
                                         {return false;}
	        SearchRuleDateDTO castOther = ( SearchRuleDateDTO) other;
	        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}


	public int hashCode()
	{
	           return new HashCodeBuilder().append(getId()).toHashCode();
	}
	public Object clone() {
	           Object o = null;
	            try {
		 o = super.clone();
	                  } catch (CloneNotSupportedException e) {
		 System.out.println(e.toString());
	                  }
	          return o;
	}
}

