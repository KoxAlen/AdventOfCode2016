package aoc.day20

import aoc.utils.Utils

/**
 * Created by KoxAlen on 20/12/2016.
 */

fun main(args: Array<String>) {
    val input = Utils.getInput(20)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val ipBlacklist = input.useLines {
        it.map { val s = it.split('-'); s[0].toLong().rangeTo(s[1].toLong()) }.sortedBy { it.start }.toList()
    }

    val openIPs = mutableListOf<LongRange>()
    var low = 0L
    ipBlacklist.forEach {
        range ->
        if (low < range.start)
            openIPs.add(low.until(range.start))
        if (low < range.endInclusive)
            low = range.endInclusive+1
    }
    openIPs.add(low.rangeTo(4294967295))

    println("[Part 1] Lowest open ip: ${openIPs.minBy { it.start }?.start}")
    println("[Part 2] Open ips: ${openIPs.sumBy { it.count() }}")
}