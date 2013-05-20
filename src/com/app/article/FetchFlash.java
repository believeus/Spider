package com.app.article;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.netconnet.NetConection;
import com.app.util.FileAssist;

public class FetchFlash implements AriticleDownload {

	@Override
	public void fetchArtical(BufferedReader br, String filePath,
			String fileName, String url) throws IOException,
			FileNotFoundException {
		// 匹配swf格式的url
		Pattern regex = Pattern.compile("http://.+\\.swf");
		// 获取媒体资源
		fetchMedia(br, filePath, fileName, regex);
		// 获取媒体详情
		fetchContentInfo(br, filePath, fileName, url);
	}
	// 获取媒体资源
	private void fetchMedia(BufferedReader br, String filePath,
			String fileName, Pattern regex) throws IOException {
		String line="";
		while ((line = br.readLine()) != null) {
			Matcher matcher = regex.matcher(line);
			System.out.println(line);
			if (matcher.find()) {
				String flashUrl = matcher.group();
				System.err.println(flashUrl);
				downloadMedia(filePath, fileName, flashUrl);
				break;
			} else {
				continue;
			}
		}
	}
	// 获取文件详情
	private void fetchContentInfo(BufferedReader br, String filePath, String fileName,
			String url) throws IOException {
		String line;
		// 读取文件内容
		Q: while ((line = br.readLine()) != null) {
			// 读取该文件的介绍内容
			if (line.contains("[简介]")) {
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.contains("table")) {
						while ((line = br.readLine()) != null) {
							sb.append(line);
							System.out.println(line);
							// 结束
							if (sb.toString().contains("</span>")) {
								FileAssist.MyPrintWriter pw = new FileAssist.MyPrintWriter();
								String stuffix = url.substring(url
										.lastIndexOf("/") + 1);
								BufferedWriter bw = pw.newPrintWriter(filePath
										+ "/" + fileName + stuffix);
								bw.write(sb.toString());
								bw.close();
								br.close();
								break Q;
							}
						}
					}
				}
			}
		}
	}
	// 下载媒体资源
	private void downloadMedia(String filePath, String fileName, String flashUrl)
			throws IOException {
		BufferedInputStream bis = NetConection
				.urlConnectReturnBufferedInputSteam(flashUrl);
		FileAssist.MyBufferedOutputStream myBos = new FileAssist.MyBufferedOutputStream();
		BufferedOutputStream bos = null;
		byte[] buf = new byte[1024*1024];
		// 下载该swf文件
		int flashSize = 0;
		int size = 0;
		bos = myBos.newBufferedOutStream(filePath, fileName);
		while ((size = bis.read(buf)) != -1) {
			flashSize = size + flashSize;
			System.out.println("已经下载--" + (((float)flashSize/NetConection.size)*100)+"%");
			bos.write(buf, 0, size);
			bos.flush();
		}
		NetConection.size=0;
		bos.close();
		bis.close();
	}

	public static void main(String[] args) {
		FetchFlash fetchFlash = new FetchFlash();
		try {
			fetchFlash.downloadMedia("D:/URL/Article/shuxue/", "test.html",
					"http://down.bn.netease.com/download/shareclass/openclass/zhengchang/zibizhengjiangzuomp4/zibizhengjiangzuo01.mp4");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
