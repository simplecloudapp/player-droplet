package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.server.repository.JooqPlayerRepository
import app.simplecloud.droplet.player.shared.db.tables.records.OfflinePlayersRecord
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerConfiguration
import build.buf.gen.simplecloud.droplet.player.v1.OfflineCloudPlayerConfiguration
import java.time.ZoneOffset
import java.util.*

data class OfflinePlayerEntity(

    val uniqueId: String = "",

    val name: String = "",
    val displayName: String? = null,
    val firstLogin: Long = 0,
    val lastLogin: Long = 0,
    val onlineTime: Long = 0,
    val lastPlayerConnection: PlayerConnectionEntity = PlayerConnectionEntity()
) {

    fun toRecord(): OfflinePlayersRecord {
        return OfflinePlayersRecord().apply {
            this.uniqueId = uniqueId
            this.name = name
            this.displayName = displayName
            this.firstLogin = firstLogin
            this.lastLogin = lastLogin
            this.onlineTime = onlineTime
        }
    }

    fun toConfiguration(): OfflineCloudPlayerConfiguration {
        return OfflineCloudPlayerConfiguration.newBuilder()
            .setUniqueId(uniqueId)
            .setName(name)
            .setDisplayName(displayName ?: name)
            .setFirstLogin(firstLogin)
            .setLastLogin(lastLogin)
            .setOnlineTime(onlineTime)
            .setPlayerConnection(lastPlayerConnection.toConfiguration())
            .build()
    }

    fun toCloudPlayerConfiguration(): CloudPlayerConfiguration {
        return CloudPlayerConfiguration.newBuilder()
            .setUniqueId(uniqueId)
            .setName(name)
            .setDisplayName(displayName ?: name)
            .setFirstLogin(firstLogin)
            .setLastLogin(lastLogin)
            .setOnlineTime(onlineTime)
            .setPlayerConnection(lastPlayerConnection.toConfiguration())
            .setConnectedServerName(lastPlayerConnection.lastServer)
            .build()
    }

    companion object {

        fun fromConfiguration(configuration: OfflineCloudPlayerConfiguration): OfflinePlayerEntity {
            return OfflinePlayerEntity(
                configuration.uniqueId,
                configuration.name,
                configuration.displayName,
                configuration.firstLogin,
                configuration.lastLogin,
                configuration.onlineTime,
                PlayerConnectionEntity.fromConfiguration(configuration.playerConnection)
            )
        }

    }

}