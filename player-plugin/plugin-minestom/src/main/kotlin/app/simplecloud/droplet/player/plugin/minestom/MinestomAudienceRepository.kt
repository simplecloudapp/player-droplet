package app.simplecloud.droplet.player.plugin.minestom

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import net.kyori.adventure.audience.Audience
import net.minestom.server.MinecraftServer
import java.util.UUID

class MinestomAudienceRepository: AudienceRepository {

    override fun getAudienceByUniqueId(uniqueId: String): Audience? {
        return MinecraftServer.getConnectionManager().getPlayer(UUID.fromString(uniqueId))
    }

}