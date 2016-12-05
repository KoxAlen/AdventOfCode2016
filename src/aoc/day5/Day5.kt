package aoc.day5

import aoc.utils.toHex
import java.security.MessageDigest

/**
 * Created by KoxAlen on 05/12/2016.
 */

class EBDoorHashSequence(val doorId: String) : Sequence<String> {
    override fun iterator(): Iterator<String> {
        return object : Iterator<String> {
            var index = 0
            val md5 = MessageDigest.getInstance("MD5")
            override fun hasNext(): Boolean {
                return true
            }

            override fun next(): String {
                return md5.digest("$doorId${index++}".toByteArray()).toHex()
            }
        }
    }
}

fun main(args: Array<String>) {
    val input = "cxdnnyjw" //Input

    val hashSequence = EBDoorHashSequence(input)
    val p1 = hashSequence.filter { it.startsWith("00000") }.take(8).map { it[5] }.joinToString(separator = "")
    println("[Part 1] The code is $p1")

    val p2 = kotlin.arrayOfNulls<Char>(8)
    for (hash in hashSequence.filter { it.startsWith("00000") }.filter { it[5] >= '0' && it[5] < '8' }) {
        val i = hash[5]-'0'
        if (p2[i] == null)
            p2[i] = hash[6]
        if (p2.all { it != null })
            break
    }
    println("[Part 2] The code is ${p2.joinToString(separator = "")}")
}