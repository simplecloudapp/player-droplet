package app.simplecloud.droplet.player.api

class PlayerApiSingleton {
        companion object {
            @JvmStatic
            lateinit var instance: PlayerApi
                private set

            fun init(playerApi: PlayerApi) {
                instance = playerApi
            }
        }
}