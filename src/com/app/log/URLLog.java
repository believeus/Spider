package com.app.log;

import java.io.BufferedWriter;

public class URLLog implements Log {

	@Override
	public void logToFile(String log, BufferedWriter bw) {
		// ����ȡ����url��ַ�������ļ���
		try {
			bw.write(log + "\r\n");
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
