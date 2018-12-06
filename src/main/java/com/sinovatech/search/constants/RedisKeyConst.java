package com.sinovatech.search.constants;

/**
 * @功能:redis key 常量
 * @author wuzhu
 * 
 */
public class RedisKeyConst {

	public static Object synObject = new Object();
	public class Search{
		//索引方式 
		public static final String SEARCHAPP_INDEXTYPE_TUI = "1";//推送
		public static final String SEARCHAPP_INDEXTYPE_SHI = "2";//视图
		public static final String SEARCHAPP_INDEXTYPE_FTP = "3";//ftp
		public static final String SEARCHAPP_INDEXTYPE_SERVICE = "4";//服务
		public static final String SEARCHAPP_INDEXTYPE_HOT = "5";//热词
		//状态
		public static final String USERING = "1";//启用
		public static final String STOP = "2";//停用
		
		public static final String RULE_DATE_TYPE_STRING="1";
		public static final String RULE_DATE_TYPE_INT="2";
		public static final String RULE_DATE_TYPE_FLOAT="3";
		public static final String RULE_DATE_TYPE_PK="4";
		
		public static final String RULE_INDEX_TYPE_NOT_INDEX="1";
		public static final String RULE_INDEX_TYPE_NOT_TOKENIZED="2";
		public static final String RULE_INDEX_TYPE_TOKENIZED="3";
		
		public static final String RULE_STORE_TYPE_YES="1";
		public static final String RULE_STORE_TYPE_NO="2";
		public static final String A_DIR="A";
		public static final String B_DIR="B";
		public static final String B_DIR_VALUE="data_b";
		public static final String A_DIR_VALUE="data_a";
		public static final String HOT_DIR_VALUE="hotworddata";
		
		public static final String IS_DELETE_SHOW = "1";//显示
		public static final String IS_DELETE_HIDE = "2";//隐藏
		
		public static final String RESET_NO = "1";//不需要重启
		public static final String RESET_YES = "2";//需要重启
		
		public static final String HOT_FILED_COMMAND= "commandCode";// command
		public static final String HOT_FILED_SEARCHKEYWORD= "searchkeyword";//热词
		public static final String HOT_FILED_SEARCHPINYIN= "searchpinyin";//全拼
		public static final String HOT_FILED_SEARCHPY= "searchpy";//简拼
		public static final String HOT_FILED_ONCE= "once";//次数
		
		public static final String SEARCH_SYS_START= "start";//启用
		public static final String SEARCH_SYS_STOP= "stop";//停用
		
		public static final String SEARCH_INDEX_TYPE_VIEW_A= "1a";//视图a 服务
		public static final String SEARCH_INDEX_TYPE_VIEW_B= "1b";//视图b 服务
		public static final String SEARCH_INDEX_TYPE_TUI_A= "2";//推送 ftp
		public static final String SEARCH_INDEX_TYPE_HOT= "5";//热词库
		
		public static final String SEARCH_TUI_OPT_DELTET= "delete";//推送删除
		public static final String SEARCH_TUI_OPT_UPDATE= "update";//推送更新
		public static final String SEARCH_TUI_OPT_ADD= "add";//推送新增
		public static final String SEARCH_TUI_OPT= "opt";//操作key
		public static final String SEARCH_TUI_APPCODE= "appcode";//注册CODE
		public static final String SEARCH_TUI_COMMANDCODE= "commandcode";//业务类型
		//是否智能提示
		public static final String IS_INTELLISENSE_TRUE = "1";
		public static final String IS_INTELLISENSE_false = "2";
		
		//是否url选项
		public static final String IS_URL_TRUE = "1";
		public static final String IS_URL_FALSE = "2";
		
		//智能提示的自动加入三个后缀
		public static final String INTELLISENSE_ZH = "_ZH";
		public static final String INTELLISENSE_PY = "_PY";
		public static final String INTELLISENSE_PINYIN = "_PINYIN";
		public static final String INTELLISENSE_PY_FC = "_PY_FC";
		public static final String INTELLISENSE_PINYIN_FC = "_PINYIN_FC";
		//lucene-db-文件夹地址
		public static final String DB_LUCENE_PATH = "db_lucene__tables"; 
		//public static final String DB_LUCENE_PATH = "E:\\db_lucene__tables"; ----
		
		//访问ws传递参数名
		public static final String CALL_WS_USERNAME = "username"; 
		public static final String CALL_WS_PWD = "pwd"; 
		public static final String CALL_WS_PAGENO = "pageno"; 
		
		//FTP文件备份路径
		public static final String SEARCH_FTP_DIR = "/_bank/";
		//FTP类型
		public static final String SEARCH_COMMAND_FTP = "1";
		public static final String SEARCH_COMMAND_SFTP = "2";
		
		public static final String SESSION_USERCODE = "usercode";
		public static final String SESSION_APPCOCE = "appcode";
	}
}
