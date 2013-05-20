package com.app.article;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.filter.ControlFilter;
import com.app.util.PropertiesAssist;

public class FetchArticle implements AriticleDownload {

	@Override
	public void fetchArtical(BufferedReader br, String filePath,
			String fileName, String url) throws IOException,
			FileNotFoundException {
		StringBuilder articalContentInfo = new StringBuilder();
//		articalContentInfo.append(fileName);
		String line = "";
		ControlFilter controlFilter = new ControlFilter();
		controlFilter.composeFilter();
		// 文章结束标签
		String articleEndTitleTag = PropertiesAssist.getPropetiesValue(PropertiesAssist.articleContentEndTag);
		while ((line = br.readLine()) != null) {
			 System.out.println(line);
			// 获取文章结束标签
			if (line.contains(articleEndTitleTag)) {
				System.out.println(line);
				String urlStuffix = url.substring(url.lastIndexOf("/") + 1)+"l";
				File file = null;
				articalContentInfo.append(line);
				if (articalContentInfo.toString().contains("IMG")
						|| articalContentInfo.toString().contains("img")) {

					String sbMsg = articalContentInfo.toString();
					// System.out.println(sbMsg);
					Pattern imgSrcPattern = Pattern
							.compile("(?<=(src|SRC)=\"|')/[A-Za-z0-9-_./]+");
					Matcher matcher = imgSrcPattern.matcher(sbMsg);
					while (matcher.find()) {
						sbMsg = sbMsg.replaceAll(matcher.group(),
								"http://itpx.eol.cn" + matcher.group());
					}
					articalContentInfo.delete(0, articalContentInfo.length());
					articalContentInfo.append(sbMsg);
				}
				// System.out.println(sb.toString());
				if (fileName.length()>15) {
					fileName = fileName.substring(0,10)+"...";
				}
				
//				String stuffix = PropertiesAssist.getPropetiesValue(PropertiesAssist.stuffix);
				file = new File(filePath + "/" + fileName + urlStuffix);
				System.out.println(file.getAbsolutePath());
				if (!file.exists()) {
					file.createNewFile();
				}
				String path = file.getAbsolutePath();
				PrintWriter articalWriter = new PrintWriter(path);
				articalWriter.write("<!--" + url + "-->");
				String articleInfo = controlFilter.doFilter(articalContentInfo
						.toString());
				articalWriter.write(articleInfo);
				articalWriter.flush();
				articalWriter.close();
				break;
			} else {
				// 每一行进行换行
				articalContentInfo.append(line + "\r\n");
				// System.out.println(line);
			}
		}
	}

}
