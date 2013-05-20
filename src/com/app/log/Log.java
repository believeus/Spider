package com.app.log;

import java.io.BufferedWriter;

public interface Log {
	public void logToFile(String log, BufferedWriter bw);
}
