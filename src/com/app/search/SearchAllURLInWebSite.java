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
 * ����һ����վ������url��ַ
 */
public class SearchAllURLInWebSite implements SearchURL {

	// ֹͣ�Ƿ�����ݹ�ı�־
	public static int STOP_Recursive_FLAG = 0;

	// switch the Recursive avoid stack memory overflow
	private static boolean SWITCH_RECURSIVE_FLAG = true;

	// switch the Recursive avoid stack memory overflow
	private static byte SWITCH_RECURSIVE_TIME_FLAG = 0;
	// �ļ��ĸ�Ŀ¼
	private static Set<String> rootUrlsFile = new HashSet<String>();

	public static Set<String> URL_UTIL_SET = new HashSet<String>();
	public static Set<String> URLSET = new HashSet<String>();
	private static Set<String> readedUrlSet = new HashSet<String>();

	@Override
	public void fetcherAllMatcherUrlsFromRootUrlWebSite(String rootUrlsFilePathName) {
		// �������ļ��л����ҳ
		rootUrlsFile = FileAssist.ReadAndFilterUrlsFromFile(rootUrlsFilePathName);
		// ����URL�ҵ����ʵ�URL
		Pattern filterUrlPattern = Pattern.compile(
				PropertiesAssist.getPropetiesValue(PropertiesAssist.FILTER_URL_MATCHER),
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		// ƥ��url
		Pattern urlPattern = Pattern.compile(
				PropertiesAssist.getPropetiesValue(PropertiesAssist.MATCHERURL),
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		// ����ھ�ƥ���URL
		SearchAllURLInWebSite.deepPickURL(rootUrlsFile, filterUrlPattern, urlPattern);
		// ����url���ı��ļ���
		PropertiesAssist.saveURLs(SearchAllURLInWebSite.URLSET);
		// return KernelParser.URLSET;
	}

	// ʹ��һ�����ϱ��������Ԫ�أ���ȡÿ��Ԫ�ص�url ����ȡ����url����һ�������ļ�����
	// �ݹ��ȡ�������������url��ַ
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
				// ��ȡһ��url�������������
				SearchAllURLInWebSite.fetchMatchUrlsforOneUrlName(urlName, br, urlPattern,
						filterUrlPattern);
			}
		}
		URLSET.addAll(URL_UTIL_SET);
		// Avoid stack memory overflow
		// ��whileѭ����������鵱ǰ�ĵݹ��� ����ǵ�һ��ͽ���ڶ��㣬����ǵڶ����������һ��
		// ��������Ŀ���Ƿ�ֹ ջ�ڴ����
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
			// �������һ�����������е�url��url�����Ѿ��������ġ���ô�˳��ݹ�
			if (STOP_Recursive_FLAG == 0) {
				return;
			}
			// first times go into
			// ����ȡ�ļ����ּ��������� �ڵ�û��Ϊֹ
			deepPickURL(URLSET, urlPattern, filterUrlPattern);
		}
	}

	// ����һ��url��http://flash.17173.com/��ͷ������url��ַ �����ڼ�����
	private static int count = 0;

	private static void fetchMatchUrlsforOneUrlName(String urlName, BufferedReader br,
			Pattern pattern1, Pattern pattern2) {
		// �ü�������������ÿ��url���������url��ַ��
		try {
			// ����ƥ��url��ַ
			String matchUrl = null;
			while ((matchUrl = br.readLine()) != null) {
				Matcher matcher = pattern1.matcher(matchUrl);
				// System.out.println(matchUrl);
				// if is url address
				while (matcher.find()) {
					// url address to the matching rules, and does not exist
					// ���治ͬ��URL��һ��������Set��������
					String url = matcher.group();
					if (rootUrlsFile.contains(url)) {
						continue;
					}
					String hostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.homeName);
					if (!hostName.equals("")) {
						url = hostName + url;
					}
					// System.out.println(url);
					// �ڶ���ε�ƥ��
					Matcher matcher2 = pattern2.matcher(url);
					if (matcher2.matches() && !URL_UTIL_SET.contains(url)) {
						count++;
						// �������һ�����������е�url��url�����Ѿ���������
						// ��ô STOP_Recursive_FLAG=0;
						// STOP_Recursive_FLAG=0 �����deepPickURL����ֹͣ�ݹ����
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
