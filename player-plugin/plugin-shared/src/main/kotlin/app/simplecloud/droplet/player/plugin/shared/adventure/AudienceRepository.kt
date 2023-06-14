package app.simplecloud.droplet.player.plugin.shared.adventure

import net.kyori.adventure.audience.Audience

interface AudienceRepository {

    fun getAudienceByUniqueId(uniqueId: String): Audience?

}