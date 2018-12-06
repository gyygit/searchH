package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.LuceneSearchAbstractDTO;
import com.sinovatech.search.entity.abstractdto.SearchAppAbstractDTO;

/**
 * <ul>
 * <li><b>目的:</b> <br />
 * <p>
 * 实体对象，注册应用业务表， 对应表:SEARCH_APP可在本类中过客户化操作
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li><b>修改历史：</b><br />
 * <p>
 * 创建: 2014-11-14 13:25:04<br />
 * 
 * @author 作者liuzhenquan
 *         </p>
 *         </li> <li><b>已知问题：</b></li>
 *         </ul>
 */
public class LuceneSearchDTO extends LuceneSearchAbstractDTO {
	public LuceneSearchDTO() {
		super();
	}

	public LuceneSearchDTO(java.lang.String id) {
		super(id);
	}

	public boolean equals(Object other) {
		if (!(other instanceof LuceneSearchDTO)) {
			return false;
		}
		LuceneSearchDTO castOther = (LuceneSearchDTO) other;
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
