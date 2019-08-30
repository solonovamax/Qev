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

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Sort {
    @NotNull
    public static <T, V extends Comparable<V>> HashMap<T, V> sortByValue(@NotNull HashMap<T, V> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<T, V>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        list.sort(Comparator.comparing(Map.Entry::getValue));

        // put data from sorted list to hashmap
        HashMap<T, V> temp = new LinkedHashMap<>();
        for (Map.Entry<T, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        HashMap<T, V> fin = new LinkedHashMap<>();


        //Reverse HashMap.

        Set<T> keySet = temp.keySet();
        T[] keyArray = (T[]) keySet.toArray();
        for (int i = keyArray.length - 1; i >= 0; i--) {
            T key = keyArray[i];
            V value = temp.get(key);
            fin.put(key, value);
        }


        return fin;
    }

    @NotNull
    public static <T extends Comparable<T>, V> HashMap<T, V> sortByKey(@NotNull HashMap<T, V> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<T, V>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        list.sort(Comparator.comparing(Map.Entry::getKey));

        // put data from sorted list to hashmap
        HashMap<T, V> temp = new LinkedHashMap<>();
        for (Map.Entry<T, V> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
