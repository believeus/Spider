package com.app.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FileAssist {
	public static class MyPrintWriter {
		public BufferedWriter newPrintWriter(String filePath) {
			FileOutputStream fos = null;
			OutputStreamWriter writer = null;
			BufferedWriter bw = null;
			try {
				File file = new File(filePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file, true);
				writer = new OutputStreamWriter(fos);
				bw = new BufferedWriter(writer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bw;
		}
	}

	public static class MyBufferedReader {
		private FileInputStream fis;
		public BufferedReader newBufferedReader(String fileName) {
			BufferedReader br = null;
			try {
				File file = new File(fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				fis = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(fis);
				br = new BufferedReader(reader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return br;
		}
	}

	public static class MyBufferedOutputStream {
		BufferedOutputStream bos = null;

		public BufferedOutputStream newBufferedOutStream(String fileFolder, String fileName) {
			File file = new File(fileFolder);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				OutputStream is = new FileOutputStream(fileFolder + fileName);
				bos = new BufferedOutputStream(is);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return bos;

		}
	}

	public static Set<String> ReadAndFilterUrlsFromFile(String fileName) {
		Set<String> urls = new HashSet<String>();
		try {
			FileAssist.MyBufferedReader myBufferedReader = new FileAssist.MyBufferedReader();
			BufferedReader br = myBufferedReader.newBufferedReader(fileName);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (!urls.contains(line)) {
					urls.add(line);
				}else {
					System.out.println(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	public static void writeUrlsToFile(Set<String> urls, String filePath) {
		try {
			FileAssist.MyPrintWriter myPrintWriter = new FileAssist.MyPrintWriter();
			BufferedWriter bw = myPrintWriter.newPrintWriter(filePath);
			Iterator<String> iterator = urls.iterator();
			while (iterator.hasNext()) {
				String url = (String) iterator.next();
				iterator.remove();
				bw.write(url);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
