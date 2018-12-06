package com.sinovatech.search.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.common.util.StringUtils;

/**
 * 文件操作工具
 * 
 * @author minghui.dai
 * 
 */
public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * copy单个文件
	 * 
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 */
	public static int copyFile(String oldFilePath, String newFilePath) {
		int byteSum = 0;
		int byteRead = 0;
		File oldFile = new File(oldFilePath);
		File newDir = new File(newFilePath);
		String fileName = getNewFileName(oldFilePath);
		if (!newDir.exists()) {
			newDir.mkdir();
		}
		if (oldFile.exists()) {
			InputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(oldFilePath);
				out = new FileOutputStream(newFilePath + File.separator
						+ fileName);
				byte[] buffer = new byte[2048];
				while ((byteRead = in.read(buffer)) != -1) {
					byteSum += byteRead;
					out.write(buffer, 0, byteRead);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.flush();
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return byteSum;
	}
	
	/**
	 * copy单个文件
	 * 
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 */
	public static String copyMyFile(String oldFilePath, String newFilePath) {
		int byteRead = 0;
		File oldFile = new File(oldFilePath);
		File newDir = new File(newFilePath);
		String fileName = getNewFileName(oldFilePath);
		if (!newDir.exists()) {
			newDir.mkdir();
		}
		if (oldFile.exists()) {
			InputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(oldFilePath);
				out = new FileOutputStream(newFilePath + File.separator
						+ fileName);
				byte[] buffer = new byte[2048];
				while ((byteRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, byteRead);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.flush();
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileName;
	}

	/**
	 * 写文件
	 * 
	 * @param dir
	 * @param fileName
	 */
	public static int writeFile(InputStream in, String dir, String fileName) {
		FileOutputStream out = null;
		int byteSum = 0;
		int byteRead = 0;
		try {
			out = new FileOutputStream(dir + File.separator + fileName);
			byte[] buffer = new byte[2048];
			while ((byteRead = in.read(buffer)) != -1) {
				byteSum += byteRead;
				out.write(buffer, 0, byteRead);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteSum;
	}

	/**
	 * 重命名文件
	 * 
	 * @param pathAndName
	 * @return
	 */
	public static boolean rename(String filePath) {
		int index = filePath.lastIndexOf(File.separator);
		String fileName = filePath.substring(index + 1, filePath.length());
		String path = filePath.substring(0, index);
		int indexTemp = fileName.lastIndexOf(".");
		String extendedName = fileName.substring(indexTemp, fileName.length());
		String tempName = fileName.substring(0, indexTemp);
		String newFileName = tempName + "-bak"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ extendedName;
		File oldFile = new File(filePath);
		File newFile = new File(path + File.separator + newFileName);
		return oldFile.renameTo(newFile);
	}

	/**
	 * copy整个目录
	 * 
	 * @param oldFilePath
	 * @param newFilePath
	 */
	public static void copyDirectory(String oldFilePath, String newFilePath) {
		File newFile = new File(newFilePath);
		File oldFile = new File(oldFilePath);
		if (oldFile.isDirectory() && !newFile.isDirectory()) {
			newFile.mkdir();
		}
		String[] files = oldFile.list();
		File tmp = null;
		String tmpPath = null;
		String newTmpPath = null;
		for (int i = 0; i < files.length; i++) {
			if (oldFilePath.endsWith(File.separator)) {
				tmpPath = oldFilePath + files[i];
			} else {
				tmpPath = oldFilePath + File.separator + files[i];
			}
			tmp = new File(tmpPath);
			String seprator = newFilePath.endsWith(File.separator) ? ""
					: File.separator;
			newTmpPath = newFilePath + seprator + files[i];
			if (tmp.isFile()) {
				copyFile(tmpPath, newTmpPath);
			} else if (tmp.isDirectory()) {
				copyDirectory(tmpPath, newTmpPath);
			}
		}
	}

	/**
	 * 删除整个目录
	 * 
	 * @param path
	 */
	public static boolean delDiectory(String path) {
		boolean flag = false;
		File file = new File(path);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
			return flag;
		}
		if (!file.isDirectory() || !file.exists()) {
			return flag;
		}
		String[] tmp = file.list();
		String newpath = null;
		for (int i = 0; i < tmp.length; i++) {
			if (path.endsWith(File.separator)) {
				newpath = path + tmp[i];
			} else {
				newpath = path + File.separator + tmp[i];
			}
			File tmpFile = new File(newpath);
			if (tmpFile.isFile()) {
				tmpFile.delete();
			} else if (tmpFile.isDirectory()) {
				delDiectory(newpath);
				tmpFile.delete();
			}
		}
		new File(path).delete();
		flag = true;
		return flag;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 读取目录下的文件名
	 * 
	 * @param fileDir
	 * @return
	 */
	public static List<String> readDirFileNames(String fileDir) {
		List<String> fileNames = new ArrayList<String>();
		File dir = new File(fileDir);
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				log.info((files[i].isDirectory() ? "目录：" : "文件：")
						+ files[i].getName());
				if (!files[i].isDirectory()) {
					fileNames.add(files[i].getName());
				}
			}
		}
		return fileNames;
	}

	public static List<String> readTxtFileByLine(String filePath,
			String encoding) {
		List<String> lineContentList = new ArrayList<String>();
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		if (StringUtils.isBlank(encoding)) {
			encoding = "UTF-8";
		}
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					lineContentList.add(lineTxt);
					//System.out.println(lineTxt);
				}
			} else {
				log.info("找不到指定的文件:" + filePath);
			}
		} catch (Exception e) {
			log.info("读取文件内容出错:" + filePath);
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
				}
				if (null != read) {
					read.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lineContentList;
	}

	/**
	 * 创建新文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(String filePath, String fileName) {
		boolean flag = false;
		createDir(filePath);
		File file = new File(filePath + File.separator + fileName);
		if (!file.exists()) {
			try {
				flag = file.createNewFile();
				if (flag) {
					log.info("创建文件:" + filePath + File.separator + fileName
							+ "成功!");
					return flag;
				} else {
					log.info("创建文件：" + filePath + File.separator + fileName
							+ "失败！");
					return flag;
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.info("创建文件：" + filePath + File.separator + fileName + "失败！"
						+ e);
				return flag;
			}
		} else {
			log.info("创建文件：" + filePath + File.separator + fileName
					+ "失败！文件已存在！");
			return flag;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param destDirName
	 * @return
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator))
			destDirName = destDirName + File.separator;
		if (dir.mkdirs()) {
			System.out.println("文件夹创建成功：" + destDirName);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 取文件扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getExtendedName(String filePath) {
		int index = filePath.lastIndexOf(File.separator);
		String fileName = filePath.substring(index + 1, filePath.length());
		int indexTemp = fileName.lastIndexOf(".");
		String extendedName = fileName.substring(indexTemp, fileName.length());
		return extendedName;
	}

	/**
	 * 生成新文件名
	 * 
	 * @param filePath
	 * @return
	 */
	private static String getNewFileName(String filePath) {
		int index = filePath.lastIndexOf(File.separator);
		String fileName = filePath.substring(index + 1, filePath.length());
		int indexTemp = fileName.lastIndexOf(".");
		String extendedName = fileName.substring(indexTemp, fileName.length());
		String tempName = fileName.substring(0, indexTemp);
		String newFileName = tempName + "-bak"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
				+ extendedName;
		return newFileName;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String file = "D:" + File.separator + "公共支撑" + File.separator +
		// "新建文件夹 (2)" + File.separator + "AOP(116114)" + File.separator +
		// "data" + File.separator + "特价机票数据模板.xlsx";
		// copyFile(file, "D:/公共支撑/新建文件夹 (2)/AOP(116114)/data/bak");
		// String file1 = "D:" + File.separator + "公共支撑" + File.separator +
		// "新建文件夹 (2)" + File.separator + "AOP(116114)" + File.separator +
		// "data" + File.separator + "bak" + File.separator + "特价机票数据模板.xlsx";
		// rename(file1);

		// String filet = "D:" + File.separator + "公共支撑" + File.separator +
		// "新建文件夹 (2)" + File.separator + "AOP(116114)" + File.separator +
		// "data" + File.separator;
		// readDirFileNames(filet);
		// String txtfile = "D:" + File.separator + "公共支撑" + File.separator +
		// "新建文件夹 (2)" + File.separator + "AOP(116114)" + File.separator +
		// "data" + File.separator + "test" + File.separator + "OK.txt";
		// List<String> list = readTxtFileByLine(txtfile, "GBK");
		// for (String content : list) {
		// //System.out.println(content);
		// }
		// createDir("D://test555/test/test");
		// createFile("D://test555/test/test", "test.txt");
		List<String> ll=readTxtFileByLine("D:\\MyEclipseWork\\searchH\\src\\main\\resources\\map.csv","GBK");
		for(String s:ll)
		{
			System.out.println("=> "+s);   
			
		}
	}
	
	  /**
		 * 文章发布同步操作
		 * <p>
		 * 调用同步脚本
		 * 
		 * @param shell
		 *            同步脚本
		 * 
		 * @throws Exception
		 */
		public static void synchro(String shell) throws Exception {
			// 脚本不为空，则执行
			if (StringUtils.isNotBlank(shell))
				log.info("excute shell begin::::::::::::::::::::::" + shell);
			try{
				Runtime.getRuntime().exec(shell);
				// 执行同步脚本后等待5s等待他执行完成(暂时这么处理,等找到exec同步执行的方法后再修改)
				Thread.sleep(5000);
				log.info("excute shell end::::::::::::::::::::::" + shell);
			}catch(Exception e){
				e.printStackTrace();
				log.info("no found shell ::::::::::" + shell);
			}
		}

}