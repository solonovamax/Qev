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
