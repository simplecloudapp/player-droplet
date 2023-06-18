package app.simplecloud.droplet.player.plugin.paper

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import java.util.UUID

class PaperAudienceRepository : AudienceRepository {
    override fun getAudienceByUniqueId(uniqueId: String): Audience? {
        return Bukkit.getPlayer(UUID.fromString(uniqueId))
    }
}