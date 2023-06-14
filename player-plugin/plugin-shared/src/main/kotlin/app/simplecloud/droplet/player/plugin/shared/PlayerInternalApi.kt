package app.simplecloud.droplet.player.plugin.shared

import app.simplecloud.droplet.player.api.impl.PlayerApiImpl
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqChannelNames
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqFactory
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import com.google.protobuf.Message

open class PlayerInternalApi(
    private val onlinePlayerChecker: OnlinePlayerChecker
) : PlayerApiImpl() {

    val consumer = RabbitMqFactory.createConsumer()

    fun <T: Message> registerRabbitMqListener(clazz: Class<out Message>, listener: RabbitMqListener<T>) {
        /*
        {
            val uniqueId = it.allFields.entries.find { it.key.name == "uniqueId" }?.value?.toString()?: return@listen true
            println("Checking if player with uniqueId $uniqueId is online")
            onlinePlayerChecker.isOnline(uniqueId)
        }
         */
        consumer.listen(RabbitMqChannelNames.ADVENTURE, clazz, {
            listener.handle(it as T)
        })
    }

}