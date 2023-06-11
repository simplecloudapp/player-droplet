package app.simplecloud.droplet.player.api.impl.configuration

import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import java.util.*

data class PlayerConnectionConfigurationWrapper(
    val uniqueId: UUID,
    val numericalClientVersion: Int,
    val name: String,
    val onlineMode: Boolean
) {
    companion object {
        fun fromConfiguration(configuration: PlayerConnectionConfiguration): PlayerConnectionConfigurationWrapper {
            return PlayerConnectionConfigurationWrapper(
                UUID.fromString(configuration.uniqueId),
                configuration.numericalClientVersion,
                configuration.name,
                configuration.onlineMode
            )
        }
    }
}