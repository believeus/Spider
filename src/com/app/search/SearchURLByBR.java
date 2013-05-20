package com.app.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.netconnet.NetConection;
import com.app.util.FileAssist;
import com.app.util.PropertiesAssist;

/*
 * ���ÿҳ��url��ַû��������ʽ�Ĺ������ʹ�ø���
 */
public class SearchURLByBR implements SearchURL {

	Set<String> urls = new TreeSet<String>();
	FileAssist.MyPrintWriter pw = new FileAssist.MyPrintWriter();
	int index = 0;

	@Override
	public void fetcherAllMatcherUrlsFromRootUrlWebSite(String rootUrlsFilePathName) {
		// 创建保存url地址的文件路径
		String affterSearchUrlsSavePath = PropertiesAssist.getPropetiesValue(PropertiesAssist.urlsPath);
		String path=affterSearchUrlsSavePath.substring(0,affterSearchUrlsSavePath.lastIndexOf("/"));
		System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			if (file.exists()) {
				File file2=new File(affterSearchUrlsSavePath);
				try {
					file2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 是否开启第一次下载
		String isProcessFirstFindURL = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.isProcessFirstFindURL);
		if (isProcessFirstFindURL.equals("true")) {
			// 捉取该页面的url的开始标志位置
			String beginFlag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.firstSearchURLBeginPosition);
			// // 捉取该页面的url的结束标志位置
			String engFlag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.firstSearchURLEndPosition);
			// 获取匹配的url
			String urlReg = PropertiesAssist.getPropetiesValue(PropertiesAssist.urlReg);
			Pattern regex = Pattern.compile(urlReg);
			BufferedWriter bw = pw.newPrintWriter(affterSearchUrlsSavePath);
			// 第一个url地址
			String indexUrl = PropertiesAssist.getPropetiesValue(PropertiesAssist.indexUrl);
			// 下一个类似的url地址
			String nextIndexUrl = PropertiesAssist.getPropetiesValue(PropertiesAssist.nextIndexUrl);
			// 获取需要替换的url地址
			String willReplaceChars = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.willReplaceChars);
			String replaceChars = PropertiesAssist.getPropetiesValue(PropertiesAssist.replaceChars);
			int startPage = 0;
			int endPage = 1;
			
