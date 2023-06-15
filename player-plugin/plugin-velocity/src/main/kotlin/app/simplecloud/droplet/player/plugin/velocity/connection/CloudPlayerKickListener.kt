package app.simplecloud.droplet.player.plugin.velocity.connection

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.CloudPlayerKickEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class CloudPlayerKickListener(
        private val proxyServer: ProxyServer,
        private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<CloudPlayerKickEvent> {
    override fun handle(message: CloudPlayerKickEvent) {
        val player = proxyServer.getPlayer(message.uniqueId) ?: return
        player.get().disconnect(componentSerializer.deserialize(message.reason.json))
    }
}