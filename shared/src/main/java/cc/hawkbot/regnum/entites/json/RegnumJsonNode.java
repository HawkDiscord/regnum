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

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Generic class for parsing json entities from a string.
 * @see JsonObject for parsing an object
 * @see JsonArray for parsing an array
 * @see Json
 */
@SuppressWarnings("unused")
public class RegnumJsonNode extends JsonNode {

    private final JsonNode jsonNode;

    RegnumJsonNode(String json) {
        this.jsonNode = Json.readJson(json);
    }

    /**
     * Encodes a {@link RegnumJsonNode} into json.
     * @see Json#toJson(Object)
     * @return the json in a string
     */
    public String encode() {
        return Json.toJson(asObject());
    }

    /**
     * Encodes a {@link RegnumJsonNode} into prettily formatted json.
     * @see Json#toPrettyJson(Object)
     * @return the json in a string
     */
    public String encodePrettily() {
        return Json.toPrettyJson(asObject());
    }

    private Object asObject() {
        return Json.fromJson(Object.class, this.toString());
    }

    protected <T> T check(T reference, Predicate<T> predicate, String name) {
        if (!predicate.test(reference)) {
            throw new IllegalStateException("That field is not an " + name);
        }
        return reference;
    }

    // Methods to implement interface

    @Override
    public JsonNode get(String fieldName) {
        return jsonNode.get(fieldName);
    }

    @Override
    public boolean isArray() {
        return jsonNode.isArray();
    }

    @Override
    public <T extends JsonNode> T deepCopy() {
        return jsonNode.deepCopy();
    }

    @Override
    public JsonToken asToken() {
        return jsonNode.asToken();
    }

    @Override
    public JsonParser.NumberType numberType() {
        return jsonNode.numberType();
    }

    @Override
    public JsonNode get(int index) {
        return jsonNode.get(index);
    }

    @Override
    public JsonNode path(String fieldName) {
        return jsonNode.path(fieldName);
    }

    @Override
    public JsonNode path(int index) {
        return jsonNode.path(index);
    }

    @Override
    public JsonParser traverse() {
        return jsonNode.traverse();
    }

    @Override
    public JsonParser traverse(ObjectCodec codec) {
        return jsonNode.traverse(codec);
    }

    @Override
    protected JsonNode _at(JsonPointer ptr) {
        return jsonNode.at(ptr);
    }

    @Override
    public JsonNodeType getNodeType() {
        return jsonNode.getNodeType();
    }

    @Override
    public String asText() {
        return jsonNode.asText();
    }

    @Override
    public JsonNode findValue(String fieldName) {
        return jsonNode.findValue(fieldName);
    }

    @Override
    public JsonNode findPath(String fieldName) {
        return jsonNode.findPath(fieldName);
    }

    @Override
    public JsonNode findParent(String fieldName) {
        return jsonNode.findParent(fieldName);
    }

