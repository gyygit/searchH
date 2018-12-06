package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.CreateIndexProcessLogAbstractDTO;

public class CreateIndexProcessLogDTO  extends CreateIndexProcessLogAbstractDTO{
	public   CreateIndexProcessLogDTO()
	{
	      super();
	}


	public  CreateIndexProcessLogDTO(java.lang.String id)
	{
	        super(id);
	}


	public boolean equals(Object other)
	{	
                                     if ( !(other instanceof  DictionaryDTO) )
                                         {return false;}
                                     CreateIndexProcessLogDTO castOther = ( CreateIndexProcessLogDTO) other;
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
