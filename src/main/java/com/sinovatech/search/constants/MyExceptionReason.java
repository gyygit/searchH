package com.sinovatech.search.constants;

/**
 * 异常原因枚举类
 * 
 * @author 陈杰
 * 
 */
public enum MyExceptionReason {
	XML_READ_ERROR, XML_READ_ENCODING_ERROR, XML_CONTENT_ERROR, EXCEL_READ_ERROR, EXCEL_READ_TYPE_ERROR, DEAULT_ERROR, UPLOAD_FILE_NOT_EXSIT//
	, FORMAT_FILE_NOT_EXSIT, //
	CSV_READ_ERROR, //
	CSV_READ_TYPE_ERROR, //
	GENERATE_TMP_CSV_ERROR, //
	FORMAT_FILE_TYPE_NULL, //
	CSV_FILE_NOT_MATCH_FORMAT, //
	WRITE_XML_ERROR, //
	ERROR_DB_INFO, DB_EXIST, DB_NOT_EXIST, //
	PRIMARY_KEY_TYPE_ERROR, //
	DB_DATE_FORMAT_INVALID, UNZIP_ERROR;

	public String toString() {
		switch (this) {
		case EXCEL_READ_ERROR:
			return "读取Excel文件时发生错误：非法的excel文件！";
		case EXCEL_READ_TYPE_ERROR:
			return "读取Excel文件时发生错误：单元格格式不匹配！";
		case DEAULT_ERROR:
			return "同步数据时出错！格式文件异常或数据库连接异常!";
		case UPLOAD_FILE_NOT_EXSIT:
			return "上传的文件并不存在";
		case FORMAT_FILE_NOT_EXSIT:
			return "格式文件并不存在";
		case FORMAT_FILE_TYPE_NULL:
			return "格式文件类型为空";
		case GENERATE_TMP_CSV_ERROR:
			return "生成临时csv数据文件时发生异常";
		case CSV_READ_ERROR:
			return "操作csv数据文件发生异常";
		case CSV_FILE_NOT_MATCH_FORMAT:
			return "csv数据文件与格式定义不匹配";
		case WRITE_XML_ERROR:
			return "写XML文件时发生异常";
		case ERROR_DB_INFO:
			return "数据库定义信息不正确";
		case DB_EXIST:
			return "数据库定义信息已存在";
		case DB_NOT_EXIST:
			return "原数据库定义信息不存在";
		case PRIMARY_KEY_TYPE_ERROR:
			return "主键类型有误";
		case DB_DATE_FORMAT_INVALID:
			return "数据库日期格式有误";
		case UNZIP_ERROR:
			return "解压zip时发生异常，请检查zip包的格式，确认是用zip方式打包";
		default:
			return "系统出现未知异常";
		}
	}
}
