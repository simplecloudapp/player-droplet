package app.simplecloud.droplet.player.server.redis

object RedisKeyNames {

    const val PLAYER_KEY_PREFIX = "droplets:player:player"
    const val ONLINE_PLAYERS_KEY = "$PLAYER_KEY_PREFIX/online-players"
    const val PLAYER_UNIQUE_IDS_KEY = "$PLAYER_KEY_PREFIX/player-uuids"

}