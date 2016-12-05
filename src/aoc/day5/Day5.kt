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

    val p2 = hashSequence.filter { it.startsWith("00000") }.filter { it[5] >= '0' && it[5] < '8' }.distinctBy { it[5] }.take(8)
            .fold(kotlin.arrayOfNulls<Char>(8)) {
                acc, it ->
                acc[it[5]-'0'] = it[6]
                acc
            }.joinToString(separator = "")
    println("[Part 2] The code is $p2")
}