package com.sinovatech.search.luceneindex.task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;

import com.sinovatech.common.config.GlobalConfig;

public class CpLuceneTablesTask implements Runnable {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(CpLuceneTablesTask.class);
	@Override
	public void  run()  {
		// TODO Auto-generated method stub
		try {
			cplucenetables();
			log.info("备份CpLuceneTablesTask-run()-info");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("备份CpLuceneTablesTask-run-error", e);
			e.printStackTrace();
		}
	}
	public static String datestring(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat();
        
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(date);
    }
	private void cplucenetables()
	{
		String dates = datestring(new Date());
		String pdir = GlobalConfig.getProperty("searchh", "lucene_db_path");
		//String lcommand = " cp -r "+pdir+" "+pdir+"_bak/lucene_db_"+dates;
		String lcommand = " tar -cvzf  "+pdir+"_bak/lucene_db_"+dates+".tar.gz " +pdir;
		String wcommand = " xcopy "+pdir+" "+pdir+"_bak\\lucene_db_"+dates+"/s/e/i ";
		String command="";
	 
		
		
		String os = System.getProperties().getProperty("os.name").toLowerCase();
		if(os.indexOf("windows")!=-1)
		{
			command=wcommand;
		}else{
			command=lcommand;
		}
		 
		try {
			
//			log.info("删除备份lucenetables文件["+pdir+"_bak"+"]");
//			delFolder(pdir+"_bak");
//			log.info("备份lucenetables文件开始["+command+"]");
			
			
			File fdir =new File(pdir+"_bak");
			if(!fdir.exists()){
				fdir.mkdir();
			}
			
			File cf =new File(pdir+"_bak"+"\\lucene_db_"+dates);
			if(!cf.exists())
			{   log.info("备份lucenetables文件begin["+command+"]");
				Runtime.getRuntime().exec(command);
				log.info("备份lucenetables文件ok["+command+"]");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("备份lucenetables文件错误", e);
			e.printStackTrace();
		}
	}
	
	 public static void delFolder(String folderPath) {
	        try {
	            delAllFile(folderPath); //删除完里面所有内容
	            String filePath = folderPath;
	            filePath = filePath.toString();
	            java.io.File myFilePath = new java.io.File(filePath);
	            myFilePath.delete(); //删除空文件夹
	        } catch (Exception e) {
	            e.printStackTrace(); 
	        }
	    }

	    /**
	     * 删除指定文件夹下所有文件
	     * @param path 文件夹完整绝对路径 ,"Z:/xuyun/save"
	     */
	    public static boolean delAllFile(String path) {
	        boolean flag = false;
	        File file = new File(path);
	        if (!file.exists()) {
	            return flag;    
	        }
	        if (!file.isDirectory()) {
	            return flag;
	        }
	        String[] tempList = file.list();
	        File temp = null;
	        for (int i = 0; i < tempList.length; i++) {
	            if (path.endsWith(File.separator)) {
	                temp = new File(path + tempList[i]);
	            } else {
	                temp = new File(path + File.separator + tempList[i]);
	            }
	            if (temp.isFile()) {
	                temp.delete();
	            }
	            if (temp.isDirectory()) {
	                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	                delFolder(path + "/" + tempList[i]);//再删除空文件夹
	                flag = true;
	            }
	        }
	        return flag;
	    }

}
