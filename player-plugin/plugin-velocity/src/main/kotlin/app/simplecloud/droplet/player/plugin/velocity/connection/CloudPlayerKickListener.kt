package app.simplecloud.droplet.player.plugin.velocity.connection

import app.simplecloud.droplet.player.proto.CloudPlayerKickEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.util.*

class CloudPlayerKickListener(
    private val proxyServer: ProxyServer,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<CloudPlayerKickEvent> {
    override fun handle(message: CloudPlayerKickEvent) {
        proxyServer.getPlayer(UUID.fromString(message.uniqueId)).ifPresent {
            it.disconnect(componentSerializer.deserialize(message.reason.json))
        }
    }
}