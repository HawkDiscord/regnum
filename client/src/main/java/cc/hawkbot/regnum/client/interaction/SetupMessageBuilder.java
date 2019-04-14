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
import net.dv8tion.jda.api.entities.User;

import java.util.Collection;
import java.util.List;

public abstract class SetupMessageBuilder<M extends SetupMessage, B extends SetupMessageBuilder<M, B>> extends ReactableMessageBuilder<M, B>{

    public SetupMessageBuilder(Regnum regnum) {
        super(regnum);
    }

    @Override
    public B setAuthorizedUsers(List<User> authorizedUsers) {
        if (authorizedUsers.size() > 1) {
            throw new UnsupportedOperationException("Setups do only support one authorized user");
        }
        return super.setAuthorizedUsers(authorizedUsers);
    }

    @Override
    public B authorizeUsers(User... authorizedUsers) {
        throw new UnsupportedOperationException("Setups do only support one authorized user");
    }

    @Override
    public B authorizeUsers(Collection<User> authorizedUsers) {
        throw new UnsupportedOperationException("Setups do only support one authorized user");
    }

}
