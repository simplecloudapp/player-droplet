package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
import dev.morphia.annotations.*

@Entity("offline-players")
data class OfflinePlayerEntity(
    @Id
    val uniqueId: String,
    @Indexed()
    val name: String,
    val displayName: String?,
    val firstLogin: Long,
    val lastLogin: Long,
    val onlineTime: Long,
    val lastPlayerConnection: PlayerConnectionEntity
) {

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