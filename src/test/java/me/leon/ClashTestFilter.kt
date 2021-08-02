package me.leon

import me.leon.domain.ClashConnectLog
import me.leon.support.fromJson
import me.leon.support.readText
import org.junit.jupiter.api.Test

class ClashTestFilter {

    @Test
    fun parseClashLog() {
        //        tr  https://sub.cm/LtHIdxd
        //        ssr https://sub.cm/TTgunAH
        //        v2 https://sub.cm/w8HCJno
        //        ss https://sub.cm/FLJ17fi
        val url = "https://sub.cm/8AyVhsC"
        // clash_win/Cache 目录下日志文件
        val clashLogPath = "C:/Users/Leon/Desktop/f_001f4b"

        val nodeMap =
            Parser.parseFromSub(url).fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply {
                    println(sub.name)
                    acc[sub.name] = sub
                }
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
