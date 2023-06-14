package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendPlaySoundEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class PlaySoundEvent(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendPlaySoundEvent> {
    override fun handle(message: SendPlaySoundEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        audience.playSound(
            Sound.sound(
                Key.key(message.sound.sound),
                Sound.Source.valueOf(message.sound.sound),
                message.sound.volume,
                message.sound.pitch
            )
        )
    }

}