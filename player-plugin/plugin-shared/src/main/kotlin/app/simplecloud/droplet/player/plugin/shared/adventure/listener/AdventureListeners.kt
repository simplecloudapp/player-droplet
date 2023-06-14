package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import com.google.protobuf.Message

object AdventureListeners {

    fun all(repository: AudienceRepository): List<RabbitMqListener<out Message>> {
        return allWithClasses(repository).map { it.second }
    }

    fun allWithClasses(repository: AudienceRepository): List<Pair<Class<out Message>, RabbitMqListener<out Message>>> {
        return listOf(
            SendClearTitleEvent::class.java to ClearTitleListener(repository),
            SendBossBarHideEvent::class.java to HideBossBarListener(repository),
            SendOpenBookEvent::class.java to OpenBookEvent(repository),
            SendPlaySoundEvent::class.java to PlaySoundEvent(repository),
            SendPlaySoundToCoordinatesEvent::class.java to PlaySoundToCoordinatesListener(repository),
            SendResetTitleEvent::class.java to ResetTitleListener(repository),
            SendActionbarEvent::class.java to SendActionbarListener(repository),
            SendMessageEvent::class.java to SendMessageListener(repository),
            SendPlayerListHeaderAndFooterEvent::class.java to SendPlayerListHeaderAndFooterListener(repository),
            SendTitlePartSubTitleEvent::class.java to SendTitlePartSubTitleListener(repository),
            SendTitlePartTimesEvent::class.java to SendTitlePartTimesListener(repository),
            SendTitlePartTitleEvent::class.java to SendTitlePartTitleListener(repository),
            SendBossBarEvent::class.java to ShowBossBarListener(repository),
            SendStopSoundEvent::class.java to StopSoundListener(repository),
        )
    }

}