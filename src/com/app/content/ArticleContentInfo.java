package com.app.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.app.download.DownLoad;
import com.app.log.Log;
import com.app.log.URLLog;
import com.app.netconnet.NetConection;
import com.app.util.PropertiesAssist;

public class ArticleContentInfo implements ContentInfo {
	public static List<String> readedUrls = new ArrayList<String>();

	@Override
	public void fetchInfoToFile(Set<String> urls, DownLoad downLoad) {
		downLoad(urls, downLoad);
	}

	private void downLoad(Set<String> urls, DownLoad downLoad) {
		try {
			// 文章标题开始标签
			String articleTitleBeginTag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.articleBeginTitleTag);
			// 文章开始标签
			String articleBeginTitleTag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.articleBeginTitleTag);
			// 文章开始标签
			String articleContentBeginTag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.articleContentBeginTag);
			// 文章标题结束标签
			String articleTitleEndTag = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.articleEndTitleTag);
			// 文章保存路径
			String toBeSaveArticalPath = PropertiesAssist
					.getPropetiesValue(PropertiesAssist.ToBeSaveArticalPath);
			File toBeSaveArticalPathFile = new File(toBeSaveArticalPath);
			// 不存在就创建该文章路径
			if (!toBeSaveArticalPathFile.exists()) {
				toBeSaveArticalPathFile.mkdirs();
			}
			Iterator<String> iterator = urls.iterator();
			int size = urls.size();
			int index = 0;
			String articleContentTag=PropertiesAssist.getPropetiesValue(PropertiesAssist.articleContentTag);
			while (iterator.hasNext()) {
				String fileName = "";
				index++;
				// if (index == 200) {
				// break;
				// }
				String url = (String) iterator.next();
				iterator.remove();
				System.out.println((index + "/" + size + "  ") + url);
				BufferedReader br = NetConection
						.urlConnectReturnBufferedReader(url);
				if (br == null) {
					continue;
				}
				String line = "";
				Q: while ((line = br.readLine()) != null) {
					if (line.contains(articleContentTag)) {
						if (line.contains(articleTitleBeginTag)) {
							fileName = fileName + line;
							// 获得文件标题
							fileName = subTitle(articleTitleBeginTag,articleTitleEndTag, fileName);
							break Q;
						}else {
							while ((line = br.readLine()) != null) {
//								System.out.println(line);
								if (line.contains(articleTitleBeginTag)) {
									fileName = fileName + line;
									while ((line = br.readLine()) != null) {
										fileName = fileName + line;
										if (!fileName.contains(articleTitleEndTag)) {
											continue;
										} else {
											fileName = subTitle(
													articleTitleBeginTag,
													articleTitleEndTag, fileName);
											break Q;
										}
									}
								}
							}
						}
					}
				}
				// }
				while ((line = br.readLine()) != null) {
					System.out.println(line+"------------"+articleContentBeginTag);
					if (line.contains(articleContentBeginTag)) {
						// times++;
						// if (times == 2) {
						downLoad.downLoad(br, toBeSaveArticalPath, fileName,
								url);
						br.close();
						break;
					}
				}
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private String subTitle(String articleTitleBeginTag,
			String articleTitleEndTag, String fileName) {
		fileName = fileName.trim();
		System.out.println(fileName);
		int beginTagIndex = fileName
				.indexOf(articleTitleBeginTag);
		int endTagIndex = fileName
				.indexOf(articleTitleEndTag);
		fileName = fileName.substring(
				beginTagIndex, endTagIndex);
		System.out.println(fileName);
		return fileName;
	}
}
