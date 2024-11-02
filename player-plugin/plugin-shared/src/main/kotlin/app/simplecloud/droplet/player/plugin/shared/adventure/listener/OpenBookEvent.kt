package app.simplecloud.droplet.player.plugin.shared.adventure.listener

import app.simplecloud.droplet.player.plugin.shared.adventure.AudienceRepository
import app.simplecloud.pubsub.PubSubListener
import build.buf.gen.simplecloud.droplet.player.v1.SendOpenBookEvent
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

class OpenBookEvent(
    private val audienceRepository: AudienceRepository,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : PubSubListener<SendOpenBookEvent> {
    override fun handle(message: SendOpenBookEvent) {
        val audience = audienceRepository.getAudienceByUniqueId(message.uniqueId) ?: return
        val book = Book.book(
            componentSerializer.deserialize(message.book.title.json),
            componentSerializer.deserialize(message.book.author.json),
            message.book.pagesList.map { componentSerializer.deserialize(it.json) })
        audience.openBook(book)
    }
}