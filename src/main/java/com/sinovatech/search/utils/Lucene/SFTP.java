package com.sinovatech.search.utils.Lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.utils.ShellUtils;

public class SFTP extends FTPAbstract{
	
	@Override
	public void handle(String url, String username, String password, String remotePath, String localPath) {
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(SFTP.class);
	private Session session;
	private Channel channel;
	/**
	 * 对外可访问 ChannelSftp对象提供的所有底层方法
	 */
	private ChannelSftp chnSftp;
	
	/**
	 * 文件移动
	 */
	@Override
	public boolean rename(FTPClient ftp, String remotePath) {
		boolean dirExist = false;
		boolean fileExist = false;
		try {
			fileExist = isFileExist(remotePath);
			// dirExist = isDirExist(distSftpFilePath);
			String distSftpFilePath = remotePath + RedisKeyConst.Search.SEARCH_FTP_DIR;
			dirExist = dirExist(RedisKeyConst.Search.SEARCH_FTP_DIR);
			if (!fileExist) {
				log.error("目标文件不存在!!!");
				return false;
			}
			if (!(dirExist)) {
				// 1建立目录
				createDir(distSftpFilePath);
				// 2设置dirExist为true
				dirExist = true;
			}
			if (dirExist && fileExist) {
				String fileName = remotePath.substring(remotePath.lastIndexOf("/"), remotePath.length());
				ByteArrayInputStream srcFtpFileStreams = getByteArrayInputStreamFile(remotePath);
				// 二进制流写文件
				this.chnSftp.put(srcFtpFileStreams, distSftpFilePath + fileName);
				this.chnSftp.rm(remotePath);
			}
		} catch (Exception e) {
			log.error("SFTP文件移动失败!!!", e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	@Override
	public boolean down(FTPClient ftp, SearchCommandDTO dto) {
		List<String> fileNames = this.getRemoteDirFileNames(dto.getLinkDir());
		if (null == fileNames || fileNames.size() <= 0) {
			log.info("无可下载文件");
			return false;
		}
		for (String name : fileNames) {
			// backupFile(name, remoteDir, localDir);
			log.info("开始下载文件:" + name);
			try {
				getFile(dto, name);
				log.info("文件下载成功!");
				backupFileByCmd(name, dto);
			} catch (Exception e) {
				log.error("SFTP下载文件出错：" , e);
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public FTPClient login(SearchCommandDTO dto) {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(dto.getUserName(), dto.getLinkAddress(), Integer.parseInt(dto.getPort()));
			session.setPassword(dto.getPassWord());
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			session.setConfig(sshConfig);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			chnSftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			log.error("SFTP登录出错!!!", e);
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public void logout(FTPClient ftp) {
		
	}
	
	private boolean dirExist(String directory) throws SftpException {
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
	 * 判断远程文件是否存在
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param srcSftpFilePath
	 * @return
	 * @throws SftpException
	 */
	private boolean isFileExist(String srcSftpFilePath) throws SftpException {
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
	private long getFileSize(String srcSftpFilePath) throws SftpException {
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
	 * 创建远程目录
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpDirPath
	 * @return 返回创建成功或者失败的代码和信息
	 * @throws SftpException
	 */
	private String createDir(String sftpDirPath) throws SftpException {
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
	
	private void cd(String sftpPath) throws SftpException {
		chnSftp.cd(sftpPath);
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
	private boolean isDirExist(String directory) throws SftpException {
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
	private ByteArrayInputStream getByteArrayInputStreamFile(String sftpFilePath)
			throws SftpException, IOException {
		if (isFileExist(sftpFilePath)) {
			byte[] srcFtpFileByte = inputStreamToByte(getFile(null ,sftpFilePath));
			ByteArrayInputStream srcFtpFileStreams = new ByteArrayInputStream(
					srcFtpFileByte);
			return srcFtpFileStreams;
		}
		return null;
	}
	
	/**
	 * 下载文件后返回流文件
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param sftpFilePath
	 * @return
	 * @throws SftpException
	 * @throws FileNotFoundException 
	 */
	private InputStream getFile(SearchCommandDTO dto, String fileName) throws SftpException, FileNotFoundException {
		
		this.chnSftp.cd(dto.getLinkDir());
		File file = new File(dto.getLocalAddress() + File.separator + fileName);
		this.chnSftp.get(dto.getLinkDir() + File.separator + fileName, new FileOutputStream(file)); 
		return null;
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
	private byte[] inputStreamToByte(InputStream iStrm) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch = 0;
		while ((ch = iStrm.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}
	/**
	 * 获得指定文件夹下的所有文件
	 * @param dir
	 * @return
	 */
	private List<String> getRemoteDirFileNames(String dir) {
		List<String> names = new ArrayList<String>();
		try {
			List<String> fileList = this.listFiles(dir, 1);
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
	
	/**
	 * 根据目录地址,文件类型返回文件或目录列表
	 * 
	 * @author elvman
	 * @since 2012.02.09
	 * @param directory
	 *            如:/home/elvman/kftesthis/201006/08/
	 * @param fileType
	 * @return 文件或者目录列表 List
	 * @throws SftpException
	 * @throws Exception
	 * 
	 */
	private List<String> listFiles(String directory, int fileType)
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
				if (fileType == 1) {
					if (!(itExist)) {
						fileList.add(directory + "/" + strName);
					}
				}
				if (fileType == 2) {
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
	private void backupFileByCmd(String fileName, SearchCommandDTO dto) {
		String filePath = "";
		if (StringUtils.isBlank(fileName)) {
			return;
		}
		if (fileName.startsWith("/")) {
			filePath = dto.getLinkDir() + fileName;
		} else {
			filePath = dto.getLinkDir() + "/" + fileName;
		}
		try {
			log.info("正在备份远程文件：" + dto.getLinkDir() + "/" + fileName
					+ " 请耐心等待，请勿中断程序或进程...");
			createDir(dto.getLinkDir() + RedisKeyConst.Search.SEARCH_FTP_DIR);
			ShellUtils.execCmd("mv " + filePath + " " + dto.getLinkDir() + RedisKeyConst.Search.SEARCH_FTP_DIR,
					dto.getUserName(), dto.getPassWord(), dto.getLinkAddress(), Integer.parseInt(dto.getPort()));
			log.info("文件：" + dto.getLinkDir() + "/" + fileName + " 备份完成.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
