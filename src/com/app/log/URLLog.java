package com.app.log;

import java.io.BufferedWriter;

public class URLLog implements Log {

	@Override
	public void logToFile(String log, BufferedWriter bw) {
		// 将读取过的url地址保存在文件中
		try {
			bw.write(log + "\r\n");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
