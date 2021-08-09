package com.github.minxyzgo.rwserver.plugins.hiniloader.timer

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.*

@ObsoleteCoroutinesApi
object Timer : CoroutineScope {
    val tasks = mutableListOf<TimerTask>()

    private val thread = newSingleThreadContext("timer_task")
    private val _tasks = mutableMapOf<TimerTask, Job>()

    fun flush() {
        for(task in tasks) {
            if(task in _tasks.keys) continue
            val job = launch {
                val ticker = ticker(task.repeatDelay, task.delay, context = coroutineContext)
                if(task.repeatCount > 0) {
                    repeat(task.repeatCount) {
                        ticker.receive()
                        task.run()
                    }

                    ticker.cancel()
                } else {
                    while (true) {
                        ticker.receive()
                        println(task.actions)
                        task.run()
                    }
                }
            }

            _tasks[task] = job
        }
    }

    fun remove(task: TimerTask) {
        _tasks[task]!!.cancel("cancel successfully")
    }

    fun cancel() = thread.close()

    override val coroutineContext: CoroutineContext = thread
}