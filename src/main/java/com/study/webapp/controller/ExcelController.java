package com.study.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.study.webapp.po.ExcelA;
import com.study.webapp.po.ExcelB;
import com.study.webapp.util.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/excel2")
@Api(value = "ExcelController2", description = "excel的api")
public class ExcelController {

	// 字段映射文档
	public String keyFilePath = "C:\\transate\\字段关系整理_xwb.xlsx";
	// 源文件目录
	public String sourceDirPath = "C:\\transate\\source1";
	// 转换后目标文件目录
	public String targetDirPath = "C:\\transate\\target";
	// 财政部模版文件
	public String templatePath = "C:\\transate\\template.xls";

	JSONObject obj_a = new JSONObject();
	JSONObject obj_b = new JSONObject();
	JSONObject obj_c = new JSONObject();
	JSONObject obj_d = new JSONObject();
	JSONObject obj_e = new JSONObject();
	JSONObject obj_f = new JSONObject();
	JSONObject obj_g = new JSONObject();
	JSONObject obj_h = new JSONObject();
	JSONObject obj_i = new JSONObject();
	JSONObject obj_j = new JSONObject();
	JSONObject obj_k = new JSONObject();
	JSONObject obj_l = new JSONObject();

	private void executeJsonObj(JSONObject obj, String stringAfter, String text) {
		if (StringUtils.isBlank(text)) {
			return;
		}
		String[] strings = text.split(",");
		if (ArrayUtils.isEmpty(strings)) {
			return;

		}
		for (String str : strings) {
			str = StringUtils.trimToEmpty(str);
			if (StringUtils.isNotBlank(str)) {
				// str = str.replaceAll("\\r", "\n");
				if (obj.containsKey(str)) {
					str = str + "," + str;
				}
				obj.put(str, stringAfter);
			}
		}
	}

	@RequestMapping("/map_a")
	@ResponseBody
	@ApiOperation(value = "合并资产负债表map", httpMethod = "GET")
	public ResultVo<JSONObject> testdepart(HttpServletRequest request, HttpServletResponse response) throws Exception {

		File tempFile = new File(keyFilePath);
		InputStream stream = new FileInputStream(tempFile);
		Workbook hssfWorkbook = null;
		try {
			hssfWorkbook = new XSSFWorkbook(stream);
		} catch (Exception ex) {
			try {
				hssfWorkbook = new XSSFWorkbook(stream);
			} catch (Exception e) {
				System.out.println("文件格式错误");
			}
		}
		Sheet hssfSheet = hssfWorkbook.getSheet("整理前");
		for (int i = 1; i < 43; i++) {
			getOneToOne(hssfSheet, i, obj_a);// 合并资产负债表
		}
		for (int i = 43; i < 98; i++) {
			getOneToOne(hssfSheet, i, obj_b);// 合并资产负债表(续)
		}
		for (int i = 98; i < 155; i++) {
			getOneToOne(hssfSheet, i, obj_c);// 合并利润表
		}
		for (int i = 155; i < 211; i++) {
			getOneToOne(hssfSheet, i, obj_d);// 合并现金流量表
		}
		for (int i = 211; i < 240; i++) {
			getOneToOne2(hssfSheet, i, obj_e);// 合并所有者权益变动表
		}
		for (int i = 240; i < 269; i++) {
			getOneToOne2(hssfSheet, i, obj_f);// 合并所有者权益变动表(续)
		}
		for (int i = 269; i < 311; i++) {
			getOneToOne(hssfSheet, i, obj_g);// 母公司资产负债表
		}
		for (int i = 311; i < 364; i++) {
			getOneToOne(hssfSheet, i, obj_h);// 母公司资产负债表(续)
		}
		for (int i = 364; i < 412; i++) {
			getOneToOne(hssfSheet, i, obj_i);// 母公司利润表
		}
		for (int i = 412; i < 469; i++) {
			getOneToOne(hssfSheet, i, obj_j);// 母公司现金流量表
		}
		for (int i = 469; i < 497; i++) {
			getOneToOne2(hssfSheet, i, obj_k);// 母公司所有者权益变动表
		}
		for (int i = 497; i < 525; i++) {
			getOneToOne2(hssfSheet, i, obj_l);// 母公司所有者权益变动表 (续)
		}
//		return new ResultVo<JSONObject>(200, obj_l);
		return null;
	}

