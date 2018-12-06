package com.sinovatech.search.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SftpUtil {
	private static final Logger log = LoggerFactory.getLogger(SftpUtil.class);
	private Session session;
	private Channel channel;

	/**
	 * 对外可访问 ChannelSftp对象提供的所有底层方法
	 */
	private ChannelSftp chnSftp;

	/**
	 * 配置的远程目录地址
	 */
	private String cfgRemotePath;

	/**
	 * 配置的远程目录历史地址
	 */
	private String cfgRemotePathHis;

	/**
	 * 文件类型
	 */
	private static final int FILE_TYPE = 1;

	/**
	 * 目录类型
	 */
	private static final int DIR_TYPE = 2;

	/**
	 * FTP服务器IP
	 */
	private String host;

	/**
	 * FTP服务器用户名
	 */
	private String username;

	/**
	 * FTP服务器密码
	 */
	private String password;

	/**
	 * FTP服务器端口 String
	 */
	@SuppressWarnings("unused")
	private String port;

	/**
	 * FTP服务器端口 int
	 */
	private int portInt;

	/**
	 * 配置的本地地址
	 */
	private String cfgLocalPath;

	/**
	 * 配置的本地历史地址
	 */
	private String cfgLocalPathHis;

	/**
	 * 配置的本地临时地址
	 */
	private String cfgLocalPathTemp;

	// public SftpUtil() {
	//
	// }

	public SftpUtil() {
		setHost("10.143.131.53");
		setPortInt(22);
		setUsername("weblogic");
		setPassword("yqMj8bvibm");
		cfgRemotePath = "";
		cfgRemotePathHis = "";
		cfgLocalPath = "";
		cfgLocalPathHis = "";
		cfgLocalPathTemp = "";
		cfgRemotePath = "/";// test
	}

	/**
	 * 通过配置 打开sftp连接资源
	 * 
	 * @author elvman
	 * @version 1.2
	 * @since 2013.09.24
	 * @throws JSchException
	 * @throws SftpException
	 */
	public void open() throws JSchException, SftpException {
		this.connect(this.getHost(), this.getPortInt(), this.getUsername(),
				this.getPassword());
	}

	/**
	 * 连接SFTP
	 * 
	 * @author elvman
	 * @version 1.2
	 * @since 2013.09.24
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @throws JSchException
	 * @throws SftpException
	 */
	protected void connect(String host, int port, String username,
			String password) throws JSchException, SftpException {
		JSch jsch = new JSch();
		session = jsch.getSession(username, host, port);
		// //System.out.println("Session created.");
		session.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		session.connect();
		// //System.out.println("Session connected.");
		channel = session.openChannel("sftp");
		// //System.out.println("Channel is Opened!");
		channel.connect();
		// //System.out.println("Channel is connected!");
		chnSftp = (ChannelSftp) channel;
		// //System.out.println("Connected to " + host + "  is sucess!");
	}

	/**
	 * 进入指定的目录并设置为当前目录
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpPath
	 * @throws Exception
	 */
	public void cd(String sftpPath) throws SftpException {
		chnSftp.cd(sftpPath);
	}

	/**
	 * 得到当前用户当前工作目录地址
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @return 返回当前工作目录地址
	 * 
	 */
	public String pwd() {
		try {
			return chnSftp.pwd();
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据目录地址,文件类型返回文件或目录列表
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param directory
	 *            如:/home/elvman/kftesthis/201006/08/
	 * @param fileType
	 *            如：FILE_TYPE或者DIR_TYPE
	 * @return 文件或者目录列表 List
	 * @throws SftpException
	 * @throws Exception
	 * 
	 */
	public List<String> listFiles(String directory, int fileType)
			throws SftpException {
		List<String> fileList = new ArrayList<String>();
		if (isDirExist(directory)) {
			boolean itExist = false;
			@SuppressWarnings("rawtypes")
			Vector vector;
			vector = chnSftp.ls(directory);
			for (int i = 0; i < vector.size(); i++) {
				Object obj = vector.get(i);
				String str = obj.toString().trim();
				int tag = str.lastIndexOf(":") + 3;
				String strName = str.substring(tag).trim();
				itExist = isDirExist(directory + "/" + strName);
				if (fileType == FILE_TYPE) {
					if (!(itExist)) {
						fileList.add(directory + "/" + strName);
					}
				}
				if (fileType == DIR_TYPE) {
					if (itExist) {
						// 目录列表中去掉目录名为.和..
						if (!(strName.equals(".") || strName.equals(".."))) {
							fileList.add(directory + "/" + strName);
						}
					}
				}

			}
		}
		return fileList;
	}

	/**
	 * 判断目录是否存在
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param directory
	 * @return
	 * @throws SftpException
	 */
	public boolean isDirExist(String directory) throws SftpException {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = chnSftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			// e.printStackTrace();
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = false;
			}
		}
		return isDirExistFlag;
	}

	public boolean dirExist(String directory) throws SftpException {
		boolean isDirExistFlag = false;
		try {
			SftpATTRS sftpATTRS = chnSftp.lstat(directory);
			isDirExistFlag = true;
			return sftpATTRS.isDir();
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().equals("no such file")) {
				isDirExistFlag = true;
				chnSftp.mkdir(directory);
			}
		}
		return isDirExistFlag;
	}

	/**
	 * 下载文件后返回流文件
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public InputStream getFile(String sftpFilePath) throws SftpException {
		if (isFileExist(sftpFilePath)) {
			return chnSftp.get(sftpFilePath);
		}
		return null;
	}

	/**
	 * 获取远程文件流
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public InputStream getInputStreamFile(String sftpFilePath)
			throws SftpException {
		return getFile(sftpFilePath);
	}

	/**
	 * 获取远程文件字节流
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 * @throws IOException
	 */
	public ByteArrayInputStream getByteArrayInputStreamFile(String sftpFilePath)
			throws SftpException, IOException {
		if (isFileExist(sftpFilePath)) {
			byte[] srcFtpFileByte = inputStreamToByte(getFile(sftpFilePath));
			ByteArrayInputStream srcFtpFileStreams = new ByteArrayInputStream(
					srcFtpFileByte);
			return srcFtpFileStreams;
		}
		return null;
	}

	/**
	 * 删除远程 说明:返回信息定义以:分隔第一个为代码，第二个为返回信息
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public String delFile(String sftpFilePath) throws SftpException {
		String retInfo = "";
		if (isFileExist(sftpFilePath)) {
			chnSftp.rm(sftpFilePath);
			retInfo = "1:File deleted.";
		} else {
			retInfo = "2:Delete file error,file not exist.";
		}
		return retInfo;
	}

	/**
	 * 移动远程文件到目标目录
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param srcSftpFilePath
	 * @param distSftpFilePath
	 * @return 返回移动成功或者失败代码和信息
	 * @throws SftpException
	 * @throws IOException
	 */
	public String moveFile(String srcSftpFilePath, String distSftpFilePath)
			throws SftpException, IOException {
		String retInfo = "";
		boolean dirExist = false;
		boolean fileExist = false;
		fileExist = isFileExist(srcSftpFilePath);
		// dirExist = isDirExist(distSftpFilePath);
		dirExist = dirExist(distSftpFilePath);
		if (!fileExist) {
			// 文件不存在直接反回.
			return "0:file not exist !";
		}
		if (!(dirExist)) {
			// 1建立目录
			createDir(distSftpFilePath);
			// 2设置dirExist为true
			dirExist = true;
		}
		if (dirExist && fileExist) {

			String fileName = srcSftpFilePath.substring(
					srcSftpFilePath.lastIndexOf("/"), srcSftpFilePath.length());
			ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
			// 二进制流写文件
			this.chnSftp.put(srcFtpFileStreams, distSftpFilePath + fileName);
			this.chnSftp.rm(srcSftpFilePath);
			retInfo = "1:move success!";
		}
		return retInfo;
	}

	/**
	 * 复制远程文件到目标目录
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param srcSftpFilePath
	 * @param distSftpFilePath
	 * @return
	 * @throws SftpException
	 * @throws IOException
	 */
	public String copyFile(String srcSftpFilePath, String distSftpFilePath)
			throws SftpException, IOException {
		String retInfo = "";
		boolean dirExist = false;
		boolean fileExist = false;
		fileExist = isFileExist(srcSftpFilePath);
		dirExist = isDirExist(distSftpFilePath);
		if (!fileExist) {
			// 文件不存在直接反回.
			return "0:file not exist !";
		}
		if (!(dirExist)) {
			// 1建立目录
			createDir(distSftpFilePath);
			// 2设置dirExist为true
			dirExist = true;
		}
		if (dirExist && fileExist) {

			String fileName = srcSftpFilePath.substring(
					srcSftpFilePath.lastIndexOf("/"), srcSftpFilePath.length());
			ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(srcSftpFilePath);
			// 二进制流写文件
			this.chnSftp.put(srcFtpFileStreams, distSftpFilePath + fileName);
			retInfo = "1:copy file success!";
		}
		return retInfo;
	}

	/**
	 * 创建远程目录
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpDirPath
	 * @return 返回创建成功或者失败的代码和信息
	 * @throws SftpException
	 */
	public String createDir(String sftpDirPath) throws SftpException {
		this.cd("/");
		if (this.isDirExist(sftpDirPath)) {
			return "0:dir is exist !";
		}
		String pathArry[] = sftpDirPath.split("/");
		for (String path : pathArry) {
			if (path.equals("")) {
				continue;
			}
			if (isDirExist(path)) {
				this.cd(path);
			} else {
				// 建立目录
				this.chnSftp.mkdir(path);
				// 进入并设置为当前目录
				this.chnSftp.cd(path);
			}
		}
		this.cd("/");
		return "1:创建目录成功";
	}

	/**
	 * 判断远程文件是否存在
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param srcSftpFilePath
	 * @return
	 * @throws SftpException
	 */
	public boolean isFileExist(String srcSftpFilePath) throws SftpException {
		boolean isExitFlag = false;
		// 文件大于等于0则存在文件
		if (getFileSize(srcSftpFilePath) >= 0) {
			isExitFlag = true;
		}
		return isExitFlag;
	}

	/**
	 * 得到远程文件大小
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @see 返回文件大小
	 * @param srcSftpFilePath
	 * @return 返回文件大小，如返回-2 文件不存在，-1文件读取异常
	 * @throws SftpException
	 */
	public long getFileSize(String srcSftpFilePath) throws SftpException {
		long filesize = 0;// 文件大于等于0则存在
		try {
			SftpATTRS sftpATTRS = chnSftp.lstat(srcSftpFilePath);
			filesize = sftpATTRS.getSize();
		} catch (Exception e) {
			filesize = -1;// 获取文件大小异常
			if (e.getMessage().toLowerCase().equals("no such file")) {
				filesize = -2;// 文件不存在
			}
		}
		return filesize;
	}

	/**
	 * 关闭资源
	 */
	public void close() {
		if (null != channel && channel.isConnected()) {
			channel.disconnect();
		}
		if (null != session && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * inputStream类型转换为byte类型
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param iStrm
	 * @return
	 * @throws IOException
	 */
	public byte[] inputStreamToByte(InputStream iStrm) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch = 0;
		while ((ch = iStrm.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	public List<String> getRemoteDirFileNames(String dir) {
		List<String> names = new ArrayList<String>();
		try {
			List<String> fileList = this.listFiles(dir, SftpUtil.FILE_TYPE);
			for (String fileName : fileList) {
				int index = 0;
				// index = fileName.lastIndexOf(File.separator);
				if (fileName.contains("/")) {
					index = fileName.lastIndexOf("/");
				} else if (fileName.contains("\\")) {
					index = fileName.lastIndexOf("\\");
				} else {
					return null;
				}
				String name = fileName.substring(index + 1, fileName.length());
				names.add(name);
			}
		} catch (SftpException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return names;
	}

	public boolean downloadFile(String remoteDir, String localDir) {
		List<String> excelFileNames = new ArrayList<String>();
		List<String> okFileNames = new ArrayList<String>();
		List<String> fileNames = this.getRemoteDirFileNames(remoteDir);
		if (null == fileNames || fileNames.size() <= 0) {
			log.info("无可下载文件");
			return false;
		}
		for (String fileName : fileNames) {
			if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
				excelFileNames.add(fileName);
			} else if (fileName.endsWith(".txt")) {
				okFileNames.add(fileName);
			}
		}
		if (null == okFileNames || okFileNames.size() <= 0) {
			log.info("无OK.txt文件不能下载");
			return false;
		}
		try {
			long size = this.getFileSize(remoteDir + "/" + okFileNames.get(0));
			if (size <= 0) {
				log.info("文件OK.txt大小为0不能下载");
				return false;
			}
		} catch (SftpException e) {
			e.printStackTrace();
			log.info("获取文件OK.txt大小异常" + e);
			return false;
		}
		if (null == excelFileNames || excelFileNames.size() <= 0) {
			log.info("无可下载数据文件");
			return false;
		}

		for (String name : excelFileNames) {
			download(name, remoteDir, localDir);
		}
		for (String name : okFileNames) {
			download(name, remoteDir, localDir);
		}
		for (String name : fileNames) {
			// backupFile(name, remoteDir, localDir);
			backupFileByCmd(name, remoteDir, localDir);
		}
		return true;
	}

	private void backupFileByCmd(String fileName, String remoteDir,
			String localDir) {
		String filePath = "";
		if (StringUtils.isBlank(fileName)) {
			return;
		}
		if (fileName.startsWith("/")) {
			filePath = remoteDir + fileName;
		} else {
			filePath = remoteDir + "/" + fileName;
		}
		try {
			log.info("正在备份远程文件：" + remoteDir + "/" + fileName
					+ " 请耐心等待，请勿中断程序或进程...");
			ShellUtils.execCmd("mv " + filePath + " " + remoteDir + "/bak",
					username, password, host, portInt);
			log.info("文件：" + remoteDir + "/" + fileName + " 备份完成.");
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void backupFile(String fileName, String remoteDir, String localDir) {
		try {
			log.info("正在备份远程文件：" + remoteDir + "/" + fileName
					+ " 请耐心等待，请勿中断程序或进程...");
			this.moveFile(remoteDir + "/" + fileName, remoteDir + "/bak");
			log.info("文件：" + remoteDir + "/" + fileName + " 备份完成.");
		} catch (SftpException e) {
			e.printStackTrace();
			log.info("文件：" + remoteDir + "/" + fileName + " 备份失败.");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("文件：" + remoteDir + "/" + fileName + " 备份失败.");
		}
	}

	private boolean download(String fileName, String remoteDir, String localDir) {
		InputStream stream = null;
		try {
			if (FileUtil.createFile(localDir, fileName)) {
				stream = this.getFile(remoteDir + "/" + fileName);
				int flag = FileUtil.writeFile(stream, localDir, fileName);
				if (flag > 0) {
					log.info(remoteDir + File.separator + fileName + "文件下载完成");
				} else {
					log.info(remoteDir + File.separator + fileName + "文件下载失败");
					return false;
				}
			}
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (null != stream) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private boolean download(List<String> fileNames, String remoteDir,
			String localDir) {
		InputStream stream = null;
		try {
			for (String name : fileNames) {
				if (FileUtil.createFile(localDir, name)) {
					stream = this.getFile(remoteDir + "/" + name);
					int flag = FileUtil.writeFile(stream, localDir, name);
					if (flag > 0) {
						log.info(remoteDir + File.separator + name + "文件下载完成");
					} else {
						log.info(remoteDir + File.separator + name + "文件下载失败");
						return false;
					}
					try {
						log.info("正在备份远程文件：" + remoteDir + "/" + name
								+ " 请耐心等待，请勿中断程序或进程...");
						this.moveFile(remoteDir + "/" + name, remoteDir
								+ "/bak");
						log.info("文件：" + remoteDir + "/" + name + " 备份完成.");
					} catch (SftpException e) {
						e.printStackTrace();
						log.info("文件：" + remoteDir + "/" + name + " 备份失败.");
						return false;
					} catch (IOException e) {
						e.printStackTrace();
						log.info("文件：" + remoteDir + "/" + name + " 备份失败.");
						return false;
					}
				}
			}
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (null != stream) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public String getCfgRemotePath() {
		return cfgRemotePath;
	}

	public void setCfgRemotePath(String cfgRemotePath) {
		this.cfgRemotePath = cfgRemotePath;
	}

	public String getCfgRemotePathHis() {
		return cfgRemotePathHis;
	}

	public void setCfgRemotePathHis(String cfgRemotePathHis) {
		this.cfgRemotePathHis = cfgRemotePathHis;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		if (StringUtils.isNotBlank(port)) {
			this.setPortInt(Integer.parseInt(port));
		}
	}

	public String getCfgLocalPath() {
		return cfgLocalPath;
	}

	public void setCfgLocalPath(String cfgLocalPath) {
		this.cfgLocalPath = cfgLocalPath;
	}

	public String getCfgLocalPathHis() {
		return cfgLocalPathHis;
	}

	public void setCfgLocalPathHis(String cfgLocalPathHis) {
		this.cfgLocalPathHis = cfgLocalPathHis;
	}

	public String getCfgLocalPathTemp() {
		return cfgLocalPathTemp;
	}

	public void setCfgLocalPathTemp(String cfgLocalPathTemp) {
		this.cfgLocalPathTemp = cfgLocalPathTemp;
	}

	public int getPortInt() {
		return portInt;
	}

	public void setPortInt(int portInt) {
		this.portInt = portInt;
	}

	public static void main(String[] args) {
		SftpUtil sftpTools = null;
		try {
			sftpTools = new SftpUtil();
			sftpTools.open();
			String remoteDir = "/app/weblogic/testftp";
			sftpTools.downloadFile(remoteDir, "D:/testss/testt");
			// sftpTools.moveFile(remoteDir+"/116114ticket2013101414-bak20131014023659.xls",
			// remoteDir+"/bak");
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sftpTools != null) {
				sftpTools.close();
			}

		}

	}
}
