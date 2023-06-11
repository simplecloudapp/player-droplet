package app.simplecloud.droplet.player.api.impl

import app.simplecloud.droplet.player.api.CloudPlayer
import app.simplecloud.droplet.player.proto.PlayerServiceGrpc.PlayerServiceBlockingStub
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.TitlePart

class CloudPlayerImpl(
    private val playerServiceStub: PlayerServiceBlockingStub,
): OfflineCloudPlayerImpl(), CloudPlayer {

    override fun getConnectedServerName(): String? {
        TODO("Not yet implemented")
    }

    override fun getConnectedProxyName(): String {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {

    }

    override fun sendActionBar(message: Component) {
    }

    override fun sendPlayerListHeaderAndFooter(header: Component, footer: Component) {

    }

    override fun <T> sendTitlePart(part: TitlePart<T>, value: T) {

    }

    override fun clearTitle() {

    }

    override fun resetTitle() {

    }

    override fun showBossBar(bar: BossBar) {

    }

    override fun hideBossBar(bar: BossBar) {

    }

    override fun playSound(sound: Sound) {

    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {

    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {

    }

    override fun stopSound(stop: SoundStop) {

    }

    override fun openBook(book: Book) {

    }

}