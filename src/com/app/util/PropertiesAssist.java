package com.app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesAssist {

	// 配置文件路径
	private static final String ConfigFile = "config.properties";
	// 匹配的url地址模式
	public static final String MATCHERURL = "PatternMatcherUrl";
	public static final String FILTER_URL_MATCHER = "filterUrlMatcher";
	// 是否开启搜索url地址
	public static final String isSearchUrls = "isSearchUrls";
	// url地址保存路径
	public static final String URL_SAVE_PATH = "UrlSavePath";
	// 读取网页内容使用什么编码
	public static final String charset = "charset";
	// 是否将文章保存到文件中
	public static final String ISToBeSaveArticalToFile = "isToBeSaveArticalToFile";
	public static final String ContentInfoClazz = "ContentInfoclazz";
	public static String ToBeSaveArticalPath = "ToBeSaveArticalPath";
	public static String articleBeginTitleTag = "articleBeginTitleTag";
	public static String articleEndTitleTag="articleEndTitleTag";
	public static String articleContentTag = "articleContentTag";
	public static String articleContentEndTag = "articleContentEndTag";
	public static String articleContentBeginTag = "articleContentBeginTag";
	public static final String urlReg = "urlReg";
	public static String searchUrlClazz = "searchUrlClazz";
	public static String endPageIndex = "endPageIndex";
	public static String indexUrl = "indexUrl";
	public static String nextIndexUrl = "nextIndexUrl";
	public static String firstSearchURLBeginPosition = "firstSearchURLBeginPosition";
	public static String firstSearchURLEndPosition = "firstSearchURLEndPosition";
	public static String isProcessFirstFindURL = "isProcessFirstFindURL";
	public static String isProcessSecondFindURL = "isProcessSecondFindURL";
	public static String SecondSearchURLBeginPosition = "SecondSearchURLBeginPosition";
	public static String SecondSearchURLEndPosition = "SecondSearchURLEndPosition";
	public static String HostName = "HostName";
	public static String isSaveIndexUrl = "isSaveIndexUrl";
	public static String URL_REPLACE_CLAZZ="URLReplaceClazz";
	
	// 通过配置文件的key获取配置的value值
	public static Properties properties = new Properties();
	static {
		try {
			InputStream resourceAsStream = PropertiesAssist.class.getClassLoader()
					.getResourceAsStream(ConfigFile);
			properties.load(resourceAsStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getPropetiesValue(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	public static void saveURLs(Set<String> urlSet) {
		if (urlSet != null && !urlSet.isEmpty()) {
			String dir = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.URL_SAVE_PATH);
			try {
				File file = new File(dir);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter write = new FileWriter(file, true);
				BufferedWriter writer = new BufferedWriter(write);
				Iterator<String> iterator = urlSet.iterator();
				boolean newLine = true;
				while (iterator.hasNext()) {
				
					if (newLine) {
						writer.newLine();
						newLine = false;
					}
					String url = (String) iterator.next();
					writer.write(url);
					writer.newLine();
				}
				writer.flush();
				writer.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

	private static Pattern regexWord = Pattern.compile("[^u4E00-u9FA5]+", Pattern.CASE_INSENSITIVE
			| Pattern.UNICODE_CASE);
	static BufferedWriter writerPic = null;
	static BufferedWriter writerWord = null;
	int wordCount = 0;

	public static void sortUrl(BufferedReader br, String url, String filePath) {
		System.out.println(url);
		System.out.println(filePath);
		try {
			int count = 0;
			writerPic = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(filePath
					+ "pic.txt", true))));
			writerWord = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(filePath
					+ "Info.txt", true))));
			String line = "";
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\s+|/|\"|\\.|;|:|'|-|%|&|#", "");
				System.out.println(line);
				Matcher matcher = regexWord.matcher(line);
				if (line.contains("pageLink")) {
					if (count > 0) {
						writerWord.write(url + "\r\n");
						writerWord.flush();
						writerWord.close();
						System.out.println("Info");
					} else {
						writerPic.write(url + "\r\n");
						writerPic.flush();
						writerPic.close();
						System.out.println("Pic");
					}
					count = 0;
					break;
				}
				if (line.contains("strong") || line.contains("font")) {
					continue;
				}
				// ÕÒµœºº×Ö
				while (matcher.find()) {
					count += matcher.group().length();
					if (count > 15) {
						break;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	List<String> unReadUrls = new ArrayList<String>();

}