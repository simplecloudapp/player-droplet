package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendPlaySoundToCoordinatesEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class PlaySoundToCoordinatesListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendPlaySoundToCoordinatesEvent> {
    override fun handle(message: SendPlaySoundToCoordinatesEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        audience.playSound(
            Sound.sound(
                Key.key(message.sound.sound),
                Sound.Source.valueOf(message.sound.sound),
                message.sound.volume,
                message.sound.pitch
            ), message.x, message.y, message.z
        )

    }
}