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

package com.solostudios.qev.framework.old.utility;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class WebUtilities {
	private static final Logger logger = LoggerFactory.getLogger(WebUtilities.class);
	
	public static JSONObject readJSONObjectFromUrl(String url) {
		try {
			HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
			urlConnection.setConnectTimeout(1000);
			urlConnection.setReadTimeout(1000);
			
			int responseCode = urlConnection.getResponseCode();
			
			JSONObject response = getJSONObject(urlConnection, responseCode);
			if (response != null) { return response; }
		} catch (IOException ignored) {
			logger.info("ioexception");
		}
		return null;
	}
	
	@Nullable private static JSONObject getJSONObject(HttpsURLConnection urlConnection, int responseCode) throws IOException {
		if (responseCode == HttpsURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String        inputLine;
			StringBuilder response = new StringBuilder();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return new JSONObject(response.toString());
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
			
			JSONObject response = getJSONObject(urlConnection, responseCode);
			if (response != null) { return response; }
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
			
			JSONArray response = getJSONArray(urlConnection, responseCode);
			if (response != null) { return response; }
		} catch (IOException ignored) {
		}
		return null;
	}
	
	@Nullable private static JSONArray getJSONArray(HttpsURLConnection urlConnection, int responseCode) throws IOException {
		if (responseCode == HttpsURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String        inputLine;
			StringBuilder response = new StringBuilder();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return new JSONArray(response.toString());
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
			
			JSONArray response = getJSONArray(urlConnection, responseCode);
			if (response != null) { return response; }
		} catch (IOException ignored) {
		}
		return null;
	}
}
