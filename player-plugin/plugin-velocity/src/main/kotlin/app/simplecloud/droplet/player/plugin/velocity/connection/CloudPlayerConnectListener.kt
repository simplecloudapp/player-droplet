package app.simplecloud.droplet.player.plugin.velocity.connection

import app.simplecloud.droplet.player.proto.ConnectCloudPlayerEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import com.velocitypowered.api.proxy.ProxyServer

class CloudPlayerConnectListener(
        private val proxyServer: ProxyServer,
) : RabbitMqListener<ConnectCloudPlayerEvent> {
    override fun handle(message: ConnectCloudPlayerEvent) {
        val player = proxyServer.getPlayer(message.uniqueId) ?: return
        val server = proxyServer.getServer(message.serverName).orElse(null) ?: return
        player.get().createConnectionRequest(server).connect()
    }
}