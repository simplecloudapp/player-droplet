package app.simplecloud.droplet.player.api

import app.simplecloud.droplet.player.api.impl.configuration.OfflineCloudPlayerConfigurationWrapper
import java.util.UUID

interface OfflineCloudPlayer {

    /**
     * @returns the uniqueId of the player.
     */
    fun getUniqueId(): UUID

    /**
     * @returns the name of the player.
     */
    fun getName(): String

    /**
     * @returns the display name of the player.
     */
    fun getDisplayName(): String

    /**
     * @returns the first login time of the player.
     */
    fun getFirstLogin(): Long

    /**
     * @returns the last login time of the player.
     */
    fun getLastLogin(): Long

    /**
     * @returns the online time of the player.
     */
    fun getOnlineTime(): Long

    /**
     * @returns if the player is online.
     */
    fun isOnline(): Boolean

    /**
     * @returns the configuration of the player.
     */
    fun toConfiguration(): OfflineCloudPlayerConfigurationWrapper

}