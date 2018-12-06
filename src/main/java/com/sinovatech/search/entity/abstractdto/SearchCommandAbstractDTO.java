package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，业务数据类型表， 对应表:SEARCH_COMMAND
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:06<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
import java.util.Date;

import com.sinovatech.search.entity.common.DtoSupport;

public abstract class SearchCommandAbstractDTO extends DtoSupport {

	public SearchCommandAbstractDTO() {
	}

	public SearchCommandAbstractDTO(java.lang.String id) {
		this.setId(id);
	}

	// 唯一标识
	private String id;
	// 业务应用id
	private String appId;
	// 业务应用编码
	private String appCode;
	//业务数据名称
	private String commandName;
	// 业务数据分类编码
	private String commandCode;
	// 视图名称
	private String viewName;
	// 视图查询条件
	private String sqlWhere;
	// 注册时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 操作人
	private String operator;
	// 用户名
	private String userName;
	// 密码
	private String passWord;
	// 链接地址
	private String linkAddress;
	// 留待扩展
	private String extend;
	//  1：ftp 2：SFTP
	private String ftpType;
	//  本地存放路径
	private String localAddress;
	// 远程目录
	private String linkDir;
	//端口号
	private String port;
	
	//是否执行存储过程
	private String isExePro;
	//存储过程名称
	private String proName;
	
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getIsExePro() {
		return isExePro;
	}

	public void setIsExePro(String isExePro) {
		this.isExePro = isExePro;
	}

	
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLinkDir() {
		return linkDir;
	}

	public void setLinkDir(String linkDir) {
		this.linkDir = linkDir;
	}

	public String getFtpType() {
		return ftpType;
	}

	public void setFtpType(String ftpType) {
		this.ftpType = ftpType;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
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
	 * get业务数据分类编码
	 * 
	 * @return 业务数据分类编码
	 */
	public String getCommandCode() {
		return commandCode;
	}

	/**
	 * se业务数据分类编码
	 * 
	 * @param 业务数据分类编码
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	/**
	 * get索引方式:1推送2视图
	 * 
	 * @return 索引方式:1推送2视图
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * se索引方式:1推送2视图
	 * 
	 * @param 索引方式
	 *            :1推送2视图
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * get定时重新创建索引:单位是分钟
	 * 
	 * @return 定时重新创建索引:单位是分钟
	 */
	public String getSqlWhere() {
		return sqlWhere;
	}

	/**
	 * se定时重新创建索引:单位是分钟
	 * 
	 * @param 定时重新创建索引
	 *            :单位是分钟
	 */
	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
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
	   public String getPKfiledName() 
	  	{
	  		return "id";
	  	}
}
