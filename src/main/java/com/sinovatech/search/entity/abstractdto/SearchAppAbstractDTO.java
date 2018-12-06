package com.sinovatech.search.entity.abstractdto;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 实体对象，注册应用业务表， 对应表:SEARCH_APP
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:2014-11-14 13:25:04<br />
 * @author liuzhenquan
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
import java.util.Date;
import com.sinovatech.search.entity.common.DtoSupport;

public abstract class SearchAppAbstractDTO extends DtoSupport {

	public SearchAppAbstractDTO() {
	}

	public SearchAppAbstractDTO(java.lang.String id) {
		this.setId(id);
	}

	// 唯一标识
	private String id;
	// 注册业务应用名称
	private String appName;
	// 注册应用编码
	private String appCode;
	// 索引路径
	private String indexPath;
	// 索引方式:1推送2视图3ftp4服务
	private String indexType;
	// 当前索引文件夹:dateindex和dateindexbak
	private String currentDir;
	// 定时重建索引（单位分钟）
	private Long loopCindexTime;
	// 同步脚本
	private String shellPath;
	// 状态：1启用2停用
	private String state;
	// 注册时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 操作人
	private String operator;
	// 是否删除(隐藏)
	private String isDelete;
	// 是否需要重启
	private String isReset;
	//IP规则
	private String IPS;
	//分词器类型 1:IKAnalyzer,2:StandardAnalyzer
	private String analyzerType;
	
	public String getAnalyzerType() {
		return analyzerType;
	}

	public void setAnalyzerType(String analyzerType) {
		this.analyzerType = analyzerType;
	}

	public String getIPS() {
		return IPS;
	}

	public void setIPS(String iPS) {
		IPS = iPS;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getIsReset() {
		return isReset;
	}

	public void setIsReset(String isReset) {
		this.isReset = isReset;
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
	 * get注册业务应用名称
	 * 
	 * @return 注册业务应用名称
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * se注册业务应用名称
	 * 
	 * @param 注册业务应用名称
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * get注册应用编码
	 * 
	 * @return 注册应用编码
	 */
	public String getAppCode() {
		return appCode;
	}

	/**
	 * se注册应用编码
	 * 
	 * @param 注册应用编码
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * get索引路径
	 * 
	 * @return 索引路径
	 */
	public String getIndexPath() {
		return indexPath;
	}

	/**
	 * se索引路径
	 * 
	 * @param 索引路径
	 */
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	/**
	 * get索引方式:1推送2视图
	 * 
	 * @return 索引方式:1推送2视图
	 */
	public String getIndexType() {
		return indexType;
	}

	/**
	 * se索引方式:1推送2视图
	 * 
	 * @param 索引方式
	 *            :1推送2视图
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	/**
	 * get当前索引文件夹:dateindex和dateindexbak
	 * 
	 * @return 当前索引文件夹:dateindex和dateindexbak
	 */
	public String getCurrentDir() {
		return currentDir;
	}

	/**
	 * se当前索引文件夹:dateindex和dateindexbak
	 * 
	 * @param 当前索引文件夹
	 *            :dateindex和dateindexbak
	 */
	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}

	/**
	 * get定时重建索引（单位分钟）
	 * 
	 * @return 定时重建索引（单位分钟）
	 */
	public Long getLoopCindexTime() {
		return loopCindexTime;
	}

	/**
	 * se定时重建索引（单位分钟）
	 * 
	 * @param 定时重建索引
	 *            （单位分钟）
	 */
	public void setLoopCindexTime(Long loopCindexTime) {
		this.loopCindexTime = loopCindexTime;
	}

	/**
	 * get同步脚本
	 * 
	 * @return 同步脚本
	 */
	public String getShellPath() {
		return shellPath;
	}

	/**
	 * se同步脚本
	 * 
	 * @param 同步脚本
	 */
	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}

	/**
	 * get状态：1启用2停用
	 * 
	 * @return 状态：1启用2停用
	 */
	public String getState() {
		return state;
	}

	/**
	 * se状态：1启用2停用
	 * 
	 * @param 状态
	 *            ：1启用2停用
	 */
	public void setState(String state) {
		this.state = state;
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
