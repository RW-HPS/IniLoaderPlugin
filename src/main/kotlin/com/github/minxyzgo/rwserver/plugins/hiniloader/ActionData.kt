package com.github.minxyzgo.rwserver.plugins.hiniloader

import com.github.minxyzgo.rwserver.plugins.hiniloader.actions.*

data class ActionData(
    val args: Map<String, String>,
    val action: Action
) {
    fun run(args: Map<String, Any>) {
        action.run(args)
    }
}
