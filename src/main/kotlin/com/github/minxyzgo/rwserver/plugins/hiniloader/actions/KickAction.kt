package com.github.minxyzgo.rwserver.plugins.hiniloader.actions

import com.github.dr.rwserver.data.*

class KickAction : Action() {
    var kickMessage: String = "kick"
    override val needMap: Map<String, Class<*>> = mapOf(
        "player" to Player::class.java
    )

    override fun run(args: Map<String, Any>) {
        val player = args["player"]!!
        player as Player
        player.con.sendKick(kickMessage)
    }
}