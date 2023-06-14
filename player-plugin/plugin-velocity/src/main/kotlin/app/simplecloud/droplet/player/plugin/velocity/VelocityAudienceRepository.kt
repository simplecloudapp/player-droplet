package app.simplecloud.droplet.player.plugin.velocity

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.audience.Audience

class VelocityAudienceRepository(
    private val proxyServer: ProxyServer
) : AudienceRepository {

    override fun getAudienceByUniqueId(uniqueId: String): Audience? {
        return proxyServer.getPlayer(uniqueId).orElse(null)
    }

}