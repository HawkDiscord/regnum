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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Some presets for embeds.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class EmbedUtil {

    /**
     * Converts an Embed {@link MessageEmbed} to a Message {@link
     * Message}.
     *
     * @param embed the Embed {@link MessageEmbed}
     * @return the Message {@link Message}
     */
    @NotNull
    public static Message message(@NotNull MessageEmbed embed) {
        return new MessageBuilder().setEmbed(embed).build();
    }

    /**
     * Converts an Embed {@link EmbedBuilder} to a Message {@link
     * Message}.
     *
     * @param builder the builder
     * @return the Message {@link Message}
     * @see EmbedUtil#message(MessageEmbed)
     */
    @NotNull
    public static Message message(@NotNull EmbedBuilder builder) {
        return message(builder.build());
    }

    /**
     * Helper method for creating embeds.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @param footer      the footer of the embed
     * @param footerLink  the link to the image of the footer
     * @param color       the color of the embed
     * @return the embed's builder
     */
    @SuppressWarnings("ConstantConditions")
    @NotNull
    public static EmbedBuilder embed(
            @Nullable String title,
            @Nullable String description,
            @Nullable String footer,
            @Nullable String footerLink,
            @Nullable Color color
    ) {
        var builder = new EmbedBuilder();
        if (title != null) {
            builder = builder.setTitle(title);
        }
        if (description != null) {
            // You're shit intellij
            builder = builder.setDescription(description);
        }
        if (footer != null && footerLink != null) {
            builder = builder.setFooter(footer, footerLink);
        }
        if (color != null) {
            builder = builder.setColor(color);
        }
        if (builder.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least a title, footer or description");
        }
        return builder;
    }

    /**
     * Helper method for basic info-embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @param footer      the footer of the embed
     * @param color       the color of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder embed(@Nullable String title, @Nullable String description, @Nullable String footer, @Nullable Color color) {
        return embed(title, description, footer, null, color);
    }

    /**
     * Helper method for basic embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @param color       the color of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder embed(@Nullable String title, @Nullable String description, @Nullable Color color) {
        return embed(title, description, null, color);
    }

    /**
     * Helper method for small embed.
     *
     * @param description the description of the embed
     * @param color       the color of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder embed(@Nullable String description, @Nullable Color color) {
        return embed(null, description, color);
    }

    /**
     * Helper method for basic info-embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder info(@Nullable String title, @Nullable String description) {
        return embed(
                String.format("%s %s", Emotes.INFO, title),
                description,
                Colors.BLUE
        );
    }

    /**
     * Helper method for small info-embed.
     *
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder info(@Nullable String description) {
        return embed(description, Colors.BLUE);
    }

    /**
     * Helper method for basic success-embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder success(@Nullable String title, @Nullable String description) {
        return embed(
                String.format("%s %s", Emotes.SUCCESS, title),
                description,
                Colors.DARK_GREEN
        );
    }

    /**
     * Helper method for small success-embed.
     *
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder success(@Nullable String description) {
        return embed(description, Colors.DARK_GREEN);
    }

    /**
     * Helper method for basic error-embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder error(@Nullable String title, @Nullable String description) {
        return embed(
                String.format("%s %s", Emotes.ERROR, title),
                description,
                Colors.DARK_RED
        );
    }

    /**
     * Helper method for small error-embed.
     *
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder error(@Nullable String description) {
        return embed(description, Colors.DARK_RED);
    }

    /**
     * Helper method for basic warn-embed.
     *
     * @param title       the title of the embed
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder warn(@Nullable String title, @Nullable String description) {
        return embed(
                String.format("%s %s", Emotes.WARN, title),
                description,
                Colors.YELLOW
        );
    }

    /**
     * Helper method for small warn-embed.
     *
     * @param description the description of the embed
     * @return the embed's builder
     */
    @NotNull
    public static EmbedBuilder warn(@Nullable String description) {
        return embed(description, Colors.YELLOW);
    }

}