package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendMessageEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendMessageListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendMessageEvent> {
    override fun handle(message: SendMessageEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.sendMessage(componentSerializer.deserialize(message.message.json))
    }
}