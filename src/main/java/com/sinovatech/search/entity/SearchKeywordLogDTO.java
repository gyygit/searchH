package com.sinovatech.search.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sinovatech.search.entity.abstractdto.SearchKeywordLogAbstractDTO;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，搜索日志表， 对应表:SEARCH_KEYWORD_LOG可在本类中过客户化操作
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建: 2014-11-14 13:25:08<br />
 * @author  作者liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
public class SearchKeywordLogDTO extends SearchKeywordLogAbstractDTO {
    public SearchKeywordLogDTO() {
        super();
    }

    public SearchKeywordLogDTO(java.lang.String id) {
        super(id);
    }

    public boolean equals(Object other) {
        if (!(other instanceof SearchKeywordLogDTO)) {
            return false;
        }
        SearchKeywordLogDTO castOther = (SearchKeywordLogDTO) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
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
