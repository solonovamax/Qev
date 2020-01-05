/*
 * Copyright (c) 2020 solonovamax <solonovamax@12oclockpoint.com>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * THIS FILE WAS INSPIRED BY AvaIre.
 *       THE ORIGINAL FILE CAN BE FOUND AT
 *       https://github.com/avaire/avaire/blob/master/src/main/java/com/avairebot/contracts/config/PropertyConfiguration.java
 */

package com.solostudios.qev.framework.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Properties {
	protected final java.util.Properties properties;
	
	public Properties(String propertyDefault) throws IOException, NullPointerException {
		java.util.Properties defaultProperties = new java.util.Properties();
		loadNativeProperty(defaultProperties, propertyDefault);
		
		properties = new java.util.Properties(defaultProperties);
	}
	
	protected void loadNativeProperty(java.util.Properties propertiesObject, String propertyFileName) throws IOException, NullPointerException {
		propertiesObject.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName)));
	}
	
	public Properties() {
		properties = new java.util.Properties();
	}
	
	protected void loadNativeProperty(String propertyFileName) throws IOException, NullPointerException {
		properties.load(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName)));
	}
	
	protected void loadProperty(String propertyFileName) throws IOException, NullPointerException {
		properties.load(new FileInputStream(new File(propertyFileName)));
	}
	
	List<String> getPropertyAsList(String property) {
		String propertyList = properties.getProperty(property);
		return (propertyList != null && propertyList.length() > 0)
			   ? Arrays.asList(propertyList.split("\\s*,\\s*"))
			   : Collections.emptyList();
	}
}
