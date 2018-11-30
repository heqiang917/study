package com.study.webapp.htmltopdf2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class Html2pdf {

	public static void htmlTopdf(String html, File file) throws Exception {
		try {
			// step 1
			Document document = new Document();
			BaseFont bfChinese;
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
			MyFontProvider myFontProvider = new MyFontProvider(BaseColor.BLACK, "", "", false, false, 16, 1, bfChinese);
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			// step 3
			document.open();

			final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
			tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
			tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);

			final CssFilesImpl cssFiles = new CssFilesImpl();
			cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
			final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
			final HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(myFontProvider));
			hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
			final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, writer));
			final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

			final XMLWorker worker = new XMLWorker(pipeline, true);

			final Charset charset = Charset.forName("UTF-8");
			final XMLParser xmlParser = new XMLParser(true, worker, charset);

			ByteArrayInputStream bais = new ByteArrayInputStream(html.getBytes("UTF-8"));
			xmlParser.parse(bais, charset);

			// step 5
			document.close();
			bais.close();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static void main(String[] args) {
		String content = "<html><head><meta content=\"text/html;charset=UTF-8\"></meta></head><body><p>哈哈</p></body></html>";
		File file = new File("d:/pdf2.pdf");
		try {
			htmlTopdf(content, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
