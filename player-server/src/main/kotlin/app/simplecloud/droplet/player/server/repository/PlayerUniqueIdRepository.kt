package app.simplecloud.droplet.player.server.repository

import app.simplecloud.droplet.player.server.redis.RedisKeyNames
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
            jedis.set("${RedisKeyNames.PLAYER_UNIQUE_IDS_KEY}/$name", uniqueId)
        }
    }

    fun delete(name: String) {
        jedisPool.resource.use { jedis ->
            jedis.del("${RedisKeyNames.PLAYER_UNIQUE_IDS_KEY}/$name")
        }
    }

    fun findByName(name: String): UUID? {
        return jedisPool.resource.use { jedis ->
            val uniqueId = jedis.get("${RedisKeyNames.PLAYER_UNIQUE_IDS_KEY}/$name") ?: return null
            UUID.fromString(uniqueId)
        }
    }

}