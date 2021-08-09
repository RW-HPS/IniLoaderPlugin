import com.github.dr.rwserver.*
import com.github.dr.rwserver.command.*
import com.github.dr.rwserver.core.*
import com.github.dr.rwserver.core.thread.*
import com.github.dr.rwserver.data.global.*
import com.github.dr.rwserver.data.plugin.*
import com.github.dr.rwserver.func.StrCons
import com.github.dr.rwserver.game.*
import com.github.dr.rwserver.plugin.*
import com.github.dr.rwserver.struct.*
import com.github.dr.rwserver.util.file.*
import com.github.dr.rwserver.util.game.*
import com.github.dr.rwserver.util.log.*
import com.github.minxyzgo.highini.*
import com.github.minxyzgo.rwserver.plugins.hiniloader.*
import java.util.logging.*
//
//val info = """
//    --------------------------------------------------------------------------------
//    			  _______            __           _    _ _____   _____
//    			 |  __ \\ \\        / /          | |  | |  __ \\ / ____|
//    			 | |__) \\ \\  /\\  / /   ______  | |__| | |__) | (___
//    			 |  _  / \\ \\/  \\/ /   |______| |  __  |  ___/ \\___ \\
//    			 | | \\ \\  \\  /\\  /             | |  | | |     ____) |
//    			 |_|  \\_\\  \\/  \\/              |_|  |_|_|    |_____/
//    --------------------------------------------------------------------------------
//""".trimIndent()
//
//typealias DataSeq = Seq<PluginsLoad.PluginLoadData>
//@Suppress("UNCHECKED_CAST")
//fun main() {
//    Log.set("ALL")
//    Log.setPrint(true)
//    Logger.getLogger("io.netty").level = Level.OFF
//    println(info)
//    Log.clog("Load ing...")
//
//    Data.core.load()
//
//    Initialization()
//    Data.config = LoadConfig(Data.Plugin_Data_Path, "Config.json")
//    ServerCommands(Data.SERVERCOMMAND)
//    ClientCommands(Data.CLIENTCOMMAND)
//    LogCommands(Data.LOGCOMMAND)
//
//    Log.clog("正在加载命令")
//
//    Main.loadNetCore()
//    Main.loadUnitList()
//    val threadMethod = Main::class.java.getDeclaredMethod("buttonMonitoring").also {
//        it.isAccessible = true
//    }
//    Threads.newThreadCore {
//        threadMethod.invoke(null)
//    }
//
//    Events.fire(EventType.ServerLoadEvent())
//
//    Data.SERVERCOMMAND.handleMessage("start", StrCons {
//        Log.clog(it)
//    })
//}

fun main() {
    Main.main(arrayOf(""))
    ConfigFactory.parseResources(
        Thread.currentThread().contextClassLoader.getResource("test.hini"),
        ParserImpl
    )
}