package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendPlaySoundToCoordinatesEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound

class PlaySoundToCoordinatesListener(
    private val audienceRepository: AudienceRepository,
) : RabbitMqListener<SendPlaySoundToCoordinatesEvent> {
    override fun handle(message: SendPlaySoundToCoordinatesEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
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