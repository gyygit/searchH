
package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，数据字典子表， 对应表:DICTIONARY
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:24:57<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class DictionaryAbstractDTO extends DtoSupport {

public  DictionaryAbstractDTO()
{
}	      
  public  DictionaryAbstractDTO(java.lang.String id)
    {
           this.setId(id);
    }
        //唯一标示
      private   String  id;
        //主表ID
      private   String  indexId;
        //主表编码
      private   String  indexcode;
        //编码
      private   String  code;
        //名称
      private   String  name;
        //值
      private   String  value;
        //描述
      private   String  description;
        //图片地址
      private   String  dictionaryImgUrl;
        //状态1启用2停用
      private   String  status;
        //更新时间
      private   Date  updatetime;
        //排序
      private   Long  sort;
      /**
       * get唯一标示
       * @return 唯一标示
       */
      public String getId()
       {
           return id;
        }
       /**
        * se唯一标示
        * @param 唯一标示
        */
      public void setId(String   id)
         {
           this.id=id;
         }
      /**
       * get主表ID
       * @return 主表ID
       */
      public String getIndexId()
       {
           return indexId;
        }
       /**
        * se主表ID
        * @param 主表ID
        */
      public void setIndexId(String   indexId)
         {
           this.indexId=indexId;
         }
      /**
       * get主表编码
       * @return 主表编码
       */
      public String getIndexcode()
       {
           return indexcode;
        }
       /**
        * se主表编码
        * @param 主表编码
        */
      public void setIndexcode(String   indexcode)
         {
           this.indexcode=indexcode;
         }
      /**
       * get编码
       * @return 编码
       */
      public String getCode()
       {
           return code;
        }
       /**
        * se编码
        * @param 编码
        */
      public void setCode(String   code)
         {
           this.code=code;
         }
      /**
       * get名称
       * @return 名称
       */
      public String getName()
       {
           return name;
        }
       /**
        * se名称
        * @param 名称
        */
      public void setName(String   name)
         {
           this.name=name;
         }
      /**
       * get值
       * @return 值
       */
      public String getValue()
       {
           return value;
        }
       /**
        * se值
        * @param 值
        */
      public void setValue(String   value)
         {
           this.value=value;
         }
      /**
       * get描述
       * @return 描述
       */
      public String getDescription()
       {
           return description;
        }
       /**
        * se描述
        * @param 描述
        */
      public void setDescription(String   description)
         {
           this.description=description;
         }
      /**
       * get图片地址
       * @return 图片地址
       */
      public String getDictionaryImgUrl()
       {
           return dictionaryImgUrl;
        }
       /**
        * se图片地址
        * @param 图片地址
        */
      public void setDictionaryImgUrl(String   dictionaryImgUrl)
         {
           this.dictionaryImgUrl=dictionaryImgUrl;
         }
      /**
       * get状态1启用2停用
       * @return 状态1启用2停用
       */
      public String getStatus()
       {
           return status;
        }
       /**
        * se状态1启用2停用
        * @param 状态1启用2停用
        */
      public void setStatus(String   status)
         {
           this.status=status;
         }
      /**
       * get更新时间
       * @return 更新时间
       */
      public Date getUpdatetime()
       {
           return updatetime;
        }
       /**
        * se更新时间
        * @param 更新时间
        */
      public void setUpdatetime(Date   updatetime)
         {
           this.updatetime=updatetime;
         }
      /**
       * get排序
       * @return 排序
       */
      public Long getSort()
       {
           return sort;
        }
       /**
        * se排序
        * @param 排序
        */
      public void setSort(Long   sort)
         {
           this.sort=sort;
         }
    public String getPKfiledName() 
  	{
  		return "id";
  	}
}

