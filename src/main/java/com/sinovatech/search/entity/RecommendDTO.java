

package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.RecommendAbstractDTO;
/**
 * 实体对象 推荐
 * @author Ma Tengfei
 *
 */
public class RecommendDTO extends RecommendAbstractDTO{
	
	private static final long serialVersionUID = -7481266579295475160L;

	public RecommendDTO() {
		super();
	}

	public RecommendDTO(java.lang.String id) {
		super(id);
	}

	public boolean equals(Object other) {
		if (!(other instanceof RecommendDTO)) {
			return false;
		}
		RecommendDTO castOther = (RecommendDTO) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
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
