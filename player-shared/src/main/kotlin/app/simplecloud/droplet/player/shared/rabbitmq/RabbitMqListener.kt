package app.simplecloud.droplet.player.shared.rabbitmq

import com.google.protobuf.Message

fun interface RabbitMqListener<T: Message> {
    fun handle(message: T)
}