package com.github.minxyzgo.rwserver.plugins.hiniloader.listener

import com.github.minxyzgo.rwserver.plugins.hiniloader.*

sealed class EventListener<T> : Executor() {
    abstract val dependOn: Class<T>
    abstract fun trigger(event: Any): Boolean
}