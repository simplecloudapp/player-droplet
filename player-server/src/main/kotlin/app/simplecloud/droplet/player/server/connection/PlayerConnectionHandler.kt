package app.simplecloud.droplet.player.server.connection

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.OfflineCloudPlayerConfiguration
import app.simplecloud.droplet.player.server.entity.OfflinePlayerEntity
import app.simplecloud.droplet.player.server.entity.PlayerConnectionEntity
import app.simplecloud.droplet.player.server.repository.JooqPlayerRepository
import org.apache.logging.log4j.LogManager

class PlayerConnectionHandler(
    private val jooqPlayerRepository: JooqPlayerRepository
) {

    fun handleLogin(request: CloudPlayerLoginRequest): Boolean {
        LOGGER.info("Player {} is logging in...", request.uniqueId)

        createPlayer(request)
        return true
    }

    private fun createPlayer(request: CloudPlayerLoginRequest): OfflineCloudPlayerConfiguration {
        val offlinePlayer = jooqPlayerRepository.findByUniqueId(request.uniqueId) ?: createOfflinePlayer(request)
        val cloudPlayerConfiguration = OfflineCloudPlayerConfiguration.newBuilder()
            .mergeFrom(offlinePlayer.toConfiguration().toByteArray())
            .build()

        jooqPlayerRepository.save(OfflinePlayerEntity.fromConfiguration(cloudPlayerConfiguration))
        return cloudPlayerConfiguration
    }

    fun handleLogout(request: CloudPlayerDisconnectRequest): Boolean {
        val onlinePlayer = jooqPlayerRepository.findByUniqueId(request.uniqueId)
        if (onlinePlayer == null) {
            LOGGER.warn("Player {} is not logged in", request.uniqueId)
            return false
        }

        val sessionTime = System.currentTimeMillis() - onlinePlayer.lastLogin
        val offlinePlayerEntity = OfflinePlayerEntity(
            onlinePlayer.uniqueId,
            onlinePlayer.name,
            onlinePlayer.displayName,
            onlinePlayer.firstLogin,
            System.currentTimeMillis(),
            onlinePlayer.onlineTime + sessionTime,
            onlinePlayer.lastPlayerConnection.copy(online = false)
        )

        jooqPlayerRepository.save(offlinePlayerEntity)

        LOGGER.info("Player {} logged out", onlinePlayer.name)
        return true
    }

    private fun createOfflinePlayer(request: CloudPlayerLoginRequest): OfflinePlayerEntity {
        val offlinePlayerEntity = OfflinePlayerEntity(
            request.uniqueId,
            request.name,
            request.name,
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            0,
            PlayerConnectionEntity.fromConfiguration(request.playerConnection)
        )

        jooqPlayerRepository.save(offlinePlayerEntity)
        return offlinePlayerEntity
    }

    companion object {
        private val LOGGER = LogManager.getLogger(PlayerConnectionHandler::class.java)
    }

}