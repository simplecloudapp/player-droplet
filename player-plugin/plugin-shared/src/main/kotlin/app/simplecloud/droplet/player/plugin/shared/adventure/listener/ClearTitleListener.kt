package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendClearTitleEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener

class ClearTitleListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendClearTitleEvent> {
    override fun handle(message: SendClearTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.clearTitle()
    }
}