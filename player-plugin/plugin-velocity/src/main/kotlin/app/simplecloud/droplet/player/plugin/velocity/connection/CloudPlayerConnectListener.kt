package app.simplecloud.droplet.player.plugin.velocity.connection

import app.simplecloud.droplet.player.proto.ConnectCloudPlayerEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import com.velocitypowered.api.proxy.ProxyServer
import java.util.UUID

class CloudPlayerConnectListener(
        private val proxyServer: ProxyServer,
) : PubSubListener<ConnectCloudPlayerEvent> {
    override fun handle(message: ConnectCloudPlayerEvent) {
        proxyServer.getPlayer(UUID.fromString(message.uniqueId)).ifPresent { player ->
            proxyServer.getServer(message.serverName).ifPresent {
                player.createConnectionRequest(it).fireAndForget()
            }
        }
    }
}
