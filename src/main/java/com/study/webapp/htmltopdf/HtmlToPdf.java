package com.study.webapp.htmltopdf;

import java.io.File;

public class HtmlToPdf {

	// wkhtmltopdf��ϵͳ�е�·��
	private static final String toPdfTool = "F:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";

	private static final String toPdfLunix = "wkhtmltopdf";

	/**
	 * htmlתpdf
	 * 
	 * @param srcPath
	 *            html·����������Ӳ���ϵ�·����Ҳ����������·��
	 * @param destPath
	 *            pdf����·��
	 * @return ת���ɹ�����true
	 */
	public static boolean convert(String srcPath, String destPath) {
		File file = new File(destPath);
		File parent = file.getParentFile();
		// ���pdf����·�������ڣ��򴴽�·��
		if (!parent.exists()) {
			parent.mkdirs();
		}
		String exe = "";
		if ("linux".equals(getCurrentOperatingSystem())) {
			exe = toPdfLunix;
		} else {
			exe = toPdfTool;
		}

		StringBuilder cmd = new StringBuilder();
		cmd.append(exe);
		cmd.append(" ");
		cmd.append(srcPath);
		cmd.append(" ");
		cmd.append(destPath);

		boolean result = true;
		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());
			HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
			HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
			error.start();
			output.start();
			proc.waitFor();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	public static String getCurrentOperatingSystem() {
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("---------��ǰ����ϵͳ��-----------" + os);
		return os;
	}

	public static void main(String[] args) {
		String htmlFile = "/opt/topdf.html";
		String pdfFile = "/opt/topdf.pdf";
		try {
			boolean flag = HtmlToPdf.convert(htmlFile, pdfFile);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
