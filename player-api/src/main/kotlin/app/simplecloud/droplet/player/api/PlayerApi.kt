package app.simplecloud.droplet.player.api

import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerConnectResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface PlayerApi {

    /**
     * @param name the name of the player.
     * @returns a [CompletableFuture] with the [OfflineCloudPlayer].
     */
    fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer>

    /**
     * @param uniqueId the uniqueId of the player.
     * @returns a [CompletableFuture] with the [OfflineCloudPlayer].
     */
    fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer>

    /**
     * @param name the name of the player.
     * @returns a [CompletableFuture] with the [CloudPlayer].
     */
    fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer>

    /**
     * @param uniqueId the uniqueId of the player.
     * @returns a [CompletableFuture] with the [CloudPlayer].
     */
    fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer>

    /**
     * @returns a [CompletableFuture] with a list of all [CloudPlayer]s.
     */
    fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>>

    /**
     * @returns a [CompletableFuture] with the count of all online players.
     */
    fun getOnlinePlayerCount(): CompletableFuture<Int>

    /**
     * @param uniqueId the uniqueId of the player.
     * @param serverName the name of the server.
     * @returns a [CompletableFuture] with a boolean if the player was successfully connected.
     */
    fun connectPlayer(uniqueId: UUID, serverName: String): CompletableFuture<CloudPlayerConnectResult>

    /**
     * @param uniqueId the uniqueId of the player.
     * @returns a [CompletableFuture] with a boolean if the player is online.
     */
    fun isOnline(uniqueId: UUID): CompletableFuture<Boolean>

}