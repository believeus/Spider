package com.app.article.templete;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IArtiticleTemplete {
	public void fetchArtical(BufferedReader br, String filePath,
			String fileName, String url) throws IOException,
			FileNotFoundException;
}
