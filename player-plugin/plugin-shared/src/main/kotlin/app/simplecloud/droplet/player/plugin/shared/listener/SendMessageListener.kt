package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendMessageEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendMessageListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendMessageEvent> {
    override fun handle(message: SendMessageEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        audience.sendMessage(componentSerializer.deserialize(message.message.json))
    }
}