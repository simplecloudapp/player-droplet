package app.simplecloud.droplet.player.server.redis

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.time.Duration

object RedisFactory {

    @JvmStatic
    fun createFromEnv(): JedisPool {
        val host = System.getenv("REDIS_HOST") ?: "localhost"
        val port = System.getenv("REDIS_PORT")?.toInt() ?: 6379
        val password = System.getenv("REDIS_PASSWORD") ?: null
        return create(host, port, password)
    }

    @JvmStatic
    fun create(host: String, port: Int, password: String?): JedisPool {
        return JedisPool(buildConfig(), host, port, 2000, password)
    }

    private fun buildConfig(): JedisPoolConfig {
        val poolConfig = JedisPoolConfig()
        poolConfig.maxTotal = 128
        poolConfig.maxIdle = 128
        poolConfig.minIdle = 16
        poolConfig.testOnBorrow = true
        poolConfig.testOnReturn = true
        poolConfig.testWhileIdle = true
        poolConfig.minEvictableIdleTime = Duration.ofSeconds(60)
        poolConfig.timeBetweenEvictionRuns = Duration.ofSeconds(30)
        poolConfig.numTestsPerEvictionRun = 3
        poolConfig.blockWhenExhausted = true
        return poolConfig
    }

}