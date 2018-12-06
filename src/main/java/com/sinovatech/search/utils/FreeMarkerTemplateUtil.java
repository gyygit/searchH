package com.sinovatech.search.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.common.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTemplateUtil {

	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(FreeMarkerTemplateUtil.class);

	/**
	 * @功能:解析freeMarker模版
	 * @param tsmap
	 * @param tplContent
	 * @return
	 */
	public static String processTemplate(Map<String, Object> tsmap,
			String tplContent) {
		StringWriter writer = new StringWriter();
		try {
			Configuration cfg = new Configuration();
			cfg.setTemplateLoader(new StringTemplateLoader(tplContent));
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate("");
			template.process(tsmap, writer);
		} catch (Exception e) {
			logger.error("freemark解析模版异常:", e);
		}

		return writer.toString();
	}

	/**
	 * 结合模板生成文件
	 * 
	 * @param tsmap
	 * @param tplContent
	 * @return
	 */
	public static void getTplString(Map tsmap, String tplContent,
			String fileName) {

		StringWriter writer = new StringWriter();
		try {
			Configuration cfg = new Configuration();
			cfg.setTemplateLoader(new StringTemplateLoader(tplContent));
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate("");
			template.process(tsmap, writer);
		} catch (Exception e) {
			// log.info("====结合模板生成文件错误====",e);
		}

		String content = writer.toString();
		BufferedWriter output = null;
		try {
			File file = new File(fileName);
			file.createNewFile();
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			if (StringUtils.isNotBlank(content)) {
				output.write(content);
			}
		} catch (IOException e) {

		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
