package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendPlayerListHeaderAndFooterEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class SendPlayerListHeaderAndFooterListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendPlayerListHeaderAndFooterEvent> {
    override fun handle(message: SendPlayerListHeaderAndFooterEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.sendPlayerListHeaderAndFooter(
            componentSerializer.deserialize(message.header.json),
            componentSerializer.deserialize(message.footer.json)
        )
    }
}