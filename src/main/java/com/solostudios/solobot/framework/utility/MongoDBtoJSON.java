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

package com.solostudios.solobot.framework.utility;

import org.bson.Document;
import org.json.JSONObject;

import java.util.Map;

public final class MongoDBtoJSON {
    public static final JSONObject toJSONObject(Document document) {

        JSONObject result = new JSONObject();

        for (Map.Entry<String, Object> item : document.entrySet()) {
            result.put(item.getKey(), item.getValue());
        }

        return result;

    }
}
