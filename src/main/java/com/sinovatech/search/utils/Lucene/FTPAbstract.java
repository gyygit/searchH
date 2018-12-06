package com.sinovatech.search.utils.Lucene;

import org.apache.commons.net.ftp.FTPClient;

import com.sinovatech.search.entity.SearchCommandDTO;

public abstract class FTPAbstract {
	
	/**
	 * 移动
	 * @return
	 */
	public abstract  boolean rename(FTPClient ftp, String remotePath);
	
	/**
	 * 下载
	 * @return
	 */
	public abstract boolean down(FTPClient ftp, SearchCommandDTO dto);
	
	public abstract void handle(String url, String username, String password, String remotePath, String localPath);
	
	/**
	 * FTP登录
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public abstract FTPClient login(SearchCommandDTO searchCommandDTO);
	
	/**
	 * FTP注销
	 * @param ftp
	 */
	public abstract void logout(FTPClient ftp);
}
