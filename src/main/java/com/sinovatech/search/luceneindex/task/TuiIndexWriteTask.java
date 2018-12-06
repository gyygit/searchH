package com.sinovatech.search.luceneindex.task;

import org.apache.commons.logging.Log;

import com.sinovatech.search.luceneindex.LuceneManager;
/**
 * 推送的自动创建索引定时器
 * @author lenovo
 *
 */
public class TuiIndexWriteTask implements Runnable {
	private static final Log log = org.apache.commons.logging.LogFactory
			.getLog(TuiIndexWriteTask.class);
	@Override
	public void  run()  {
		// TODO Auto-generated method stub
		try {
			//log.info("TuiIndexWriteTask- run()开始");
			LuceneManager.doTuiIndexWrite();
		} catch (Exception e) {
			log.info("TuiIndexWriteTask- run()",e);
			e.printStackTrace();
		}
	}
}
