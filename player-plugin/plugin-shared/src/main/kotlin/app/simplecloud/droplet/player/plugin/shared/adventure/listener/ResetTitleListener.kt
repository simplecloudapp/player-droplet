package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendResetTitleEvent

class ResetTitleListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendResetTitleEvent> {
    override fun handle(message: SendResetTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.resetTitle()
    }
}