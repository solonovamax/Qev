package com.solostudios.qev.framework.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Properties {
	protected final java.util.Properties properties;
	
	public Properties(String propertyDefault) throws IOException, NullPointerException {
		java.util.Properties defaultProperties = new java.util.Properties();
		defaultProperties.load(this.getClass().getClassLoader().getResourceAsStream(propertyDefault));
		
		properties = new java.util.Properties(defaultProperties);
	}
	
	public Properties() {
		properties = new java.util.Properties();
	}
	
	protected void loadProperty(ClassLoader classLoader, String propertyFileName) throws IOException, NullPointerException {
		properties.load(Objects.requireNonNull(classLoader.getResourceAsStream(propertyFileName)));
	}
	
	List<String> getPropertyAsList(String property) {
		return ((property != null) || (property.length() > 0))
			   ? Arrays.asList(property.split("\\s*,\\s*"))
			   : Collections.emptyList();
	}
}
