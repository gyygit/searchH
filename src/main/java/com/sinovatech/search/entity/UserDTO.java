package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.UserAbstractDTO;
/**
 * 实体对象，用户User表(用户添加、登陆等操作)
 * @author Ma Tengfei
 *
 */
public class UserDTO extends UserAbstractDTO{

	public UserDTO() {
		super();
	}

	public UserDTO(java.lang.String id) {
		super(id);
	}

	public boolean equals(Object other) {
		if (!(other instanceof UserDTO)) {
			return false;
		}
		UserDTO castOther = (UserDTO) other;
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
