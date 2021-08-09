package com.github.minxyzgo.rwserver.plugins.hiniloader.timer

import com.github.minxyzgo.rwserver.plugins.hiniloader.*

class TimerTask : Executor() {
    var delay: Long = 1000
    var repeatDelay: Long = 1000
    var repeatCount: Int = 0

    fun run() = actions.forEach {
        if(it.action.trigger.get().apply { println(this) }) {
            println("do")
            it.run(mapOf())
        }
    }
}