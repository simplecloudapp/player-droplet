package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendStopSoundEvent
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop

class StopSoundListener(
    private val audienceRepository: AudienceRepository,
) : PubSubListener<SendStopSoundEvent> {
    override fun handle(message: SendStopSoundEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        if (message.sound.sound == null) {
            audience.stopSound(SoundStop.source(Sound.Source.valueOf(message.sound.source)))
        } else {
            audience.stopSound(SoundStop.named(Key.key(message.sound.sound)))
        }
    }
}