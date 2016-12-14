package aoc.day14

import aoc.utils.toLowerHex
import java.security.MessageDigest

/**
 * Created by KoxAlen on 14/12/2016.
 */


abstract class HashGeneratorFactory {
    object Builder {
        fun getHashFactory(input: String, stretchTimes: Int = 0, cacheSize: Int = 1000): HashGeneratorFactory {
            return object : HashGeneratorFactory() {
                private val cache = object : LinkedHashMap<Int, String>(cacheSize+10, 1F, true) {
                    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Int, String>?): Boolean = size > cacheSize
                }
                val md5 = MessageDigest.getInstance("MD5")
                val seed = input.toByteArray()
                override fun getHashGenerator(index: Int): Sequence<Pair<Int, String>> {
                    return generateSequence(index, Int::inc).map {
                        Pair(it, cache.getOrPut(it) {
                            md5.update(seed)
                            md5.update(it.toString().toByteArray())
                            if (stretchTimes > 0)
                                for (i in 1..stretchTimes)
                                    md5.update(md5.digest().toLowerHex().toByteArray())
                            md5.digest().toLowerHex()
                        })
                    }
                }
            }
        }
    }
    abstract fun getHashGenerator(index: Int = 0): Sequence<Pair<Int, String>>
}

fun main(args: Array<String>) {
    val input = "ngcjuoqr" //Puzzle input
    val hashPhases = 2016 //Part 2 input

    val hashFactory = HashGeneratorFactory.Builder.getHashFactory(input)
    val p1 = hashFactory.getHashGenerator().filter {
        (i, it) ->
        val c = (0..it.length-3).asSequence().map { i -> it.substring(i,i+3)  }.firstOrNull { it[0] == it[1] && it[0] == it[2] }?.get(0)
        if (c != null)
            hashFactory.getHashGenerator(i + 1).take(1000).firstOrNull { (_, it) -> it.contains("$c$c$c$c$c") } != null
        else
            false
    }.take(64).last().first
    println("[Part 1] Index of last hash: $p1")

    val hashFactory2 = HashGeneratorFactory.Builder.getHashFactory(input, hashPhases)
    val p2 = hashFactory2.getHashGenerator().filter {
        (i, it) ->
        val c = (0..it.length-3).asSequence().map { i -> it.substring(i,i+3)  }.firstOrNull { it[0] == it[1] && it[0] == it[2] }?.get(0)
        if (c != null)
            hashFactory2.getHashGenerator(i + 1).take(1000).firstOrNull { (_, it) -> it.contains("$c$c$c$c$c") } != null
        else
            false
    }.take(64).last().first
    println("[Part 2] Index of last hash: $p2")
}