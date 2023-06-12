package app.simplecloud.droplet.player.plugin.velocity.listener

import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.proto.CloudPlayerLoginRequest
import app.simplecloud.droplet.player.proto.PlayerConnectionConfiguration
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent

class PlayerConnectionListener(
    val playerApi: PlayerProxyApi
) {

    @Subscribe
    fun onPlayerJoin(event: PostLoginEvent) {
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
    }

}