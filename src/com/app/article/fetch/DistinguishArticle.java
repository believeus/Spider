package com.app.article.fetch;

import com.app.util.PropertiesAssist;
/**/
public class DistinguishArticle implements IFetchArticle {
	String articleEndTitleTag = PropertiesAssist
			.getPropetiesValue(PropertiesAssist.articleContentEndTag);
	@Override
	public boolean getContent(String line, String fileName, String url,String filePath) {
		boolean is_ok=false;
		if (line.contains(articleEndTitleTag)) {
			is_ok=true;
		}else {
			
		}
		return is_ok;
	}


}
