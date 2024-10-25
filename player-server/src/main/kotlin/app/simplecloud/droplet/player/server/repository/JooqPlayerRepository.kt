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
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class JooqPlayerRepository(
    private val datbase: Database
) : PlayerRepository<OfflinePlayerEntity> {
    override fun save(player: OfflinePlayerEntity) {
        datbase.context.insertInto(OfflinePlayers.OFFLINE_PLAYERS, OFFLINE_PLAYERS.UNIQUE_ID, OFFLINE_PLAYERS.NAME, OFFLINE_PLAYERS.DISPLAY_NAME, OFFLINE_PLAYERS.FIRST_LOGIN, OFFLINE_PLAYERS.LAST_LOGIN, OFFLINE_PLAYERS.ONLINE_TIME)
            .values(player.uniqueId, player.name, player.displayName, LocalDateTime.ofEpochSecond(player.firstLogin, 0, ZoneOffset.of("UTC+1")), LocalDateTime.ofEpochSecond(player.lastLogin, 0, ZoneOffset.of("UTC+1")), player.onlineTime)
            .onDuplicateKeyUpdate()
            .set(OFFLINE_PLAYERS.NAME, player.name)
            .set(OFFLINE_PLAYERS.DISPLAY_NAME, player.displayName)
            .set(OFFLINE_PLAYERS.FIRST_LOGIN, LocalDateTime.ofEpochSecond(player.firstLogin, 0, ZoneOffset.of("UTC+1")))
            .set(OFFLINE_PLAYERS.LAST_LOGIN, LocalDateTime.ofEpochSecond(player.lastLogin, 0, ZoneOffset.of("UTC+1")))
            .set(OFFLINE_PLAYERS.ONLINE_TIME, player.onlineTime)
            .execute()

        saveConnection(player)

    }

    fun saveConnection(connection: OfflinePlayerEntity) {
        datbase.context.insertInto(PlayerConnection.PLAYER_CONNECTION, PLAYER_CONNECTION.UNIQUE_ID, PLAYER_CONNECTION.NUMERICAL_CLIENT_VERSION, PLAYER_CONNECTION.ONLINE_MODE, PLAYER_CONNECTION.LAST_SERVER, PLAYER_CONNECTION.ONLINE)
            .values(connection.uniqueId, connection.lastPlayerConnection.numericalClientVersion, connection.lastPlayerConnection.onlineMode, connection.lastPlayerConnection.lastServer, connection.lastPlayerConnection.online)
            .onDuplicateKeyUpdate()
            .set(PLAYER_CONNECTION.NUMERICAL_CLIENT_VERSION, connection.lastPlayerConnection.numericalClientVersion)
            .set(PLAYER_CONNECTION.ONLINE_MODE, connection.lastPlayerConnection.onlineMode)
            .set(PLAYER_CONNECTION.LAST_SERVER, connection.lastPlayerConnection.lastServer)
            .set(PLAYER_CONNECTION.ONLINE, connection.lastPlayerConnection.online)
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
