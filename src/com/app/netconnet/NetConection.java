package com.app.netconnet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.app.util.PropertiesAssist;

public class NetConection {

	private static InputStream instream;
	private static InputStreamReader reader;
	private static BufferedReader br;
	private static BufferedInputStream bis;

	public static BufferedReader urlConnectReturnBufferedReader(String urlName) {
		try {
			URL url = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000 * 1000);
			if (200 == conn.getResponseCode()) {
				instream = conn.getInputStream();
				String charaset = PropertiesAssist.getPropetiesValue(PropertiesAssist.charset);
				reader = new InputStreamReader(instream, charaset);
				br = new BufferedReader(reader);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return br;
	}

	public static int size=0;
	public static BufferedInputStream urlConnectReturnBufferedInputSteam(String urlName) {
		try {
			URL swfUrl = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection) swfUrl.openConnection();
			//  设置请求头解除盗链
			conn.setRequestProperty("Referer", "http://down.foodmate.net/");
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 1000);
			if (200 == conn.getResponseCode()) {
				size=conn.getContentLength();
				conn.getContentLength();
				instream = conn.getInputStream();
				bis = new BufferedInputStream(instream);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bis;
	}
	
}
