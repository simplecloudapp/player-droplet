package app.simplecloud.droplet.player.server.repository

import app.simplecloud.droplet.player.server.PLAYER_UNIQUE_IDS_KEY
import redis.clients.jedis.JedisPool
import java.util.UUID

class PlayerUniqueIdRepository(
    private val jedisPool: JedisPool
) {

    fun save(name: String, uniqueId: UUID) {
        save(name, uniqueId.toString())
    }

    fun save(name: String, uniqueId: String) {
        jedisPool.resource.use { jedis ->
            jedis.set("$PLAYER_UNIQUE_IDS_KEY/$name", uniqueId)
        }
    }

    fun delete(name: String) {
        jedisPool.resource.use { jedis ->
            jedis.del("$PLAYER_UNIQUE_IDS_KEY/$name")
        }
    }

    fun findByName(name: String): UUID? {
        return jedisPool.resource.use { jedis ->
            val uniqueId = jedis.get("$PLAYER_UNIQUE_IDS_KEY/$name") ?: return null
            UUID.fromString(uniqueId)
        }
    }

}