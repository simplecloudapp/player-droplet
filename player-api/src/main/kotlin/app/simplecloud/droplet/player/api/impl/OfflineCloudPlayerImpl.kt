package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.OfflineCloudPlayer
import app.simplecloud.droplet.player.api.impl.configuration.OfflineCloudPlayerConfigurationWrapper
import java.util.*

open class OfflineCloudPlayerImpl(
    private val configurationWrapper: OfflineCloudPlayerConfigurationWrapper
) : OfflineCloudPlayer {

    override fun getUniqueId(): UUID {
        return this.configurationWrapper.uniqueId
    }

    override fun getName(): String {
        return this.configurationWrapper.name
    }

    override fun getDisplayName(): String {
        return this.configurationWrapper.displayName
    }

    override fun getFirstLogin(): Long {
        return this.configurationWrapper.firstLogin
    }

    override fun getLastLogin(): Long {
        return this.configurationWrapper.lastLogin
    }

    override fun getOnlineTime(): Long {
        return this.configurationWrapper.onlineTime
    }

    override fun isOnline(): Boolean {
        return false
    }

    override fun toConfiguration(): OfflineCloudPlayerConfigurationWrapper {
        return this.configurationWrapper
    }

}