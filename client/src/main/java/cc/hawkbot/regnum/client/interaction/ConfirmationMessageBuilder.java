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

package cc.hawkbot.regnum.client.interaction;

import cc.hawkbot.regnum.client.Regnum;
import cc.hawkbot.regnum.client.util.TranslationUtil;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;

public class ConfirmationMessageBuilder extends ReactableMessageBuilder<ConfirmationMessage, ConfirmationMessageBuilder> {

    private String yesEmote = "✅";
    private String yesKeyword = "yes";
    private String noEmote = "\uD83D\uDEAB";
    private String noKeyword = "no";
    private Consumer<InteractableMessage.Context> yesConsumer;
    private Consumer<InteractableMessage.Context> noConsumer;
    
    public ConfirmationMessageBuilder(Regnum regnum) {
        super(regnum);
    }

    public ConfirmationMessageBuilder setYesEmote(String yesEmote) {
        this.yesEmote = yesEmote;
        return this;
    }

    public ConfirmationMessageBuilder setYesKeyword(String yesKeyword) {
        this.yesKeyword = yesKeyword;
        return this;
    }

    public ConfirmationMessageBuilder setNoEmote(String noEmote) {
        this.noEmote = noEmote;
        return this;
    }

    public ConfirmationMessageBuilder setNoKeyword(String noKeyword) {
        this.noKeyword = noKeyword;
        return this;
    }

    public ConfirmationMessageBuilder setYesConsumer(Consumer<InteractableMessage.Context> yesConsumer) {
        this.yesConsumer = yesConsumer;
        return this;
    }

    public ConfirmationMessageBuilder setNoConsumer(Consumer<InteractableMessage.Context> noConsumer) {
        this.noConsumer = noConsumer;
        return this;
    }

    @Override
    public ConfirmationMessageBuilder setMessage(TextChannel channel, EmbedBuilder builder) {
        Preconditions.checkState(!getAuthorizedUsers().isEmpty(), "Authorized users may not be empty");
        var user = getAuthorizedUsers().get(0);
        var message = String.format(TranslationUtil.translate(
                getRegnum(),
                "phrases.confirmation.footer",
                user), yesKeyword, noKeyword, getTimeout(), getTimeUnit().toString().toLowerCase());
        builder.setFooter(message, null);
        return super.setMessage(channel, builder);
    }

    @Override
    protected void checks() {
        Preconditions.checkNotNull(yesConsumer, "Yes consumer may not be null");
        Preconditions.checkNotNull(noConsumer, "No consumer may not be null");
        super.checks();
    }

    @Override
    public ConfirmationMessage build() {
        checks();
        return new ConfirmationMessage(
                getRegnum(),
                getMessage(),
                getAuthorizedUsers(),
                getTimeout(),
                getTimeUnit(),
                yesEmote,
                yesKeyword,
                noEmote,
                noKeyword,
                yesConsumer,
                noConsumer
        );
    }
}
