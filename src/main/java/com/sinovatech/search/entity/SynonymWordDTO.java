package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.SynonymWordAbstractDTO;

/**
 * 同义词实体类，对应表:SYNONYM_WORD可在本类中过客户化操作
 * @author ChenZhuo
 * @date 2016年12月6日 下午5:24:35
 */
public class SynonymWordDTO extends SynonymWordAbstractDTO {

    private static final long serialVersionUID = 7304260725515459467L;

    public SynonymWordDTO() {
    }

    public SynonymWordDTO(String id) {
        super(id);
    }
    
    public boolean equals(Object other) {
		if (!(other instanceof SynonymWordDTO)) {
			return false;
		}
		SynonymWordDTO castOther = (SynonymWordDTO) other;
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
