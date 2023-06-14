package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendTitlePartTitleEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.title.TitlePart

class SendTitlePartTitleListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendTitlePartTitleEvent> {
    override fun handle(message: SendTitlePartTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.sendTitlePart(TitlePart.TITLE, componentSerializer.deserialize(message.component.json))
    }
}