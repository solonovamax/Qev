/*
 *
 * Copyright 2016 2019 solonovamax <solonovamax@12oclockpoint.com>
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
 *
 */

package com.solostudios.qev.framework.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class WebUtils {
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	
	public static JSONObject readJSONObjectFromUrl(String url) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
			urlConnection.setConnectTimeout(1000);
			urlConnection.setReadTimeout(1000);
			
			int responseCode = urlConnection.getResponseCode();
			logger.info(Integer.toString(responseCode));
			
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String        inputLine;
				StringBuilder response = new StringBuilder();
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
				logger.info(response.toString());
				
				return new JSONObject(response.toString());
			}
		} catch (IOException ignored) {
		}
		return null;
	}
	
	public static JSONObject readJSONObjectFromUrl(String url, String clientID) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
			urlConnection.setRequestProperty("Authorization", "Client-ID " + clientID);
			urlConnection.setConnectTimeout(1000);
			urlConnection.setReadTimeout(1000);
			
			int responseCode = urlConnection.getResponseCode();
			logger.info(Integer.toString(responseCode));
			
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String        inputLine;
				StringBuilder response = new StringBuilder();
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
				logger.info(response.toString());
				
				return new JSONObject(response.toString());
			}
		} catch (IOException ignored) {
		}
		return null;
	}
	
	public static JSONArray readJSONArrayFromUrl(String url) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
			urlConnection.setConnectTimeout(1000);
			urlConnection.setReadTimeout(1000);
			
			int responseCode = urlConnection.getResponseCode();
			logger.info(Integer.toString(responseCode));
			
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String        inputLine;
				StringBuilder response = new StringBuilder();
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
				logger.info(response.toString());
				
				return new JSONArray(response.toString());
			}
		} catch (IOException ignored) {
		}
		return null;
	}
	
	public static JSONArray readJSONArrayFromUrl(String url, String clientID) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
			urlConnection.setRequestProperty("Authorization", "Client-ID " + clientID);
			urlConnection.setConnectTimeout(1000);
			urlConnection.setReadTimeout(1000);
			
			int responseCode = urlConnection.getResponseCode();
			logger.info(Integer.toString(responseCode));
			
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String        inputLine;
				StringBuilder response = new StringBuilder();
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				// print result
				logger.info(response.toString());
				
				return new JSONArray(response.toString());
			}
		} catch (IOException ignored) {
		}
		return null;
	}
}
