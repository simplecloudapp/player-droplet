package app.simplecloud.droplet.player.api.impl.configuration

import app.simplecloud.droplet.player.proto.CloudPlayerConfiguration
import java.util.*

class CloudPlayerConfigurationWrapper(
    name: String,
    uniqueId: UUID,
    firstLogin: Long,
    lastLogin: Long,
    onlineTime: Long,
    playerConnection: PlayerConnectionConfigurationWrapper,
    displayName: String,
    val connectedServerName: String?,
    val connectedProxyName: String
): OfflineCloudPlayerConfigurationWrapper(
    name,
    uniqueId,
    firstLogin,
    lastLogin,
    onlineTime,
    displayName,
    playerConnection
) {
    companion object {
        fun fromConfiguration(configuration: CloudPlayerConfiguration): CloudPlayerConfigurationWrapper {
            return CloudPlayerConfigurationWrapper(
                configuration.name,
                UUID.fromString(configuration.uniqueId),
                configuration.firstLogin,
                configuration.lastLogin,
                configuration.onlineTime,
                PlayerConnectionConfigurationWrapper.fromConfiguration(configuration.playerConnection),
                configuration.displayName,
                configuration.connectedServerName,
                configuration.connectedProxyName
            )
        }
    }
}