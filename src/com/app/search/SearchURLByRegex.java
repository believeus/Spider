package com.app.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.netconnet.NetConection;
import com.app.util.PropertiesAssist;

/*
 * 如果每一页的url地址是相同的模式可以使用该类
 */
public class SearchURLByRegex implements SearchURL {
	public void fetcherAllMatcherUrlsFromRootUrlWebSite(String rootUrlsFilePathName) {
		ArrayList<String> urls = new ArrayList<String>();
		String saveFileFolder = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.urlsPath);
		File file = new File(saveFileFolder);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fis = new FileOutputStream(new File(saveFileFolder), true);
			OutputStreamWriter writer = new OutputStreamWriter(fis, "UTF-8");
			BufferedWriter bw = new BufferedWriter(writer);
			BufferedReader br = null;
			Pattern regex = Pattern.compile(PropertiesAssist.searchUrlPattern,
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			String indexUrl = PropertiesAssist.getPropetiesValue(PropertiesAssist.indexUrl);
			String noIndexUrl = PropertiesAssist.getPropetiesValue(PropertiesAssist.nextIndexUrl);
			int startPage = Integer.parseInt(PropertiesAssist
					.getPropetiesValue(PropertiesAssist.startPageIndex));
			int endPage = Integer.parseInt(PropertiesAssist
					.getPropetiesValue(PropertiesAssist.endPageIndex));
			String hostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.HostName);
			String urlAddress = "";
			int index = 0;
			for (int i = startPage; i <= endPage; i++) {
				String url = "";
				if (indexUrl.equals("")) {
					url = noIndexUrl.replaceAll("\"+i+\"", i + "");
				} else {
					url = indexUrl;
				}
				System.out.println(url);
				if (endPage == 1) {
					String endIndex = PropertiesAssist
							.getPropetiesValue(PropertiesAssist.endPageIndex);
					if (endIndex.equals("1")) {
						url = noIndexUrl.replaceAll("", "_" + 1);
						System.out.println("-------------->" + url);
						break;
					} else if (endIndex.equals("0")) {
						urls.add(indexUrl);
						break;
					} else {
						endPage = Integer.parseInt(endIndex);
					}
				}
				br = NetConection.urlConnectReturnBufferedReader(url);
				String line = "";
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					Matcher regexMatcher = regex.matcher(line);
					while (regexMatcher.find()) {
						if (urls.contains(regexMatcher.group())) {
							continue;
						} else {
							if (hostName.equals("")) {
								urlAddress = hostName + regexMatcher.group();
							} else {
								urlAddress = regexMatcher.group();
							}
							urls.add(urlAddress);
							index++;
							System.out.println(index + ":" + regexMatcher.group());
						}

					}
				}
			}
			Iterator<String> iterator = urls.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				count++;
				String url1 = (String) iterator.next();
				bw.write(url1 + "\r\n");
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
