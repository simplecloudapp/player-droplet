package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
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