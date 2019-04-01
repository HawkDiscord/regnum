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

package cc.hawkbot.regnum.client.util;

import java.util.Optional;

/**
 * An interface for a message cache
 *
 * @author Roman Gräf
 */
public interface MessageCache {
    /**
     * Stores a message in the cache.
     *
     * @param messageId the message id
     * @param content   the message content
     */
    void storeMessage(long messageId, String content);

    /**
     * Checks whether a message is present in the cache
     *
     * @param messageId the id of the message
     * @return whether the message is present
     */
    boolean hasMesssage(long messageId);

    /**
     * Gets a cached message content or {@link Optional#empty()} if not present in the cache
     *
     * @param messageId the id of the message in question
     * @return the message content or {@link Optional#empty()}
     */
    Optional<String> getMessageContent(long messageId);

    /**
     * Delete all messages in the cache
     */
    void clearAll();

    /**
     * Delete one message in the cache
     *
     * @param messageId the id of the message to delete
     */
    void clearMessage(long messageId);

}
