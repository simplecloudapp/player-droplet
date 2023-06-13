package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import dev.morphia.annotations.Entity

@Entity
data class PlayerConnectionEntity(
    val clientLanguage: String = "",
    val numericalClientVersion: Int = 0,
    val onlineMode: Boolean = false
) {

    fun toConfiguration(): PlayerConnectionConfiguration {
        return PlayerConnectionConfiguration.newBuilder()
            .setClientLanguage(clientLanguage)
            .setNumericalClientVersion(numericalClientVersion)
            .setOnlineMode(onlineMode)
            .build()
    }

    companion object {
        fun fromConfiguration(configuration: PlayerConnectionConfiguration): PlayerConnectionEntity {
            return PlayerConnectionEntity(
                configuration.clientLanguage,
                configuration.numericalClientVersion,
                configuration.onlineMode
            )
        }
    }

}