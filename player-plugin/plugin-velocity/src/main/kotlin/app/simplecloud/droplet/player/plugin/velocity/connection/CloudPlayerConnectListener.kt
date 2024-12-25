package app.simplecloud.droplet.player.plugin.velocity.connection

import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.ConnectCloudPlayerEvent
import com.velocitypowered.api.proxy.ProxyServer
import java.util.*

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
