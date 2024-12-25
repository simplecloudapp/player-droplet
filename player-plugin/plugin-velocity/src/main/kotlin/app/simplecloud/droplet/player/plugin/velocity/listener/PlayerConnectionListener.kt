package app.simplecloud.droplet.player.plugin.velocity.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.plugin.velocity.PlayerVelocityPlugin
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerLoginRequest
import build.buf.gen.simplecloud.droplet.player.v1.PlayerConnectionConfiguration
import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class PlayerConnectionListener(
    private val playerApi: PlayerProxyApi,
    private val proxyServer: ProxyServer,
    private val playerVelocityPlugin: PlayerVelocityPlugin
) {

    @Subscribe(order = PostOrder.EARLY)
    fun onPlayerJoin(event: LoginEvent, continuation: Continuation) {
        if (!event.result.isAllowed) return

        val player = event.player
        playerApi.proxyController.handleLogin(
            CloudPlayerLoginRequest.newBuilder()
                .setName(player.username)
                .setUniqueId(player.uniqueId.toString())
                .setPlayerConnection(
                    PlayerConnectionConfiguration.newBuilder()
                        .setNumericalClientVersion(player.protocolVersion.protocol)
                        .setClientLanguage(player.effectiveLocale?.language ?: "en")
                        .setOnlineMode(player.isOnlineMode)
                        .setOnline(true)
                        .setLastServerName(
                            if (player.currentServer == null || player.currentServer.isEmpty) "lobby" else player.currentServer.get().serverInfo.name
                        )
                        .build()
                )
                .build()
        )
            .thenAccept {
                continuation.resume()
            }
            .exceptionally {
                event.result = ResultedEvent.ComponentResult.denied(
                    Component.text(
                        "Unable to connect player!",
                        NamedTextColor.RED
                    )
                )
                continuation.resumeWithException(it)
                null
            }

    }


    @Subscribe(order = PostOrder.LAST)
    fun onServerSwitch(event: ServerConnectedEvent) {
        val player = event.player
        playerApi.getFutureApi().updateServer(
            player.uniqueId,
            event.server.serverInfo.name
        )
    }

}