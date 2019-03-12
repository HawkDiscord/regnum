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

package cc.hawkbot.regnum.client.entities.permissions;

import cc.hawkbot.regnum.client.command.permission.PermissionManager;
import cc.hawkbot.regnum.client.entities.cassandra.CassandraEntity;
import cc.hawkbot.regnum.client.entities.cassandra.SnowflakeCassandraEntity;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.*;
import org.jetbrains.annotations.NotNull;

@Table(name = CassandraEntity.TABLE_PREFIX + "permissions")
@SuppressWarnings("unused")
public class PermissionNode extends SnowflakeCassandraEntity<PermissionNode> {

    @Transient
    private PermissionManager manager;

    private boolean negated;
    @PartitionKey(1)
    private long guildId;
    @PartitionKey(2)
    private String permissionNode;
    @PartitionKey(3)
    private PermissionTarget type;

    public PermissionNode(Long idAsLong, boolean negated, long guildId, String permissionNode, PermissionTarget type) {
        super(idAsLong);
        this.negated = negated;
        this.guildId = guildId;
        this.permissionNode = permissionNode;
        this.type = type;
    }

    public boolean isNegated() {
        return negated;
    }

    public PermissionTarget getType() {
        return type;
    }

    public long getGuildId() {
        return guildId;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    @NotNull
    public PermissionManager.PermissionInfoContainer toInfo() {
        return new PermissionManager.PermissionInfoContainer(getIdLong(), guildId, permissionNode);
    }

    public PermissionManager getManager() {
        return manager;
    }

    public void setManager(PermissionManager permissionManager) {
        this.manager = permissionManager;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public enum PermissionTarget {
        USER("permissiontargets.user"),
        ROLE("permissiontargets.role");

        private String translation;

        PermissionTarget(String translation) {
            this.translation = translation;
        }

        public String getTranslation() {
            return translation;
        }
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor {
        @Query("SELECT * FROM permissions WHERE id = :userId AND guild_id = :guildId AND node = :node")
        Result<PermissionNode> getNode(@Param("userId") long userId, @Param("guildId") long guildId, @Param("node") String node);
    }

}
