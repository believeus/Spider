package com.app.content;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import com.app.download.DownLoad;
import com.app.netconnet.NetConection;
import com.app.util.PropertiesAssist;

public class BinContentInfo implements ContentInfo {

	@Override
	public void fetchInfoToFile(Set<String> urls, DownLoad downLoad) {
		String toBeSaveArticalPath = PropertiesAssist
				.getPropetiesValue(PropertiesAssist.ToBeSaveArticalPath);
		File toBeSaveArticalPathFile = new File(toBeSaveArticalPath);
		if (!toBeSaveArticalPathFile.exists()) {
			toBeSaveArticalPathFile.mkdirs();
		}
		Iterator<String> iterator = urls.iterator();
		int size = urls.size();
		int index = 0;
		while (iterator.hasNext()) {
			index++;
			// if (index == 200) {
			// break;
			// }
			String url = (String) iterator.next();
			iterator.remove();
			String authID = url.substring(url.lastIndexOf("/")+1,url.length() - 5);
			System.out.println((index + "/" + size + "  ") + authID);
			BufferedInputStream br = NetConection
					.urlConnectReturnBufferedInputSteam(url);
			if (br == null) {
				continue;
			}
			try {
				OutputStream fos = new FileOutputStream(toBeSaveArticalPathFile+"/"+authID+".rar");
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = br.read(buf)) != -1) {
					bos.write(buf, 0, len);
					bos.flush();
				}
				bos.close();
				fos.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
