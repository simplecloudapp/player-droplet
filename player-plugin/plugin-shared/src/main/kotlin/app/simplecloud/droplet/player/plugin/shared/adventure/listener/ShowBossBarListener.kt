package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendBossBarEvent
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class ShowBossBarListener(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendBossBarEvent> {
    override fun handle(message: SendBossBarEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        val bossBar = BossBar.bossBar(
            componentSerializer.deserialize(message.bossBar.title.json),
            message.bossBar.progress,
            BossBar.Color.valueOf(message.bossBar.color),
            BossBar.Overlay.valueOf(message.bossBar.overlay),
        )
        bossBar.addFlags(BossBar.Flag.values().filter { message.bossBar.flagsList.contains(it.name) }.toSet())
        audience.showBossBar(bossBar)
    }
}