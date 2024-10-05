package app.simplecloud.droplet.player.plugin.bungeecord.connection

import app.simplecloud.droplet.player.proto.CloudPlayerKickEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.md_5.bungee.api.ProxyServer
import java.util.UUID

class CloudPlayerKickListener(
        private val proxyServer: ProxyServer,
        private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<CloudPlayerKickEvent> {
    override fun handle(message: CloudPlayerKickEvent) {
        val player = proxyServer.getPlayer(UUID.fromString(message.uniqueId)) ?: return
        val component = componentSerializer.deserialize(message.reason.json)
        player.disconnect(LegacyComponentSerializer.legacySection().serialize(component))
    }
}