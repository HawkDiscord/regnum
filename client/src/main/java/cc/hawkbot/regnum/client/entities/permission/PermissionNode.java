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

package cc.hawkbot.regnum.client.entities.permission;

import cc.hawkbot.regnum.client.command.permission.PermissionManager;
import cc.hawkbot.regnum.client.entities.cache.CassandraCache;
import cc.hawkbot.regnum.entites.cassandra.CassandraEntity;
import cc.hawkbot.regnum.entites.cassandra.SnowflakeCassandraEntity;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.*;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletionStage;

/**
 * Entity which represents permissions.
 */
@Table(name = CassandraEntity.TABLE_PREFIX + "permissions")
@SuppressWarnings("unused")
public class PermissionNode extends SnowflakeCassandraEntity<PermissionNode> {

    @Transient
    private PermissionManager manager;

    private boolean negated;
    @PartitionKey(1)
    @Column(name = "guild_id")
    private long guildId;
    @PartitionKey(2)
    @Column(name = "permission_node")
    private String permissionNode;
    @PartitionKey(3)
    private PermissionTarget type;

    /**
     * Used for serialization.
     */
    @CassandraCache.Constructor
    public PermissionNode() {
        super(-1L);
    }

    /**
     * Constructs a new PermissionNode.
     *
     * @param idAsLong       the id of the permission holder
     * @param negated        whether the node should be negated or not
     * @param guildId        the id of the guild where the permission should be valid on
     * @param permissionNode the node itself
     * @param type           the type{@link PermissionTarget}
     * @see PermissionManager#createPermissionNode(IPermissionHolder, String, boolean)
     * @see PermissionManager#createPermissionNode(long, long, String, PermissionTarget, boolean)
     */
    public PermissionNode(Long idAsLong, boolean negated, long guildId, String permissionNode, PermissionTarget type) {
        super(idAsLong);
        this.negated = negated;
        this.guildId = guildId;
        this.permissionNode = permissionNode;
        this.type = type;
    }

    /**
     * Returns whether the node is negated or not.
     *
     * @return whether the node is negated or not
     */
    public boolean isNegated() {
        return negated;
    }

    /**
     * Returns the type of the permission holder.
     *
     * @return the {@link PermissionTarget}
     */
    public PermissionTarget getType() {
        return type;
    }

    /**
     * Returns the id of the guild the node is on.
     *
     * @return the id
     */
    public long getGuildId() {
        return guildId;
    }

    /**
     * Returns the permission node itself.
     *
     * @return the node
     */
    public String getPermissionNode() {
        return permissionNode;
    }

    /**
     * Negates the permission.
     */
    public void negate() {
        this.negated = true;
    }

    /**
     * Allows (un-negates) a permission node.
     */
    public void allow() {
        this.negated = false;
    }

    /**
     * Deletes a permission node.
     *
     * @return a {@link CompletionStage} which completes when the permission node got deleted
     */
    @Override
    public CompletionStage<Void> deleteAsync() {
        manager.deleteNode(this);
        return super.deleteAsync();
    }

    /**
     * Updates a permission node.
     *
     * @return a {@link CompletionStage} which completes when the permission node got updated
     */
    @Override
    public CompletionStage<Void> saveAsync() {
        manager.updateNode(this);
        return super.saveAsync();
    }

    /**
     * Converts the permission node to an information container.
     *
     * @return the {@link cc.hawkbot.regnum.client.command.permission.PermissionManager.PermissionInfoContainer}
     */
    @NotNull
    public PermissionManager.PermissionInfoContainer toInfo() {
        return new PermissionManager.PermissionInfoContainer(getIdLong(), guildId, permissionNode, false);
    }

    /**
     * Returns the permission manager.
     *
     * @return the {@link PermissionManager}
     */
    public PermissionManager getManager() {
        return manager;
    }

    /**
     * Internally used for injecting the permission manager
     *
     * @param permissionManager the {@link PermissionManager}
     */
    public void setManager(PermissionManager permissionManager) {
        this.manager = permissionManager;
    }

    /**
     * Enum for different types of permission holders.
     */
    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public enum PermissionTarget {
        USER("permissiontargets.user"),
        ROLE("permissiontargets.role");

        private final String translation;

        PermissionTarget(String translation) {
            this.translation = translation;
        }

        /**
         * Returns the translation key of this permission target.
         *
         * @return the translation key
         */
        public String getTranslation() {
            return translation;
        }
    }

    /**
     * Accessor used by {@link PermissionManager}
     */
    @com.datastax.driver.mapping.annotations.Accessor
    public interface Accessor {
        @Query("SELECT * FROM " + CassandraEntity.TABLE_PREFIX + "permissions WHERE id = :userId AND guild_id = :guildId AND permission_node = :node")
        Result<PermissionNode> getNode(@Param("userId") long userId, @Param("guildId") long guildId, @Param("node") String node);

        @Query("SELECT * FROM " + CassandraEntity.TABLE_PREFIX + "permissions WHERE id = :userId AND guild_id = :guildId")
        Result<PermissionNode> getNodes(@Param("userId") long userId, @Param("guildId") long guildId);
    }

}