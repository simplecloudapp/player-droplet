package app.simplecloud.droplet.player.runtime.repository

import java.util.UUID

interface PlayerRepository<T> {

    suspend fun save(player: T)

    suspend fun updateDisplayName(uniqueId: UUID, displayName: String)

    suspend fun updateCurrentServer(uniqueId: UUID, serverName: String)

    suspend fun delete(player: T)

    suspend fun findByName(name: String): T?

    suspend fun findByUniqueId(uniqueId: UUID): T?

    suspend fun findByUniqueId(uniqueId: String): T?

    suspend fun findAll(): List<T>

    suspend fun count(): Int

}