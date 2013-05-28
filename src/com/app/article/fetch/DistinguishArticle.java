package com.app.article.fetch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.util.PropertiesAssist;

/**/
public class DistinguishArticle implements IFetchArticle {
	String articleEndTitleTag = PropertiesAssist
			.getPropetiesValue(PropertiesAssist.articleContentEndTag);
	// 获取标签之间内容则正则表达式
	StringBuilder articalContentInfo = new StringBuilder();
	Pattern regex = Pattern
			.compile("(?!:(<?[a-z][a-z0-9]*[^<>]*>))([a-zA-Z0-9\u4e00-\u9fa5：\\s，。,`~《》\"<”“；;、&#.()/\\\\（）%Ф-]+)(?=(</[a-z][a-z0-9]*[^<>]*>))");

	private boolean is_ok = false;

	@Override
	public boolean getContent(String line, String fileName, String url,
			String filePath) {
		articalContentInfo.append(fileName);
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (line.contains(articleEndTitleTag)) {
			Matcher matcher = regex.matcher(articalContentInfo.toString());
			try {
				String path = file.getAbsolutePath();
				PrintWriter articalWriter = new PrintWriter(path);
				articalWriter.write(url+"\r\n");
				while (matcher.find()) {
					articalWriter.println(matcher.group());
				}
				articalWriter.println("--------------------------------");
				articalWriter.flush();
				articalWriter.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			is_ok = true;
		} else {
			// 获取每个标签之间的内容，遍历每一行的html标签
			// 每一行进行换行
			articalContentInfo.append(line + "\r\n");
			// System.out.println(line);
		}
		return is_ok;
	}

}
