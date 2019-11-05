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

package com.solostudios.solobot.framework.main;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings {
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);

    private static final File settings = new File("settings.json");
    @SuppressWarnings("FieldCanBeLocal")
    private static JSONObject jsettings;


    /**
     * Used to get the settings within the `settings.json` file.
     *
     * @return returns JSON settings object.
     */
    @NotNull
    public static JSONObject get() {
        if (!settings.exists()) {
            logger.info("Settings file does not exist.\n" +
                    "Creating a new one.");
            try {
                Settings.create();
            } catch (IOException e) {
                logger.error("Could not create settings file.", e);
                e.printStackTrace();
            }

            logger.info("Created your config file. Now running the bot.\n" +
                    "Terminating the process now. Please provide a token for your discord bot.");
            System.exit(0);
        }

        JSONObject jsettings = null;
        logger.info("Loading settings from file.");
        try {
            jsettings = Settings.load();
        } catch (IOException e) {
            logger.error("Couldn't load settings file.", e);
            System.exit(0);
        }

        return jsettings;
    }

    /**
     * @throws IOException Will be thrown if the file can't be written.
     */
    private static void create() throws IOException {
        Files.write(Paths.get(settings.getPath()),
                new JSONObject()
                        .put("prefix", "!")
                        .put("token", "YOUR-TOKEN-HERE")
                        .put("botOwner", "YOUR-ID-HERE")
                        .put("youtube", "YOUR-TOKEN-HERE")
                        .put("imgur", "YOUR-TOKEN-HERE")
                        .put("debug", false)
                        .toString(11).getBytes()
        );


    }

    private static JSONObject load() throws IOException {
        jsettings = new JSONObject(Files.readString(Paths.get(settings.getPath())));
        return jsettings;
    }

}
