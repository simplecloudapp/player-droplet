package app.simplecloud.droplet.player.api

import app.simplecloud.droplet.player.api.impl.PlayerApiCoroutineImpl
import app.simplecloud.droplet.player.api.impl.PlayerApiFutureImpl
import build.buf.gen.simplecloud.droplet.player.v1.CloudPlayerConnectResult
import app.simplecloud.pubsub.PubSubClient
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Represents the main API for player management in SimpleCloud
 * This API provides both Future and Coroutine implementations for handling player operations
 */
interface PlayerApi {

    /**
     * CompletableFuture-based API interface for player operations
     */
    fun getFutureApi(): PlayerApi.Future

    /**
     * Coroutine-based API interface for player operations
     */
    fun getCoroutineApi(): PlayerApi.Coroutine

    /**
     * Future-based API interface for player operations
     */
    interface Future {
        /**
         * Gets an offline player by their name
         * @param name the name of the player
         * @return a [CompletableFuture] containing the [OfflineCloudPlayer] if found
         */
        fun getOfflinePlayer(name: String): CompletableFuture<OfflineCloudPlayer>

        /**
         * Gets an offline player by their UUID
         * @param uniqueId the UUID of the player
         * @return a [CompletableFuture] containing the [OfflineCloudPlayer] if found
         */
        fun getOfflinePlayer(uniqueId: UUID): CompletableFuture<OfflineCloudPlayer>

        /**
         * Gets an online player by their name
         * @param name the name of the player
         * @return a [CompletableFuture] containing the [CloudPlayer] if found
         */
        fun getOnlinePlayer(name: String): CompletableFuture<CloudPlayer>

        /**
         * Gets an online player by their UUID
         * @param uniqueId the UUID of the player
         * @return a [CompletableFuture] containing the [CloudPlayer] if found
         */
        fun getOnlinePlayer(uniqueId: UUID): CompletableFuture<CloudPlayer>

        /**
         * Gets a list of all currently online players
         * @return a [CompletableFuture] containing a list of all [CloudPlayer]s
         */
        fun getOnlinePlayers(): CompletableFuture<List<CloudPlayer>>

        /**
         * Gets the total count of online players
         * @return a [CompletableFuture] containing the number of online players
         */
        fun getOnlinePlayerCount(): CompletableFuture<Int>

        /**
         * Attempts to connect a player to a specific server
         * @param uniqueId the UUID of the player to connect
         * @param serverName the name of the target server
         * @return a [CompletableFuture] containing the [CloudPlayerConnectResult]
         */
        fun connectPlayer(uniqueId: UUID, serverName: String): CompletableFuture<CloudPlayerConnectResult>

        /**
         * Checks if a player is currently online
         * @param uniqueId the UUID of the player to check
         * @return a [CompletableFuture] containing true if the player is online, false otherwise
         */
        fun isOnline(uniqueId: UUID): CompletableFuture<Boolean>

        /**
         * Updates the server information for a player
         * @param uniqueId the UUID of the player
         * @param serverName the name of the server the player is now on
         * @return a [CompletableFuture] containing true if the update was successful
         */
        fun updateServer(uniqueId: UUID, serverName: String): CompletableFuture<Boolean>

        /**
         * Gets the PubSub client for player-related events
         * @return the [PubSubClient] instance for subscribing to player events
         */
        fun getPubSubClient(): PubSubClient
    }

    /**
     * Coroutine-based API interface for player operations
     */
    interface Coroutine {
        /**
         * Gets an offline player by their name
         * @param name the name of the player
         * @return the [OfflineCloudPlayer] if found
         */
        suspend fun getOfflinePlayer(name: String): OfflineCloudPlayer

        /**
         * Gets an offline player by their UUID
         * @param uniqueId the UUID of the player
         * @return the [OfflineCloudPlayer] if found
         */
        suspend fun getOfflinePlayer(uniqueId: UUID): OfflineCloudPlayer

        /**
         * Gets an online player by their name
         * @param name the name of the player
         * @return the [CloudPlayer] if found
         */
        suspend fun getOnlinePlayer(name: String): CloudPlayer

        /**
         * Gets an online player by their UUID
         * @param uniqueId the UUID of the player
         * @return the [CloudPlayer] if found
         */
        suspend fun getOnlinePlayer(uniqueId: UUID): CloudPlayer

        /**
         * Gets a list of all currently online players
         * @return a list of all [CloudPlayer]s
         */
        suspend fun getOnlinePlayers(): List<CloudPlayer>

        /**
         * Gets the total count of online players
         * @return the number of online players
         */
        suspend fun getOnlinePlayerCount(): Int

        /**
         * Attempts to connect a player to a specific server
         * @param uniqueId the UUID of the player to connect
         * @param serverName the name of the target server
         * @return the [CloudPlayerConnectResult]
         */
        suspend fun connectPlayer(uniqueId: UUID, serverName: String): CloudPlayerConnectResult

        /**
         * Checks if a player is currently online
         * @param uniqueId the UUID of the player to check
         * @return true if the player is online, false otherwise
         */
        suspend fun isOnline(uniqueId: UUID): Boolean

        /**
         * Updates the server information for a player
         * @param uniqueId the UUID of the player
         * @param serverName the name of the server the player is now on
         * @return true if the update was successful
         */
        suspend fun updateServer(uniqueId: UUID, serverName: String): Boolean

        /**
         * Gets the PubSub client for player-related events
         * @return the [PubSubClient] instance for subscribing to player events
         */
        fun getPubSubClient(): PubSubClient
    }

    companion object {
        /**
         * Creates a new [PlayerApi.Future] instance using the environment variable CONTROLLER_SECRET
         * @return the created [PlayerApi.Future]
         * @throws IllegalStateException if CONTROLLER_SECRET is not set
         */
        @JvmStatic
        fun createFutureApi(): Future {
            val authSecret = System.getenv("CONTROLLER_SECRET")
            return createFutureApi(authSecret)
        }

        /**
         * Creates a new [PlayerApi.Future] instance with the provided authentication secret
         * @param authSecret the authentication key used by the Player Droplet
         * @return the created [PlayerApi.Future]
         */
        @JvmStatic
        fun createFutureApi(authSecret: String): Future {
            return PlayerApiFutureImpl(authSecret)
        }

        /**
         * Creates a new [PlayerApi.Coroutine] instance using the environment variable CONTROLLER_SECRET
         * @return the created [PlayerApi.Coroutine]
         * @throws IllegalStateException if CONTROLLER_SECRET is not set
         */
        @JvmStatic
        fun createCoroutineApi(): Coroutine {
            val authSecret = System.getenv("CONTROLLER_SECRET")
            return createCoroutineApi(authSecret)
        }

        /**
         * Creates a new [PlayerApi.Coroutine] instance with the provided authentication secret
         * @param authSecret the authentication key used by the Player Droplet
         * @return the created [PlayerApi.Coroutine]
         */
        @JvmStatic
        fun createCoroutineApi(authSecret: String): Coroutine {
            return PlayerApiCoroutineImpl(authSecret)
        }
    }
}