    @Override
    public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
        return jsonNode.findValues(fieldName, foundSoFar);
    }

    @Override
    public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
        return jsonNode.findValuesAsText(fieldName, foundSoFar);
    }

    @Override
    public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
        return jsonNode.findParents(fieldName, foundSoFar);
    }

    @Override
    public String toString() {
        return jsonNode.toString();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return jsonNode.equals(o);
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        jsonNode.serialize(gen, serializers);
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        jsonNode.serializeWithType(gen, serializers, typeSer);
    }
    
    @Override
    public int size() {
        return jsonNode.size();
    }

    @Override
    public boolean isMissingNode() {
        return jsonNode.isMissingNode();
    }

    @Override
    public boolean isObject() {
        return jsonNode.isObject();
    }

    @Override
    public Iterator<String> fieldNames() {
        return jsonNode.fieldNames();
    }

    @Override
    public boolean isIntegralNumber() {
        return jsonNode.isIntegralNumber();
    }

    @Override
    public boolean isFloatingPointNumber() {
        return jsonNode.isFloatingPointNumber();
    }

    @Override
    public boolean isShort() {
        return jsonNode.isShort();
    }

    @Override
    public boolean isInt() {
        return jsonNode.isInt();
    }

    @Override
    public boolean isLong() {
        return jsonNode.isLong();
    }

    @Override
    public boolean isFloat() {
        return jsonNode.isFloat();
    }

    @Override
    public boolean isDouble() {
        return jsonNode.isDouble();
    }

    @Override
    public boolean isBigDecimal() {
        return jsonNode.isBigDecimal();
    }

    @Override
    public boolean isBigInteger() {
        return jsonNode.isBigInteger();
    }

    @Override
    public boolean canConvertToInt() {
        return jsonNode.canConvertToInt();
    }

    @Override
    public boolean canConvertToLong() {
        return jsonNode.canConvertToLong();
    }

    @Override
    public String textValue() {
        return jsonNode.textValue();
    }

    @Override
    public byte[] binaryValue() throws IOException {
        return jsonNode.binaryValue();
    }

    @Override
    public boolean booleanValue() {
        return jsonNode.booleanValue();
    }

    @Override
    public Number numberValue() {
        return jsonNode.numberValue();
    }

    @Override
    public short shortValue() {
        return jsonNode.shortValue();
    }

    @Override
    public int intValue() {
        return jsonNode.intValue();
    }

    @Override
    public long longValue() {
        return jsonNode.longValue();
    }

    @Override
    public float floatValue() {
        return jsonNode.floatValue();
    }

    @Override
    public double doubleValue() {
        return jsonNode.doubleValue();
    }

    @Override
    public BigDecimal decimalValue() {
        return jsonNode.decimalValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return jsonNode.bigIntegerValue();
    }

    @Override
    public String asText(String defaultValue) {
        return jsonNode.asText(defaultValue);
    }

    @Override
    public int asInt() {
        return jsonNode.asInt();
    }

    @Override
    public int asInt(int defaultValue) {
        return jsonNode.asInt(defaultValue);
    }

    @Override
    public long asLong() {
        return jsonNode.asLong();
    }

    @Override
    public long asLong(long defaultValue) {
        return jsonNode.asLong(defaultValue);
    }

    @Override
    public double asDouble() {
        return jsonNode.asDouble();
    }

    @Override
    public double asDouble(double defaultValue) {
        return jsonNode.asDouble(defaultValue);
    }

    @Override
    public boolean asBoolean() {
        return jsonNode.asBoolean();
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return jsonNode.asBoolean(defaultValue);
    }

    @Override
    public boolean has(String fieldName) {
        return jsonNode.has(fieldName);
    }

    @Override
    public boolean has(int index) {
        return jsonNode.has(index);
    }

    @Override
    public boolean hasNonNull(String fieldName) {
        return jsonNode.hasNonNull(fieldName);
    }

    @Override
    public boolean hasNonNull(int index) {
        return jsonNode.hasNonNull(index);
    }

    @Override
    public Iterator<JsonNode> elements() {
        return jsonNode.elements();
    }

    @Override
    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return jsonNode.fields();
    }

    @Override
    public JsonNode with(String propertyName) {
        return jsonNode.with(propertyName);
    }

    @Override
    public JsonNode withArray(String propertyName) {
        return jsonNode.withArray(propertyName);
    }

    @Override
    public boolean equals(Comparator<JsonNode> comparator, JsonNode other) {
        return jsonNode.equals(comparator, other);
    }

    @Override
    public boolean isEmpty(SerializerProvider serializers) {
        return jsonNode.isEmpty(serializers);
    }

    @Override
    public void forEach(Consumer<? super JsonNode> action) {
        jsonNode.forEach(action);
    }

    @Override
    public Spliterator<JsonNode> spliterator() {
        return jsonNode.spliterator();
    }

    @Override
    public int hashCode() {
        return jsonNode.hashCode();
    }
}
