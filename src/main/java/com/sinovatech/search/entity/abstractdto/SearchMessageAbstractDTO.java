package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，推送信息表， 对应表:SEARCH_MESSAGE
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-12-18 14:06:04<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class SearchMessageAbstractDTO extends DtoSupport {

public  SearchMessageAbstractDTO()
{
}	      
  public  SearchMessageAbstractDTO(java.lang.String id)
    {
           this.setId(id);
    }
        //唯一标识
      private   String  id;
        //注册应用编码
      private   String  appcode;
        //业务数据分类编码
      private   String  commandcode;
        //相关数据
      private   String  fileinfo;
      
      private   String  opt;
      public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	/**
       * get唯一标识
       * @return 唯一标识
       */
      public String getId()
       {
           return id;
        }
       /**
        * se唯一标识
        * @param 唯一标识
        */
      public void setId(String   id)
         {
           this.id=id;
         }
      /**
       * get注册应用编码
       * @return 注册应用编码
       */
      public String getAppcode()
       {
           return appcode;
        }
       /**
        * se注册应用编码
        * @param 注册应用编码
        */
      public void setAppcode(String   appcode)
         {
           this.appcode=appcode;
         }
      /**
       * get业务数据分类编码
       * @return 业务数据分类编码
       */
      public String getCommandcode()
       {
           return commandcode;
        }
       /**
        * se业务数据分类编码
        * @param 业务数据分类编码
        */
      public void setCommandcode(String   commandcode)
         {
           this.commandcode=commandcode;
         }
      /**
       * get相关数据
       * @return 相关数据
       */
      public String getFileinfo()
       {
           return fileinfo;
        }
       /**
        * se相关数据
        * @param 相关数据
        */
      public void setFileinfo(String   fileinfo)
         {
           this.fileinfo=fileinfo;
         }
      public String getPKfiledName() 
    	{
    		return "id";
    	}
}
