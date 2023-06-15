package app.simplecloud.droplet.player.shared.rabbitmq

object RabbitMqChannelNames {

    const val CHANNEL_PREFIX = "droplet.player"
    const val ADVENTURE = "$CHANNEL_PREFIX/adventure"
    const val CONNECTION = "$CHANNEL_PREFIX/connection"

    fun all(): List<String> {
        return listOf(ADVENTURE)
    }

}