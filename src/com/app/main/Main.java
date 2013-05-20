package com.app.main;

import java.util.Set;

import com.app.content.ContentInfo;
import com.app.download.DownLoad;
import com.app.download.ContentDownLoad;
import com.app.factory.Factory;
import com.app.search.SearchURL;
import com.app.util.FileAssist;
import com.app.util.PropertiesAssist;

public class Main {
	
	public static void main(String[] args) {
		String contentinfoclazz = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.ContentInfoclazz);
		ContentInfo fetchContent = (ContentInfo) Factory.newInstance(contentinfoclazz);
		String isSearchUrls = PropertiesAssist.getPropetiesValue(PropertiesAssist.isSearchUrls);
		// 是否开启url地址下载
		if (isSearchUrls.equals("true")) {
			String rootUrlsFilePathName = PropertiesAssist.getPropetiesValue(PropertiesAssist.rootUrlsFileName);
			//需要哪个类进行下载
			String clazzName = PropertiesAssist.getPropetiesValue(PropertiesAssist.searchUrlClazz);
			// 使用反射工厂创建下载类
			SearchURL searchUrl = (SearchURL) Factory.newInstance(clazzName);
			// 将该url地址下的链接全部获取
			searchUrl.fetcherAllMatcherUrlsFromRootUrlWebSite(rootUrlsFilePathName);
		}
		String isTheSecondFilter = PropertiesAssist.getPropetiesValue(PropertiesAssist.isTheSecondFilter);
		// 是否开启第二次下载，将该文章页面下所有的链接再次获取
		if (isTheSecondFilter.equals("true")) {
			String fileName = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.willFilterFileName);
			Set<String> filterUrls = FileAssist.ReadAndFilterUrlsFromFile(fileName);
			// 
			String filterURLWillSavePath = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.filterURLWillSavePath);
			FileAssist.writeUrlsToFile(filterUrls, filterURLWillSavePath);
		}
		// 
		if (PropertiesAssist.getPropetiesValue(PropertiesAssist.isToBeSaveArticalToFile).equals(
				"true")) {
			DownLoad downLoadArtical = new ContentDownLoad();
			// URL保存位置
			String affterSearchUrlsSavePath = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.urlsPath);
			Set<String> urls = FileAssist.ReadAndFilterUrlsFromFile(affterSearchUrlsSavePath);
//			Set<String> urls=new HashSet<String>();
//			urls.add("down.foodmate.net/standard/sort/3/31588.html");
			System.out.println(urls.size());
			if (urls != null && !urls.isEmpty()) {
				fetchContent.fetchInfoToFile(urls, downLoadArtical);
			}
		}
	}
}
