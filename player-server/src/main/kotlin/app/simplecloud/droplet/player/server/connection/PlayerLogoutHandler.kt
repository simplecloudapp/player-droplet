package app.simplecloud.droplet.player.server.connection

import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import app.simplecloud.droplet.player.server.entity.OfflinePlayerEntity
import app.simplecloud.droplet.player.server.entity.PlayerConnectionEntity
import app.simplecloud.droplet.player.server.repository.OfflinePlayerRepository
import app.simplecloud.droplet.player.server.repository.OnlinePlayerRepository
import org.apache.logging.log4j.LogManager

class PlayerLogoutHandler(
    private val offlinePlayerRepository: OfflinePlayerRepository,
    private val onlinePlayerRepository: OnlinePlayerRepository
) {

    fun handleLogout(request: CloudPlayerDisconnectRequest): Boolean {
        val onlinePlayer = onlinePlayerRepository.findByUniqueId(request.uniqueId)
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
            PlayerConnectionEntity.fromConfiguration(onlinePlayer.playerConnection)
        )

        onlinePlayerRepository.delete(onlinePlayer)
        offlinePlayerRepository.save(offlinePlayerEntity)

        LOGGER.info("Player {} logged out", onlinePlayer.name)
        return true
    }

    companion object {
        private val LOGGER = LogManager.getLogger(PlayerLogoutHandler::class.java)
    }

}