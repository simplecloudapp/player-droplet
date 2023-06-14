package app.simplecloud.droplet.player.plugin.shared.listener

import app.simplecloud.droplet.player.plugin.shared.repository.AudienceRepository
import app.simplecloud.droplet.player.proto.SendBossBarHideEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class HideBossBarListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendBossBarHideEvent> {
    override fun handle(message: SendBossBarHideEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)
        val bossBar = BossBar.bossBar(
            componentSerializer.deserialize(message.bossBar.title.json),
            message.bossBar.progress,
            BossBar.Color.valueOf(message.bossBar.color),
            BossBar.Overlay.valueOf(message.bossBar.overlay),
        )
        bossBar.addFlags(BossBar.Flag.values().filter { message.bossBar.flagsList.contains(it.name) }.toSet())
        audience.hideBossBar(bossBar)
    }
}