package app.simplecloud.droplet.player.server.connection

import app.simplecloud.droplet.player.proto.CloudPlayerConfiguration
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.server.entity.OfflinePlayerEntity
import app.simplecloud.droplet.player.server.entity.PlayerConnectionEntity
import app.simplecloud.droplet.player.server.repository.JooqPlayerRepository
import app.simplecloud.droplet.player.server.repository.OfflinePlayerRepository
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import org.apache.logging.log4j.LogManager

class PlayerLoginHandler(
    private val offlinePlayerRepository: JooqPlayerRepository,
    private val onlinePlayerRepository: OnlinePlayerRepository
) {


    /**
     * Handles the login of a player.
     * @return true if player is successfully logged in, false otherwise
     */
    fun handleLogin(request: CloudPlayerLoginRequest): Boolean {
        LOGGER.info("Player {} is logging in...", request.uniqueId)
        if (checkIfPlayerIsAlreadyLoggedIn(request.uniqueId)) {
            return false
        }

        createPlayer(request)
        return true
    }

    private fun checkIfPlayerIsAlreadyLoggedIn(uniqueId: String): Boolean {
        val onlinePlayer = onlinePlayerRepository.findByUniqueId(uniqueId)
        if (onlinePlayer != null) {
            LOGGER.warn("Player {} is already logged in", uniqueId)
            return true
        }
        return false
    }

    private fun createPlayer(request: CloudPlayerLoginRequest): CloudPlayerConfiguration {
        val offlinePlayer = offlinePlayerRepository.findByUniqueId(request.uniqueId)?: createOfflinePlayer(request)
        val cloudPlayerConfiguration = CloudPlayerConfiguration.newBuilder()
            .mergeFrom(offlinePlayer.toConfiguration().toByteArray())
            .setConnectedProxyName("proxy")
            .build()

        onlinePlayerRepository.save(cloudPlayerConfiguration)
        return cloudPlayerConfiguration
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
        offlinePlayerRepository.save(offlinePlayerEntity)
        return offlinePlayerEntity
    }

    companion object {
        private val LOGGER = LogManager.getLogger(PlayerLoginHandler::class.java)
    }

}