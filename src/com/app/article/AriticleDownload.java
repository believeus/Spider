package com.app.article;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface AriticleDownload {
	public void fetchArtical(BufferedReader br, String filePath,
			String fileName, String url) throws IOException,
			FileNotFoundException;
}
