package com.sinovatech.search.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.search.constants.RedisKeyConst;

public class FtpUtil {

	/**
	 * 日志对象
	 */
	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//renamedownFile
		renamelist("192.168.1.40", 22, "anonymous", "xddxdd", "a",
				"beifen");
	}
	//把文件夹下的所有文件移动到指定文件夹下
	public static boolean renamelist(String url, int port, String username, String password, String remotePath, 
			String toPath) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url);
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.print("失败");
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			ftp.makeDirectory("/" + remotePath + toPath);
			FTPFile[] file = ftp.listFiles();
			for (FTPFile ff : file) {
				success = ftp.rename(ff.getName(), toPath + ff.getName());
				System.out.println(success);
			}
			ftp.logout();
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
		
		return success;
		
	}
	
	//移动备份
	public static boolean rename(String url, int port, String username, String password, String remotePath, 
			String fileName, String localPath) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// ftp.connect(url, port);
			ftp.connect(url);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.println("失败");
				ftp.disconnect();
				return success;
			}
			System.out.println(ftp.changeWorkingDirectory("/beifen"));// 转移到FTP服务器目录
			success = ftp.rename("aaa.txt", "/a/"+fileName);
			ftp.logout();
			System.out.print(success);
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
		return success;
	}
	
	public static String getDateStr() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	public static InputStream getUrlInputStream(String URLName)
			throws Exception {// URLName照片地址
		// target本地地址
		int HttpResult = 0; // 服务器返回的状态
		URL url = new URL(URLName); // 创建URL
		URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码urlconn.connect();
		HttpURLConnection httpconn = (HttpURLConnection) urlconn;
		HttpResult = httpconn.getResponseCode();
		// System.out.println(HttpResult);
		// 不等于HTTP_OK说明连接不成功System.out.print("fail");
		if (HttpResult != HttpURLConnection.HTTP_OK) {
			return null;
		} else {
			int filesize = urlconn.getContentLength(); // 取数据长度//System.out.println(filesize);
			log.debug("filesize========" + filesize);
			return urlconn.getInputStream();
		}
	}

	public static void getUrlImg(String URLName, String target)
			throws Exception {// URLName照片地址
		// target本地地址
		int HttpResult = 0; // 服务器返回的状态
		URL url = new URL(URLName); // 创建URL
		URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码urlconn.connect();
		HttpURLConnection httpconn = (HttpURLConnection) urlconn;
		HttpResult = httpconn.getResponseCode();
		// System.out.println(HttpResult);
		// 不等于HTTP_OK说明连接不成功System.out.print("fail");
		if (HttpResult != HttpURLConnection.HTTP_OK) {
		} else {
			int filesize = urlconn.getContentLength(); // 取数据长度//System.out.println(filesize);
			log.debug("filesize========" + filesize);
			BufferedInputStream bis = new BufferedInputStream(
					urlconn.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(target));
			byte[] buffer = new byte[1024]; // 创建存放输入流的缓冲
			int num = -1; // 读入的字节数
			while (true) {
				num = bis.read(buffer); // 读入到缓冲区
				if (num == -1) {
					bos.flush();
					break; // 已经读完
				}
				bos.flush();
				bos.write(buffer, 0, num);
			}
		}
	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url, int port, String username,
			String password, String path, String filename, String filePath) {
		// int HttpResult = 0; // 服务器返回的状态
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// InputStream input = getUrlInputStream(filePath);
			InputStream input = new FileInputStream(filePath);
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.print("qqqq");
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(path);
			// 设置以二进制方式传输
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// 设置被动模式
			ftp.enterLocalPassiveMode();
			boolean flag = ftp.storeFile(filename, input);
			log.debug("=========" + flag);

			input.close();
			ftp.logout();
			success = true;
			System.out.print("下载成功");
		} catch (Exception e) {
			System.out.print("下载失败");
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port, String username,
			String password, String remotePath, String localPath) {
		boolean success = false;
		log.info("开始下载文件");
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// ftp.connect(url, port);
			ftp.connect(url);
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				log.error("登录失败，无法下载资源");
				ftp.disconnect();
				return success;
			}
			FileUtil.createDir(localPath);
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				String fileName = ff.getName();
				int num = fileName.indexOf(".");
				if(fileName.substring(num).endsWith(".txt")){
					File localFile = new File(localPath + File.separator + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			ftp.logout();
			success = true;
			log.info("下载成功");
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
		return success;
	}
}

