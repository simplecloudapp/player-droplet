package app.simplecloud.droplet.player.api

import net.kyori.adventure.audience.Audience

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
     * @returns the time of the players' session.
     */
    fun getSessionTime(): Long {
        return System.currentTimeMillis() - getLastLogin()
    }

}