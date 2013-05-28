package com.app.article.fetch;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.app.util.PropertiesAssist;

/*将整一篇文档下载保存到文件中*/
public class NoDistinguishArticle implements IFetchArticle {
	// 文章结束标签
	StringBuilder articalContentInfo = new StringBuilder();
	String articleEndTitleTag = PropertiesAssist
			.getPropetiesValue(PropertiesAssist.articleContentEndTag);

	@Override
	public boolean getContent(String line, String fileName, String url,
			String filePath) {
		boolean is_ok = false;
		try {
			articalContentInfo.append(fileName);
			if (line.contains(articleEndTitleTag)) {
				articalContentInfo.append(line);
				System.out.println(line);
				String urlStuffix = url.substring(url.lastIndexOf("/") + 1)
						+ "l";
				File file = null;
				file = new File(filePath + "/" + fileName + urlStuffix);
				System.out.println(fileName);
				System.out.println(file.getAbsolutePath());
				if (!file.exists()) {
					file.createNewFile();
				}
				String path = file.getAbsolutePath();
				PrintWriter articalWriter = new PrintWriter(path);
				articalWriter.write("<!--" + url + "-->");
				articalWriter.flush();
				articalWriter.close();
				// 下载结束
				is_ok = true;
			} else {
				// 每一行进行换行
				articalContentInfo.append(line + "\r\n");
				// System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is_ok;
	}
}
