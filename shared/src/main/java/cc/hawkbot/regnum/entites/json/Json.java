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

package cc.hawkbot.regnum.entites.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * Class with some useful Json helper methods
 */
@SuppressWarnings("WeakerAccess")
public class Json {

    /**
     * Jackson mapper used for serialization
     */
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    public static ObjectMapper JACKSON;

    static {
        JACKSON = new ObjectMapper();
    }

    /**
     * Converts a object into json.
     *
     * @param writer the writer to format the object
     * @param obj    the object
     * @return the json object as a string
     */
    public static String toJson(ObjectWriter writer, Object obj) {
        try {
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encodes an {@link Object} into json.
     *
     * @return the json in a string
     * @see Json#toJson(ObjectWriter, Object)
     */
    public static String toJson(Object obj) {
        return toJson(JACKSON.writer(), obj);
    }

    /**
     * Encodes an {@link Object} into prettily formatted json.
     *
     * @return the json in a string
     * @see Json#toJson(ObjectWriter, Object)
     */
    public static String toPrettyJson(Object obj) {
        return toJson(JACKSON.writerWithDefaultPrettyPrinter(), obj);
    }

    /**
     * Parses a {@link JsonNode}.
     *
     * @param json the json in a string
     * @return the json node
     * @throws JsonParseException if underlying input contains invalid content
     *                            of type {@link JsonParser} supports (JSON for default case)
     */
    @SuppressWarnings("JavaDoc")
    public static JsonNode readJson(String json) {
        try {
            return JACKSON.readTree(json);
        } catch (IOException e) {
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
