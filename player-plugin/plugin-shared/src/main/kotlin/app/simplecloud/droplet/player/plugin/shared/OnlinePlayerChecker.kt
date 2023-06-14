package app.simplecloud.droplet.player.plugin.shared

fun interface OnlinePlayerChecker {

    fun isOnline(uniqueId: String): Boolean

}