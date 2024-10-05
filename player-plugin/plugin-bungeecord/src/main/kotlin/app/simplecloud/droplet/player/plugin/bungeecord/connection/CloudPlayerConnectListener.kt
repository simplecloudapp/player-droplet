package app.simplecloud.droplet.player.plugin.bungeecord.connection

import app.simplecloud.droplet.player.proto.ConnectCloudPlayerEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import net.md_5.bungee.api.ProxyServer
import java.util.*

class CloudPlayerConnectListener(
        private val proxyServer: ProxyServer,
) : PubSubListener<ConnectCloudPlayerEvent> {
    override fun handle(message: ConnectCloudPlayerEvent) {
        val player = proxyServer.getPlayer(UUID.fromString(message.uniqueId)) ?: return
        val server = proxyServer.getServerInfo(message.serverName) ?: return
        player.connect(server)

    }
}