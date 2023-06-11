package app.simplecloud.droplet.player.server.repository

import java.util.UUID

interface PlayerRepository<T> {

    fun save(player: T)

    fun saveDisplayName(uniqueId: UUID, displayName: String)

    fun delete(player: T)

    fun findByName(name: String): T?

    fun findByUniqueId(uniqueId: UUID): T?

    fun findByUniqueId(uniqueId: String): T?

    fun findAll(): List<T>

    fun count(): Int

}