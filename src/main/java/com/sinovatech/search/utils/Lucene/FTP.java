package com.sinovatech.search.utils.Lucene;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.entity.SearchCommandDTO;

public class FTP extends FTPAbstract{
	private static final Log log = org.apache.commons.logging.LogFactory.getLog(FTP.class);
	
	/**
	 * 移动
	 */
	@Override
	public boolean rename(FTPClient ftp,String remotePath) {
		try {
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			ftp.makeDirectory("/" + remotePath + RedisKeyConst.Search.SEARCH_FTP_DIR);
			FTPFile[] file = ftp.listFiles();
			for (FTPFile ff : file) {
				ftp.rename(ff.getName(), "/" + remotePath + RedisKeyConst.Search.SEARCH_FTP_DIR + ff.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != ftp && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 下载
	 */
	@Override
	public boolean down(FTPClient ftp, SearchCommandDTO dto) {
		log.info("开始下载文件");
		try {
			ftp.changeWorkingDirectory(dto.getLinkDir());// 转移到FTP服务器目录
			FTPFile[] fs;
			fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				String fileName = ff.getName();
				int num = fileName.indexOf(".");
				if(num > 0) {
					log.info("开始下载文件" + fileName);
					File localFile = new File(dto.getLocalAddress() + File.separator + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			log.info("下载完成！");
		} catch (IOException e) {
			log.error("文件下载失败", e);
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public FTPClient login(SearchCommandDTO dto) {
		FTPClient ftp = new FTPClient();
		int reply;
		// ftp.connect(url, port);
		try {
			ftp.connect(dto.getLinkAddress(), Integer.parseInt(dto.getPort()));
			log.info("FTP链接成功");
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(dto.getUserName(), dto.getPassWord());// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				log.error("登录失败，帐号密码错误或服务器ftp服务没开");
				ftp.disconnect();
				return null;
			}
		} catch (Exception e) {
			log.error("登录失败!", e);
			e.printStackTrace();
		}
		return ftp;
	}
	@Override
	public void logout(FTPClient ftp) {
		try {
			ftp.logout();
		} catch (IOException e) {
			log.error("注销失败", e);
			e.printStackTrace();
		}
	}
	@Override
	public void handle(String url, String username, String password,
			String remotePath, String localPath) {
		
	}
}
