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
import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Predicate;

/**
 * Class that represents an json-array.
 */
@SuppressWarnings("unused")
public class JsonArray extends RegnumJsonNode {

    /**
     * Constructs a new JsonObject.
     *
     * @param json the json in a string
     * @throws JsonParseException if underlying input contains invalid content
     *                            of type {@link JsonParser} supports (JSON for default case)
     */
    @SuppressWarnings("JavaDoc")
    public JsonArray(String json) {
        super(json);
        if (!isArray()) {
            throw new IllegalArgumentException("Provided json does contain an object instead of an array.");
        }
    }

    /**
     * Returns the string value of the specified field or {@code null} if those field does not exist.
     *
     * @param index the index of the field
     * @return the value
     * @see JsonNode#asText()
     */
    public String getString(int index) {
        return get(index, JsonNode::isTextual, "string").asText();
    }

    /**
     * Returns the string value of the specified field or the specified default value if those field does not exist.
     *
     * @param index the index of the field
     * @param defaultValue the default value
     * @return the value
     * @see JsonNode#asText(String)
     */
    public String getString(int index, String defaultValue) {
        return get(index, JsonNode::isTextual, "string").asText(defaultValue);
    }

    /**
     * Returns the boolean value of the specified field or {@code null} if those field does not exist.
     *
     * @param index the index of the field
     * @return the value
     * @see JsonNode#asBoolean()
     */
    public boolean getBoolean(int index) {
        return get(index, JsonNode::isBoolean, "boolean").asBoolean();
    }

    /**
     * Returns the boolean value of the specified field or the specified default value if those field does not exist.
     *
     * @param index the index of the field
     * @param defaultValue the default value
     * @return the value
     * @see JsonNode#asBoolean(boolean)
     */
    public boolean getBoolean(int index, boolean defaultValue) {
        return get(index, JsonNode::isBoolean, "boolean").asBoolean(defaultValue);
    }

    /**
     * Returns the double value of the specified field or {@code null} if those field does not exist.
     *
     * @param index the index of the field
     * @return the value
     * @see JsonNode#asDouble()
     */
    public double getDouble(int index) {
        return get(index, JsonNode::isDouble, "double").asDouble();
    }

    /**
     * Returns the double value of the specified field or the specified default value if those field does not exist.
     *
     * @param index the index of the field
     * @param defaultValue the default value
     * @return the value
     * @see JsonNode#asDouble(double)
     */
    public double getDouble(int index, double defaultValue) {
        return get(index, JsonNode::isDouble, "double").asDouble(defaultValue);
    }

    /**
     * Returns the integer value of the specified field or {@code null} if those field does not exist.
     *
     * @param index the index of the field
     * @return the value
     * @see JsonNode#asInt()
     */
    public int getInt(int index) {
        return get(index, JsonNode::isInt, "int").asInt();
    }

    /**
     * Returns the integer value of the specified field or the specified default value if those field does not exist.
     *
     * @param index the index of the field
     * @param defaultValue the default value
     * @return the value
     * @see JsonNode#asInt(int)
     */
    public int getInt(int index, int defaultValue) {
        return get(index, JsonNode::isInt, "int").asInt(defaultValue);
    }

    /**
     * Returns the integer value of the specified field or {@code null} if those field does not exist.
     *
     * @param index the index of the field
     * @return the value
     * @see JsonNode#asInt()
     */
    public long getLong(int index) {
        return get(index, JsonNode::isLong, "long").asLong();
    }

    /**
     * Returns the long value of the specified field or the specified default value if those field does not exist.
     *
     * @param index the index of the field
     * @param defaultValue the default value
     * @return the value
     * @see JsonNode#asLong()
     */
    public long getLong(int index, long defaultValue) {
        return get(index, JsonNode::isLong, "long").asLong(defaultValue);
    }

    public JsonNode get(int index, Predicate<JsonNode> predicate, String name) {
        var object = get(index);
        return check(object, predicate, name);
    }

    @Override
    public JsonNode get(int index) {
        throw new UnsupportedOperationException("Fields are not supported in json arrays");
    }

    @Override
    public JsonNode path(int index) {
        throw new UnsupportedOperationException("Fields are not supported in json arrays");
    }
}
