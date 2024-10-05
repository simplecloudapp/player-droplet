package app.simplecloud.droplet.player.plugin.velocity.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.TitlePart

class PlayerConnectionListener(
    private val playerApi: PlayerProxyApi,
    private val proxyServer: ProxyServer
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
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        player.sendMessage(Component.text("Player: ${player.username}"))
        playerApi.getOnlinePlayer(player.uniqueId).thenAccept {
            it.sendMessage(Component.text("Player: ${it.getName()}"))
            it.kick(Component.text("You are not allowed to join!"))
        }
        playerApi.getOnlinePlayers().thenAccept {
            it.forEach {
                println(it.getName())
            }
        }
    }
}