package com.sinovatech.search.luceneindex.db;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.Version;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.util.UUID;
import com.sinovatech.search.entity.common.DtoSupport;
import com.sinovatech.search.luceneindex.LuceneManager;
import com.sinovatech.search.utils.DateUtil;

public abstract class AbstractLuceneDao<T extends DtoSupport> implements
		LuceneDao<T> {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(AbstractLuceneDao.class);
	private  String daoName =this.getClass().getSimpleName();

    public AbstractLuceneDao() {
        this.indexWriter = InitDbLuceneConnection.getDbLuceneOptMap()
                .get(getTableNameKey()).getIndexWriter();
        this.indexSearcher = InitDbLuceneConnection.getDbLuceneOptMap()
                .get(getTableNameKey()).getIndexSearcher();
    }

	public Map<String, DbLuceneCon> getDbLuceneOptMap() {
		return InitDbLuceneConnection.getDbLuceneOptMap();
	}

	// private Class<T> clazz;

	private IndexWriter indexWriter = null;

	private IndexSearcher indexSearcher = null;
	private Analyzer analyzer = new IKAnalyzer();
	public String getTableNameKey() {
		// clazz = (Class<upT>) ((ParameterizedType)
		// getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		// System.out.println(clazz.getClass().getSimpleName());
		// System.out.println("===============================>>>>>>>>>>>>>>"+clazz.getSimpleName());
		// return clazz.getSimpleName();
		return this.getClass().getSimpleName();
	}

	public IndexWriter getIndexWriter() {

		return indexWriter;
	}

	public IndexSearcher getIndexSearcher() {
		indexSearcher = reopen(getDbLuceneOptMap().get(getTableNameKey()).getIndexSearcher(),getTableNameKey());
		getDbLuceneOptMap().get(getTableNameKey()).setIndexSearcher(indexSearcher);
		return indexSearcher;
	}

	public boolean add(T obj) {
		boolean is = true;
		Document doc = makeDoc(obj.getFiledValueForString(),
				obj.getPKfiledName());
		if (doc != null) {
			try {
				getIndexWriter().addDocument(doc);
				getIndexWriter().commit();
				log.info(daoName+" add(T obj) ok:" + obj.getClass().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				is = false;
				log.error(daoName+"add(T obj) error:", e);
			}

		}
		return is;
	}

	public  boolean  update(T obj) {
		//return del(obj) && add(obj);
		return update(obj, getIndexWriter());
	}

	public boolean del(T obj) {
		return del(obj, getIndexWriter());
	}
	 

	public Page list(Page page) {
		page = LuceneQueryUtil.query(page, getIndexSearcher());
		return page;
	}

	public List<T> listForT(Page page) {
		page = LuceneQueryUtil.query(page, getIndexSearcher());
		List<T> lst = null;
		try {
			lst = transform(page.getListData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(daoName + "listForT error:", e);
		}
		return lst;
	}

	private List<T> transform(List<Map<String, String>> mapValue)
			throws InstantiationException, IllegalAccessException {
		ConvertUtils.register(new Converter() {

			@Override
			public Object convert(Class type, Object value) {
				try {
					if (value == null || "".equals(value)) {
						return null;
					}
					java.util.Date rsd=null;
 
					try{
						rsd = new SimpleDateFormat(DateUtil.yyyyMMddHHmmssSpt).parse(value + "");
					}catch(ParseException eee)
					{
						//log.error(daoName + "old date tarnsfor err try:",eee);
						return rsd;
					}
					try{
						rsd = new SimpleDateFormat(DateUtil.yyyyMMddSpt).parse(value + "");
						return rsd;
					}catch(ParseException ee)
					{
						//log.error(daoName + "date tarnsfor err try:", ee);
					}
					return new SimpleDateFormat(DateUtil.yyyyMMddHHmmssSpt)
							.parse(value + "");
				} catch (ParseException e) {
					throw new RuntimeException(e);
					// e.printStackTrace();
				}
			}
		}, Date.class);

		List<T> rList = new ArrayList<T>();

		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		Map<String, Object[]> ff = clazz.newInstance().getFiledValue();
		for (Map<String, String> vmp : mapValue) {
			T clazz1 = clazz.newInstance();
			// Object ov = null;
			for (String strf : vmp.keySet()) {
				// ov = null;
				try {
					BeanUtils.setProperty(clazz1, strf, vmp.get(strf));
					// Method method
					// =clazz.getDeclaredMethod("set"+strf.substring(0,1).toUpperCase()+strf.substring(1),
					// (Class)(ff.get(strf)[1]));
					// System.out.println(method.getName());

					// String ovs = vmp.get(strf);
					// if("String".equals(ff.get(strf)[0])){
					// ov = vmp.get(strf);
					// }
					// else if("Integer".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Integer.parseInt(ovs);
					// }
					// }
					// else if("int".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Integer.parseInt(ovs);
					// }
					// }
					// else if("Date".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = DateUtil.parseDateTime(ovs, DateUtil.yyyyMMddSpt);
					// }
					// }
					// else if("Long".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Long.valueOf(ovs);
					// }
					// }
					// else if("long".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Long.valueOf(ovs);
					// }
					// }
					// else if("Double".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Double.valueOf(ovs);
					// }
					// }
					// else if("double".equals(ff.get(strf)[0])){
					// if(ovs!=null && !"null".equals(ovs) && !"".equals(ovs))
					// {
					// ov = Double.valueOf(ovs);
					// }
					// }else
					// {
					// ov = vmp.get(strf);
					// }
					// if (ov != null) {
					// method.invoke(clazz1,new Object[]{ov});
					// }
				} catch (Exception e) {
					log.info("ff.get(strf)[0]:" + ff.get(strf)[0]
							+ "|ovs:" + vmp.get(strf) + "|strf:" + strf);
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
			rList.add(clazz1);
		}

		return rList;
	}

	private Document makeDoc(Map<String, String> valueMap, String pkf) {
		log.info(daoName +"===========makeDoc===pkf===============》" + pkf);

		Document doc = new Document();
		try {

			for (String f : valueMap.keySet()) {
				if (pkf.equals(f)) {
					if (("").equals(valueMap.get(f))||"null".equals(valueMap.get(f))||null==valueMap.get(f)) {// 为null则是添加,否则就是修改
						valueMap.put(pkf, UUID.getUUID());
					}
					log.info(daoName+"==============pkf===============》"
							+ pkf + "=========valueMap.get(f)============>"
							+ valueMap.get(f));
				}
				doc.add(new StringField(f, valueMap.get(f) == null ? ""
						: valueMap.get(f), Field.Store.YES));
			}
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(daoName +" adddoc error", e);
			doc = null;
		}
		return doc;
	}

	private synchronized boolean del(T obj, IndexWriter indexWriter) {
			boolean ris = false;
			String pkf = obj.getPKfiledName();
	
			try {
				Query q = new TermQuery(new Term(pkf, obj.getFiledValueForString()
						.get(pkf)));
				indexWriter.deleteDocuments(q);// 删除
				indexWriter.commit();
				log.info(daoName +" del-ok pkf[pkf:" + obj.getFiledValue().get(pkf) + "]");
				ris = true;
			} catch (Exception e) {
				e.printStackTrace();
				log.error(daoName+" del-error pkf[pkf:" + obj.getFiledValue().get(pkf)
						+ "]删除doc失败原因为" + e.getMessage(), e);
				ris = false;
			}
	
			return ris;
	}
	
	public synchronized boolean del(String del) {
		boolean ris = false;
		 

		try {
		 	 QueryParser parser = new QueryParser(Version.LUCENE_47, "", analyzer);
		 	 Query query =parser.parse(del);
		    indexWriter.deleteDocuments(query);// 删除
			indexWriter.commit();
			log.info(daoName +" del-ok pkf[pkf:" +del + "]");
			ris = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(daoName+" del-error pkf[pkf:" + del
					+ "]删除doc失败原因为" + e.getMessage(), e);
			ris = false;
		}

		return ris;
}
	
	
	private  synchronized boolean update(T obj, IndexWriter indexWriter) {
    	boolean ris = false;
    	String pkf = obj.getPKfiledName();
    	try {
    		Term q =  new Term(pkf, obj.getFiledValueForString().get(pkf));
    		Document doc = makeDoc(obj.getFiledValueForString(),obj.getPKfiledName());
    		indexWriter.updateDocument(q, doc);// 更新
    		indexWriter.commit();
    		log.info(daoName+ " update-ok pkf[pkf:" + obj.getFiledValue().get(pkf) + "]");
    		ris = true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error(daoName+" del-error pkf[pkf:" + obj.getFiledValue().get(pkf)
    				+ "]删除doc失败原因为" + e.getMessage(), e);
    		ris = false;
    	}
    
    	return ris;
    }
	
	private final Object refreshLock = new Object();
 
    private IndexSearcher reopen(IndexSearcher indexSearcher, String lucenePath) {
        synchronized (refreshLock) {
            try {
                log.info("indexSearcher:" + indexSearcher.hashCode());
                IndexReader oldReader = indexSearcher.getIndexReader();

                // 可用状态
                if (oldReader.getRefCount() > 0) {
                    IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader) oldReader);// reader.reopen();
                    // //
                    // 读入新增加的增量索引内容，满足实时索引需求
                    if (newReader != null) {
 
                        log.info("newReader!= null重新刷新缓存reopen-IndexReader 重新构造IndexSearcher");
                        IndexSearcher searcher2 = new IndexSearcher(newReader);
                        log.info("旧IndesReader失效，将其关闭。");
                        oldReader.close();
                        return searcher2;

                    } else {
                        log.info("缓存无改动，沿用以前的IndexSearcher");

                        return indexSearcher;
                    }
                } else {
                    log.info("IndexReader被关闭，创建一个新的。" + lucenePath);
                    //创建新的indexSearch
                    WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
                    ServletContext arg0 = wac.getServletContext();
//                    String dir = arg0.getRealPath(RedisKeyConst.Search.DB_LUCENE_PATH);
//                    String dir = RedisKeyConst.Search.DB_LUCENE_PATH;
                    String dir = GlobalConfig.getProperty("searchh", "lucene_db_path");
                    lucenePath = dir + File.separator + lucenePath;
                    indexSearcher = LuceneManager.createIndexSearcher(lucenePath);
                    return indexSearcher;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("刷新缓存失败：", e);

                return indexSearcher;
            }
            // return indexSearcher;
        }
    }

	@Override
	public List<Map<String, String>> getAll() {
		Page page = new Page();
		page.setQueryStr("*:*");
		page.setRowDisplayed(Integer.MAX_VALUE);
		page = this.list(page);

		return page.getListData();
	}

	public List<T> getAllForT() {

		Page page = new Page();
		page.setQueryStr("*:*");
		page.setRowDisplayed(Integer.MAX_VALUE);
		page = this.list(page);
		List<T> lst = null;
		try {
			lst = transform(page.getListData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("listForT error:", e);
		}
		return lst;
	}

	@Override
	public T getById(String id) {
		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		T c = null;
		List<T> rlst = null;
		try {
			c = clazz.newInstance();
			Page page = new Page();
			page.setQueryStr("" + c.getPKfiledName() + ":" + id + "");
			page.setRowDisplayed(1);
			page = this.list(page);
			List<Map<String, String>> mlst = page.getListData();

			if (mlst != null) {
				rlst = transform(mlst);
			}
			if (rlst != null && rlst.size() > 0) {
				c = rlst.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return c;
	}

	@Override
	public List<T> listByIds(String ids) {
		List<T> rlst = null;
		T o = null;
		if (ids != null && !"".equals(ids)) {
			String[] idss = ids.split(",");
			rlst = new ArrayList<T>();
			for (String id : idss) {
				o = this.getById(id);
				if (o != null) {
					rlst.add(o);
				}
			}

		}
		return rlst;
	}
	
	public void group(Page page) throws Exception{
		LuceneQueryUtil.group(page, getIndexSearcher());
	}
}
