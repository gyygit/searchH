package com.sinovatech.search.luceneindex.task;

import com.sinovatech.search.luceneindex.LuceneManager;

public class ClickVisitlogTask implements Runnable{

	@Override
	public void run() {
		try {
			LuceneManager.saveClickLogByCallbackSearchweb();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
