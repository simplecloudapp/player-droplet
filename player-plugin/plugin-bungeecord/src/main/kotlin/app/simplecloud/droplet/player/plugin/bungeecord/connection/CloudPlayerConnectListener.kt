package app.simplecloud.droplet.player.plugin.bungeecord.connection

import app.simplecloud.droplet.player.proto.ConnectCloudPlayerEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.md_5.bungee.api.ProxyServer

class CloudPlayerConnectListener(
        private val proxyServer: ProxyServer,
) : RabbitMqListener<ConnectCloudPlayerEvent> {
    override fun handle(message: ConnectCloudPlayerEvent) {
        val player = proxyServer.getPlayer(message.uniqueId) ?: return
        val server = proxyServer.getServerInfo(message.serverName) ?: return
        player.connect(server)

    }
}