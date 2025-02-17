package app.simplecloud.droplet.player.plugin.bungeecord.listener

import app.simplecloud.droplet.player.plugin.bungeecord.PlayerBungeecordPlugin
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerLoginRequest
import build.buf.gen.simplecloud.droplet.player.v1.PlayerConnectionConfiguration
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.concurrent.TimeUnit


class PlayerConnectionListener(
    private val proxyApi: PlayerProxyApi,
    private val plugin: PlayerBungeecordPlugin
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
                        .setClientLanguage(if (player.locale != null) player.locale.language else "en")
                        .setOnline(true)
                        .setLastServerName(
                            if (player.server == null) "lobby" else player.server.info.name
                        )
                        .build()
                )
                .build()
        ).exceptionally {
            event.player.disconnect("§cUnable to connect player!")
            null
        }


    }

    @EventHandler
    fun onServerChange(event: ServerConnectedEvent) {
        val player = event.player
        proxyApi.getFutureApi().updateServer(
            player.uniqueId,
            event.server.info.name,
        )
    }

}