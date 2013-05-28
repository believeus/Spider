package com.app.main;

import com.app.factory.Factory;
import com.app.search.SearchURL;
import com.app.util.PropertiesAssist;

public class URLGo {
	public void urlGo() {
		String isSearchUrls = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.isSearchUrls);
		// 是否开启url地址下载
		if (isSearchUrls.equals("true")) {
			// 需要哪个类进行下载
			String clazzName = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.searchUrlClazz);
			// 使用反射工厂创建下载类
			SearchURL searchUrl = (SearchURL) Factory.newInstance(clazzName);
			// 将该url地址下的链接全部获取
			searchUrl
					.fetcherAllMatcherUrlsFromRootUrlWebSite();
		}
	}
}
