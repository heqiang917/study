// package com.study.webapp.lowagie;
//
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.FileOutputStream;
//
// import javax.imageio.ImageIO;
//
// import com.lowagie.text.Document;
// import com.lowagie.text.Image;
// import com.lowagie.text.Rectangle;
// import com.lowagie.text.pdf.PdfWriter;
//
// public class ImageToPDF {
//
// public static void main(String[] args) {
// long time1 = System.currentTimeMillis();
// toPDF("D:/资料/工作/", "D:/资料/工作/新建文件夹/ceshi.pdf");
// long time2 = System.currentTimeMillis();
// int time = (int) ((time2 - time1) / 1000);
// System.out.println("执行了：" + time + "秒！");
// }
//
// public static void toPDF(String imageFolderPath, String pdfPath) {
// try {
// String imagePath = null;
// FileOutputStream fos = new FileOutputStream(pdfPath);
// Document doc = new Document(null, 0, 0, 0, 0);
// PdfWriter.getInstance(doc, fos);
// BufferedImage img = null;
// Image image = null;
// File file = new File(imageFolderPath);
// File[] files = file.listFiles();
// for (File f : files) {
// if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg") ||
// f.getName().endsWith(".gif")
// || f.getName().endsWith(".jpeg") || f.getName().endsWith(".tif")) {
// imagePath = imageFolderPath + f.getName();
// img = ImageIO.read(new File(imagePath));
// doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
// image = Image.getInstance(imagePath);
// doc.open();
// doc.add(image);
//
// }
// }
// doc.close();
// } catch (Exception e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// }
//
// }
