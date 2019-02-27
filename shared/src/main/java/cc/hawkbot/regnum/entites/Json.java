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

package cc.hawkbot.regnum.entites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Class with some useful Json helper methods
 */
@SuppressWarnings("WeakerAccess")
public class Json {

    /**
     * Jackson mapper used for serialization
     */
    @SuppressWarnings("WeakerAccess")
    public static ObjectMapper JACKSON;

    static {
        JACKSON = new ObjectMapper();
    }

    /**
     * Converts a object into json.
     *
     * @param obj the object
     * @return the json object as a string
     */
    public static String toJson(Object obj) {
        try {
            return JACKSON.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a json object into it's entity object.
     *
     * @param type the class of the object
     * @param json the json object
     * @param <T>  the object type
     * @return the generated object
     */
    public static <T> T fromJson(Class<T> type, String json) {
        try {
            return JACKSON.readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parses a json string into a {@link JsonNode}.
     *
     * @param json the json object
     * @return the json node
     */
    public static JsonNode parseJson(String json) {
        try {
            return JACKSON.readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
