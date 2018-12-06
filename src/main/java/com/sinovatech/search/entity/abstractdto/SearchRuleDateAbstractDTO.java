package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，索引数据配置表， 对应表:SEARCH_RULE_DATE
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:11<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class SearchRuleDateAbstractDTO extends DtoSupport {

	public SearchRuleDateAbstractDTO() {
	}

	public SearchRuleDateAbstractDTO(java.lang.String id) {
		this.setId(id);
	}

	// 唯一标识
	private String id;
	// 业务应用id
	private String appId;
	// 业务应用编码
	private String appCode;
	// 字段名称
	private String fieldName;
	// 字段值类型1:String,2:int,3:float
	private String fileldDateType;
	// 字段索引类型1:不索引,2:不分词索引,3:分词索引
	private String fieldIndexType;
	// 字段值是否存储1:存储,2:不存储
	private String fieldStoreType;
	// 业务数据类型编码
	private String commandCode;
	// 业务数据分类名称
	private String commandName;
	// 注册时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 操作人
	private String operator;
	// 操作(推送辅助属性，不记入数据库)
	private String opt;
	//是否智能提示
	private String isIntelliSense;
	
	//是否URL选项  1 是 2是否
	private String isUrl;

	
	public String getIsUrl() {
		return isUrl;
	}

	public void setIsUrl(String isUrl) {
		this.isUrl = isUrl;
	}

	/**
	 * get是否只能提示
	 * 
	 * @return 是否只能提示
	 */
	public String getIsIntelliSense() {
		return isIntelliSense;
	}

	public void setIsIntelliSense(String isIntelliSense) {
		this.isIntelliSense = isIntelliSense;
	}

	/**
	 * get唯一标识
	 * 
	 * @return 唯一标识
	 */
	public String getId() {
		return id;
	}

	/**
	 * se唯一标识
	 * 
	 * @param 唯一标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get业务应用id
	 * 
	 * @return 业务应用id
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * se业务应用id
	 * 
	 * @param 业务应用id
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * get业务应用编码
	 * 
	 * @return 业务应用编码
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * se业务应用编码
	 * 
	 * @param 业务应用编码
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * get字段名称
	 * 
	 * @return 字段名称
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * se字段名称
	 * 
	 * @param 字段名称
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * get字段值类型1:String,2:int,3:float
	 * 
	 * @return 字段值类型1:String,2:int,3:float
	 */
	public String getFileldDateType() {
		return fileldDateType;
	}

	/**
	 * se字段值类型1:String,2:int,3:float
	 * 
	 * @param 字段值类型1
	 *            :String,2:int,3:float
	 */
	public void setFileldDateType(String fileldDateType) {
		this.fileldDateType = fileldDateType;
	}

	/**
	 * get字段索引类型1:不索引,2:不分词索引,3:分词索引
	 * 
	 * @return 字段索引类型1:不索引,2:不分词索引,3:分词索引
	 */
	public String getFieldIndexType() {
		return fieldIndexType;
	}

	/**
	 * se字段索引类型1:不索引,2:不分词索引,3:分词索引
	 * 
	 * @param 字段索引类型1
	 *            :不索引,2:不分词索引,3:分词索引
	 */
	public void setFieldIndexType(String fieldIndexType) {
		this.fieldIndexType = fieldIndexType;
	}

	/**
	 * get字段值是否存储1:存储,2:不存储
	 * 
	 * @return 字段值是否存储1:存储,2:不存储
	 */
	public String getFieldStoreType() {
		return fieldStoreType;
	}

	/**
	 * se字段值是否存储1:存储,2:不存储
	 * 
	 * @param 字段值是否存储1
	 *            :存储,2:不存储
	 */
	public void setFieldStoreType(String fieldStoreType) {
		this.fieldStoreType = fieldStoreType;
	}

	/**
	 * get业务数据类型编码
	 * 
	 * @return 业务数据类型编码
	 */
	public String getCommandCode() {
		return commandCode;
	}

	/**
	 * se业务数据类型编码
	 * 
	 * @param 业务数据类型编码
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	/**
	 * get注册时间
	 * 
	 * @return 注册时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * se注册时间
	 * 
	 * @param 注册时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * get修改时间
	 * 
	 * @return 修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * se修改时间
	 * 
	 * @param 修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * get操作人
	 * 
	 * @return 操作人
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * se操作人
	 * 
	 * @param 操作人
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	   public String getPKfiledName() 
	  	{
	  		return "id";
	  	}
}
