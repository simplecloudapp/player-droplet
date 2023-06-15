package app.simplecloud.droplet.player.api

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

interface CloudPlayer : OfflineCloudPlayer, Audience {

    /**
     * @returns the server name of the player.
     */
    fun getConnectedServerName(): String?

    /**
     * @returns the proxy name of the player.
     */
    fun getConnectedProxyName(): String

    /**
     * Kick the player from the network.
     */
    fun kick(reason: Component)

    /**
     * @returns the time of the players' session.
     */
    fun getSessionTime(): Long {
        return System.currentTimeMillis() - getLastLogin()
    }

}