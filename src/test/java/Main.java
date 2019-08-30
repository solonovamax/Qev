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

public class Main {
    public static void main(String[] args) {
        InterfaceUse.useInterface((x, y) -> {
            for (int i = 0; i < x; i++) {
                String[] arr = new String[10000];
                for (int j = 0; j < 10000; j++) {
                    arr[j] = new Object().toString();
                }
                if (i % 10 == 0) {
                    System.out.println("[2:] " + Math.sin(i) + " " + Math.signum(i) + " " + Math.sqrt(i));
                }
            }
        });
        for (int i = 0; i < 100000; i++) {
            if (i % 1000 == 0) {
                System.out.println("[1:] " + Math.sin(i) + " " + Math.signum(i) + " " + Math.sqrt(i));
            }
        }
    }

}
