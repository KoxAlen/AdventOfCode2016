package aoc.day6

import aoc.utils.Utils

/**
 * Created by KoxAlen on 06/12/2016.
 */

fun main(args: Array<String>) {
    val input = Utils.getInput(6)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val frequencies = input.useLines {
        it.fold(mutableMapOf<Int, MutableList<Char>>()) {
            acc, it ->
            it.forEachIndexed { i, c -> acc.getOrPut(i, { mutableListOf() }).add(c) }
            acc
        }.toSortedMap().mapValues { it.value.groupBy { it }.mapValues { it.value.size } }
    }

    val m1 = frequencies.map { it.value.maxBy { it.value }!!.key }.joinToString(separator = "")
    println("[Part 1] Message: $m1")

    val m2 = frequencies.map { it.value.minBy { it.value }!!.key }.joinToString(separator = "")
    println("[Part 2] Message: $m2")
}