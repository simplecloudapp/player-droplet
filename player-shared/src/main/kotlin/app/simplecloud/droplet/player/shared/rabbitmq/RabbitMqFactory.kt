package app.simplecloud.droplet.player.shared.rabbitmq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object RabbitMqFactory {

    fun createConnectionFromEnv(): Connection {
        val host = System.getenv("RABBITMQ_HOST")?: "127.0.0.1"
        val username = System.getenv("RABBITMQ_USERNAME")?: "user"
        val password = System.getenv("RABBITMQ_PASSWORD")?: "bitnami"

        return createConnection(host, username, password)
    }

    fun createConnection(host: String, username: String, password: String): Connection {
        val connectionFactory = ConnectionFactory()
        connectionFactory.host = host
        connectionFactory.username = username
        connectionFactory.password = password

        return connectionFactory.newConnection()
    }

}