			try {
				for (int i = startPage; i <= endPage; i++) {
					String url = "";
					if (endPage > 1) {
						url = nextIndexUrl.replaceAll(willReplaceChars, i+".html");
						System.out.println("-------------->" + url);
					} else {
						url = indexUrl;
						System.out.println(url);
					}
					findMatcherUrl(beginFlag, engFlag, regex, bw, url);
					System.out.println(urls.size());
					if (endPage == 1) {
						String endIndex = PropertiesAssist
								.getPropetiesValue(PropertiesAssist.endPageIndex);
						if (endIndex.equals("1")) {
							url = nextIndexUrl.replaceAll(willReplaceChars, "1.html");
							findMatcherUrl(beginFlag, engFlag, regex, bw, url);
							System.out.println("-------------->" + url);
							System.out.println(urls.size());
							break;
						} else if (endIndex.equals("0")) {
							if (notFind) {
								urls.add(indexUrl);
							}
							System.out.println(urls.size());
							break;
						} else {
							endPage = Integer.parseInt(endIndex);
						}
					}
				}
				System.out.println(urls.size());
				// 将url写入文件
				FileAssist.writeUrlsToFile(urls, affterSearchUrlsSavePath);
				if (PropertiesAssist.getPropetiesValue(PropertiesAssist.isSaveIndexUrl).equals("true")) {
					// 该
					Set<String> urls = FileAssist
							.ReadAndFilterUrlsFromFile(affterSearchUrlsSavePath);
					if (!urls.contains(PropertiesAssist
							.getPropetiesValue(PropertiesAssist.indexUrl))) {
						Set<String> treeSet = new TreeSet<String>();
						treeSet.add(PropertiesAssist.getPropetiesValue(PropertiesAssist.indexUrl));
						FileAssist.writeUrlsToFile(treeSet, affterSearchUrlsSavePath);
					}
				}
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String isProcessSecondFindURL = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.isProcessSecondFindURL);
		// 是否开启第二次下载
		if (isProcessSecondFindURL.equals("true")) {
			Set<String> urls = FileAssist.ReadAndFilterUrlsFromFile(affterSearchUrlsSavePath);
			deepURL(urls, affterSearchUrlsSavePath);
		}

	}

	private static boolean notFind = true;

	private void findMatcherUrl(String beginFlag, String engFlag, Pattern regex, BufferedWriter bw,
			String indexUrl) throws IOException {
		Pattern htmlTagPattern = Pattern.compile("</?[a-z][a-z0-9]*[^<>]*>",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		String isAddHostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.isAddHostName);
		String hostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.HostName);
		BufferedReader br = NetConection.urlConnectReturnBufferedReader(indexUrl);
		String line = "";
		String urlAddress = "";
		notFind = true;
		Q: while ((line = br.readLine()) != null) {
			if (line.contains(beginFlag)) {
//				System.out.println(line);
				notFind = false;
				// 在每一行的html标签中找到url
				Matcher htmlMatcher = htmlTagPattern.matcher(line);
				boolean flag = findMatcherInLine(htmlMatcher, regex, engFlag, urlAddress,isAddHostName, hostName);
				if (flag) {
					break Q;
				} else {
					while ((line = br.readLine()) != null) {
						 System.out.println(line);
						htmlMatcher = htmlTagPattern.matcher(line);
						flag = findMatcherInLine(htmlMatcher, regex, engFlag, urlAddress,isAddHostName, hostName);
						if (flag) {
							bw.flush();
							break Q;
						}
					}
				}
			}
		}
		// bw.close();
		
	}

	int c = 0;

	private boolean findMatcherInLine(Matcher htmlMatcher, Pattern regex, String engFlag,
			String urlAddress, String isAddHostName, String hostName) {
		boolean flag = false;
		Q: while (htmlMatcher.find()) {
			// System.out.println((c++) + " : " + htmlMatcher.group());
			if (htmlMatcher.group().contains(engFlag)) {
				flag = true;
				break Q;
			}
			Matcher urlMatcher = regex.matcher(htmlMatcher.group());
			if (urlMatcher.find()) {
				urlAddress = urlMatcher.group();
				System.out.println(urlAddress);
				String host = PropertiesAssist.getPropetiesValue(PropertiesAssist.HostName);
				String url="";
				if (urlAddress.contains(host)) {
					url=urlAddress;
				}else {
					urlAddress = host + urlAddress;
				}
				if (!urls.contains(urlAddress)) {
					String stuffix = urlAddress.substring(urlAddress.lastIndexOf(".") + 1);
//					if (stuffix.equals("html")) {
						System.err.println((index++) + ":" + urlAddress);
						urls.add(urlAddress);
//					}
				}
			}
		}
		return flag;
	}

	private void deepURL(Set<String> urls, String savefilePath) {
		Set<String> urlSet = new HashSet<String>();

		try {
			String urlReg = PropertiesAssist.getPropetiesValue(PropertiesAssist.urlReg);
			Pattern urlRegex = Pattern.compile(urlReg,Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			String SecondSearchURLBeginPosition = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.SecondSearchURLBeginPosition);
			String SecondSearchURLEndPosition = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.SecondSearchURLEndPosition);
			int size = urls.size();
			Iterator<String> iterator = urls.iterator();
			int count = 0;
			int index = 0;
			while (iterator.hasNext()) {
				String url = iterator.next();
				iterator.remove();
				System.out.println("---->" + (index++) + "/" + size + "------------" + url);
				BufferedReader br = NetConection.urlConnectReturnBufferedReader(url);
				String line = "";
				Q: while ((line = br.readLine()) != null) {
//					System.out.println(line);
					if (line.contains(SecondSearchURLBeginPosition)) {
						System.out.println(line);
						Matcher matcher = urlRegex.matcher(line);
						while (matcher.find()) {
							 System.out.println(matcher.group());
							if (!urls.contains(matcher.group())) {
								if (!urlSet.contains(matcher.group())) {
									String stuffix = matcher.group().substring(
											matcher.group().lastIndexOf(".") + 1);
//									if (stuffix.equals("shtml")) {
										String hostName = PropertiesAssist.getPropetiesValue(PropertiesAssist.HostName);
										String url2 = "";
										if (matcher.group().contains(hostName)) {
											url2=matcher.group();
										}else{
											url2 = hostName + matcher.group();
										}
										System.err.println((count++) + ":" + url2);
										urlSet.add(url2);
//									}
								}
							}
						}
						if (line.contains(SecondSearchURLEndPosition)) {
							break Q;
						}
					}
				}
			}
			FileAssist.writeUrlsToFile(urlSet, savefilePath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
