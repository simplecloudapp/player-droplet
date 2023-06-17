package app.simplecloud.droplet.player.plugin.bungeecord.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler


class PlayerConnectionListener(
    private val proxyApi: PlayerProxyApi
) : Listener {

    @EventHandler
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        proxyApi.proxyController.handleLogin(
            CloudPlayerLoginRequest.newBuilder()
                .setName(player.name)
                .setUniqueId(player.uniqueId.toString())
                .setPlayerConnection(
                    PlayerConnectionConfiguration.newBuilder()
                        .setOnlineMode(player.pendingConnection.isOnlineMode)
                        .setNumericalClientVersion(player.pendingConnection.version)
                        .setClientLanguage(player.locale.language)
                        .build()
                )
                .build()
        ).exceptionally {
            event.player.disconnect("§cUnable to connect player!")
            null
        }
    }

}