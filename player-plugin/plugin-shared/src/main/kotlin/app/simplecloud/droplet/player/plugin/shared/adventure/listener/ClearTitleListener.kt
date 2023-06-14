package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendClearTitleEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener

class ClearTitleListener(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendClearTitleEvent> {
    override fun handle(message: SendClearTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.clearTitle()
    }
}