package com.github.minxyzgo.rwserver.plugins.hiniloader

import com.github.dr.rwserver.game.*
import com.github.dr.rwserver.util.game.*
import com.github.minxyzgo.highini.exception.*
import com.github.minxyzgo.highini.exception.ParseException.Companion.setStackTrace
import com.github.minxyzgo.highini.func.Boolp
import com.github.minxyzgo.highini.parse.*
import com.github.minxyzgo.highini.type.*
import com.github.minxyzgo.highini.util.*
import com.github.minxyzgo.rwserver.plugins.hiniloader.actions.*
import com.github.minxyzgo.rwserver.plugins.hiniloader.listener.*
import com.github.minxyzgo.rwserver.plugins.hiniloader.timer.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@ObsoleteCoroutinesApi
object ParserImpl : Parser(true) {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    private val actionReader = { section: ConfigSection, key: String, value: String, line: Int ->
        when(key) {
            "actions" -> {
                val tree = section.parent!!
                val list = if (value.startsWith("[")) {
                    if (!value.endsWith("]")) throw SyntaxException("Missing ] after list.")
                    val message = value.removePrefix("[").removeSuffix("]").split(",")
                    message.map {
                        it.trim().run {
                            val args = parameters.find(this)?.value ?: ""
                            println("args $args")
                            args.split(",").map { p -> p.trim() } to ((ParserImpl.parseReferenceValue(
                                tree,
                                if (args.isNotBlank()) this.replace("($args)", "") else this,
                                line
                            ) ?: it) as ConfigSection).parseToObject(true) as Action
                        }
                    }
                } else {
                    throw SyntaxException("Missing [ before list.")
                }

                list.map { (args, action) ->
                    val map = mutableMapOf<String, String>()
                    val reallyArgs = args.filter { it.isNotBlank() }
                    if(reallyArgs.isNotEmpty()) {
                        for(arg in reallyArgs) {
                            println("arg: $arg")
                            val split = arg.split("=", limit = 2)
                            if(split.size < 2) throw SyntaxException("")
                            val second = split[1].trim()

                            map[split.first().trim()] = second
                        }
                    }
                    val data = ActionData(map, action)
                    data
                }
            }

            "trigger" -> value
            else -> null
        }
    }
    private val parameters = Regex("""(?<=\().*(?=\))""")
    private val actionList = mapOf<String, Class<*>>(
        "chat" to ChatAction::class.java,
        "kick" to KickAction::class.java
    )

    private val eventList =  mapOf<String, Class<*>>(
        "chat" to ChatEventListener::class.java
    )

    init {
        eventList.forEach { (key, value) ->
            this.tags.add(
                ConfigTag(
                    "event_$key",
                    value,
                    reader = actionReader
                )
            )
        }

        actionList.forEach { (key, value) ->
            this.tags.add(
                ConfigTag(
                    "action_$key",
                    value
                )
            )
        }

        this.classParsers[Regex::class.java] = {
            val regex = Regex(it)
            regex
        }

        this.tags.add(
            ConfigTag(
                "timer",
                TimerTask::class.java,
                reader = actionReader
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun endParse() {
        super.endParse()
        val mergeList = eventList.map { "event_${it.key}" }
        for(tree in configTreeList) {
            for(section in tree.mutableMap.values) {
                if(section.tag?.name.let { it !in mergeList && it != "timer" }) continue
                val configValue = section["actions"]!!
                section["trigger"]?.value = Boolp { true }
                val line = configValue.line
                configValue.setStackTrace()

                val list: List<ActionData> = configValue.value as List<ActionData>

                val obj = section.parseToObject(true) as Executor

                list.forEach { (args, action) ->
                    fun doAction(param: Any?) {
                        val selfSection = param?.asSection("self", accessible = true)
                        val pMap = args.mapValues { (_, second) ->
                            if (second.startsWith("::")) {
                                ParserImpl.parseGetReference(
                                    second.removePrefix("::"),
                                    tree,
                                    line,
                                    selfSection
                                ).value!!()!!
                            } else {
                                second
                            }
                        }
                        val boolp = section["trigger"]?.let {
                            parseBoolean(
                                it.stringValue,
                                tree,
                                it.line,
                                selfSection
                            )
                        }
                        if(obj is EventListener<*>) {
                            if (obj.trigger(param!!)) {
                                if (action.trigger.get() && boolp?.get().let { it == null || it == true }) action.run(
                                    pMap
                                )
                            }
                        }
                    }

                    if(args.isNotEmpty() && obj is EventListener<*>) {
                        println("is listener")
                        Events.on(obj.dependOn) { event ->
                            doAction(event)
                        }
                    } else {
                        if(args.isNotEmpty()) throw ParseException("")

                        if(obj is EventListener<*>) {
                            println("is listener")
                            Events.on(obj.dependOn) { event: Any ->
                                val boolp = section["trigger"]?.let {
                                    parseBoolean(
                                        it.stringValue,
                                        tree,
                                        it.line,
                                        event.asSection("self", accessible = true)
                                    )
                                }
                                if (action.trigger.get()
                                    && boolp?.get().let { it == null || it == true }
                                    && obj.trigger(event)
                                ) {
                                    action.run(mapOf())
                                }
                            }
                        } else if(obj is TimerTask) {
                            println("is timer task")
                            Timer.tasks.add(obj)
                            Timer.flush()
                        } else {
                            println("nothing?")
                        }
                    }
                }
            }
        }
    }
}