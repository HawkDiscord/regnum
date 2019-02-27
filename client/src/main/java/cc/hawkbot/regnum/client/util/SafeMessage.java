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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.internal.requests.restaction.MessageActionImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Util class for sending messages with permission checks.
 */
@SuppressWarnings({"WeakerAccess", "unused", "CanBeFinal"})
public class SafeMessage {

    public static Runnable DEFAULT_ERROR_HANDLER = () -> {
    };
    public static int DEFAULT_DELETE_TIME = -1;
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private static Boolean hasPermission(
            Guild guild,
            GuildChannel channel,
            Permission... permissions
    ) {
        return guild.getSelfMember().hasPermission(channel, permissions);
    }

    private static MessageAction checkPermissions(Message message, TextChannel channel,
                                                  Runnable errorHandler, BiFunction<Message, TextChannel, MessageAction> processor) {
        var guild = channel.getGuild();
        if (!message.getEmbeds().isEmpty()) {
            if (hasPermission(guild, channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
                return processor.apply(message, channel);
            } else if (hasPermission(guild, channel, Permission.MESSAGE_WRITE)) {
                return processor.apply(
                        new MessageBuilder(FormatUtil.stringifyEmbed(message.getEmbeds().get(0)))
                                .build(), channel);
            } else {
                errorHandler.run();
            }
        } else if (hasPermission(guild, channel, Permission.MESSAGE_WRITE)) {
            return processor.apply(message, channel);
        } else {
            errorHandler.run();
        }
        //noinspection ConstantConditions
        return new MessageActionImpl(null, null, null);
    }


    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message
     * @param errorHandler    the {@link Runnable} that should run when there is no permission to edit
     *                        the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage, Message newMessage,
                                            Runnable errorHandler) {
        var channel = ((TextChannel) previousMessage.getChannel());
        return checkPermissions(newMessage, channel, errorHandler,
                (message, textChannel) -> previousMessage.editMessage(message));
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage, Message newMessage) {
        return editMessage(previousMessage, newMessage, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @param errorHandler    the {@link Runnable} that should run when there is no permission to edit
     *                        the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage,
                                            MessageBuilder newMessage, Runnable errorHandler) {
        return editMessage(previousMessage, newMessage.build(), errorHandler);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage,
                                            MessageBuilder newMessage) {
        return editMessage(previousMessage, newMessage, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @param errorHandler    the {@link Runnable} that should run when there is no permission to edit
     *                        the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage,
                                            MessageEmbed newMessage, Runnable errorHandler) {
        return editMessage(previousMessage, new MessageBuilder().setEmbed(newMessage), errorHandler);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @return a future containing the edited {@link Message} the message
     */
    public static MessageAction editMessage(Message previousMessage, MessageEmbed newMessage) {
        return editMessage(previousMessage, newMessage, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @param errorHandler    the {@link Runnable} that should run when there is no permission to edit
     *                        the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage,
                                            EmbedBuilder newMessage, Runnable errorHandler) {
        return editMessage(previousMessage, newMessage.build(), errorHandler);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @return a future containing the edited {@link Message} the message
     */
    public static MessageAction editMessage(Message previousMessage,
                                            EmbedBuilder newMessage) {
        return editMessage(previousMessage, newMessage, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @param errorHandler    the {@link Runnable} that should run when there is no permission to edit
     *                        the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage,
                                            String newMessage, Runnable errorHandler) {
        return editMessage(previousMessage, new MessageBuilder().setContent(newMessage), errorHandler);
    }

    /**
     * Util method that checks permissions before editing a {@link Message}.
     *
     * @param previousMessage the message that should be edited
     * @param newMessage      the new content of the message the message
     * @return a future containing the edited {@link Message}
     */
    public static MessageAction editMessage(Message previousMessage, String newMessage) {
        return editMessage(previousMessage, newMessage, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending a message {@link Message}.
     *
     * @param content      the message {@link Message} that should be send
     * @param channel      the channel in which the message should be send
     * @param deleteTime   the time after the message should be deleted (-1 for disable)
     * @param errorHandler A runnable that will be called when there was no permission to send the
     *                     message
     * @return the future of the API Request
     */
    public static MessageAction sendMessage(
            Message content,
            TextChannel channel,
            int deleteTime,
            Runnable errorHandler
    ) {
        return delete(checkPermissions(content, channel, errorHandler,
                (message, textChannel) -> textChannel.sendMessage(message)), deleteTime);
    }

    /**
     * Util method that checks permissions before sending a message {@link Message} with default error
     * handler.
     *
     * @param content    the message {@link Message} that should be send
     * @param channel    the channel in which the message should be send
     * @param deleteTime the time after the message should be deleted (-1 for disable)
     * @return the future of the API Request
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(
            Message content,
            TextChannel channel,
            int deleteTime
    ) {
        return sendMessage(content, channel, deleteTime, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending a message {@link Message} with default error
     * handler.
     *
     * @param content the message {@link Message} that should be send
     * @param channel the channel in which the message should be send
     * @return the future of the API Request
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(Message content, TextChannel channel) {
        return sendMessage(content, channel, DEFAULT_DELETE_TIME, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending a message builder {@link MessageBuilder}.
     *
     * @param content      the message builder {@link MessageBuilder} that should be send
     * @param channel      the channel in which the message should be send
     * @param deleteTime   the time after the message should be deleted (-1 for disable)
     * @param errorHandler A runnable that will be called when there was no permission to send the
     *                     message
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(Message, TextChannel, int, Runnable)
     */
    public static MessageAction sendMessage(
            MessageBuilder content,
            TextChannel channel,
            int deleteTime,
            Runnable errorHandler
    ) {
        return sendMessage(content.build(), channel, deleteTime, errorHandler);
    }

    /**
     * Util method that checks permissions before sending a message builder {@link MessageBuilder}
     * using default error handler.
     *
     * @param content    the message builder {@link MessageBuilder} that should be send
     * @param channel    the channel in which the message should be send
     * @param deleteTime the time after the message should be deleted (-1 for disable)
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(Message, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(
            MessageBuilder content,
            TextChannel channel,
            int deleteTime
    ) {
        return sendMessage(content, channel, deleteTime, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending a message builder {@link MessageBuilder}
     * using default error handler without deleting.
     *
     * @param content the message builder {@link MessageBuilder} that should be send
     * @param channel the channel in which the message should be send
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(Message, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(MessageBuilder content, TextChannel channel) {
        return sendMessage(content, channel, DEFAULT_DELETE_TIME, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed {@link MessageEmbed}.
     *
     * @param content      the MessageEmbed {@link MessageEmbed} that should be send
     * @param channel      the channel in which the message should be send
     * @param deleteTime   the time after the message should be deleted (-1 for disable)
     * @param errorHandler A runnable that will be called when there was no permission to send the
     *                     message
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     */
    public static MessageAction sendMessage(
            MessageEmbed content,
            TextChannel channel,
            int deleteTime,
            Runnable errorHandler
    ) {
        return sendMessage(new MessageBuilder().setEmbed(content), channel, deleteTime, errorHandler);
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed {@link MessageEmbed} with default error
     * handler.
     *
     * @param content    the MessageEmbed {@link MessageEmbed} that should be send
     * @param channel    the channel in which the message should be send
     * @param deleteTime the time after the message should be deleted (-1 for disable)
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(
            MessageEmbed content,
            TextChannel channel,
            int deleteTime
    ) {
        return sendMessage(
                new MessageBuilder().setEmbed(content),
                channel,
                deleteTime,
                DEFAULT_ERROR_HANDLER
        );
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed {@link MessageEmbed} with default error
     * handler without deleting.
     *
     * @param content the MessageEmbed {@link MessageEmbed} that should be send
     * @param channel the channel in which the message should be send
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(MessageEmbed content, TextChannel channel) {
        return sendMessage(
                new MessageBuilder().setEmbed(content),
                channel,
                DEFAULT_DELETE_TIME,
                DEFAULT_ERROR_HANDLER
        );
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed builder {@link EmbedBuilder}.
     *
     * @param content      the MessageEmbed builder {@link EmbedBuilder} that should be send
     * @param channel      the channel in which the message should be send
     * @param deleteTime   the time after the message should be deleted (-1 for disable)
     * @param errorHandler A runnable that will be called when there was no permission to send the
     *                     message
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageEmbed, TextChannel, int, Runnable)
     */
    public static MessageAction sendMessage(
            EmbedBuilder content,
            TextChannel channel,
            int deleteTime,
            Runnable errorHandler
    ) {
        return sendMessage(content.build(), channel, deleteTime, errorHandler);
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed builder {@link EmbedBuilder} using
     * default error handler.
     *
     * @param content    the MessageEmbed builder {@link EmbedBuilder} that should be send
     * @param channel    the channel in which the message should be send
     * @param deleteTime the time after the message should be deleted (-1 for disable)
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageEmbed, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(
            EmbedBuilder content,
            TextChannel channel,
            int deleteTime
    ) {
        return sendMessage(content.build(), channel, deleteTime, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending an MessageEmbed builder {@link EmbedBuilder} using
     * default error handler without deleting.
     *
     * @param content the MessageEmbed builder {@link EmbedBuilder} that should be send
     * @param channel the channel in which the message should be send
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageEmbed, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(EmbedBuilder content, TextChannel channel) {
        try {
            return sendMessage(content.build(), channel, DEFAULT_DELETE_TIME, DEFAULT_ERROR_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //noinspection ConstantConditions
        return new MessageActionImpl(null, null, null);
    }

    /**
     * Util method that checks permissions before sending plain text {@link String}.
     *
     * @param content      the plain text {@link String} that should be send
     * @param channel      the channel in which the message should be send
     * @param deleteTime   the time after the message should be deleted (-1 for disable)
     * @param errorHandler A runnable that will be called when there was no permission to send the
     *                     message
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     */
    public static MessageAction sendMessage(
            String content,
            TextChannel channel,
            int deleteTime,
            Runnable errorHandler
    ) {
        return sendMessage(
                new MessageBuilder().setContent(content).build(),
                channel,
                deleteTime,
                errorHandler
        );
    }

    /**
     * Util method that checks permissions before sending plain text {@link String} using default
     * error handler.
     *
     * @param content    the plain text {@link String} that should be send
     * @param channel    the channel in which the message should be send
     * @param deleteTime the time after the message should be deleted (-1 for disable)
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(
            String content,
            TextChannel channel,
            int deleteTime
    ) {
        return sendMessage(content, channel, deleteTime, DEFAULT_ERROR_HANDLER);
    }

    /**
     * Util method that checks permissions before sending plain text {@link String} using default
     * error handler without deleting.
     *
     * @param content the plain text {@link String} that should be send
     * @param channel the channel in which the message should be send
     * @return the future of the API Request
     * @see SafeMessage#sendMessage(MessageBuilder, TextChannel, int, Runnable)
     * @see SafeMessage#DEFAULT_ERROR_HANDLER
     */
    public static MessageAction sendMessage(String content, TextChannel channel) {
        return sendMessage(content, channel, DEFAULT_DELETE_TIME, DEFAULT_ERROR_HANDLER);
    }

    private static MessageAction delete(MessageAction future, int deleteTime) {
        if (deleteTime != -1) {
            future.queue(msg -> scheduler.schedule(
                    (msg::delete), deleteTime, TimeUnit.SECONDS));
        }
        return future;

    }
}
