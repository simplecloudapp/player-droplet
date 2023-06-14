package app.simplecloud.droplet.player.server.repository

import app.simplecloud.droplet.player.proto.CloudPlayerConfiguration
import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import app.simplecloud.droplet.player.server.redis.RedisKeyNames
import com.google.gson.GsonBuilder
import redis.clients.jedis.JedisPool
import java.util.*

class OnlinePlayerRepository(
    private val jedisPool: JedisPool,
    private val playerUniqueIdRepository: PlayerUniqueIdRepository
) : PlayerRepository<CloudPlayerConfiguration> {

    override fun save(player: CloudPlayerConfiguration) {
        playerUniqueIdRepository.save(player.name, player.uniqueId)
        jedisPool.resource.use { jedis ->
            jedis.hset("${RedisKeyNames.ONLINE_PLAYERS_KEY}:${player.uniqueId}", mapFromCloudPlayer(player))
        }
    }

    override fun updateDisplayName(uniqueId: UUID, displayName: String) {
        jedisPool.resource.use { jedis ->
            jedis.hset("${RedisKeyNames.ONLINE_PLAYERS_KEY}/$uniqueId", "displayName", displayName)
        }
    }

    override fun delete(player: CloudPlayerConfiguration) {
        playerUniqueIdRepository.delete(player.name)
        jedisPool.resource.use { jedis ->
            jedis.del("${RedisKeyNames.ONLINE_PLAYERS_KEY}:${player.uniqueId}")
        }
    }

    override fun findByName(name: String): CloudPlayerConfiguration? {
        val uniqueId = playerUniqueIdRepository.findByName(name) ?: return null
        return findByUniqueId(uniqueId)
    }

    override fun findByUniqueId(uniqueId: UUID): CloudPlayerConfiguration? {
        return findByUniqueId(uniqueId.toString())
    }

    override fun findByUniqueId(uniqueId: String): CloudPlayerConfiguration? {
        return jedisPool.resource.use { jedis ->
            val player = jedis.hgetAll("${RedisKeyNames.ONLINE_PLAYERS_KEY}:$uniqueId")
            if (player.isEmpty()) return null
            mapRedisHashToCloudPlayer(player)
        }
    }

    override fun findAll(): List<CloudPlayerConfiguration> {
        return jedisPool.resource.use { jedis ->
            jedis.keys("${RedisKeyNames.ONLINE_PLAYERS_KEY}:*").map { key ->
                mapRedisHashToCloudPlayer(jedis.hgetAll(key))
            }
        }
    }

    override fun count(): Int {
        return jedisPool.resource.use { jedis ->
            jedis.keys("${RedisKeyNames.ONLINE_PLAYERS_KEY}:*").size
        }
    }

    private fun mapRedisHashToCloudPlayer(player: Map<String, String>): CloudPlayerConfiguration {
        return CloudPlayerConfiguration.newBuilder()
            .setUniqueId(player["uniqueId"])
            .setName(player["name"])
            .setDisplayName(player["displayName"])
            .setFirstLogin(player["firstLogin"]?.toLong() ?: 0)
            .setLastLogin(player["lastLogin"]?.toLong() ?: 0)
            .setOnlineTime(player["onlineTime"]?.toLong() ?: 0)
            .setPlayerConnection(
                GSON.fromJson(player["playerConnection"], PlayerConnectionConfiguration::class.java)
            )
            .setConnectedServerName(player["connectedServerName"])
            .setConnectedProxyName(player["connectedProxyName"])
            .build()
    }

    private fun mapFromCloudPlayer(player: CloudPlayerConfiguration): Map<String, String> {
        return mapOf(
            "uniqueId" to player.uniqueId,
            "name" to player.name,
            "displayName" to player.displayName,
            "firstLogin" to player.firstLogin.toString(),
            "lastLogin" to player.lastLogin.toString(),
            "onlineTime" to player.onlineTime.toString(),
            "displayName" to player.displayName,
            "playerConnection" to GSON.toJson(player.playerConnection),
            "connectedServerName" to player.connectedServerName,
            "connectedProxyName" to player.connectedProxyName,
        )
    }

    companion object {
        private val GSON = GsonBuilder().create()
    }

}