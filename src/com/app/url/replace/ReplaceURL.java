package com.app.url.replace;

public class ReplaceURL implements IURLReplace{
/*http://www.945156.com/main/company_list.html?pageNo=i*/
	// 处理下一页pageNo=i，i在变化的这种情况
	@Override
	public String urlReplace(String url,int i) {
		url=url.replaceAll("=i", "="+i);
		return url;
	}

}
