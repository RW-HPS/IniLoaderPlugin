package com.github.minxyzgo.rwserver.plugins.hiniloader.actions

import com.github.minxyzgo.highini.func.Boolp

sealed class Action {
    protected abstract val needMap: Map<String, Class<*>>
    var trigger: Boolp = Boolp { true }

    abstract fun run(args: Map<String, Any>)
}