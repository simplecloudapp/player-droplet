package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
import app.simplecloud.droplet.player.shared.db.tables.records.OfflinePlayersRecord
import dev.morphia.annotations.*

@Entity("offline-players")
data class OfflinePlayerEntity(
    @Id
    val uniqueId: String = "",
    @Indexed()
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

}