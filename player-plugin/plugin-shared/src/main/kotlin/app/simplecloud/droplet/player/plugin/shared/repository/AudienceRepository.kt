package app.simplecloud.droplet.player.plugin.shared.repository

import net.kyori.adventure.audience.Audience

interface AudienceRepository {

    fun getAudienceByUniqueId(uniqueId: String): Audience

}