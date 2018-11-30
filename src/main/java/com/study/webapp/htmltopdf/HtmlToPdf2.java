package com.study.webapp.htmltopdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;

public class HtmlToPdf2 {

	/**
	 * 将HTML转成PD格式的文件。html文件的格式比较严格
	 * 
	 * @param htmlFile
	 * @param pdfFile
	 * @throws Exception
	 */
	public static void htmlToPdf(String htmlFile, String pdfFile) throws Exception {

		String url = new File(htmlFile).toURI().toURL().toString();
		System.out.println(url);

		OutputStream os = new FileOutputStream(pdfFile);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(url);

		// step 3 解决中文支持
		ITextFontResolver fontResolver = renderer.getFontResolver();
		if ("linux".equals(getCurrentOperatingSystem())) {
			fontResolver.addFont("/usr/share/fonts/chiness/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} else {
			fontResolver.addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		}

		renderer.layout();
		renderer.createPDF(os);
		os.close();

		System.out.println("create pdf done!!");

	}

	public static String getCurrentOperatingSystem() {
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("---------当前操作系统是-----------" + os);
		return os;
	}

	public static void main(String[] args) {
		String htmlFile = "d:/topdf.html";
		String pdfFile = "d:/login.pdf";
		try {
			File file = new File(pdfFile);
			FileInputStream input = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(file.getName(), input);
			HtmlToPdf2.htmlToPdf(htmlFile, pdfFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
