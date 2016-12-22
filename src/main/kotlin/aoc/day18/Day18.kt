package aoc.day18

import aoc.utils.Utils

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 18/12/2016.
*/
fun main(args: Array<String>) {
    val input = Utils.getInput(18)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val rows = mutableListOf(input.readLines()[0].toCharArray())
    val p1 = 40 //Input p1

    populateRows(rows, p1)
    println("[Part 1] Safe tiles: ${rows.sumBy { it.count { it == '.' } }}")

    val p2 = 400000 //Input p2

    populateRows(rows, p2)
    println("[Part 2] Safe tiles: ${rows.sumBy { it.count { it == '.' } }}")
}

fun populateRows(rows: MutableList<CharArray>, deep: Int) {
    while (rows.size < deep) {
        val pRow = rows.last()
        rows.add(CharArray(pRow.size) { if (pRow.getOrElse(it-1, {'.'}) != pRow.getOrElse(it+1, {'.'})) '^' else '.' })
    }
}
