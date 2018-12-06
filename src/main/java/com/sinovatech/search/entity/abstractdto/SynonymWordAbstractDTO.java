package com.sinovatech.search.entity.abstractdto;

import java.util.Date;

import com.sinovatech.search.entity.common.DtoSupport;

/**
 *
 * @author ChenZhuo
 * @date 2016年12月6日 下午5:01:26
 */
public abstract class SynonymWordAbstractDTO extends DtoSupport {

    private static final long serialVersionUID = 6698410189287069636L;
    
    public SynonymWordAbstractDTO() {
    }
    
    public SynonymWordAbstractDTO(String id) {
        this.setId(id);
    }
    
    // 唯一标识
    private String id;
    //同义词关键词
    private String keyWord;
    //同义词词组（以“，”分隔）
    private String synonymArray;
    // 注册业务应用名称
    private String appName;
    // 注册应用编码
    private String appCode;
    // 业务code--commandcode
    private String commandCode;
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

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the keyWord
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * @param keyWord the keyWord to set
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    /**
     * @return the synonymArray
     */
    public String getSynonymArray() {
        return synonymArray;
    }

    /**
     * @param synonymArray the synonymArray to set
     */
    public void setSynonymArray(String synonymArray) {
        this.synonymArray = synonymArray;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the appCode
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * @param appCode the appCode to set
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}

	/**
     * @return the indexPath
     */
    public String getIndexPath() {
        return indexPath;
    }

    /**
     * @param indexPath the indexPath to set
     */
    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    /**
     * @return the indexType
     */
    public String getIndexType() {
        return indexType;
    }

    /**
     * @param indexType the indexType to set
     */
    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    /**
     * @return the currentDir
     */
    public String getCurrentDir() {
        return currentDir;
    }

    /**
     * @param currentDir the currentDir to set
     */
    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    /**
     * @return the loopCindexTime
     */
    public Long getLoopCindexTime() {
        return loopCindexTime;
    }

    /**
     * @param loopCindexTime the loopCindexTime to set
     */
    public void setLoopCindexTime(Long loopCindexTime) {
        this.loopCindexTime = loopCindexTime;
    }

    /**
     * @return the shellPath
     */
    public String getShellPath() {
        return shellPath;
    }

    /**
     * @param shellPath the shellPath to set
     */
    public void setShellPath(String shellPath) {
        this.shellPath = shellPath;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return the isDelete
     */
    public String getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete the isDelete to set
     */
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    /**
     * 主键
     */
    public String getPKfiledName() {
        return "id";
    }

}
