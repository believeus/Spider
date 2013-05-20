package com.app.download;

import java.io.BufferedReader;

public interface DownLoad {
	public boolean downLoad(BufferedReader br, String filePath, String fileName, String url)
			throws Exception;
}
