package aoc.day16

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 16/12/2016.
*/

        tailrec fun genNotSoRandomData(seed: String, size: Int): String {
    if (seed.length >= size)
        return seed.substring(0, size)
    val b = seed.reversed().map { when(it) {'1' -> '0' else -> '1'} }.joinToString("")
    return genNotSoRandomData("${seed}0$b", size)
}

private tailrec fun getCheckSum(data: List<Char>): List<Char> {
    val checksum = (0 until data.size step 2).map { if (data[it] == data[it+1]) '1' else '0' }
    if (checksum.size % 2 == 0)
        return getCheckSum(checksum)
    return checksum
}
fun getCheckSum(data: String): String {
    return getCheckSum(data.toList()).joinToString("")
}

fun main(args: Array<String>) {
    val input = "10111100110001111" // Puzzle input

    val p1 = 272 //P1 input
    println("[Part 1] Data checksum: ${getCheckSum(genNotSoRandomData(input, p1))}")

    val p2 = 35651584 //P1 input
    println("[Part 2] Data checksum: ${getCheckSum(genNotSoRandomData(input, p2))}")
}