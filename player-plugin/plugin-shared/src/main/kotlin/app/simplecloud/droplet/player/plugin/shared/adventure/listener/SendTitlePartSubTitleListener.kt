package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendTitlePartSubTitleEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.title.TitlePart

class SendTitlePartSubTitleListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendTitlePartSubTitleEvent> {
    override fun handle(message: SendTitlePartSubTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.sendTitlePart(TitlePart.SUBTITLE, componentSerializer.deserialize(message.component.json))
    }
}