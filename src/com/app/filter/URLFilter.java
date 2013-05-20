package com.app.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLFilter implements Filter {

	public String filter(String value, ControlFilter controlFilter) {
		Pattern urlPattern = Pattern
				.compile(
						"(?<=href=\"|')\\s*\\b(?:(?:https?|ftp|file)://|www\\.|ftp\\.)[-A-Z0-9+&@#/%=~_|$?!:,.]*[A-Z0-9+&@#/%=~_|$]",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = urlPattern.matcher(value);
		while (matcher.find()) {
			value = value.replaceAll(matcher.group(), "#");
		}
		value = controlFilter.doFilter(value);
		return value;
	}
}
