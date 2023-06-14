package app.simplecloud.droplet.player.shared.rabbitmq

import app.simplecloud.droplet.player.proto.MessageBody
import com.google.protobuf.Message
import com.rabbitmq.client.Delivery
import kotlin.concurrent.thread

class RabbitMqConsumer(
    private val queues: List<String>
) {

    private val connection = RabbitMqFactory.createConnectionFromEnv()
    private val channel = connection.createChannel()

    private val listeners = mutableMapOf<MessageListenerKey, MessageListenerValue<out Message>>()

    fun start() {
        channel.exchangeDeclare(RabbitMqChannelNames.CHANNEL_PREFIX, "fanout", true)
        queues.forEach {queueName ->
            thread {
                val deliverCallback: (String?, Delivery) -> Unit = { consumerTag: String?, delivery: Delivery ->
                    println("Received message from $queueName ${delivery.envelope.deliveryTag}")
                    val message = MessageBody.parseFrom(delivery.body)
                    val dataType = message.type
                    val listener = listeners[MessageListenerKey(queueName, dataType)]

                    listener?.let {
                        val messageData = message.messageData.unpack(it.dataType)
                        if (!it.canHandle(messageData)) {
                            channel.basicNack(delivery.envelope.deliveryTag, false, true)
                            return@let
                        }

                        it.handle(messageData)
                        channel.basicAck(delivery.envelope.deliveryTag, false)
                    }
                }

                    channel.basicConsume(queueName, false, deliverCallback) { _: String? ->  }
            }
        }
    }

    fun <T : Message> listen(queueName: String, dataType: Class<T>, listener: RabbitMqListener<T>) {
        listen(queueName, dataType, null, listener)
    }

    fun <T : Message> listen(queueName: String, dataType: Class<T>, filter: ((T) -> Boolean)?, listener: RabbitMqListener<T>) {
        val key = MessageListenerKey(queueName, dataType.name)
        listeners[key] = MessageListenerValue<T>(dataType, listener, filter)
        channel.queueDeclare(queueName, false, false, false, null)
        channel.queueBind(queueName, RabbitMqChannelNames.CHANNEL_PREFIX, "")
    }

    data class MessageListenerKey(
        val queueName: String,
        val dataType: String
    )

    data class MessageListenerValue<T: Message>(
        val dataType: Class<out Message>,
        val listener: RabbitMqListener<T>,
        val filter: RabbitMqFilter<T>? = null
    ) {

        fun handle(message: Message) {
            listener.handle(message as T)
        }

        fun canHandle(message: Message): Boolean {
            return filter?.filter(message as T) ?: true
        }

    }

}