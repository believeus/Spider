package com.app.filter;

import java.util.ArrayList;
import java.util.List;

import com.app.factory.Factory;
import com.app.util.PropertiesAssist;

public class ControlFilter implements Filter {
	List<Filter> filterList = new ArrayList<Filter>();

	public void addFilter(Filter filter) {
		filterList.add(filter);
	}

	int index = 0;

	public String doFilter(String value) {
		return filter(value, this);
	}

	@Override
	public String filter(String value, ControlFilter controlFilter) {
		if (index >= filterList.size()) {
			index = 0;
			return value;
		}
		Filter filter = controlFilter.filterList.get(0);
		index++;
		value = filter.filter(value, controlFilter);
		return value;
	}

	// ×é×°¹ýÂËÆ÷
	public void composeFilter() {
		String filterNum = PropertiesAssist.getPropetiesValue(PropertiesAssist.filterNum);
		int count = Integer.parseInt(filterNum);
		String filterClass = PropertiesAssist.getPropetiesValue(PropertiesAssist.filterClass);
		for (int i = 0; i < count; i++) {
			filterClass = filterClass + PropertiesAssist.getPropetiesValue(i + "");
			Filter filter = (Filter) Factory.newInstance(filterClass);
			this.addFilter(filter);
		}
	}
}
