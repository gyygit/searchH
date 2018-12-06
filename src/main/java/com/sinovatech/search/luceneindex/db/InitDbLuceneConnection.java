package com.sinovatech.search.luceneindex.db;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.entity.DictionaryDTO;
import com.sinovatech.search.luceneindex.LuceneManager;

public class InitDbLuceneConnection {
    
    private static final Log log = org.apache.commons.logging.LogFactory.getLog(InitDbLuceneConnection.class);
    
    private static Map<String, DbLuceneCon> dbLuceneOptMap;
    
    private static String[] tableNames = getClassName("com.sinovatech.search.luceneindex.db.dao");

    public static Map<String, DbLuceneCon> getDbLuceneOptMap() {
        return dbLuceneOptMap;
    }

    static {
        log.info("InitDbLuceneConnection init doJob() beigin");
        doJob();
        log.info("InitDbLuceneConnection init doJob() end");
    }

    public static void doJob() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        ServletContext arg0 = wac.getServletContext();
        IndexWriter indexWriter = null;
        IndexSearcher indexSearcher = null;
        dbLuceneOptMap = new HashMap<String, DbLuceneCon>();
//        String dir = arg0.getRealPath(RedisKeyConst.Search.DB_LUCENE_PATH);
        String pdir = GlobalConfig.getProperty("searchh", "lucene_db_path");
        String dir = RedisKeyConst.Search.DB_LUCENE_PATH;
        if(pdir!=null && !"".equals(pdir))
        {
        	dir=pdir;
        	log.info("use  self config lucene_db_path: "+dir);
        }
       
        String path = "";
        Map<String, String> mapConfig = new HashMap<String, String>();
        if (tableNames == null) {
            throw new IllegalStateException("tableNames==null Error ");
        }
        for (String s : tableNames) {
            path = dir + File.separator + s;
            cheakAndMakeDir(path);//检验是否存在文件夹
            DbLuceneCon dbLuceneCon = new DbLuceneCon();
            mapConfig.put("path", path);
            mapConfig.put("openMode", "3");
            mapConfig.put("appCode", "");
            try {
                indexWriter = LuceneManager.createIndexWriter(mapConfig);
                createDoc(s, indexWriter);
                indexSearcher = LuceneManager.createIndexSearcher(path);
                dbLuceneCon.setTableName(s);
                dbLuceneCon.setIndexSearcher(indexSearcher);
                dbLuceneCon.setIndexWriter(indexWriter);
                dbLuceneOptMap.put(s, dbLuceneCon);
                log.info("创建dbLuceneCon成功,信息为：tableName[" + s + "]path[" + path + "]");
            } catch (Exception e) {
                e.printStackTrace();//抛出运行时异常
                log.error("创建数luceneDb的错误ERROR", e);
            }

        }
        //		DictionaryDAO dictionaryDAO = SpringContextHolder.getBean("dictionaryDAOL");
        //		DictionaryDTO  dto =new DictionaryDTO();
        //		dto.setId("123");
        //		dto.setCode("223");
        //		dto.setIndexcode("323");
        //		dto.setName("测试");
        //		dto.setSort(1111l);
        //		dictionaryDAO.add(dto);
        //		Page page =new Page();
        //		page.setQueryStr("code:223");
        //		DictionaryDTO  dictionaryDTO1 =dictionaryDAO.getById("123");
        //		System.out.println("==============dictionaryDTO1.getName()=======================>>"+dictionaryDTO1.getName());
        ////		List<DictionaryDTO> pp =dictionaryDAO.getAll();
        ////		System.out.println("size():"+pp.size());
        ////		for(DictionaryDTO ddd:pp)
        ////		{
        ////			System.out.println("==============DictionaryDTO.getName()=======================>>"+ddd.getName());
        ////		}

    }

    private static void createDoc(String tableName, IndexWriter indexWriter) {
        Document doc = new Document();
        doc.add(new StringField("tableName", tableName, Store.YES));
        try {
            if (indexWriter != null) {
                indexWriter.addDocument(doc);//创建一个索引防止前台服务不能打开reader
                del(indexWriter, tableName);
                indexWriter.commit();
                log.info(" createDoc new StringField(tableName,  " + tableName + " ,  Store.YES)  ok");
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(" createDoc new StringField(tableName,  " + tableName + " ,  Store.YES) error", e);
        }
    }

    private static boolean del(IndexWriter indexWriter, String tableName) {
        boolean ris = false;

        try {
            Query q = new TermQuery(new Term("tableName", tableName));
            indexWriter.deleteDocuments(q);//删除
            indexWriter.commit();
            log.info("del-ok InitDbLuceneConnection[tableName-test:" + tableName + "]");
            ris = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("del-error InitDbLuceneConnection[tableName-test:" + tableName + "]删除doc失败原因为" + e.getMessage(), e);
            ris = false;
        }

        return ris;
    }

    private static void cheakAndMakeDir(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
            log.info("File路径path[" + path + "]不存在所以创建");
        }
    }

    public static void main(String[] args) throws Exception {
        DictionaryDTO ss = new DictionaryDTO();
        ss.getFiledValue();

    }

    private static String[] getClassName(String pack) {
        String[] rfs = null;
        if (pack == null || "".equals(pack.trim())) {
            throw new IllegalStateException("pack 包名为null 或者是 '' ");
        }
        String packageDirName = pack.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String fpath = url.getFile();

            File dir = new File(fpath);
            File[] dirFiles = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    boolean acceptClass = file.getName().endsWith("class");// 接受class文件
                    return acceptClass;
                }
            });
            rfs = new String[dirFiles.length];
            int i = 0;
            for (File f : dirFiles) {
                rfs[i] = f.getName().replaceAll(".class", "");
                System.out.println(rfs[i]);
                i++;

            }
        }
        return rfs;
    }

}
