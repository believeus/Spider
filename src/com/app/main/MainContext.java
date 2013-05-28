package com.app.main;

public class MainContext {
	public void go() {
		// 先下载url
		URLGo urlGo = new URLGo();
		urlGo.urlGo();
		// 下载文章
		ArticleGo articleGo = new ArticleGo();
		articleGo.articleGo();
	}
}
