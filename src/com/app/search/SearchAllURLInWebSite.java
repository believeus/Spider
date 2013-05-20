package com.app.search;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.netconnet.NetConection;
import com.app.util.FileAssist;
import com.app.util.PropertiesAssist;

/*
 * 查找一个网站的所有url地址
 */
public class SearchAllURLInWebSite implements SearchURL {

	// 停止是否继续递归的标志
	public static int STOP_Recursive_FLAG = 0;

	// switch the Recursive avoid stack memory overflow
	private static boolean SWITCH_RECURSIVE_FLAG = true;

	// switch the Recursive avoid stack memory overflow
	private static byte SWITCH_RECURSIVE_TIME_FLAG = 0;
	// 文件的根目录
	private static Set<String> rootUrlsFile = new HashSet<String>();

	public static Set<String> URL_UTIL_SET = new HashSet<String>();
	public static Set<String> URLSET = new HashSet<String>();
	private static Set<String> readedUrlSet = new HashSet<String>();

	@Override
	public void fetcherAllMatcherUrlsFromRootUrlWebSite(String rootUrlsFilePathName) {
		// 从配置文件中获得主页
		rootUrlsFile = FileAssist.ReadAndFilterUrlsFromFile(rootUrlsFilePathName);
		// 过滤URL找到合适的URL
		Pattern filterUrlPattern = Pattern.compile(
				PropertiesAssist.getPropetiesValue(PropertiesAssist.FILTER_URL_MATCHER),
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		// 匹配url
		Pattern urlPattern = Pattern.compile(
				PropertiesAssist.getPropetiesValue(PropertiesAssist.MATCHERURL),
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		// 深度挖掘匹配的URL
		SearchAllURLInWebSite.deepPickURL(rootUrlsFile, filterUrlPattern, urlPattern);
		// 保存url到文本文件中
		PropertiesAssist.saveURLs(SearchAllURLInWebSite.URLSET);
		// return KernelParser.URLSET;
	}

	// 使用一个集合遍历里面的元素，获取每个元素的url 将获取到的url放入一个公共的集合中
	// 递归获取下面符合条件的url地址
	private static void deepPickURL(Set<String> urlSet, Pattern urlPattern, Pattern filterUrlPattern) {
		STOP_Recursive_FLAG = 0;
		Iterator<String> iterator = urlSet.iterator();
		while (iterator.hasNext()) {
			// error
			String urlName = (String) iterator.next();
			// no longer read when the url alread read
			if (!readedUrlSet.contains(urlName)) {
				BufferedReader br = NetConection.urlConnectReturnBufferedReader(urlName);
				// readed the url into collection avoid read again
				readedUrlSet.add(urlName);
				// 获取一个url下面的所有链接
				SearchAllURLInWebSite.fetchMatchUrlsforOneUrlName(urlName, br, urlPattern,
						filterUrlPattern);
			}
		}
		URLSET.addAll(URL_UTIL_SET);
		// Avoid stack memory overflow
		// 该while循环是用来检查当前的递归层次 如果是第一层就进入第二层，如果是第二层就跳到第一层
		// 这样做的目的是防止 栈内存溢出
		while (true) {
			// The times initial value is 0
			switch (SWITCH_RECURSIVE_TIME_FLAG) {
			// first times
			case 0:
				SWITCH_RECURSIVE_TIME_FLAG = 1;
				SWITCH_RECURSIVE_FLAG = true;
				break;
			// second times
			case 1:
				SWITCH_RECURSIVE_TIME_FLAG = 0;
				SWITCH_RECURSIVE_FLAG = false;
				break;
			}
			// second times return
			if (SWITCH_RECURSIVE_FLAG == false) {
				return;
			}
			// return all Recursive
			// 如果遍历一个集合下所有的url的url都是已经遍历过的。那么退出递归
			if (STOP_Recursive_FLAG == 0) {
				return;
			}
			// first times go into
			// 将获取的集合又继续往下挖 挖到没有为止
			deepPickURL(URLSET, urlPattern, filterUrlPattern);
		}
	}

	// 查找一个url下http://flash.17173.com/打头的所有url地址 保存在集合中
	private static int count = 0;

	private static void fetchMatchUrlsforOneUrlName(String urlName, BufferedReader br,
			Pattern pattern1, Pattern pattern2) {
		// 该集合是用来保存每个url下面的所有url地址的
		try {
			// 用来匹配url地址
			String matchUrl = null;
			while ((matchUrl = br.readLine()) != null) {
				Matcher matcher = pattern1.matcher(matchUrl);
				// System.out.println(matchUrl);
				// if is url address
				while (matcher.find()) {
					// url address to the matching rules, and does not exist
					// 保存不同的URL在一个公共的Set集合里面
					String url = matcher.group();
					if (rootUrlsFile.contains(url)) {
						continue;
					}
					String hostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.homeName);
					if (!hostName.equals("")) {
						url = hostName + url;
					}
					// System.out.println(url);
					// 第二层次的匹配
					Matcher matcher2 = pattern2.matcher(url);
					if (matcher2.matches() && !URL_UTIL_SET.contains(url)) {
						count++;
						// 如果遍历一个集合下所有的url的url都是已经遍历过的
						// 那么 STOP_Recursive_FLAG=0;
						// STOP_Recursive_FLAG=0 会告诉deepPickURL方法停止递归调用
						SearchAllURLInWebSite.STOP_Recursive_FLAG++;
						URL_UTIL_SET.add(url);
						System.err.println((count) + ":" + url);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
