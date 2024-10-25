package app.simplecloud.droplet.player.server.entity

import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
import app.simplecloud.droplet.player.server.repository.JooqPlayerRepository
import app.simplecloud.droplet.player.shared.db.tables.records.OfflinePlayersRecord
import dev.morphia.annotations.*
import java.time.ZoneOffset
import java.util.*

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

    companion object {
        fun fromRecord(record: OfflinePlayersRecord, jooqPlayerRepository: JooqPlayerRepository): OfflinePlayerEntity {
            return OfflinePlayerEntity(
                record.uniqueId!!,
                record.name!!,
                record.displayName,
                record.firstLogin!!.toEpochSecond(ZoneOffset.UTC) * 1000,
                record.lastLogin!!.toEpochSecond(ZoneOffset.UTC) * 1000,
                record.onlineTime!!,
                jooqPlayerRepository.findConnectionByUniqueId(UUID.fromString(record.uniqueId)) ?: PlayerConnectionEntity()
            )
        }

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