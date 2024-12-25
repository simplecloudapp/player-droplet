package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendTitlePartTitleEvent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.title.TitlePart

class SendTitlePartTitleListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendTitlePartTitleEvent> {
    override fun handle(message: SendTitlePartTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        audience.sendTitlePart(TitlePart.TITLE, componentSerializer.deserialize(message.component.json))
    }
}