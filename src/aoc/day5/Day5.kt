package aoc.day5

import aoc.utils.toHex
import java.security.MessageDigest

/**
 * Created by KoxAlen on 05/12/2016.
 */

fun main(args: Array<String>) {
    val input = "cxdnnyjw" //Input

    val md5 = MessageDigest.getInstance("MD5")
    val hashSequence = generateSequence(0, Int::inc).map { md5.digest("$input$it".toByteArray()).toHex() }

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