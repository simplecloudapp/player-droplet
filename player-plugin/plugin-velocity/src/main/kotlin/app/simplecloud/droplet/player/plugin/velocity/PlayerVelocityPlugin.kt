package app.simplecloud.droplet.player.plugin.velocity

import app.simplecloud.droplet.player.api.PlayerApiSingleton
import app.simplecloud.droplet.player.plugin.shared.OnlinePlayerChecker
import app.simplecloud.droplet.player.plugin.shared.proxy.PlayerProxyApi
import app.simplecloud.droplet.player.plugin.velocity.connection.CloudPlayerConnectListener
import app.simplecloud.droplet.player.plugin.velocity.connection.CloudPlayerKickListener
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerConnectionListener
import app.simplecloud.droplet.player.plugin.velocity.listener.PlayerDisconnectListener
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerKickEvent
import build.buf.gen.simplecloud.droplet.player.v1.ConnectCloudPlayerEvent
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.proxy.ProxyServer
import java.util.*

class PlayerVelocityPlugin @Inject constructor(
    private val proxyServer: ProxyServer,
) {

    private val playerApi = PlayerProxyApi(
        OnlinePlayerChecker { proxyServer.getPlayer(UUID.fromString(it)).isPresent }
    )

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        PlayerApiSingleton.init(playerApi)

        playerApi.registerPubSubListener(
            RabbitMqChannelNames.CONNECTION,
                CloudPlayerKickEvent::class.java, CloudPlayerKickListener((proxyServer)
        ))
        playerApi.registerPubSubListener(
            RabbitMqChannelNames.CONNECTION,
                ConnectCloudPlayerEvent::class.java, CloudPlayerConnectListener((proxyServer)
        ))

        proxyServer.eventManager.register(this, PlayerConnectionListener(playerApi, proxyServer, this))
        proxyServer.eventManager.register(this, PlayerDisconnectListener(playerApi))
    }

}