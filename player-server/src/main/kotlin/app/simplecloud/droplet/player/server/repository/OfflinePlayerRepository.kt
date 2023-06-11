package app.simplecloud.droplet.player.server.repository

import app.simplecloud.droplet.player.server.entity.OfflinePlayerEntity
import dev.morphia.Datastore
import dev.morphia.UpdateOptions
import dev.morphia.query.filters.Filters
import dev.morphia.query.updates.UpdateOperators
import java.util.*

class OfflinePlayerRepository(
    private val datastore: Datastore
): PlayerRepository<OfflinePlayerEntity> {

    override fun save(player: OfflinePlayerEntity) {
        datastore.save(player)
    }

    override fun updateDisplayName(uniqueId: UUID, displayName: String) {
        datastore.find(OfflinePlayerEntity::class.java)
            .filter(Filters.eq("uniqueId", uniqueId.toString()))
            .update(UpdateOptions(), UpdateOperators.set("displayName", displayName))
    }

    override fun findByName(name: String): OfflinePlayerEntity? {
        return datastore.find(OfflinePlayerEntity::class.java)
            .filter(Filters.eq("name", name))
            .first()
    }

    override fun findByUniqueId(uniqueId: UUID): OfflinePlayerEntity? {
        return findByUniqueId(uniqueId.toString())
    }

    override fun findByUniqueId(uniqueId: String): OfflinePlayerEntity? {
        return datastore.find(OfflinePlayerEntity::class.java)
            .filter(Filters.eq("uniqueId", uniqueId))
            .first()
    }

    override fun findAll(): List<OfflinePlayerEntity> {
        return datastore.find(OfflinePlayerEntity::class.java).toList()
    }

    override fun count(): Int {
        return datastore.find(OfflinePlayerEntity::class.java).count().toInt()
    }

    override fun delete(player: OfflinePlayerEntity) {
        datastore.delete(player)
    }

}