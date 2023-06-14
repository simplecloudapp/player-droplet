package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendPlayerListHeaderAndFooterEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendPlayerListHeaderAndFooterListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendPlayerListHeaderAndFooterEvent> {
    override fun handle(message: SendPlayerListHeaderAndFooterEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        audience.sendPlayerListHeaderAndFooter(
            componentSerializer.deserialize(message.header.json),
            componentSerializer.deserialize(message.footer.json)
        )
    }
}