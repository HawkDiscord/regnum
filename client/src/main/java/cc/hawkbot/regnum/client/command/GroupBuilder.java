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

package cc.hawkbot.regnum.client.command;

import cc.hawkbot.regnum.client.command.impl.GroupImpl;
import cc.hawkbot.regnum.client.command.permission.IPermissions;

/**
 * Builder for groups {@link Group}.
 */
@SuppressWarnings("unused")
public class GroupBuilder {

    private boolean _public;
    private String name;
    private String description;
    private IPermissions permissions;

    /**
     * Returns whether the group is public or not.
     * @return whether the group is public or not
     */
    public boolean isPublic() {
        return _public;
    }

    /**
     * Sets whether the group should be listed in help messages or not.
     * @param _public whether the group should be listed in help messages or not
     * @return the builder
     */
    public GroupBuilder setPublic(boolean _public) {
        this._public = _public;
        return this;
    }

    public GroupBuilder hide() {
        setPublic(false);
        return this;
    }

    /**
     * Returns the name of the group.
     * @return the name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the group.
     * @param name the name
     * @return the builder
     */
    public GroupBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Returns the description of the group.
     * @return the description of the group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the group.
     * @param description the description
     * @return the builder
     */
    public GroupBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the groups permissions {@link IPermissions}.
     * @return the groups permissions {@link IPermissions}
     */
    public IPermissions getPermissions() {
        return permissions;
    }

    /**
     * Sets the groups permissions {@link IPermissions}
     * @param permissions the permissions
     * @return the builder
     */
    public GroupBuilder setPermissions(IPermissions permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * Builds the group.
     * @return the group
     */
    public Group build() {
        return new GroupImpl(
                _public,
                name,
                description,
                permissions
        );
    }
}
