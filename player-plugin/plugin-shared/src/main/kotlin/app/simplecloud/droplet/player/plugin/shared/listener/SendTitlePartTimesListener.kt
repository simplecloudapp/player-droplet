package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendTitlePartTimesEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import java.time.Duration

class SendTitlePartTimesListener(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendTitlePartTimesEvent> {
    override fun handle(message: SendTitlePartTimesEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        audience.sendTitlePart(
            TitlePart.TIMES,
            Title.Times.times(
                Duration.ofMillis(message.fadeIn),
                Duration.ofMillis(message.stay),
                Duration.ofMillis(message.fadeOut)
            )
        )
    }
}