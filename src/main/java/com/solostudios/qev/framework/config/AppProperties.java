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

package com.solostudios.qev.framework.config;

import com.solostudios.qev.framework.utility.GenericUtil;
import com.sun.istack.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class AppProperties extends Properties {
	private static Logger logger = LoggerFactory.getLogger(AppProperties.class);
	
	public final String  version;
	public final String  defaultPrefix;
	public final String  botToken;
	public final String  botOwner;
	public final String  supportServer;
	public final String  github;
	public final boolean debug;
	
	public final List<String>            botAdminList;
	public final HashMap<String, String> tokenList = new HashMap<>();
	
	private AppProperties() throws IOException {
		
		super("default.properties");
		loadProperty("app.properties");
		
		
		version = getProperty("version");
		defaultPrefix = getProperty("defaultPrefix");
		botToken = getProperty("botToken");
		botAdminList = getPropertyAsList("botAdmins");
		botOwner = getProperty("botOwner");
		supportServer = getProperty("supportServer");
		github = getProperty("githubUsername") + "/" + getProperty("githubRepository");
		debug = GenericUtil.getBoolean(getProperty("debug"));
		
		logger.info(botAdminList.toString());
		
		
		for (String stringPropertyName : properties.stringPropertyNames()) {
			if (stringPropertyName.startsWith("token")) {
				tokenList.put(stringPropertyName.replaceFirst("token", ""), getProperty(stringPropertyName));
			}
		}
	}
	
	@Nullable
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public static AppProperties getProperties() {
		try {
			return new AppProperties();
		} catch (IOException | NullPointerException e) {
			logger.error("Could not load app.properties", e);
			return null;
		}
	}
}
