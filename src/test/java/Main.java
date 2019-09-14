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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {
    static String[] words = new String[]{
            "game",
            "sword",
            "online",
            "girls",
            "fairy",
            "magician",
            "isekai",
            "black"
    };

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        int item = 0;
        Random r = new Random();

        for (int i = 0; i < 35; i++) {
            String urlStart = "https://api.jikan.moe/v3/search/anime/?q=";
            StringBuilder urlSearch = new StringBuilder();
            for (int j = 1; j > 0; j--) {
                urlSearch.append(words[r.nextInt(words.length)]);
            }
            String urlEnd = "&limit=1";

            String url;
            url = urlStart + URLEncoder.encode(urlSearch.toString(), StandardCharsets.UTF_8) + urlEnd;

            try {
                URL jikan = new URL(url);
                URLConnection connection = jikan.openConnection();


                DataInputStream dis = new DataInputStream(connection.getInputStream());
                String inputLine;

                System.out.print(++item + " - " + Math.round((System.currentTimeMillis() - startTime) / 1000) + " - ");

                while ((inputLine = dis.readLine()) != null) {
                    System.out.println(inputLine);
                }
                dis.close();
            } catch (IOException ignored) {
            }


            // JSONObject malJSON = WebUtils.readJSONObjectFromUrl(url);
            // System.out.println(malJSON != null ? malJSON.toString(11) : null);
        }


    }

}
