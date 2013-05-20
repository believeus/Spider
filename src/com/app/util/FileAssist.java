package com.app.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

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
		public BufferedReader newBufferedReader(String fileName) {
			BufferedReader br = null;
			try {
				File file = new File(fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileInputStream fis = new FileInputStream(file);
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

	// 从文件中读取url
	public static Set<String> ReadAndFilterUrlsFromFile(String fileName) {
		Set<String> urls = new HashSet<String>();
		try {
			FileAssist.MyBufferedReader myBufferedReader = new FileAssist.MyBufferedReader();
			BufferedReader br = myBufferedReader.newBufferedReader(fileName);
			String line = "";
			String filterKey = PropertiesAssist.getPropetiesValue(PropertiesAssist.filterKey);
			while ((line = br.readLine()) != null) {
				// System.out.println(filterKey+"="+line);
				// if (!line.contains(filterKey)) {
				// continue;
				// }
				if (!urls.contains(line)) {
					urls.add(line);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	// 从propertiesXML文件中读取url
	public static Set<String> getUrlsFromPropertiesXMLFile(String fileName) {
		Set<String> urls = new HashSet<String>();
		Properties properties = new Properties();
		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			properties.loadFromXML(fis);
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				urls.add(properties.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urls;
	}

	// 将没有读取过的url放到另一个文件中
	public static void findUnReadUrlToOtherFile(String PathNameOfAllUrlByDeepFind,
			String logFileName) {
		try {
			String unReadUrlSavePath = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.unReadUrlSavePath);
			File file = new File(unReadUrlSavePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileAssist.MyPrintWriter myPrintWriter = new FileAssist.MyPrintWriter();
			BufferedWriter bw = myPrintWriter.newPrintWriter(unReadUrlSavePath);
			Set<String> allUrls = ReadAndFilterUrlsFromFile(PathNameOfAllUrlByDeepFind);
			Set<String> logUrls = ReadAndFilterUrlsFromFile(logFileName);
			Iterator<String> iterator = allUrls.iterator();
			int index = 0;
			while (iterator.hasNext()) {
				String url = (String) iterator.next();
				index++;

				System.out.println(url);
				if (!logUrls.contains(url)) {
					bw.write(url);
					bw.newLine();
					bw.flush();
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	// 合并文件
	public static void mergeFile(String filePath) {
		Vector<FileInputStream> vector = new Vector<FileInputStream>();
		File file = new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.isFile()) {
					System.out.println(file2.getAbsolutePath());
					try {
						vector.add(new FileInputStream(file2.getAbsolutePath()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			Enumeration<FileInputStream> enumeration = vector.elements();
			SequenceInputStream sis = new SequenceInputStream(enumeration);
			InputStreamReader reader = new InputStreamReader(sis);
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			FileAssist.MyPrintWriter myPrintWriter = new FileAssist.MyPrintWriter();
			BufferedWriter bw = myPrintWriter.newPrintWriter("D:\\URL\\6\\url\\mergeFile.txt");
			try {
				while ((line = br.readLine()) != null) {
					bw.write(line);
					bw.newLine();
					bw.flush();
				}
				bw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(vector.size());
		}
	}

	// 获得文件名
	public static List<String> getFlieName(String filePath) {
		List<String> fileNames = new ArrayList<String>();
		File file = new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				String name = file2.getName();
				// System.out.println(name);
				fileNames.add(name);
			}
		}
		return fileNames;
	}
}
