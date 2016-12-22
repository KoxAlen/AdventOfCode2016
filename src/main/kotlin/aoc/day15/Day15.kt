package aoc.day15

import aoc.utils.Utils

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 15/12/2016.
*/

data class Disc(val max: Int, var current: Int) {
    companion object {
        private val reDisc = Regex("""has (\d+) positions; at time=0, it is at position (\d+)""")
        fun parseDisc(raw: String): Disc? {
            return reDisc.find(raw)?.let {
                val (max, current) = it.destructured
                Disc(max.toInt(), current.toInt())
            }
        }
    }
    val atSlot: Boolean
        get() = current == 0
    fun tick() {
        current = (current+1)%max
    }
}

fun simulate(discs: List<Disc>): Long {
    discs.forEachIndexed { i, disc -> (0..i).forEach { disc.tick() } }
    generateSequence(0L, Long::inc).forEach {
        if (discs.all(Disc::atSlot))
            return it
        discs.forEach(Disc::tick)
    }
    return -1
}

fun main(args: Array<String>) {
    val input = Utils.getInput(15)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val discs = input.useLines { it.map { Disc.parseDisc(it.trim()) }.filterNotNull().toList() }

    val p1Discs = discs.map { it.copy() }
    println("[Part 1] time to drop: ${simulate(p1Discs)}")

    val p2Discs = discs.map { it.copy() } + Disc(11, 0) //Part 2 input
    println("[Part 1] time to drop: ${simulate(p2Discs)}")
}