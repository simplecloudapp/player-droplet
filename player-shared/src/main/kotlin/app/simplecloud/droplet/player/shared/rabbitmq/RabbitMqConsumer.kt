package app.simplecloud.droplet.player.shared.rabbitmq

import app.simplecloud.droplet.player.proto.MessageBody
import com.google.protobuf.Message
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

class RabbitMqConsumer {

    private val connection = RabbitMqFactory.createConnectionFromEnv()
    private val channel = connection.createChannel().apply {
        this.exchangeDeclare(RabbitMqChannelNames.CHANNEL_PREFIX, "fanout", true)
    }

    fun <T : Message> listen(
        queueName: String,
        dataType: Class<T>,
        listener: RabbitMqListener<T>
    ) {
        val queue = channel.queueDeclare()
        channel.queueBind(queue.queue, RabbitMqChannelNames.CHANNEL_PREFIX, "")
        channel.basicConsume(queue.queue, true, CustomConsumer(dataType, queueName, listener))
    }

    private inner class CustomConsumer<T : Message>(
        val dataType: Class<T>,
        val queueName: String,
        val listener: RabbitMqListener<T>
    ) : DefaultConsumer(channel) {
        override fun handleDelivery(
            consumerTag: String?,
            envelope: Envelope?,
            properties: AMQP.BasicProperties?,
            body: ByteArray?
        ) {
            val message = MessageBody.parseFrom(body)
            if (message.type != dataType.name) return
            println("Received message from $dataType $queueName ${envelope?.deliveryTag}")
            val messageData = message.messageData.unpack(dataType)
            listener.handle(messageData)
        }
    }

}