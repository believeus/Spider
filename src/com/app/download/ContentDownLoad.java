package com.app.download;

import java.io.BufferedReader;

import com.app.article.templete.IArtiticleTemplete;
import com.app.article.templete.ArticleTemplete;

public class ContentDownLoad implements DownLoad {

	@Override
	public boolean downLoad(BufferedReader br, String filePath, String fileName, String url)
			throws Exception {
		if (!fileName.equals("")) {
			// 处理文件名的html标签
			fileName = fileName.replaceAll("</?[a-z][a-z0-9]*[^<>]*>", "");
			fileName = fileName.replaceAll("<h1>|</h1>|$|#|@|%|~|`|\\+|\\s+|\\|", "");
			fileName = fileName.replaceAll("/|\"|\\.|;|:|'|-|%|&|#|\\?|《|<|>|》|\\*|!|^|\\\\","");
		}
		// 下载文章内容
		IArtiticleTemplete download=new ArticleTemplete();
		download.fetchArtical(br, filePath, fileName, url);
		return false;
	}

	
}
