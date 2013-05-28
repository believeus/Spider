package com.app.main;

import java.util.Set;

import com.app.content.Content;
import com.app.download.ContentDownLoad;
import com.app.download.DownLoad;
import com.app.factory.Factory;
import com.app.util.FileAssist;
import com.app.util.PropertiesAssist;

public class ArticleGo {
	public void articleGo() {
		String contentinfoclazz = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.ContentInfoClazz);
		Content fetchContent = (Content) Factory.newInstance(contentinfoclazz);
		//
		if (PropertiesAssist.getPropetiesValue(
				PropertiesAssist.ISToBeSaveArticalToFile).equals("true")) {
			DownLoad downLoadArtical = new ContentDownLoad();
			// URL保存位置
			String affterSearchUrlsSavePath = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.URL_SAVE_PATH);
			Set<String> urls = FileAssist
					.ReadAndFilterUrlsFromFile(affterSearchUrlsSavePath);
			System.out
					.println(urls.size() + "-----" + affterSearchUrlsSavePath);
			if (urls != null && !urls.isEmpty()) {
				fetchContent.fetchInfoToFile(urls, downLoadArtical);
			}
		}
	}
}
