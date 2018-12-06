package com.sinovatech.search.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.common.util.StringUtils;

/**
 * Excel操作工具
 * 
 * @author minghui.dai
 * 
 */
public class ExcelUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	/**
	 * 读取Excel文件
	 * 
	 * @param wb
	 * @return
	 * @throws Exception
	 */
	private static List<List<List<Map<String, String>>>> read(Workbook wb) throws Exception {
		List<List<List<Map<String, String>>>> sheetList = new ArrayList<List<List<Map<String, String>>>>();
		try {
			// 循环sheet
			for (int k = 0; k < wb.getNumberOfSheets(); k++) {
				Sheet sheet = wb.getSheetAt(k);
				if (null == sheet) {
					continue;
				}
				List<List<Map<String, String>>> sheetData = getSheetData(sheet, 1, 0);
				sheetList.add(sheetData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheetList;
	}

	/**
	 * 四舍五入保留指定位数小数
	 * 
	 * @param doubleData
	 * @param parten
	 *            :#.##
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String doubleFomatter(double doubleData, String parten) {
		// parten默认为#.##格式
		if (StringUtils.isBlank(parten)) {
			parten = "#.##";
		}
		return new DecimalFormat(parten).format(doubleData);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param fomat
	 * @return
	 */
	private static String dateFomatter(final Date date, String parten) {
		if (null == date) {
			return null;
		} else {
			// parten默认为yyyy-MM-dd格式
			if (StringUtils.isBlank(parten)) {
				parten = "yyyy-MM-dd";
			}
			return new SimpleDateFormat(parten).format(date);
		}
	}

	/**
	 * 获取单元格数据
	 * 
	 * @param cell
	 *            :数据单元格
	 * @return
	 */
	private static String getCellData(final Cell cell, int cellNumber) {

		// 单元格数据
		String value = null;
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_FORMULA:
				switch (cell.getCachedFormulaResultType()) {
				case Cell.CELL_TYPE_NUMERIC:
					value = Double.toString(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				default:
					value = cell.getStringCellValue();
					break;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					value = dateFomatter(cell.getDateCellValue(), "yyyy-MM-dd");
				} else {
					value = Double.toString(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = Boolean.toString(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				value = "";
				break;
			default:
				value = "";
				break;
			}
		} else {
			value = "";
		}
		return value;
	}

	/**
	 * 
	 * @param sheet
	 *            :Excel表格单个sheet
	 * @param minDataRowNumber
	 *            :数据最小行号（非标题行）
	 * @param titleRowNumber
	 *            :标题行号
	 * @return
	 * @throws Exception
	 */
	private static List<List<Map<String, String>>> getSheetData(Sheet sheet, int minDataRowNumber, int titleRowNumber) throws Exception {
		// 所有行数据
		List<List<Map<String, String>>> rowsList = new ArrayList<List<Map<String, String>>>();
		String[] titles = getTitles(sheet, titleRowNumber);
		int rows = sheet.getPhysicalNumberOfRows();
		//是否需要过滤空单元格:true:需要过滤;false:不需要过滤
		boolean needFilterBlankField = true;
		// 循环行
		for (int r = minDataRowNumber; r < rows; r++) {
			// 一行数据
			List<Map<String, String>> rowData = new ArrayList<Map<String, String>>();
			Row row = sheet.getRow(r);
			boolean cellFieldIsNotBlank = false;
			if (null != row) {
				int cells = row.getPhysicalNumberOfCells();
				// 循环列
				for (short c = 0; c < cells; c++) {
					Cell cell = row.getCell(c);
					// 一个单元格数据
					Map<String, String> cellData = new HashMap<String, String>();
					String cellElementData = getCellData(cell,c);
					if (needFilterBlankField) {
						//判断是否为空单元格
						cellFieldIsNotBlank = fieldIsNotBlank(cellElementData, c);
						if (cellFieldIsNotBlank) {
							cellData.put(titles[c], cellElementData);
						} else {
							log.info("第 "+r+" 行，第 "+c+" 列为空,此行数据无效!");
							break;
						}
						rowData.add(cellData);
					} else {
						cellData.put(titles[c], cellElementData);
						rowData.add(cellData);
						
					}
					
				}
			}
			//判断是否需要过滤空单元格
			if (needFilterBlankField) {
				//判断单元格是否为空
				if (cellFieldIsNotBlank) {
					//只取不包含空单元格的行
					rowsList.add(rowData);
				}
			} else {
				if (null != rowData && rowData.size() > 0) {
					rowsList.add(rowData);
				}
			}
			
		}
		return rowsList;
	}

	private static boolean fieldIsNotBlank (String cellData,int cellNumber) {
		boolean isNotBlank = true;
		if (-1 == cellNumber) {
			if (StringUtils.isBlank(cellData)) {
				isNotBlank = false;
			}
		} else 	if (cellNumber == 1 || cellNumber == 2 || cellNumber == 3 || cellNumber == 4 || cellNumber == 5 || cellNumber == 6 || cellNumber == 8 || cellNumber == 9 || cellNumber == 11 || cellNumber == 12) {
			if (StringUtils.isBlank(cellData)) {
				isNotBlank = false;
			}
		}
		return isNotBlank;
	}
	/**
	 * 
	 * @param sheet
	 *            :Excel中单个sheet
	 * @param titleRowNumber
	 *            :标题所在行索引
	 * @throws Exception
	 */
	private static String[] getTitles(Sheet sheet, int titleRowNumber) throws Exception {
		String[] titles = null;
		int rows = sheet.getPhysicalNumberOfRows();
		// 循环行
		for (int r = 0; r < rows; r++) {
			if (r == titleRowNumber) {
				Row row = sheet.getRow(r);
				if (null != row) {
					int cells = row.getPhysicalNumberOfCells();
					titles = new String[cells];
					// 循环列
					for (short c = 0; c <= cells; c++) {
						Cell cell = row.getCell(c);
						// 单元格数据
						String cellData = getCellData(cell,c);
						if (StringUtils.isNotBlank(cellData)) {
							titles[c] = cellData;
						}
					}
				}
				break;
			}
		}
		return titles;
	}

	/**
	 * 
	 * @param fileName
	 *            :文件路径+文件名
	 * @param flag
	 *            :true(2003版本Excel);false(2007版本Excel)
	 * @throws Exception
	 */
	public static List<List<List<Map<String, String>>>> read(String fileName, boolean flag) throws Exception {
		Workbook wb = null;
		File f = new File(fileName);
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
			if (flag) {// 2003
				POIFSFileSystem fs = new POIFSFileSystem(is);
				wb = new HSSFWorkbook(fs);
			} else {// 2007
				wb = new XSSFWorkbook(is);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				is.close();
			}
		}
		if (null == wb) {
			return null;
		}
		return read(wb);
	}

	/**
	 * 
	 * @param is
	 *            :Excel文件输入流
	 * @param flag
	 *            :true(2003版本Excel);false(2007版本Excel)
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void read(InputStream is, boolean flag) throws Exception {
		Workbook wb = null;
		if (flag) {// 2003
			wb = new HSSFWorkbook(is);
		} else {// 2007
			wb = new XSSFWorkbook(is);
		}
		read(wb);
	}
}