package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendActionbarEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendActionbarListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendActionbarEvent> {
    override fun handle(message: SendActionbarEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        audience.sendActionBar(componentSerializer.deserialize(message.message.json))
    }
}