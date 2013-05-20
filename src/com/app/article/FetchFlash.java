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
		// ƥ��swf��ʽ��url
		Pattern regex = Pattern.compile("http://.+\\.swf");
		// ��ȡý����Դ
		fetchMedia(br, filePath, fileName, regex);
		// ��ȡý������
		fetchContentInfo(br, filePath, fileName, url);
	}
	// ��ȡý����Դ
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
	// ��ȡ�ļ�����
	private void fetchContentInfo(BufferedReader br, String filePath, String fileName,
			String url) throws IOException {
		String line;
		// ��ȡ�ļ�����
		Q: while ((line = br.readLine()) != null) {
			// ��ȡ���ļ��Ľ�������
			if (line.contains("[���]")) {
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.contains("table")) {
						while ((line = br.readLine()) != null) {
							sb.append(line);
							System.out.println(line);
							// ����
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
	// ����ý����Դ
	private void downloadMedia(String filePath, String fileName, String flashUrl)
			throws IOException {
		BufferedInputStream bis = NetConection
				.urlConnectReturnBufferedInputSteam(flashUrl);
		FileAssist.MyBufferedOutputStream myBos = new FileAssist.MyBufferedOutputStream();
		BufferedOutputStream bos = null;
		byte[] buf = new byte[1024*1024];
		// ���ظ�swf�ļ�
		int flashSize = 0;
		int size = 0;
		bos = myBos.newBufferedOutStream(filePath, fileName);
		while ((size = bis.read(buf)) != -1) {
			flashSize = size + flashSize;
			System.out.println("�Ѿ�����--" + (((float)flashSize/NetConection.size)*100)+"%");
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
