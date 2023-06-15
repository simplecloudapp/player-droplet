package app.simplecloud.droplet.player.plugin.bungeecord.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerDisconnectRequest
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerDisconnectListener(
    private val playerApi: PlayerProxyApi
) : Listener {

    @EventHandler
    fun onDisconnect(event: PlayerDisconnectEvent) {
        val player = event.player
        playerApi.proxyController.handleDisconnect(
            CloudPlayerDisconnectRequest.newBuilder()
                .setUniqueId(player.uniqueId.toString())
                .build()
        )
    }

}