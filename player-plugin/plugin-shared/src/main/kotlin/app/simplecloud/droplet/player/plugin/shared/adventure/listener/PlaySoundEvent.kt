package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendPlaySoundEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class PlaySoundEvent(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendPlaySoundEvent> {
    override fun handle(message: SendPlaySoundEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        audience.playSound(
            Sound.sound(
                Key.key(message.sound.sound),
                Sound.Source.valueOf(message.sound.source),
                message.sound.volume,
                message.sound.pitch
            )
        )
    }

}