	@RequestMapping("/excel_a")
	@ResponseBody
	@ApiOperation(value = "合并资产负债表excel", httpMethod = "GET")
	public ResultVo<JSONObject> excel_a(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			File filedir = new File(sourceDirPath);
			File list[] = filedir.listFiles();
			for (int i = 0; i < list.length; i++) {
				if (list[i].isFile()) {
					String filename = list[i].getName();
					String fileType = StringUtils.substringAfterLast(filename, ".");
					try {
						if (!fileType.equals("txt")) {
							excel(list[i]);
						}
					} catch (Exception e) {
						System.out.println(filename);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return new ResultVo<JSONObject>(200, "");
		return null;
	}

	public void excel(File tempFile2) throws Exception {
		String filename = tempFile2.getName();
		String name = StringUtils.substringBeforeLast(filename, ".");
		String fileType = StringUtils.substringAfterLast(filename, ".");
		String msg = name + "表错误信息\r\n";
		InputStream stream2 = new FileInputStream(tempFile2);
		Workbook hssfWorkbook2 = null;
		try {
			if (fileType.equals("xls")) {
				hssfWorkbook2 = new HSSFWorkbook(stream2);
			} else if (fileType.equals("xlsx")) {
				hssfWorkbook2 = new XSSFWorkbook(stream2);
			} else {
				System.out.println("文件格式错误");
			}
		} catch (Exception ex) {

			System.out.println("文件格式错误");
		}
		Map<String, ExcelA> map_a = new HashMap<String, ExcelA>();
		Sheet hssfSheet2 = hssfWorkbook2.getSheet("合并资产负债表");

		if (hssfSheet2 != null) {
			map_a = getExcelData(hssfSheet2);
			ExcelA b = map_a.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并资产负债表" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		Map<String, ExcelA> map_b = new HashMap<String, ExcelA>();
		Sheet hssfSheet21 = hssfWorkbook2.getSheet("合并资产负债表（续）");
		if (hssfSheet21 != null) {
			map_b = getExcelData2(hssfSheet21);
			ExcelA b = map_b.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并资产负债表（续）" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		int flag = 0;
		Map<String, ExcelA> map_c = new HashMap<String, ExcelA>();
		Sheet hssfSheet22 = null;
		// hssfSheet22 = hssfWorkbook2.getSheetAt(2);
		hssfSheet22 = hssfWorkbook2.getSheet("合并利润表");
		if (hssfSheet22 != null) {
			map_c = getExcelData5(hssfSheet22);
			ExcelA b = map_c.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并利润表" + "\r\n";
				msg = msg + b.getMsg();
			}
		}
		if (map_c.containsKey("利息收入")) {
			flag = 1;
		}

		Map<String, ExcelA> map_d = new HashMap<String, ExcelA>();
		Sheet hssfSheet23 = null;
		hssfSheet23 = hssfWorkbook2.getSheet("合并现金流量表");
		if (hssfSheet23 != null) {
			map_d = getExcelData3(hssfSheet23);
			ExcelA b = map_d.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并现金流量表出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		Map<String, ExcelB> map_e = new HashMap<String, ExcelB>();
		Sheet hssfSheet24 = null;
		hssfSheet24 = hssfWorkbook2.getSheet("合并所有者权益变动表");
		if (hssfSheet24 != null) {
			map_e = getExcelData4(hssfSheet24);
			ExcelB b = map_e.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并所有者权益变动表出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		Map<String, ExcelB> map_f = new HashMap<String, ExcelB>();
		Sheet hssfSheet25 = null;
		hssfSheet25 = hssfWorkbook2.getSheet("合并所有者权益变动表（续）");
		if (hssfSheet25 != null) {
			map_f = getExcelData4(hssfSheet25);
			ExcelB b = map_f.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取合并所有者权益变动表（续）出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		Map<String, ExcelA> map_g = new HashMap<String, ExcelA>();
		Sheet hssfSheet26 = null;
		hssfSheet26 = hssfWorkbook2.getSheet("母公司资产负债表");
		if (hssfSheet26 == null) {
			hssfSheet26 = hssfWorkbook2.getSheet("资产负债表");
		}
		if (hssfSheet26 != null) {
			map_g = getExcelData(hssfSheet26);
			ExcelA b = map_g.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司资产负债表/资产负债表出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		Map<String, ExcelA> map_h = new HashMap<String, ExcelA>();
		Sheet hssfSheet27 = null;
		hssfSheet27 = hssfWorkbook2.getSheet("母公司资产负债表（续）");
		if (hssfSheet27 == null) {
			hssfSheet27 = hssfWorkbook2.getSheet("资产负债表（续）");
		}
		if (hssfSheet27 != null) {
			map_h = getExcelData2(hssfSheet27);
			ExcelA b = map_h.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司资产负债表（续）/资产负债表（续）出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		int flag1 = 0;
		Map<String, ExcelA> map_i = new HashMap<String, ExcelA>();
		Sheet hssfSheet28 = null;
		hssfSheet28 = hssfWorkbook2.getSheet("母公司利润表");
		if (hssfSheet28 == null) {
			hssfSheet28 = hssfWorkbook2.getSheet("利润表");
		}
		if (hssfSheet28 != null) {
			map_i = getExcelData5(hssfSheet28);
			ExcelA b = map_i.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司利润表/利润表出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}
		if (map_i.containsKey("利息收入")) {
			flag1 = 1;
		}

		Map<String, ExcelA> map_j = new HashMap<String, ExcelA>();

		Sheet hssfSheet29 = null;
		hssfSheet29 = hssfWorkbook2.getSheet("母公司现金流量表");
		if (hssfSheet29 == null) {
			hssfSheet29 = hssfWorkbook2.getSheet("现金流量表");
		}
		if (hssfSheet29 != null) {
			map_j = getExcelData3(hssfSheet29);
			ExcelA b = map_j.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司现金流量表/现金流量表 出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}

		int flag2 = 0;
		Map<String, ExcelB> map_k = new HashMap<String, ExcelB>();
		Sheet hssfSheet210 = null;
		// hssfSheet210 = hssfWorkbook2.getSheetAt(10);
		hssfSheet210 = hssfWorkbook2.getSheet("母公司所有者权益变动表");
		if (hssfSheet210 == null) {
			hssfSheet210 = hssfWorkbook2.getSheet("所有者权益变动表");
		}
		if (hssfSheet210 != null) {
			map_k = getExcelData6(hssfSheet210);
			ExcelB b = map_k.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司所有者权益变动表/所有者权益变动表 出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}
		if (map_k.containsKey("3．其他")) {
			flag2 = 1;
		}
		Map<String, ExcelB> map_l = new HashMap<String, ExcelB>();
		Sheet hssfSheet211 = null;
		hssfSheet211 = hssfWorkbook2.getSheet("母公司所有者权益变动表 (续)");
		if (hssfSheet211 == null) {
			hssfSheet211 = hssfWorkbook2.getSheet("所有者权益变动表 (续)");
		}
		if (hssfSheet211 != null) {
			map_l = getExcelData6(hssfSheet211);
			ExcelB b = map_l.get("msg");
			if (!b.getMsg().equals("")) {
				msg = msg + "获取母公司所有者权益变动表 (续)出错" + "\r\n";
				msg = msg + b.getMsg();
			}
		}
		// 开始写入数据//////////////////////////
		File tempFile = new File(templatePath);
		InputStream stream = new FileInputStream(tempFile);
		File dir = new File(targetDirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// String[] splits= filename.split(".");
		File file = new File(dir.getAbsolutePath() + File.separator + filename);
		FileOutputStream out = new FileOutputStream(file);
		Workbook hssfWorkbook = null;
		try {
			hssfWorkbook = new HSSFWorkbook(stream);
		} catch (Exception ex) {
			try {
				hssfWorkbook = new XSSFWorkbook(stream);
			} catch (Exception e) {
				System.out.println("文件格式错误");
			}
		}
		String bianzhi = StringUtils.substringAfterLast(name, "]");
		bianzhi = StringUtils.substringBeforeLast(bianzhi, "司") + "司";
		try {
			Sheet hssfSheet1 = hssfWorkbook.getSheet("合并资产负债表");
			Row row = hssfSheet1.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年12月31日");
			row.getCell(4).setCellValue("编制单位： " + bianzhi);
			row.getCell(5).setCellValue("2017年12月31日");
			if (!map_a.isEmpty() || !map_b.isEmpty()) {
				JSONObject obj = setExcelData(hssfSheet1, map_a, map_b);
				String msg1 = obj.getString("msg1");
				String msg2 = obj.getString("msg2");
				if (!msg1.equals("")) {
					msg = msg + "写入合并资产负债表出错" + "\r\n";
					msg = msg + msg1;
				}
				if (!msg2.equals("")) {
					msg = msg + "写入合并资产负债表(续)出错" + "\r\n";
					msg = msg + msg2;
				}
			}
		} catch (Exception e) {
			msg = msg + "写入合并资产负债表出错" + "\r\n";
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet12 = hssfWorkbook.getSheet("合并利润表");
			Row row = hssfSheet12.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年度");
			if (!map_c.isEmpty()) {
				String msg1 = setExcelData2(hssfSheet12, map_c, flag);
				if (!msg1.equals("")) {
					msg = msg + "写入合并利润表出错" + "\r\n";
					msg = msg + msg1;
				}
			}
		} catch (Exception e) {
			msg = msg + "写入合并利润表出错" + "\r\n";
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet13 = hssfWorkbook.getSheet("合并现金流量表");
			Row row = hssfSheet13.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年度");
			if (!map_d.isEmpty()) {
				String msg1 = setExcelData3(hssfSheet13, map_d);
				if (!msg1.equals("")) {
					msg = msg + "写入合并利润表出错" + "\r\n";
					msg = msg + msg1;
				}
			}
		} catch (Exception e) {
			msg = msg + "写入合并现金流量表出错" + "\r\n";
			System.out.println("写入合并现金流量表出错");
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet14 = hssfWorkbook.getSheet("合并所有者权益变动表");
			Row row = hssfSheet14.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(6).setCellValue("2017年度");
			row.getCell(14).setCellValue("编制单位： " + bianzhi);
			row.getCell(20).setCellValue("2017年度");
			if (!map_e.isEmpty() || !map_f.isEmpty()) {
				JSONObject obj = setExcelData4(hssfSheet14, map_e, map_f);
				String msg1 = obj.getString("msg1");
				String msg2 = obj.getString("msg2");
				if (!msg1.equals("")) {
					msg = msg + "写入合并所有者权益变动表出错" + "\r\n";
					msg = msg + msg1;
				}
				if (!msg2.equals("")) {
					msg = msg + "写入合并所有者权益变动表(续)出错" + "\r\n";
					msg = msg + msg2;
				}
			}
		} catch (Exception e) {
			msg = msg + "写入合并所有者权益变动表出错" + "\r\n";
			System.out.println("写入合并所有者权益变动表出错");
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet15 = hssfWorkbook.getSheet("资产负债表");
			Row row = hssfSheet15.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年12月31日");
			row.getCell(4).setCellValue("编制单位： " + bianzhi);
			row.getCell(5).setCellValue("2017年12月31日");
			if (!map_g.isEmpty() || !map_h.isEmpty()) {
				JSONObject obj = setExcelData5(hssfSheet15, map_g, map_h);
				String msg1 = obj.getString("msg1");
				String msg2 = obj.getString("msg2");
				if (!msg1.equals("")) {
					msg = msg + "写入资产负债表出错" + "\r\n";
					msg = msg + msg1;
				}
				if (!msg2.equals("")) {
					msg = msg + "写入资产负债表(续)出错" + "\r\n";
					msg = msg + msg2;
				}
			}

		} catch (Exception e) {
			msg = msg + "写入资产负债表出错" + "\r\n";
			System.out.println("写入资产负债表出错");
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet16 = hssfWorkbook.getSheet("利润表");
			Row row = hssfSheet16.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年度");
			if (!map_i.isEmpty()) {
				String msg1 = setExcelData6(hssfSheet16, map_i, flag1);
				if (!msg1.equals("")) {
					msg = msg + "写入利润表出错" + "\r\n";
					msg = msg + msg1;
				}
			}
		} catch (Exception e) {
			msg = msg + "写入利润表出错" + "\r\n";
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet17 = hssfWorkbook.getSheet("现金流量表");
			Row row = hssfSheet17.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(1).setCellValue("2017年度");
			if (!map_j.isEmpty()) {
				String msg1 = setExcelData7(hssfSheet17, map_j);
				if (!msg1.equals("")) {
					msg = msg + "写入现金流量表出错" + "\r\n";
					msg = msg + msg1;
				}
			}

		} catch (Exception e) {
			msg = msg + "写入现金流量表出错" + "\r\n";
			System.out.println("写入现金流量表出错");
			e.printStackTrace();
		}

		try {
			Sheet hssfSheet18 = hssfWorkbook.getSheet("所有者权益变动表");
			Row row = hssfSheet18.getRow(2);
			row.getCell(0).setCellValue("编制单位： " + bianzhi);
			row.getCell(2).setCellValue("2017年度");
			row.getCell(13).setCellValue("编制单位： " + bianzhi);
			row.getCell(19).setCellValue("2017年度");
			if (!map_k.isEmpty() || !map_l.isEmpty()) {
				JSONObject obj = setExcelData8(hssfSheet18, map_k, map_l, flag2);
				String msg1 = obj.getString("msg1");
				String msg2 = obj.getString("msg2");
				if (!msg1.equals("")) {
					msg = msg + "写入所有者权益变动表出错" + "\r\n";
					msg = msg + msg1;
				}
				if (!msg2.equals("")) {
					msg = msg + "写入所有者权益变动表(续)出错" + "\r\n";
					msg = msg + msg2;
				}
			}

		} catch (Exception e) {
			msg = msg + "写入所有者权益变动表出错" + "\r\n";
			System.out.println("写入所有者权益变动表出错");
			e.printStackTrace();
		}

		hssfWorkbook.write(out);
		if (out != null) {
			out.close();
		}
		if (!msg.equals(name + "表错误信息\r\n")) {
			File filemsg = new File(dir.getAbsolutePath() + File.separator + name + ".txt");
			if (!filemsg.exists()) {
				filemsg.createNewFile();
			}
			FileOutputStream out2 = new FileOutputStream(filemsg);
			out2.write(msg.getBytes("gbk"));
			if (out2 != null) {
				out2.flush();
				out2.close();
			}
		}

	}

	public JSONObject setExcelData8(Sheet hssfSheet, Map<String, ExcelB> map_e, Map<String, ExcelB> map_f, int flag2) {
		JSONObject obj = new JSONObject();
		String msg1 = "";
		String msg2 = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_e.keySet()) {
			if (key.equals("4．其他,4．其他") || key.equals("3．其他")) {
				int b = 0;
				ExcelB a = map_e.get(key);
				if (a != null) {
					String name = "4.其他";
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(0);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b > 0) {
										row.getCell(1).setCellValue(a.getValue1());
										row.getCell(2).setCellValue(a.getValue2());
										row.getCell(3).setCellValue(a.getValue3());
										row.getCell(4).setCellValue(a.getValue4());
										row.getCell(5).setCellValue(a.getValue5());
										row.getCell(6).setCellValue(a.getValue6());
										row.getCell(7).setCellValue(a.getValue7());
										row.getCell(8).setCellValue(a.getValue8());
										row.getCell(9).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(10).setCellValue(a.getValue10());
											row.getCell(11).setCellValue(a.getValue11());
											row.getCell(12).setCellValue(a.getValue12());
										} else {
											// row.getCell(10).setCellValue(a.getValue9());
											row.getCell(11).setCellValue(a.getValue10());
											row.getCell(12).setCellValue(a.getValue11());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			} else {
				int b = 0;
				ExcelB a = map_e.get(key);
				String name = obj_k.getString(key);
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						if (row != null) {
							Cell cellAfter = row.getCell(0);
							String StringAfter = cellString(cellAfter);
							if (StringAfter.equals(name)) {
								if (b == 0) {
									row.getCell(1).setCellValue(a.getValue1());
									row.getCell(2).setCellValue(a.getValue2());
									row.getCell(3).setCellValue(a.getValue3());
									row.getCell(4).setCellValue(a.getValue4());
									row.getCell(5).setCellValue(a.getValue5());
									row.getCell(6).setCellValue(a.getValue6());
									row.getCell(7).setCellValue(a.getValue7());
									row.getCell(8).setCellValue(a.getValue8());
									row.getCell(9).setCellValue(a.getValue9());
									if (a.isFlag() == true) {
										row.getCell(10).setCellValue(a.getValue10());
										row.getCell(11).setCellValue(a.getValue11());
										row.getCell(12).setCellValue(a.getValue12());
									} else {
										// row.getCell(10).setCellValue(a.getValue9());
										row.getCell(11).setCellValue(a.getValue10());
										row.getCell(12).setCellValue(a.getValue11());
									}
								}
								b++;
							}
						}
					} catch (Exception e) {
						msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}

		for (String key : map_f.keySet()) {
			if (key.equals("4．其他,4．其他") || key.equals("3．其他")) {
				int b = 0;
				ExcelB a = map_f.get(key);
				if (a != null) {
					String name = "4.其他";
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(13);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b > 0) {
										row.getCell(14).setCellValue(a.getValue1());
										row.getCell(15).setCellValue(a.getValue2());
										row.getCell(16).setCellValue(a.getValue3());
										row.getCell(17).setCellValue(a.getValue4());
										row.getCell(18).setCellValue(a.getValue5());
										row.getCell(19).setCellValue(a.getValue6());
										row.getCell(20).setCellValue(a.getValue7());
										row.getCell(21).setCellValue(a.getValue8());
										row.getCell(22).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(23).setCellValue(a.getValue10());
											row.getCell(24).setCellValue(a.getValue11());
											row.getCell(25).setCellValue(a.getValue12());
										} else {
											// row.getCell(23).setCellValue(a.getValue9());
											row.getCell(24).setCellValue(a.getValue10());
											row.getCell(25).setCellValue(a.getValue11());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						}
					}
				}
			} else {
				int b = 0;
				ExcelB a = map_f.get(key);
				String name = obj_l.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(13);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b == 0) {
										row.getCell(14).setCellValue(a.getValue1());
										row.getCell(15).setCellValue(a.getValue2());
										row.getCell(16).setCellValue(a.getValue3());
										row.getCell(17).setCellValue(a.getValue4());
										row.getCell(18).setCellValue(a.getValue5());
										row.getCell(19).setCellValue(a.getValue6());
										row.getCell(20).setCellValue(a.getValue7());
										row.getCell(21).setCellValue(a.getValue8());
										row.getCell(22).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(23).setCellValue(a.getValue10());
											row.getCell(24).setCellValue(a.getValue11());
											row.getCell(25).setCellValue(a.getValue12());
										} else {
											// row.getCell(23).setCellValue(a.getValue9());
											row.getCell(24).setCellValue(a.getValue10());
											row.getCell(25).setCellValue(a.getValue11());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						}
					}
				}
			}
		}
		obj.put("msg1", msg1);
		obj.put("msg2", msg2);
		return obj;
	}

	public String setExcelData7(Sheet hssfSheet, Map<String, ExcelA> map_j) {
		String msg = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_j.keySet()) {
			ExcelA a = map_j.get(key);
			String name = obj_j.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (StringAfter.equals(name)) {
							row.getCell(1).setCellValue(a.getValue2());
							row.getCell(2).setCellValue(a.getValue3());
							row.getCell(3).setCellValue(a.getValue1());
						}
					} catch (Exception e) {
						msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}
		return msg;
	}

	public String setExcelData6(Sheet hssfSheet, Map<String, ExcelA> map_c, int flag) {
		String msg = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_c.keySet()) {
			ExcelA a = map_c.get(key);
			String name = obj_i.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (flag == 1) {
							if (StringAfter.equals("一、营业总收入")) {
								ExcelA b = map_c.get("一、营业收入");
								if (b != null) {
									row.getCell(1).setCellValue(b.getValue2());
									row.getCell(2).setCellValue(b.getValue3());
									row.getCell(3).setCellValue(b.getValue1());
								}
							} else {
								if (StringAfter.equals(name)) {
									row.getCell(1).setCellValue(a.getValue2());
									row.getCell(2).setCellValue(a.getValue3());
									row.getCell(3).setCellValue(a.getValue1());
									break;
								}
							}
						} else {
							if (StringAfter.equals(name)) {
								row.getCell(1).setCellValue(a.getValue2());
								row.getCell(2).setCellValue(a.getValue3());
								row.getCell(3).setCellValue(a.getValue1());
								break;
							}
						}
					} catch (Exception e) {
						msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}
		return msg;
	}

	public JSONObject setExcelData5(Sheet hssfSheet, Map<String, ExcelA> map_a, Map<String, ExcelA> map_b) {
		JSONObject obj = new JSONObject();
		String msg1 = "";
		String msg2 = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_a.keySet()) {
			ExcelA a = map_a.get(key);
			String name = obj_g.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (StringAfter.equals(name)) {
							row.getCell(1).setCellValue(a.getValue2());
							row.getCell(2).setCellValue(a.getValue3());
							row.getCell(3).setCellValue(a.getValue1());
						}
					} catch (Exception e) {
						msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}

		for (String key : map_b.keySet()) {
			if (key.split(",").length > 1) {
				int names = 0;
				ExcelA a = map_b.get(key);
				String name = obj_h.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							Cell cellAfter = row.getCell(4);
							String StringAfter = cellString(cellAfter);
							if (StringAfter.equals(name)) {
								if (names > 0) {
									row.getCell(5).setCellValue(a.getValue2());
									row.getCell(6).setCellValue(a.getValue3());
									row.getCell(7).setCellValue(a.getValue1());
									break;
								}
								names++;
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			} else {
				ExcelA a = map_b.get(key);
				String name = obj_b.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							Cell cellAfter = row.getCell(4);
							String StringAfter = cellString(cellAfter);
							if (StringAfter.equals(name)) {
								row.getCell(5).setCellValue(a.getValue2());
								row.getCell(6).setCellValue(a.getValue3());
								row.getCell(7).setCellValue(a.getValue1());
								break;
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			}
		}
		obj.put("msg1", msg1);
		obj.put("msg2", msg2);
		return obj;
	}

	public JSONObject setExcelData4(Sheet hssfSheet, Map<String, ExcelB> map_e, Map<String, ExcelB> map_f) {
		JSONObject obj = new JSONObject();
		String msg1 = "";
		String msg2 = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_e.keySet()) {
			if (key.equals("4．其他,4．其他") || key.equals("3．其他")) {
				int b = 0;
				ExcelB a = map_e.get(key);
				if (a != null) {
					String name = "4.其他";
					// String name = obj_e.getString(key);
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(0);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b > 0) {
										row.getCell(1).setCellValue(a.getValue1());
										row.getCell(2).setCellValue(a.getValue2());
										row.getCell(3).setCellValue(a.getValue3());
										row.getCell(4).setCellValue(a.getValue4());
										row.getCell(5).setCellValue(a.getValue5());
										row.getCell(6).setCellValue(a.getValue6());
										row.getCell(7).setCellValue(a.getValue7());
										row.getCell(8).setCellValue(a.getValue8());
										row.getCell(9).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(10).setCellValue(a.getValue10());
											row.getCell(11).setCellValue(a.getValue11());
											row.getCell(12).setCellValue(a.getValue12());
											row.getCell(13).setCellValue(a.getValue13());
										} else {
											row.getCell(11).setCellValue(a.getValue10());
											row.getCell(12).setCellValue(a.getValue11());
											row.getCell(13).setCellValue(a.getValue12());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			} else {
				int b = 0;
				ExcelB a = map_e.get(key);
				// if(a==null){
				// System.out.println(x);
				// }
				String name = obj_e.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(0);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b == 0) {
										row.getCell(1).setCellValue(a.getValue1());
										row.getCell(2).setCellValue(a.getValue2());
										row.getCell(3).setCellValue(a.getValue3());
										row.getCell(4).setCellValue(a.getValue4());
										row.getCell(5).setCellValue(a.getValue5());
										row.getCell(6).setCellValue(a.getValue6());
										row.getCell(7).setCellValue(a.getValue7());
										row.getCell(8).setCellValue(a.getValue8());
										row.getCell(9).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(10).setCellValue(a.getValue10());
											row.getCell(11).setCellValue(a.getValue11());
											row.getCell(12).setCellValue(a.getValue12());
											row.getCell(13).setCellValue(a.getValue13());
										} else {
											row.getCell(11).setCellValue(a.getValue10());
											row.getCell(12).setCellValue(a.getValue11());
											row.getCell(13).setCellValue(a.getValue12());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			}
		}

		for (String key : map_f.keySet()) {
			if (key.equals("4．其他,4．其他") || key.equals("3．其他")) {
				int b = 0;
				ExcelB a = map_f.get(key);
				if (a != null) {
					// String name = obj_f.getString(key);
					String name = "4.其他";
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(14);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b > 0) {
										row.getCell(15).setCellValue(a.getValue1());
										row.getCell(16).setCellValue(a.getValue2());
										row.getCell(17).setCellValue(a.getValue3());
										row.getCell(18).setCellValue(a.getValue4());
										row.getCell(19).setCellValue(a.getValue5());
										row.getCell(20).setCellValue(a.getValue6());
										row.getCell(21).setCellValue(a.getValue7());
										row.getCell(22).setCellValue(a.getValue8());
										row.getCell(23).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(24).setCellValue(a.getValue10());
											row.getCell(25).setCellValue(a.getValue11());
											row.getCell(26).setCellValue(a.getValue12());
											row.getCell(27).setCellValue(a.getValue13());
										} else {
											// row.getCell(24).setCellValue(a.getValue9());
											row.getCell(25).setCellValue(a.getValue10());
											row.getCell(26).setCellValue(a.getValue11());
											row.getCell(27).setCellValue(a.getValue12());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			} else {
				int b = 0;
				ExcelB a = map_f.get(key);
				String name = obj_f.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							if (row != null) {
								Cell cellAfter = row.getCell(14);
								String StringAfter = cellString(cellAfter);
								if (StringAfter.equals(name)) {
									if (b == 0) {
										row.getCell(15).setCellValue(a.getValue1());
										row.getCell(16).setCellValue(a.getValue2());
										row.getCell(17).setCellValue(a.getValue3());
										row.getCell(18).setCellValue(a.getValue4());
										row.getCell(19).setCellValue(a.getValue5());
										row.getCell(20).setCellValue(a.getValue6());
										row.getCell(21).setCellValue(a.getValue7());
										row.getCell(22).setCellValue(a.getValue8());
										row.getCell(23).setCellValue(a.getValue9());
										if (a.isFlag() == true) {
											row.getCell(24).setCellValue(a.getValue10());
											row.getCell(25).setCellValue(a.getValue11());
											row.getCell(26).setCellValue(a.getValue12());
											row.getCell(27).setCellValue(a.getValue13());
										} else {
											// row.getCell(24).setCellValue(a.getValue9());
											row.getCell(25).setCellValue(a.getValue10());
											row.getCell(26).setCellValue(a.getValue11());
											row.getCell(27).setCellValue(a.getValue12());
										}
									}
									b++;
								}
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			}
		}
		obj.put("msg1", msg1);
		obj.put("msg2", msg2);
		return obj;
	}

	public String setExcelData3(Sheet hssfSheet, Map<String, ExcelA> map_d) {
		String msg = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_d.keySet()) {
			ExcelA a = map_d.get(key);
			String name = obj_d.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (StringAfter.equals(name)) {
							row.getCell(1).setCellValue(a.getValue2());
							row.getCell(2).setCellValue(a.getValue3());
							row.getCell(3).setCellValue(a.getValue1());
						}
					} catch (Exception e) {
						msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}
		return msg;
	}

	public String setExcelData2(Sheet hssfSheet, Map<String, ExcelA> map_c, int flag) {
		String msg = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_c.keySet()) {
			ExcelA a = map_c.get(key);
			String name = obj_c.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (flag == 1) {
							if (StringAfter.equals("一、营业总收入")) {
								ExcelA b = map_c.get("一、营业收入");
								if (b != null) {
									row.getCell(1).setCellValue(b.getValue2());
									row.getCell(2).setCellValue(b.getValue3());
									row.getCell(3).setCellValue(b.getValue1());
								}
							} else {
								if (StringAfter.equals(name)) {
									row.getCell(1).setCellValue(a.getValue2());
									row.getCell(2).setCellValue(a.getValue3());
									row.getCell(3).setCellValue(a.getValue1());
									break;
								}
							}
						} else {
							if (StringAfter.equals(name)) {
								row.getCell(1).setCellValue(a.getValue2());
								row.getCell(2).setCellValue(a.getValue3());
								row.getCell(3).setCellValue(a.getValue1());
								break;
							}
						}
					} catch (Exception e) {
						msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}
		return msg;
	}

	public JSONObject setExcelData(Sheet hssfSheet, Map<String, ExcelA> map_a, Map<String, ExcelA> map_b) {
		JSONObject obj = new JSONObject();
		String msg1 = "";
		String msg2 = "";
		int rowCount = hssfSheet.getPhysicalNumberOfRows();
		for (String key : map_a.keySet()) {
			ExcelA a = map_a.get(key);
			String name = obj_a.getString(key);
			if (name != null && !name.equals("")) {
				for (int i = 1; i < rowCount; i++) {
					try {
						Row row = hssfSheet.getRow(i);
						Cell cellAfter = row.getCell(0);
						String StringAfter = cellString(cellAfter);
						if (StringAfter.equals(name)) {
							row.getCell(1).setCellValue(a.getValue2());
							row.getCell(2).setCellValue(a.getValue3());
							row.getCell(3).setCellValue(a.getValue1());
						}
					} catch (Exception e) {
						msg1 = msg1 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
						e.printStackTrace();
					}
				}
			}
		}

		for (String key : map_b.keySet()) {
			if (key.split(",").length > 1) {
				int names = 0;
				ExcelA a = map_b.get(key);
				String name = obj_b.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							Cell cellAfter = row.getCell(4);
							String StringAfter = cellString(cellAfter);
							if (StringAfter.equals(name)) {
								if (names > 0) {
									row.getCell(5).setCellValue(a.getValue2());
									row.getCell(6).setCellValue(a.getValue3());
									row.getCell(7).setCellValue(a.getValue1());
									break;
								}
								names++;
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			} else {
				ExcelA a = map_b.get(key);
				String name = obj_b.getString(key);
				if (name != null && !name.equals("")) {
					for (int i = 1; i < rowCount; i++) {
						try {
							Row row = hssfSheet.getRow(i);
							Cell cellAfter = row.getCell(4);
							String StringAfter = cellString(cellAfter);
							if (StringAfter.equals(name)) {
								row.getCell(5).setCellValue(a.getValue2());
								row.getCell(6).setCellValue(a.getValue3());
								row.getCell(7).setCellValue(a.getValue1());
								break;
							}
						} catch (Exception e) {
							msg2 = msg2 + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
							e.printStackTrace();
						}
					}
				}
			}
		}
		obj.put("msg1", msg1);
		obj.put("msg2", msg2);
		return obj;
	}

	/**
	 * 所有者权益表
	 * 
	 * @param hssfSheet2
	 * @return
	 */
	public Map<String, ExcelB> getExcelData6(Sheet hssfSheet2) {
		String msg = "";
		boolean flag = false;
		ExcelB b = new ExcelB();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelB> map = new HashMap<String, ExcelB>();
		for (int i = 2; i < 10; i++) {
			Row row = hssfSheet2.getRow(i);
			if (row != null) {
				for (int j = 2; j < 15; j++) {
					Cell cellAfter = row.getCell(j);
					String StringAfter = cellString2(cellAfter);
					if (StringAfter.indexOf("风险准备") > -1) {
						flag = true;
						break;
					}
				}
			}
		}
		for (int i = 2; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				if (row != null) {
					Cell cellAfter = row.getCell(0);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						Cell cellbefor2 = row.getCell(2);
						String Stringbefor2 = cellString2(cellbefor2);
						Cell cellbefor3 = row.getCell(3);
						String Stringbefor3 = cellString2(cellbefor3);
						Cell cellbefor4 = row.getCell(4);
						String Stringbefor4 = cellString2(cellbefor4);
						Cell cellbefor5 = row.getCell(5);
						String Stringbefor5 = cellString2(cellbefor5);
						Cell cellbefor6 = row.getCell(6);
						String Stringbefor6 = cellString2(cellbefor6);
						Cell cellbefor7 = row.getCell(7);
						String Stringbefor7 = cellString2(cellbefor7);
						Cell cellbefor8 = row.getCell(8);
						String Stringbefor8 = cellString2(cellbefor8);
						Cell cellbefor9 = row.getCell(9);
						String Stringbefor9 = cellString2(cellbefor9);
						Cell cellbefor10 = row.getCell(10);
						String Stringbefor10 = cellString2(cellbefor10);
						Cell cellbefor11 = row.getCell(11);
						String Stringbefor11 = cellString2(cellbefor11);
						Cell cellbefor12 = row.getCell(12);
						String Stringbefor12 = cellString2(cellbefor12);
						Cell cellbefor13 = row.getCell(13);
						String Stringbefor13 = cellString2(cellbefor13);
						String aa = "";
						ExcelB a = new ExcelB();
						a.setValue1(Stringbefor2);
						a.setValue2(Stringbefor3);
						a.setValue3(Stringbefor4);
						a.setValue4(Stringbefor5);
						a.setValue5(Stringbefor6);
						a.setValue6(Stringbefor7);
						a.setValue7(Stringbefor8);
						a.setValue8(Stringbefor9);
						a.setValue9(Stringbefor10);
						a.setValue10(Stringbefor11);
						a.setValue11(Stringbefor12);
						a.setValue12(Stringbefor13);
						a.setFlag(flag);
						if (map.containsKey(StringAfter)) {
							aa = StringAfter + "," + StringAfter;
						}
						if (map.containsKey(aa)) {
							aa = aa + "," + StringAfter;
						}
						if (!aa.equals("")) {
							map.put(aa, a);
						} else {
							map.put(StringAfter, a);
						}
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		ExcelB value = null;
		if (map.containsKey("3．其他")) {
			for (String key : map.keySet()) {
				if (key.equals("4．其他,4．其他")) {
					value = map.get("4．其他,4．其他");
					map.put(key, null);
				}
			}
			map.put("4．其他,4．其他,4．其他", value);
		}
		return map;
	}

	/**
	 * 合并所有者权益表
	 * 
	 * @param hssfSheet2
	 * @return
	 */
	public Map<String, ExcelB> getExcelData4(Sheet hssfSheet2) {
		String msg = "";
		boolean flag = false;
		ExcelB b = new ExcelB();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelB> map = new HashMap<String, ExcelB>();
		for (int i = 2; i < 10; i++) {
			Row row = hssfSheet2.getRow(i);
			if (row != null) {
				for (int j = 2; j < 15; j++) {
					Cell cellAfter = row.getCell(j);
					String StringAfter = cellString2(cellAfter);
					if (StringAfter.indexOf("风险准备") > -1) {
						flag = true;
						break;
					}
				}
			}
		}
		for (int i = 2; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				if (row != null) {
					Cell cellAfter = row.getCell(0);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						Cell cellbefor2 = row.getCell(2);
						String Stringbefor2 = cellString2(cellbefor2);
						Cell cellbefor3 = row.getCell(3);
						String Stringbefor3 = cellString2(cellbefor3);
						Cell cellbefor4 = row.getCell(4);
						String Stringbefor4 = cellString2(cellbefor4);
						Cell cellbefor5 = row.getCell(5);
						String Stringbefor5 = cellString2(cellbefor5);
						Cell cellbefor6 = row.getCell(6);
						String Stringbefor6 = cellString2(cellbefor6);
						Cell cellbefor7 = row.getCell(7);
						String Stringbefor7 = cellString2(cellbefor7);
						Cell cellbefor8 = row.getCell(8);
						String Stringbefor8 = cellString2(cellbefor8);
						Cell cellbefor9 = row.getCell(9);
						String Stringbefor9 = cellString2(cellbefor9);
						Cell cellbefor10 = row.getCell(10);
						String Stringbefor10 = cellString2(cellbefor10);
						Cell cellbefor11 = row.getCell(11);
						String Stringbefor11 = cellString2(cellbefor11);
						Cell cellbefor12 = row.getCell(12);
						String Stringbefor12 = cellString2(cellbefor12);
						Cell cellbefor13 = row.getCell(13);
						String Stringbefor13 = cellString2(cellbefor13);
						Cell cellbefor14 = row.getCell(14);
						String Stringbefor14 = cellString2(cellbefor14);
						String aa = "";
						ExcelB a = new ExcelB();
						a.setValue1(Stringbefor2);
						a.setValue2(Stringbefor3);
						a.setValue3(Stringbefor4);
						a.setValue4(Stringbefor5);
						a.setValue5(Stringbefor6);
						a.setValue6(Stringbefor7);
						a.setValue7(Stringbefor8);
						a.setValue8(Stringbefor9);
						a.setValue9(Stringbefor10);
						a.setValue10(Stringbefor11);
						a.setValue11(Stringbefor12);
						a.setValue12(Stringbefor13);
						a.setValue13(Stringbefor14);
						a.setFlag(flag);
						if (map.containsKey(StringAfter)) {
							aa = StringAfter + "," + StringAfter;
						}
						if (map.containsKey(aa)) {
							aa = aa + "," + StringAfter;
						}
						if (!aa.equals("")) {
							map.put(aa, a);
						} else {
							map.put(StringAfter, a);
						}
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		ExcelB value = null;
		if (map.containsKey("3．其他")) {
			for (String key : map.keySet()) {
				if (key.equals("4．其他,4．其他")) {
					value = map.get("4．其他,4．其他");
					map.put(key, null);
				}
			}
			map.put("4．其他,4．其他,4．其他", value);
		}
		return map;
	}

	public Map<String, ExcelA> getExcelData3(Sheet hssfSheet2) {
		String msg = "";
		ExcelA b = new ExcelA();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelA> map = new HashMap<String, ExcelA>();
		for (int i = 5; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				if (row != null) {
					Cell cellAfter = row.getCell(1);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						Cell cellbefor1 = row.getCell(2);
						String Stringbefor1 = cellString2(cellbefor1);
						Cell cellbefor2 = row.getCell(4);
						String Stringbefor2 = cellString2(cellbefor2);
						Cell celldiff = row.getCell(5);
						String Stringdiff = cellString2(celldiff);
						ExcelA a = new ExcelA();
						a.setValue1(Stringbefor1);
						a.setValue2(Stringbefor2);
						a.setValue3(Stringdiff);
						if (map.containsKey(StringAfter)) {
							StringAfter = StringAfter + "," + StringAfter;
						}
						map.put(StringAfter, a);
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				// e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		return map;
	}

	public Map<String, ExcelA> getExcelData5(Sheet hssfSheet2) {
		String msg = "";
		ExcelA b = new ExcelA();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelA> map = new HashMap<String, ExcelA>();
		for (int i = 1; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				int columnNum = hssfSheet2.getRow(i).getPhysicalNumberOfCells();
				if (row != null) {
					Cell cellAfter = row.getCell(1);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						String Stringbefor1 = "0.00";
						String Stringbefor2 = "0.00";
						String Stringdiff = "0.00";
						if (StringAfter.equals("(一)基本每股收益") || StringAfter.equals("(二)稀释每股收益")) {
							Cell cellbefor1 = row.getCell(2);
							Stringbefor1 = cellString3(cellbefor1);
							Cell cellbefor2 = row.getCell(4);
							Stringbefor2 = cellString3(cellbefor2);
							Cell celldiff = null;
							try {
								celldiff = row.getCell(7);
							} catch (Exception e) {
								celldiff = row.getCell(5);
							}
							if (celldiff == null) {
								celldiff = row.getCell(5);
							}
							Stringdiff = cellString3(celldiff);
						} else {
							Cell cellbefor1 = row.getCell(2);
							Stringbefor1 = cellString2(cellbefor1);
							Cell cellbefor2 = row.getCell(4);
							Stringbefor2 = cellString2(cellbefor2);
							Cell celldiff = null;
							try {
								celldiff = row.getCell(7);
							} catch (Exception e) {
								celldiff = row.getCell(5);
							}
							if (celldiff == null) {
								celldiff = row.getCell(5);
							}
							Stringdiff = cellString2(celldiff);
						}
						ExcelA a = new ExcelA();
						a.setValue1(Stringbefor1);
						a.setValue2(Stringbefor2);
						a.setValue3(Stringdiff);
						if (map.containsKey(StringAfter)) {
							StringAfter = StringAfter + "," + StringAfter;
						}
						map.put(StringAfter, a);
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		return map;
	}

	public Map<String, ExcelA> getExcelData(Sheet hssfSheet2) {
		String msg = "";
		ExcelA b = new ExcelA();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelA> map = new HashMap<String, ExcelA>();
		for (int i = 1; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				if (row != null) {
					Cell cellAfter = row.getCell(1);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						Cell cellbefor1 = row.getCell(2);
						String Stringbefor1 = cellString2(cellbefor1);
						Cell cellbefor2 = row.getCell(4);
						String Stringbefor2 = cellString2(cellbefor2);
						Cell celldiff = row.getCell(5);
						String Stringdiff = cellString2(celldiff);
						ExcelA a = new ExcelA();
						a.setValue1(Stringbefor1);
						a.setValue2(Stringbefor2);
						a.setValue3(Stringdiff);
						if (map.containsKey(StringAfter)) {
							StringAfter = StringAfter + "," + StringAfter;
						}
						map.put(StringAfter, a);
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		return map;
	}

	public Map<String, ExcelA> getExcelData2(Sheet hssfSheet2) {
		String msg = "";
		ExcelA b = new ExcelA();
		int rowCount2 = hssfSheet2.getPhysicalNumberOfRows();
		Map<String, ExcelA> map = new HashMap<String, ExcelA>();
		for (int i = 1; i < rowCount2; i++) {
			try {
				Row row = hssfSheet2.getRow(i);
				if (row != null) {
					Cell cellAfter = row.getCell(0);
					String StringAfter = cellString(cellAfter);

					if (StringAfter != null && !StringAfter.equals("")) {
						Cell cellbefor1 = row.getCell(1);
						String Stringbefor1 = cellString2(cellbefor1);
						Cell cellbefor2 = row.getCell(3);
						String Stringbefor2 = cellString2(cellbefor2);
						Cell celldiff = row.getCell(4);
						String Stringdiff = cellString2(celldiff);
						ExcelA a = new ExcelA();
						a.setValue1(Stringbefor1);
						a.setValue2(Stringbefor2);
						a.setValue3(Stringdiff);
						if (map.containsKey(StringAfter)) {
							StringAfter = StringAfter + "," + StringAfter;
						}
						map.put(StringAfter, a);
					}
				}
			} catch (Exception e) {
				msg = msg + "   第" + (i + 1) + "行出错 " + e.getMessage() + "\r\n";
				e.printStackTrace();
			}
		}
		b.setMsg(msg);
		map.put("msg", b);
		return map;
	}

	public static String cellString(Cell cell) {
		String cellString = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:// 数字
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
				cellString = df.format(cell.getNumericCellValue());
				// cellString = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:// 字符串
				cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式
				try {
					java.text.DecimalFormat df1 = new java.text.DecimalFormat("0.00");
					cellString = df1.format(cell.getNumericCellValue());
				} catch (Exception e) {
					cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				}
				break;
			default:
				cellString = cell.toString().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				break;
			}
		}
		return cellString;
	}

	public static String cellString2(Cell cell) {
		String cellString = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:// 数字
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
				// double value = cell.getNumericCellValue();
				// if(value < 0.005D && value > 0){
				// df = new java.text.DecimalFormat("0.000");
				// }
				cellString = df.format(cell.getNumericCellValue());
				// cellString = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:// 字符串
				cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "")
						.replaceAll("\r\n", "").replaceAll("\n", "");
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式
				try {
					java.text.DecimalFormat df1 = new java.text.DecimalFormat("0.00");
					// BigDecimal bigDecimal1 = new
					// BigDecimal(cell.getNumericCellValue());
					// BigDecimal bigDecimal2 = new
					// BigDecimal(cell.getNumericCellValue());
					// bigDecimal1 = bigDecimal1.setScale(2,
					// BigDecimal.ROUND_DOWN);
					// bigDecimal2 = bigDecimal2.setScale(3,
					// BigDecimal.ROUND_DOWN);
					double value2 = cell.getNumericCellValue();
					// if(value < 0.005D && value > 0){
					// df1 = new java.text.DecimalFormat("0.000");
					// }
					// cellString = (bigDecimal1.compareTo(bigDecimal2) == -1) ?
					// bigDecimal2.toPlainString():bigDecimal1.toPlainString();
					cellString = df1.format(value2);
				} catch (Exception e) {
					try {
						cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ",
								"");
					} catch (Exception ex) {
						if (ex.getMessage().contains("Cannot get a text value from a boolean formula cell")) {
							cellString = "";
						} else {
							throw ex;
						}
					}
				}
				break;
			default:
				cellString = cell.toString().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				break;
			}
		}
		if (cellString.contains("本期增减变动金额")) {
			System.out.println(cellString);
		}
		return cellString;
	}

	public static String cellString3(Cell cell) {
		String cellString = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:// 数字
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
				cellString = df.format(cell.getNumericCellValue());
				// cellString = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:// 字符串
				cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式
				try {
					java.text.DecimalFormat df1 = new java.text.DecimalFormat("0.00");
					cellString = df1.format(cell.getNumericCellValue());
				} catch (Exception e) {
					cellString = cell.getStringCellValue().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				}
				break;
			default:
				cellString = cell.toString().replaceAll("　", "").replaceAll("  ", "").replaceAll(" ", "");
				break;
			}
		}
		return cellString;
	}

	public JSONObject getOneToOne(Sheet hssfSheet, int i, JSONObject obj) {
		Row row = hssfSheet.getRow(i);
		Cell cellAfter = row.getCell(1);
		String StringAfter = cellString(cellAfter);
		Cell cellbefor1 = row.getCell(3);
		String Stringbefor1 = cellString(cellbefor1);
		Cell cellbefor2 = row.getCell(6);
		String Stringbefor2 = cellString(cellbefor2);
		Cell celldiff = row.getCell(8);
		String Stringdiff = cellString(celldiff);
		if (Stringdiff.equals("相同")) {
			executeJsonObj(obj, StringAfter, Stringbefor2);
			// if (Stringbefor2 != null && !Stringbefor2.equals("")) {
			// if (obj.containsKey(Stringbefor2)) {
			// Stringbefor2 = Stringbefor2 + "," + Stringbefor2;
			// }
			// obj.put(Stringbefor2, StringAfter);
			// }
		} else {
			executeJsonObj(obj, StringAfter, Stringbefor1);
			executeJsonObj(obj, StringAfter, Stringbefor2);
			// if (Stringbefor1 != null && !Stringbefor1.equals("")) {
			// if (obj.containsKey(Stringbefor1)) {
			// Stringbefor1 = Stringbefor1 + "," + Stringbefor1;
			// }
			// obj.put(Stringbefor1, StringAfter);
			// }
			// if (Stringbefor2 != null && !Stringbefor2.equals("")) {
			// if (obj.containsKey(Stringbefor2)) {
			// Stringbefor2 = Stringbefor2 + "," + Stringbefor2;
			// }
			// obj.put(Stringbefor2, StringAfter);
			// }
		}
		return obj;
	}

	public JSONObject getOneToOne2(Sheet hssfSheet, int i, JSONObject obj) {
		Row row = hssfSheet.getRow(i);
		Cell cellAfter = row.getCell(1);
		String StringAfter = cellString(cellAfter);
		Cell cellbefor1 = row.getCell(3);
		String Stringbefor1 = cellString(cellbefor1);
		Cell cellbefor2 = row.getCell(6);
		String Stringbefor2 = cellString(cellbefor2);
		Cell celldiff = row.getCell(8);
		String Stringdiff = cellString(celldiff);
		if (Stringdiff.equals("相同")) {
			String aa = "";
			if (Stringbefor2 != null && !Stringbefor2.equals("")) {
				if (obj.containsKey(Stringbefor2)) {
					aa = Stringbefor2 + "," + Stringbefor2;
				}
				if (obj.containsKey(aa)) {
					aa = aa + "," + Stringbefor2;
				}
				if (!aa.equals("")) {
					obj.put(aa, StringAfter);
				} else {
					obj.put(Stringbefor2, StringAfter);
				}
			}
		} else {
			String aa = "";
			String bb = "";
			if (Stringbefor1 != null && !Stringbefor1.equals("")) {
				if (obj.containsKey(Stringbefor1)) {
					aa = Stringbefor1 + "," + Stringbefor1;
				}
				if (obj.containsKey(aa)) {
					aa = aa + "," + Stringbefor2;
				}
				if (!aa.equals("")) {
					obj.put(aa, StringAfter);
				} else {
					obj.put(Stringbefor1, StringAfter);
				}
			}
			if (Stringbefor2 != null && !Stringbefor2.equals("")) {
				if (obj.containsKey(Stringbefor2)) {
					bb = Stringbefor2 + "," + Stringbefor2;
				}
				if (obj.containsKey(bb)) {
					bb = bb + "," + Stringbefor2;
				}
				if (!bb.equals("")) {
					obj.put(bb, StringAfter);
				} else {
					obj.put(Stringbefor2, StringAfter);
				}
			}
		}
		return obj;
	}

}
