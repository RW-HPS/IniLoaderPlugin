package com.github.minxyzgo.rwserver.plugins.hiniloader.listener

import com.github.dr.rwserver.game.EventType.PlayerChatEvent

class ChatEventListener : EventListener<PlayerChatEvent>() {
    val filter: Regex? = null

    override fun trigger(event: Any): Boolean {
        return filter?.let { (event as PlayerChatEvent).message.contains(it) } ?: true
    }

    override val dependOn = PlayerChatEvent::class.java
}