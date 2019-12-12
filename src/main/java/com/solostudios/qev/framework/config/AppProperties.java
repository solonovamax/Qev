package com.solostudios.qev.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class AppProperties extends Properties {
	private static Logger logger = LoggerFactory.getLogger(AppProperties.class);
	
	private AppProperties() throws IOException {
		loadProperty(getClass().getClassLoader(), "app.properties");
	}
	
	public static Properties getProperties() {
		try {
			return new AppProperties();
		} catch (IOException e) {
			logger.error("Could not load app.properties");
			return null;
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
