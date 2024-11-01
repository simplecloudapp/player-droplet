package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendTitlePartTimesEvent
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import java.time.Duration

class SendTitlePartTimesListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendTitlePartTimesEvent> {
    override fun handle(message: SendTitlePartTimesEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
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