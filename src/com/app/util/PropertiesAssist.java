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

	// Ö÷Ò³
	public static final String homePage = "HomePage";
	// ÅäÖÃÎÄŒþµÄÂ·Ÿ¶
	private static final String ConfigFile = "config.properties";
	// URLµØÖ·Æ¥ÅäÄ£Êœ
	public static final String MATCHERURL = "PatternMatcherUrl";
	public static final String FILTER_URL_MATCHER = "filterUrlMatcher";
	// ÊÇ·ñœ«»ñÈ¡µÄURL±£ŽæµœÎÄŒþÖÐ
	public static final String isSearchUrls = "isSearchUrls";
	// ±£ŽæµÄÎ»ÖÃ
	public static final String urlsPath = "urlsPath";
	// ¶ÁÈ¡ÎÄŒþµÄ±àÂë
	public static final String charset = "charset";
	// ÐèÒªÆŽœÓµÄÖ÷»úÃû
	public static final String homeName = "HostName";
	// ÒÑŽæÔÚurlµÄ±£ŽæµØÖ·
	public static final String HAVEBEENSAVEDURLSPATH = "HaveBeenSavedUrlsPath";
	// œ«Òª±£ŽæµÄÎÄÕÂµÄµØÖ·
	public static final String TOBESAVEARTICALPATH = "ToBeSaveArticalPath";
	// ÎÄÕÂ±£ŽæµÄžñÊœ
	public static final String stuffix = "stuffix";
	// ÊÇ·ñœ«Ò³ÃæÄÚÈÝ±£ŽæµœÎÄŒþÖÐ
	public static final String isToBeSaveArticalToFile = "isToBeSaveArticalToFile";
	// Ê¹ÓÃÄÄžöÀàÈ¥œâÎöÒ³ÃæÄÚÈÝ
	public static final String ContentInfoclazz = "ContentInfoclazz";
	// ŽíÎóÈÕÖŸ±£ŽæµÄÂ·Ÿ¶
	public static final String errorFilePath = "errorFilePath";
	// ±£Žæµ±Ç°ÎÄŒþµÄÎÄŒþÃû
	public static List<String> fileNames = new ArrayList<String>();

	// ÊÇ·ñ±£ŽæÎÄÕÂÄÚÈÝ
	public static String IsInfoSave = "infoSave";

	// ÍŒÆ¬±£ŽæµÄÂ·Ÿ¶
	public static String ToBeSavePicPath = "ToBeSavePicPath";
	// ÎÄÕÂ±£ŽæµÄÂ·Ÿ¶
	public static String ToBeSaveArticalPath = "ToBeSaveArticalPath";
	// ÒÑŸ­¶ÁÈ¡µÄurlµØÖ·
	public static String haveBeanReadUrlPath = "haveBeanReadUrlPath";
	// logµÄÈÕÖŸµØÖ·
	public static String logSavePath = "logSavePath";
	// ÊÇ·ñ±£ŽæÈÕÖŸ±£Žæurl
	public static String isToBeSaveLog = "isToBeSaveLog";
	// žùurlµØÖ·Žæ·ÅµÄÄ¿ÂŒ
	public static String rootUrlsFileName = "rootUrlsFileName";
	// ÎÄÕÂµÄ±êÌâ¿ªÊŒµÄ±êŒÇ±êÇ©
	public static String articleBeginTitleTag = "articleBeginTitleTag";
	// ÎÄÕÂµÄ±êÌâœáÊøµÄ±êŒÇ±êÇ©
	public static String articleEndTitleTag="articleEndTitleTag";
	// ÎÄÕÂÄÚÈÝ¿ªÊŒ±êŒÇ
	public static String articleContentTag = "articleContentTag";
	// ÎÄÕÂÄÚÈÝœáÊø±êŒÇ
	public static String articleContentEndTag = "articleContentEndTag";
	// ÎÄÕÂÄÚÈÝ¿ªÊŒ±êŒÇ
	public static String articleContentBeginTag = "articleContentBeginTag";
	public static final String urlReg = "urlReg";
	//
	public static String unReadUrlSavePath = "unReadUrlSavePath";

	// ¹ýÂËÆ÷µÄÊýÁ¿
	public static String filterNum = "filterNum";
	// ¹ýÂËÆ÷µÄÀà
	public static String filterClass = "filterClass";

	// --------------------------------urlµÚ¶þŽÎ¹ýÂËÊ¹ÓÃ---------------------------------------------------------
	// ÊÇ·ñÒªœøÐÐµÚ¶þŽÎURL¹ýÂË
	public static String isTheSecondFilter = "isTheSecondFilter";
	// ÐèÒª¹ýÂËµÄÎÄŒþÃû
	public static String willFilterFileName = "willFilterFileName";
	// ¹ýÂËurlµÄ¹ØŒü×Ö
	public static String filterKey = "filterKey";
	// ¹ýÂËÖ®ºóUrlœ«±£ŽæµÄÂ·Ÿ¶
	public static String filterURLWillSavePath = "filterURLWillSavePath";

	// -------------------------------------------------------------------------------------

	// Ê¹ÓÃÄÄžöÀàœøÐÐurlµØÖ·µÄËÑË÷
	public static String searchUrlClazz = "searchUrlClazz";

	// ÆðÊŒÒ³
	public static String startPageIndex = "startPageIndex";
	// Ä©Ò³
	public static String endPageIndex = "endPageIndex";
	//
	public static String indexUrl = "indexUrl";

	public static String nextIndexUrl = "nextIndexUrl";

	public static String firstSearchURLBeginPosition = "firstSearchURLBeginPosition";

	public static String firstSearchURLEndPosition = "firstSearchURLEndPosition";

	public static String searchUrlPattern = "searchUrlPattern";

	public static String isProcessFirstFindURL = "isProcessFirstFindURL";

	public static String isProcessSecondFindURL = "isProcessSecondFindURL";

	public static String SecondSearchURLBeginPosition = "SecondSearchURLBeginPosition";
	public static String SecondSearchURLEndPosition = "SecondSearchURLEndPosition";
	public static String willReplaceChars = "willReplaceChars";
	public static String replaceChars = "replaceChars";
	public static String isAddHostName = "isAddHostName";
	public static String HostName = "HostName";
	public static String isSaveIndexUrl = "isSaveIndexUrl";
	
	// ŒÓÔØPropertiesÎÄŒþ
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
					.getPropetiesValue(PropertiesAssist.urlsPath);
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
					// »»ÐÐ,±ÜÃâœ«urlµØÖ·ºÍÆäËûurlÓÐÁ¬œÓ
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