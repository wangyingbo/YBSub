package me.leon

import me.leon.domain.ClashConnectLog
import me.leon.support.fromJson
import me.leon.support.readText
import org.junit.jupiter.api.Test

class ClashTestFilter {

    @Test
    fun parseClashLog() {
        val url = "https://sub.cm/RwvVWlS"
        // clash_win/Cache 目录下日志文件
        val clashLogPath = "C:/Users/Leon/Desktop/f_00ff41"

        val nodeMap =
            Parser.parseFromSub(url).fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply { acc[sub.name] = sub }
            }

        clashLogPath
            .readText()
            .fromJson<ClashConnectLog>()
            .proxies
            .filter {
                it.value.isNode &&
                    it.value.history.isNotEmpty() &&
                    it.value.history.last().delay > 0
            }
            .also {
                it.forEach { (t, u) -> println("$t  ${u.history.last().delay}") }
                println()
            }
            .filter { nodeMap[it.key] != null }
            .map { nodeMap[it.key] }
            .also { println(it.joinToString("\n") { it!!.toUri() }) }
            .forEach { println(it!!.name) }
    }
}
