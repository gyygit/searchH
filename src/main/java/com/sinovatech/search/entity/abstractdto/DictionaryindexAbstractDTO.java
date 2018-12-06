
package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，数据字典主表， 对应表:DICTIONARYINDEX
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:00<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class DictionaryindexAbstractDTO extends DtoSupport {

public  DictionaryindexAbstractDTO()
{
}	      
  public  DictionaryindexAbstractDTO(java.lang.String id)
    {
           this.setId(id);
    }
        //主键
      private   String  id;
        //数据字典主表code
      private   String  indexcode;
        //数据字典主表名称
      private   String  indexname;
        //数据字典主表描述
      private   String  description;
        //状态1可用2不可用
      private   String  status;
        //
      private   Date  updatetime;
      /**
       * get主键
       * @return 主键
       */
      public String getId()
       {
           return id;
        }
       /**
        * se主键
        * @param 主键
        */
      public void setId(String   id)
         {
           this.id=id;
         }
      /**
       * get数据字典主表code
       * @return 数据字典主表code
       */
      public String getIndexcode()
       {
           return indexcode;
        }
       /**
        * se数据字典主表code
        * @param 数据字典主表code
        */
      public void setIndexcode(String   indexcode)
         {
           this.indexcode=indexcode;
         }
      /**
       * get数据字典主表名称
       * @return 数据字典主表名称
       */
      public String getIndexname()
       {
           return indexname;
        }
       /**
        * se数据字典主表名称
        * @param 数据字典主表名称
        */
      public void setIndexname(String   indexname)
         {
           this.indexname=indexname;
         }
      /**
       * get数据字典主表描述
       * @return 数据字典主表描述
       */
      public String getDescription()
       {
           return description;
        }
       /**
        * se数据字典主表描述
        * @param 数据字典主表描述
        */
      public void setDescription(String   description)
         {
           this.description=description;
         }
      /**
       * get状态1可用2不可用
       * @return 状态1可用2不可用
       */
      public String getStatus()
       {
           return status;
        }
       /**
        * se状态1可用2不可用
        * @param 状态1可用2不可用
        */
      public void setStatus(String   status)
         {
           this.status=status;
         }
      /**
       * get
       * @return 
       */
      public Date getUpdatetime()
       {
           return updatetime;
        }
       /**
        * se
        * @param 
        */
      public void setUpdatetime(Date   updatetime)
         {
           this.updatetime=updatetime;
         }
   public String getPKfiledName() 
  	{
  		return "id";
  	}
}

