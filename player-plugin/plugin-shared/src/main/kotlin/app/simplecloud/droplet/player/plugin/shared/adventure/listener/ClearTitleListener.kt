package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendClearTitleEvent

class ClearTitleListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendClearTitleEvent> {
    override fun handle(message: SendClearTitleEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        audience.clearTitle()
    }
}