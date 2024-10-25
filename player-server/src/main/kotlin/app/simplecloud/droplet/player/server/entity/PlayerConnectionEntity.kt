package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import app.simplecloud.droplet.player.shared.db.tables.records.PlayerConnectionRecord
import dev.morphia.annotations.Entity

@Entity
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
            .setOnlineMode(onlineMode)
            .build()
    }

    fun toRecord(): PlayerConnectionRecord {
        return PlayerConnectionRecord().apply {
            this.clientLanguage = clientLanguage
            this.numericalClientVersion = numericalClientVersion
            this.onlineMode = onlineMode
            this.lastServer = lastServer
            this.online = online
        }
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