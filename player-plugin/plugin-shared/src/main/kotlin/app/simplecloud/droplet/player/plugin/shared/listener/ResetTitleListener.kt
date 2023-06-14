package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendResetTitleEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener

class ResetTitleListener(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendResetTitleEvent> {
    override fun handle(message: SendResetTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        audience.resetTitle()
    }
}