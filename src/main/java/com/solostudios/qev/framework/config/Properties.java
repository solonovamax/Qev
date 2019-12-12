package com.solostudios.qev.framework.config;

import java.io.IOException;


public class Properties {
	protected final java.util.Properties properties = new java.util.Properties();
	
	protected void loadProperty(ClassLoader classLoader, String propertyFileName) throws IOException {
		properties.load(classLoader.getResourceAsStream(propertyFileName));
	}
}
