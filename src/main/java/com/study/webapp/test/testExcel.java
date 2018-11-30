package com.study.webapp.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class testExcel {

	public static void main(String[] args) throws Exception {
		File tempFile = new File("D:\\cc-cc\\中汇原文件\\0255[会审]方大锦化化工科技股份有限公司6.xls");
		InputStream stream = new FileInputStream(tempFile);
		Workbook hssfWorkbook = null;
		try {
			hssfWorkbook = new HSSFWorkbook(stream);
		} catch (Exception ex) {
			try {
				hssfWorkbook = new HSSFWorkbook(stream);
			} catch (Exception e) {
				System.out.println("文件格式错误");
			}
		}
		FormulaEvaluator formulaEvaluator = hssfWorkbook.getCreationHelper().createFormulaEvaluator();
		Sheet hssfSheet = hssfWorkbook.getSheet("母公司资产负债表（续）");
		// int rowCount = hssfSheet.getPhysicalNumberOfRows();
		int columnNum = hssfSheet.getRow(4).getPhysicalNumberOfCells();
		Row row = hssfSheet.getRow(9);
		// Cell cell = row.getCell(1);
		// String name = cellString(cell, formulaEvaluator);
		Cell cell1 = row.getCell(3);
		String name1 = cellString(cell1, formulaEvaluator);
		// Cell cell2 = row.getCell(4);
		// String name2 = cellString(cell2, formulaEvaluator);
		// Cell cell3 = row.getCell(5);
		// String name4 = cellString(cell3, formulaEvaluator);
		System.out.println(name1);

	}

	public static String departString(String departName) {
		String[] departNames = departName.split("\n");
		if (departNames.length > 1) {
			departName = departNames[1];
		} else {
			departName = departNames[0];
		}
		return departName.trim();
	}

	public static String cellString(Cell cell, FormulaEvaluator formulaEvaluator) {
		String cellString = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:// 数字
				cellString = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:// 字符串
				cellString = cell.getStringCellValue().trim();
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式
				try {
					java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
					cellString = df.format(cell.getNumericCellValue());
				} catch (Exception e) {
					try {
					} catch (Exception e1) {
						cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ",
								"");
					}
				}
				break;
			default:
				cellString = cell.toString().trim();
				break;
			}
		}
		return cellString;
	}

}
