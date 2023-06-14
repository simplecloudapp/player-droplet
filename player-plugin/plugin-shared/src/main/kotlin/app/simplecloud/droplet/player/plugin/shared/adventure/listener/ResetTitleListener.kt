package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendResetTitleEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener

class ResetTitleListener(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendResetTitleEvent> {
    override fun handle(message: SendResetTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.resetTitle()
    }
}