package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendActionbarEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendActionbarListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendActionbarEvent> {
    override fun handle(message: SendActionbarEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.sendActionBar(componentSerializer.deserialize(message.message.json))
    }
}