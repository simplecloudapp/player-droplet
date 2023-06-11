package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import dev.morphia.annotations.Entity

@Entity
data class PlayerConnectionEntity(
    val clientLanguage: String,
    val numericalClientVersion: Int,
    val onlineMode: Boolean
) {

    fun toConfiguration(): PlayerConnectionConfiguration {
        return PlayerConnectionConfiguration.newBuilder()
            .setClientLanguage(clientLanguage)
            .setNumericalClientVersion(numericalClientVersion)
            .setOnlineMode(onlineMode)
            .build()
    }

}