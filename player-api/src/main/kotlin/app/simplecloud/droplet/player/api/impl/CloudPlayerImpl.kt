package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.proto.*
import app.simplecloud.droplet.player.proto.PlayerServiceGrpc.PlayerServiceFutureStub
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart

class CloudPlayerImpl(
    private val playerServiceStub: PlayerServiceFutureStub,
    private val configuration: CloudPlayerConfiguration,
    private val componentSerializer: GsonComponentSerializer = GsonComponentSerializer.gson(),
) : OfflineCloudPlayerImpl(OfflineCloudPlayerConfiguration.parseFrom(configuration.toByteArray())), CloudPlayer {

    override fun getConnectedServerName(): String? {
        return this.configuration.connectedServerName
    }

    override fun getConnectedProxyName(): String {
        return this.configuration.connectedProxyName
    }

    override fun isOnline(): Boolean {
        return true
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        playerServiceStub.sendMessage(
            SendMessageRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setMessage(AdventureComponent.newBuilder().setJson(componentSerializer.serialize(message)).build())
                .build()
        )
    }

    override fun sendActionBar(message: Component) {
        playerServiceStub.sendActionbar(
            SendActionbarRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setMessage(AdventureComponent.newBuilder().setJson(componentSerializer.serialize(message)).build())
                .build()
        )
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {
        playerServiceStub.sendPlayerListHeaderAndFooter(
            SendPlayerListHeaderAndFooterRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setFooter(AdventureComponent.newBuilder().setJson(componentSerializer.serialize(footer)).build())
                .setHeader(AdventureComponent.newBuilder().setJson(componentSerializer.serialize(header)).build())
                .build()
        )
    }

    override fun <T: Any> sendTitlePart(part: TitlePart<T>, value: T) {
        if (value is Title.Times) {
            playerServiceStub.sendTitlePartTimes(
                SendTitlePartTimesRequest.newBuilder()
                    .setUniqueId(getUniqueId().toString())
                    .setFadeIn(value.fadeIn().toMillis().toInt())
                    .setStay(value.stay().toMillis().toInt())
                    .setFadeOut(value.fadeOut().toMillis().toInt())
                    .build()
            )
        } else {
            val component = value as Component
            playerServiceStub.sendTitlePartComponent(
                SendTitlePartComponentRequest.newBuilder()
                    .setUniqueId(getUniqueId().toString())
                    .setComponent(
                        AdventureComponent.newBuilder().setJson(componentSerializer.serialize(component)).build()
                    )
                    .build()
            )
        }
    }

    override fun clearTitle() {
        playerServiceStub.sendClearTitle(
            SendClearTitleRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .build()
        )
    }

    override fun resetTitle() {
        playerServiceStub.sendResetTitle(
            SendResetTitleRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .build()
        )
    }

    override fun showBossBar(bar: BossBar) {
        playerServiceStub.sendBossBar(
            SendBossBarRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setBossBar(
                    AdventureBossBar.newBuilder()
                        .setColor(bar.color().name)
                        .setOverlay(bar.overlay().name)
                        .setProgress(bar.progress())
                        .setTitle(
                            AdventureComponent.newBuilder().setJson(componentSerializer.serialize(bar.name())).build()
                        )
                        .addAllFlags(bar.flags().map { it.name })
                        .build()
                )
                .build()
        )
    }

    override fun hideBossBar(bar: BossBar) {
        playerServiceStub.sendBossBarRemove(
            SendBossBarHideRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setBossBar(
                    AdventureBossBar.newBuilder()
                        .setColor(bar.color().name)
                        .setOverlay(bar.overlay().name)
                        .setProgress(bar.progress())
                        .setTitle(
                            AdventureComponent.newBuilder().setJson(componentSerializer.serialize(bar.name())).build()
                        )
                        .addAllFlags(bar.flags().map { it.name })
                        .build()
                )
                .build()
        )
    }

    override fun playSound(sound: Sound) {
        playerServiceStub.sendPlaySound(
            SendPlaySoundRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setSound(AdventureSound.newBuilder().setSound(sound.name().asString()).build())
                .build()
        )
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        playerServiceStub.sendPlaySoundToCoordinates(
            SendPlaySoundToCoordinatesRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setSound(AdventureSound.newBuilder().setSound(sound.name().asString()).build())
                .setX(x)
                .setY(y)
                .setZ(z)
                .build()
        )
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        playSound(sound)
    }

    override fun stopSound(stop: SoundStop) {
        val sound = stop.sound()?.let { AdventureSound.newBuilder().setSound(it.asString()).build() }
        val source = stop.source()?.toString()

        playerServiceStub.sendStopSound(
            SendStopSoundRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setSound(sound)
                .setSource(source)
                .build()
        )
    }

    override fun openBook(book: Book) {
        val bookBuilder = AdventureBook.newBuilder()

        bookBuilder.setTitle(
            AdventureComponent.newBuilder().setJson(componentSerializer.serialize(book.title())).build()
        )
        bookBuilder.setAuthor(
            AdventureComponent.newBuilder().setJson(componentSerializer.serialize(book.author())).build()
        )

        for (page in book.pages()) {
            bookBuilder.addPages(AdventureComponent.newBuilder().setJson(componentSerializer.serialize(page)).build())
        }

        playerServiceStub.sendOpenBook(
            SendOpenBookRequest.newBuilder()
                .setUniqueId(getUniqueId().toString())
                .setBook(bookBuilder.build())
                .build()
        )
    }


}