package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.droplet.player.proto.SendOpenBookEvent
import app.simplecloud.droplet.player.shared.rabbitmq.RabbitMqListener
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class OpenBookEvent(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : RabbitMqListener<SendOpenBookEvent> {
    override fun handle(message: SendOpenBookEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId)?: return
        val book = Book.book(
            componentSerializer.deserialize(message.book.title.json),
            componentSerializer.deserialize(message.book.author.json),
            message.book.pagesList.map { componentSerializer.deserialize(it.json) })
        audience.openBook(book)
    }
}