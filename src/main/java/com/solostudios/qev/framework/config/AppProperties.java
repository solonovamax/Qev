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
		loadProperty(getClass().getClassLoader(), "app.properties");
		
		version = getProperty("version");
		defaultPrefix = getProperty("defaultPrefix");
		botToken = getProperty("botToken");
		botAdminList = getPropertyAsList("botAdmins");
		botOwner = getProperty("botOwner");
		supportServer = getProperty("supportServer");
		github = getProperty("github");
		debug = GenericUtil.getBoolean(getProperty("debug"));
		
		
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
