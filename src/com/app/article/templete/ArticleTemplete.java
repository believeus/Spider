package com.app.article.templete;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.app.article.fetch.IFetchArticle;

public class ArticleTemplete implements IArtiticleTemplete {
	IFetchArticle fetch;
	public IFetchArticle getFetch() {
		return fetch;
	}
	public void setFetch(IFetchArticle fetch) {
		this.fetch = fetch;
	}
	@Override
	public void fetchArtical(BufferedReader br, String filePath,
			String fileName, String url) throws IOException,
			FileNotFoundException {
		
		String line = "";
		while ((line = br.readLine()) != null) {
			 boolean isok = fetch.getContent(line, fileName, url, filePath);
			 if (isok) {
				break;
			}
		}
	}

}
