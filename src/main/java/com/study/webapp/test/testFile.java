package com.study.webapp.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class testFile {
	public static void main(String[] args) throws FileNotFoundException, IOException {

		File list[] = new File("C:\\home\\upload").listFiles();
		File file = new File("C:\\home\\upload\\3.txt");
		file.createNewFile();
		RandomAccessFile in = new RandomAccessFile(file, "rw");
		in.setLength(0);
		in.seek(0);
		int len = -1;
		byte[] bytes = new byte[1024];
		for (int i = 0; i < list.length; i++) {
			RandomAccessFile out = new RandomAccessFile(list[i], "rw");
			while ((len = out.read(bytes)) != -1) {
				in.write(bytes, 0, len);
			}
			out.close();
		}
		in.close();
	}
}
