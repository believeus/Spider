package com.app.content;

import java.util.Set;

import com.app.download.DownLoad;

public interface ContentInfo {
	public void fetchInfoToFile(Set<String> urls,DownLoad downLoad);
}
