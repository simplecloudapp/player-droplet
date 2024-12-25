package app.simplecloud.droplet.player.plugin.velocity.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerDisconnectRequest
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent

class PlayerDisconnectListener(
    private val playerApi: PlayerProxyApi
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