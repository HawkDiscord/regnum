/*
 * Regnum - A Discord bot clustering system made for Hawk
 *
 * Copyright (C) 2019  Michael Rittmeister
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package cc.hawkbot.regnum.test;

import cc.hawkbot.regnum.entities.json.JsonArray;
import cc.hawkbot.regnum.entities.json.JsonObject;
import cc.hawkbot.regnum.entities.json.RegnumJsonNode;
import org.junit.Test;

@SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
public class JSONTest {

    private final String ARRAY = "[\"HUI\",\"HUII\",\"HUIII\"]";
    private final String OBJECT = "{\"test\":true,\"string\":\"string\"}";

    @Test
    public void test() {
        array();
        object();
    }

    private void array() {
        var array = new JsonArray(ARRAY);
        testObject(ARRAY, array);
    }

    private void object() {
        var object = new JsonObject(OBJECT);
        testObject(OBJECT, object);
    }

    /**
     * Compares the original json string with the generated json string.
     *
     * @param origin the original string
     * @param json   the parsed object
     */
    private void testObject(String origin, RegnumJsonNode json) {
        System.out.println("Testing: " + origin);
        var nodeFormatted = json.encode();
        if (!nodeFormatted.equals(origin)) {
            System.out.println("Origin: " + origin);
            System.out.println("Node: " + nodeFormatted);
            throw new RuntimeException("Parsing was invalid");
        }
        System.out.println("Test passed");
    }
}
