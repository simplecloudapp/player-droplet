package app.simplecloud.droplet.player.server

import org.apache.logging.log4j.LogManager

class PlayerServer {

    private val logger = LogManager.getLogger(PlayerServer::class.java)

    fun start() {
        logger.info("Starting PlayerServer...")
    }

}