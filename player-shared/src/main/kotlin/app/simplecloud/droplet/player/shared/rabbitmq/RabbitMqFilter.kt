package app.simplecloud.droplet.player.shared.rabbitmq

fun interface RabbitMqFilter<T> {
    fun filter(message: T): Boolean
}