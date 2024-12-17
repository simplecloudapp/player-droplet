package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.PlayerApi

open class PlayerApiImpl(
    private val futureApi: PlayerApi.Future,
    private val coroutineApi: PlayerApi.Coroutine
) : PlayerApi {

    override fun getFutureApi(): PlayerApi.Future {
        return futureApi
    }

    override fun getCoroutineApi(): PlayerApi.Coroutine {
        return coroutineApi
    }

}