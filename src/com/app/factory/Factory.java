package com.app.factory;

import java.lang.reflect.Constructor;

import com.app.util.PropertiesAssist;

public class Factory {
	public static Object newInstance(String clazzName) {
		Object contentInfo = null;
		try {
			@SuppressWarnings("static-access")
			Class<?> clazz = PropertiesAssist.class.forName(clazzName);
			Constructor<?> constructor = clazz.getConstructor();
			contentInfo = constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentInfo;
	}
}
