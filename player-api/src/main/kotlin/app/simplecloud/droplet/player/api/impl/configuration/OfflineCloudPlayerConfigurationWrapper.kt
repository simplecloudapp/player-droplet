package app.simplecloud.droplet.player.api.impl.configuration

import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
import java.util.*

open class OfflineCloudPlayerConfigurationWrapper(
    val name: String,
    val uniqueId: UUID,
    val firstLogin: Long,
    val lastLogin: Long,
    val onlineTime: Long,
    val displayName: String,
    val lastPlayerConnection: PlayerConnectionConfigurationWrapper,
) {
    companion object {
        fun fromConfiguration(configuration: OfflineCloudPlayerConfiguration): OfflineCloudPlayerConfigurationWrapper {
            return OfflineCloudPlayerConfigurationWrapper(
                configuration.name,
                UUID.fromString(configuration.uniqueId),
                configuration.firstLogin,
                configuration.lastLogin,
                configuration.onlineTime,
                configuration.displayName,
                PlayerConnectionConfigurationWrapper.fromConfiguration(configuration.lastPlayerConnection)
            )
        }
    }
}