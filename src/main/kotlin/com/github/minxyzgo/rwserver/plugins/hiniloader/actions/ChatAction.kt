package com.github.minxyzgo.rwserver.plugins.hiniloader.actions

import com.github.dr.rwserver.core.*

class ChatAction : Action() {
    var chatAllMsg: String? = null
    override val needMap: Map<String, Class<*>> = mapOf()

    override fun run(args: Map<String, Any>) {
        println("chat")
        Call.sendSystemMessage(chatAllMsg)
    }
}