package app.simplecloud.droplet.player.runtime.entity

import app.simplecloud.droplet.player.shared.db.tables.records.PlayerConnectionRecord
import build.buf.gen.simplecloud.droplet.player.v1.PlayerConnectionConfiguration

data class PlayerConnectionEntity(
    val clientLanguage: String = "",
    val numericalClientVersion: Int = 0,
    val onlineMode: Boolean = false,
    val lastServer: String = "",
    val online: Boolean = false
) {

    fun toConfiguration(): PlayerConnectionConfiguration {
        return PlayerConnectionConfiguration.newBuilder()
            .setClientLanguage(clientLanguage)
            .setNumericalClientVersion(numericalClientVersion)
            .setLastServerName(lastServer)
            .setOnline(online)
            .setOnlineMode(onlineMode)
            .build()
    }

    companion object {
        fun fromConfiguration(configuration: PlayerConnectionConfiguration): PlayerConnectionEntity {
            return PlayerConnectionEntity(
                configuration.clientLanguage,
                configuration.numericalClientVersion,
                configuration.onlineMode,
                configuration.lastServerName,
                configuration.online
            )
        }

        fun fromRecord(record: PlayerConnectionRecord): PlayerConnectionEntity {
            return PlayerConnectionEntity(
                record.clientLanguage!!,
                record.numericalClientVersion!!,
                record.onlineMode!!,
                record.lastServer!!,
                record.online!!
            )
        }

    }

}