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

package com.solostudios.qev.framework.internal.utils;

import com.solostudios.qev.framework.api.exceptions.HttpException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class WebUtil {
    /**
     * Gets a {@link JSONObject} from the provided url.
     *
     * @param url
     *         url that you want to query.
     *
     * @return {@link JSONObject} constructed from the data that is returned by the url
     *
     * @throws IOException
     */
    public static JSONObject getJSONFromUrl(String url) throws IOException {
        return new JSONObject(getURLReturn(initConnection(url)));
    }
    
    /**
     * Gets the body of a url request and returns it as a {@link String}.
     *
     * @param urlConnection
     *         {@link URLConnection} object which you want to get the body of.
     *
     * @return {@link String} containing the url request body.
     *
     * @throws IOException
     */
    public static @NotNull String getURLReturn(URLConnection urlConnection) throws IOException {
        int responseCode = ((HttpURLConnection) urlConnection).getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in       = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String         inputLine;
            StringBuilder  response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return response.toString();
        }
        throw new HttpException("Connection returned with bad code.", responseCode);
    }
    
    /**
     * Builds a {@link URLConnection} with 1000ms timeouts on read and connection.
     *
     * @param url
     *         url you want to connect to.
     *
     * @return {@link URLConnection} from the provided link.
     *
     * @throws IOException
     */
    public static URLConnection initConnection(String url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        return urlConnection;
    }
    
    /**
     * Gets a {@link String} from the provided url.
     *
     * @param url
     *         url that you want to query.
     *
     * @return {@link String} constructed from the data that is returned by the url
     *
     * @throws IOException
     */
    public static String getStringFromURL(String url) throws IOException {
        return getURLReturn(initConnection(url));
    }
    
    /**
     * Gets a {@link JSONArray} from the provided url.
     *
     * @param url
     *         url that you want to query.
     *
     * @return {@link JSONArray} constructed from the data that is returned by the url
     *
     * @throws IOException
     */
    public static JSONArray getJSONArrayFromUrl(String url) throws IOException {
        return new JSONArray(getURLReturn(initConnection(url)));
    }
    
    /**
     * Gets a {@link JSONObject} from the provided url.
     *
     * @param url
     *         url that you want to query.
     * @param authKey
     *         Key that is used for the authentication in the url request header. It will generally be something like
     *         {@code "Authentication"}
     * @param authContents
     *         Generally an auth key or client ID. This is what is sent in the url request header, with {@code authKey}
     *         as a key.
     *
     * @return {@link JSONObject} constructed from the data that is returned by the url.
     *
     * @throws IOException
     */
    public static JSONObject getJSONFromURLWithAuth(String url, String authKey, String authContents)
            throws IOException {
        getJSONFromURLWithAuth("", "", "");
        return new JSONObject(getURLReturn(initConnWithAuth(url, authKey, authContents)));
        
    }
    
    /**
     * Gets a {@link JSONObject} from the provided url.
     *
     * @param url
     *         url that you want to query.
     * @param authContents
     *         Generally an auth key or client ID. This is what is sent in the url request header, with {@code
     *         "Authorization"} as a key.
     *
     * @return {@link JSONObject} constructed from the data that is returned by the url.
     *
     * @throws IOException
     */
    public static JSONObject getJSONFromURLWithAuth(String url, String authContents) throws IOException {
        return new JSONObject(getURLReturn(initConnWithAuth(url, "Authorization", authContents)));
    }
    
    private static URLConnection initConnWithAuth(String url, String authKey, String authContents) throws IOException {
        URLConnection urlConnection = initConnection(url);
        urlConnection.setRequestProperty(authKey, authContents);
        return urlConnection;
    }
    
    /**
     * Gets a {@link JSONArray} from the provided url.
     *
     * @param url
     *         url that you want to query.
     * @param authContents
     *         Generally an auth key or client ID. This is what is sent in the url request header, with {@code
     *         "Authorization"} as a key.
     *
     * @return {@link JSONArray} constructed from the data that is returned by the url.
     *
     * @throws IOException
     */
    public static JSONArray getJSONArrayFromURLWithAuth(String url, String authContents) throws IOException {
        return new JSONArray(getURLReturn(initConnWithAuth(url, "Authorization", authContents)));
    }
    
    /**
     * Gets a {@link JSONArray} from the provided url.
     *
     * @param url
     *         url that you want to query.
     * @param authKey
     *         Key that is used for the authentication in the url request header. It will generally be something like
     *         {@code "Authentication"}
     * @param authContents
     *         Generally an auth key or client ID. This is what is sent in the url request header, with {@code authKey}
     *         as a key.
     *
     * @return {@link JSONArray} constructed from the data that is returned by the url.
     *
     * @throws IOException
     */
    public static JSONArray getJSONArrayFromURLWithAuth(String url, String authKey, String authContents)
            throws IOException {
        return new JSONArray(getURLReturn(initConnWithAuth(url, authKey, authContents)));
    }
}
