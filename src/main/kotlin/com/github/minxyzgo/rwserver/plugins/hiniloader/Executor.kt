package com.github.minxyzgo.rwserver.plugins.hiniloader

import com.github.minxyzgo.highini.func.*
import com.github.minxyzgo.rwserver.plugins.hiniloader.actions.*

abstract class Executor {
    var trigger: Boolp = Boolp { true }

    lateinit var actions: List<ActionData>
}