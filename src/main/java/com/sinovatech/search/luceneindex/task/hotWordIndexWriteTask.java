package com.sinovatech.search.luceneindex.task;

import com.sinovatech.search.luceneindex.LuceneManager;

public class hotWordIndexWriteTask implements Runnable {

	@Override
	public void  run()  {
		// TODO Auto-generated method stub
		try {
			LuceneManager.doHotWordIndexWrite();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
