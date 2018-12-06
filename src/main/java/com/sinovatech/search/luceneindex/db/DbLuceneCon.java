package com.sinovatech.search.luceneindex.db;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

public class DbLuceneCon {

	private String tableName;
	private IndexWriter indexWriter;
	private IndexSearcher indexSearcher;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}
	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}
	public IndexSearcher getIndexSearcher() {
		return indexSearcher;
	}
	public void setIndexSearcher(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
	
}
