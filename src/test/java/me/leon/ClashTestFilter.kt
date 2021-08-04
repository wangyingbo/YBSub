package me.leon

import me.leon.domain.ClashConnectLog
import me.leon.support.*
import org.junit.jupiter.api.Test

class ClashTestFilter {

    companion object {
        //        tr  https://sub.cm/LtHIdxd
        //        ssr https://sub.cm/TTgunAH
        //        v2 https://sub.cm/w8HCJno
        //        ss https://sub.cm/FLJ17fi
        const val URL = "https://sub.cm/7lWFj2u"
    }

    @Test
    fun parseClashLog() {
        // clash_win/Cache 目录下日志文件
        val clashLogPath = "C:/Users/Leon/Desktop/f_0022c6"
        val nodeMap =
            Parser.parseFromSub(URL).fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply {
                    println(sub.name)
                    acc[sub.name] = sub
                }
            }

        clashLogPath
            .readText()
            .fromJson<ClashConnectLog>()
            .proxies
            .filter { it.value.hasSpeedTestHistory }
            .also {
                it.forEach { (t, u) -> println("$t  ${u.history.last().delay}") }
                println()
            }
            .filter { nodeMap[it.key] != null }
            .map { nodeMap[it.key] }
            .also { println(it.joinToString("\n") { it!!.toUri() }) }
            .forEach { println(it!!.name) }
    }

    @Test
    fun speedTestResultParse() {
        val map =
            Parser.parseFromSub(URL).fold(mutableMapOf<String, Sub>()) { acc, sub ->
                acc.apply {
                    //                    println(sub.name)
                    acc[sub.name] = sub
                }
            }
        println(map)
        SPEED_TEST_RESULT
            .readLines()
            .asSequence()
            .distinct()
            .map { it.substringBeforeLast('|') to it.substringAfterLast('|') }
            .sortedByDescending { it.second.replace("Mb|MB".toRegex(), "").toFloat() }
            .filter { map[it.first] != null }
            .groupBy { map[it.first]!!.javaClass }
            .forEach { (t, u) ->
                val data =
                    u
                        .joinToString("\n") {
                            map[it.first]!!
                                .apply {
                                    name =
                                        name.replace(NodeCrawler.REG_AD, "")
                                            .removeFlags()
                                            .substringBeforeLast('|') + "|" + it.second
                                }
                                //                                .also { println(it.name) }
                                .toUri()
                        }
                        .b64Encode()

                println(u.size)
                println(data)
            }
    }
}
