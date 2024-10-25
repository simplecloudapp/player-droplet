package app.simplecloud.droplet.player.server.repository

import app.simplecloud.droplet.player.server.database.Database
import app.simplecloud.droplet.player.server.entity.OfflinePlayerEntity
import app.simplecloud.droplet.player.server.entity.PlayerConnectionEntity
import app.simplecloud.droplet.player.shared.db.tables.OfflinePlayers
import app.simplecloud.droplet.player.shared.db.tables.PlayerConnection
import app.simplecloud.droplet.player.shared.db.tables.records.OfflinePlayersRecord
import app.simplecloud.droplet.player.shared.db.tables.records.PlayerConnectionRecord
import app.simplecloud.droplet.player.shared.db.tables.references.OFFLINE_PLAYERS
import app.simplecloud.droplet.player.shared.db.tables.references.PLAYER_CONNECTION
import java.time.ZoneOffset
import java.util.*

class JooqPlayerRepository(
    private val datbase: Database
) : PlayerRepository<OfflinePlayerEntity> {
    override fun save(player: OfflinePlayerEntity) {
        datbase.context.insertInto(OfflinePlayers.OFFLINE_PLAYERS)
            .set(player.toRecord())
            .execute()

        saveConnection(player.lastPlayerConnection)

    }

    fun saveConnection(connection: PlayerConnectionEntity) {
        datbase.context.insertInto(PLAYER_CONNECTION)
            .set(connection.toRecord())
            .execute()
    }

    override fun updateDisplayName(uniqueId: UUID, displayName: String) {
        datbase.context.update(OfflinePlayers.OFFLINE_PLAYERS)
            .set(OfflinePlayers.OFFLINE_PLAYERS.DISPLAY_NAME, displayName)
            .where(OfflinePlayers.OFFLINE_PLAYERS.UNIQUE_ID.eq(uniqueId.toString()))
            .execute()
    }

    override fun findByName(name: String): OfflinePlayerEntity? {
        val fetchOneInto = datbase.context.selectFrom(OfflinePlayers.OFFLINE_PLAYERS)
            .where(OfflinePlayers.OFFLINE_PLAYERS.NAME.eq(name))
            .fetchOneInto(OFFLINE_PLAYERS)

        if (fetchOneInto == null) {
            return null
        }

        return mapOfflinePlayersRecordToEntity(fetchOneInto)
    }

    override fun findByUniqueId(uniqueId: UUID): OfflinePlayerEntity? {
        return findByUniqueId(uniqueId.toString())
    }

    override fun findByUniqueId(uniqueId: String): OfflinePlayerEntity? {
        val fetchOneInto = datbase.context.selectFrom(OfflinePlayers.OFFLINE_PLAYERS)
            .where(OfflinePlayers.OFFLINE_PLAYERS.UNIQUE_ID.eq(uniqueId))
            .fetchOneInto(OFFLINE_PLAYERS)

        if  (fetchOneInto == null) {
            return null
        }

        return mapOfflinePlayersRecordToEntity(fetchOneInto)
    }

    override fun findAll(): List<OfflinePlayerEntity> {
        val fetchInto = datbase.context.selectFrom(OfflinePlayers.OFFLINE_PLAYERS)
            .fetchInto(OFFLINE_PLAYERS)
        return fetchInto.map { mapOfflinePlayersRecordToEntity(it) }
    }

    override fun count(): Int {
        return datbase.context.fetchCount(OfflinePlayers.OFFLINE_PLAYERS)
    }

    override fun delete(player: OfflinePlayerEntity) {
        datbase.context.deleteFrom(OfflinePlayers.OFFLINE_PLAYERS)
            .where(OfflinePlayers.OFFLINE_PLAYERS.UNIQUE_ID.eq(player.uniqueId))
            .execute()
    }

    private fun mapOfflinePlayersRecordToEntity(record: OfflinePlayersRecord): OfflinePlayerEntity {
        val lastPlayerConnection = datbase.context.select(PLAYER_CONNECTION)
            .where(PLAYER_CONNECTION.UNIQUE_ID.eq(record.uniqueId))
            .fetchInto(PlayerConnectionRecord::class.java)

        return OfflinePlayerEntity(
            record.uniqueId!!,
            record.name!!,
            record.displayName,
            record.firstLogin!!.toEpochSecond(ZoneOffset.of("UTC+1")),
            record.lastLogin!!.toEpochSecond(ZoneOffset.of("UTC+1")),
            record.onlineTime!!,
            mapPlayerConnectionsRecordToEntity(lastPlayerConnection.first())
        )
    }

    private fun mapPlayerConnectionsRecordToEntity(record: PlayerConnectionRecord): PlayerConnectionEntity {
        return PlayerConnectionEntity(
            record.uniqueId!!,
            record.numericalClientVersion!!,
            record.onlineMode!!,
            record.lastServer!!,
            record.online!!
        )
    }

}
