package app.simplecloud.droplet.player.plugin.velocity.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent

class PlayerDisconnectListener(
    val playerApi: PlayerProxyApi
) {

    @Subscribe
    fun onPlayerQuit(event: DisconnectEvent) {
        val player = event.player
        playerApi.proxyController.handleDisconnect(
            CloudPlayerDisconnectRequest.newBuilder()
                .setUniqueId(player.uniqueId.toString())
                .build()
        )
    }

}