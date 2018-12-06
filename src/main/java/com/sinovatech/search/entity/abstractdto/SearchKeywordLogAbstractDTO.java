
package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，搜索日志表， 对应表:SEARCH_KEYWORD_LOG
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:08<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
*/
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class SearchKeywordLogAbstractDTO extends DtoSupport {

public  SearchKeywordLogAbstractDTO()
{
}	      
  public  SearchKeywordLogAbstractDTO(java.lang.String id)
    {
           this.setId(id);
    }
        //唯一标识
      private   String  id;
        //业务应用编码
      private   String  appCode;
        //业务数据分类编码
      private   String  commandCode;
        //搜索词
      private   String  searchKeyword;
        //搜索词全拼音
      private   String  searchPinyin;
        //搜索词简拼音
      private   String  searchPy;
        //搜索时间
      private   Date  createTime;
        //临时次数
      private int once=1;
      
	    public int getOnce() {
			return once;
		}
		public void setOnce(int once) {
			this.once = once;
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
       * get业务应用编码
       * @return 业务应用编码
       */
      public String getAppCode()
       {
           return appCode;
        }
       /**
        * se业务应用编码
        * @param 业务应用编码
        */
      public void setAppCode(String   appCode)
         {
           this.appCode=appCode;
         }
      /**
       * get业务数据分类编码
       * @return 业务数据分类编码
       */
      public String getCommandCode()
       {
           return commandCode;
        }
       /**
        * se业务数据分类编码
        * @param 业务数据分类编码
        */
      public void setCommandCode(String   commandCode)
         {
           this.commandCode=commandCode;
         }
      /**
       * get搜索词
       * @return 搜索词
       */
      public String getSearchKeyword()
       {
           return searchKeyword;
        }
       /**
        * se搜索词
        * @param 搜索词
        */
      public void setSearchKeyword(String   searchKeyword)
         {
           this.searchKeyword=searchKeyword;
         }
      /**
       * get搜索词全拼音
       * @return 搜索词全拼音
       */
      public String getSearchPinyin()
       {
           return searchPinyin;
        }
       /**
        * se搜索词全拼音
        * @param 搜索词全拼音
        */
      public void setSearchPinyin(String   searchPinyin)
         {
           this.searchPinyin=searchPinyin;
         }
      /**
       * get搜索词简拼音
       * @return 搜索词简拼音
       */
      public String getSearchPy()
       {
           return searchPy;
        }
       /**
        * se搜索词简拼音
        * @param 搜索词简拼音
        */
      public void setSearchPy(String   searchPy)
         {
           this.searchPy=searchPy;
         }
      /**
       * get搜索时间
       * @return 搜索时间
       */
      public Date getCreateTime()
       {
           return createTime;
        }
       /**
        * se搜索时间
        * @param 搜索时间
        */
      public void setCreateTime(Date   createTime)
         {
           this.createTime=createTime;
         }
      public String getPKfiledName() 
    	{
    		return "id";
    	}
}

