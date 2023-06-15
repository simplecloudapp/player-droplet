package app.simplecloud.droplet.player.plugin.spigot

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import java.util.UUID

class SpigotAudienceRepository(
    private val bukkitAudiences: BukkitAudiences
): AudienceRepository {

    override fun getAudienceByUniqueId(uniqueId: String): Audience {
        return bukkitAudiences.player(UUID.fromString(uniqueId))
    }

}