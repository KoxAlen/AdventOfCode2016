package aoc.day14

import aoc.utils.toLowerHex
import java.security.MessageDigest
import java.time.Duration
import kotlin.system.measureNanoTime

/**
 * Created by KoxAlen on 14/12/2016.
 */

private const val HEX_CHARS = "0123456789abcdef"
private fun Char.asHexToInt(): Int {
    return HEX_CHARS.indexOf(this)
}
private val HEX_BYTES = HEX_CHARS.map(Char::toByte)
// This shaves 3 seconds off the second part
private fun ByteArray.asLowerHex(): ByteArray {
    val bytes = ByteArray(size*2)
    for (i in 0..size-1) {
        val n = i*2
        val octet = this[i].toInt()
        bytes[n] = HEX_BYTES[octet ushr 4 and 0x0F]
        bytes[n+1] = HEX_BYTES[octet and 0x0F]
    }
    return bytes
}

/*
    https://www.reddit.com/r/adventofcode/comments/5i9gzj/upping_the_ante_2016_day_14_part_1_widening_the/
 */

private fun defaultHasher(): (ByteArray, Int) -> String {
    val md5 = MessageDigest.getInstance("MD5")
    return {
        seed, index ->
        md5.update(seed)
        md5.digest(index.toString().toByteArray()).toLowerHex()
    }
}
private fun stretchingHasher(times: Int): (ByteArray, Int) -> String {
    val md5 = MessageDigest.getInstance("MD5")
    return {
        seed, index ->
        md5.update(seed)
        md5.update(index.toString().toByteArray())
        for (i in 1..times)
            md5.update(md5.digest().asLowerHex())
        md5.digest().toLowerHex()
    }
}
private fun defaultValidator(index: Int, hash: String, candidates: Array<MutableList<Int>>, found: MutableList<Int>) {
    for (i in 0..27) {
        val c = hash[i]
        if (c == hash[i+1] && c == hash[i+2] && c == hash[i+3] && c == hash[i+4]) {
            val l = candidates[c.asHexToInt()]
            found.addAll(l.filter { val d = index-it; d < 1000 && d > 0  })
            l.clear()
        }
    }
}
private fun sevenCharValidator(index: Int, hash: String, candidates: Array<MutableList<Int>>, found: MutableList<Int>) {
    for (i in 0..25) {
        val c = hash[i]
        if (c == hash[i+1] && c == hash[i+2] && c == hash[i+3] && c == hash[i+4] && c == hash[i+5] && c == hash[i+6]) {
            val l = candidates[c.asHexToInt()]
            found.addAll(l.filter { val d = index-it; d < 100000 && d > 0  })
            l.clear()
        }
    }
}
private fun defaultPromoter(index: Int, hash: String, candidates: Array<MutableList<Int>>) {
    for (i in 0..29) {
        val c = hash[i]
        if (c == hash[i+1] && c == hash[i+2]) {
            candidates[c.asHexToInt()].add(index)
            return
        }
    }
}
private fun fourCharPromoter(index: Int, hash: String, candidates: Array<MutableList<Int>>) {
    for (i in 0..28) {
        val c = hash[i]
        if (c == hash[i+1] && c == hash[i+2] && c == hash[i+3]) {
            candidates[c.asHexToInt()].add(index)
            return
        }
    }
}

fun solve(input: String, goal: Int, hashToExplore: Int,
          hasher: (ByteArray, Int) -> String,
          hashPromoter: (Int, String, Array<MutableList<Int>>) -> Unit,
          hashValidator: (Int, String, Array<MutableList<Int>>, MutableList<Int>) -> Unit ) {
    val seed = input.toByteArray()
    val candidates = Array<MutableList<Int>>(16, { mutableListOf() })
    val found = mutableListOf<Int>()
    var index = -1
    while (found.size < goal) {
        val hash = hasher(seed, ++index)
        hashValidator(index, hash, candidates, found)
        hashPromoter(index, hash, candidates)
    }
    found.sort()
    val tentative = found.elementAt(goal-1)
    val exhaust = candidates.flatMap { it }.filter { it < tentative }.max() ?: 0
    // Make sure there are not lower index hashes yet to validate
    while (index < exhaust+hashToExplore) {
        val hash = hasher(seed, ++index)
        hashValidator(index, hash, candidates, found)
    }
    found.sort()
    println(found.elementAt(goal-1))
}

fun main(args: Array<String>) {
    val original = "ngcjuoqr"
    println("Part 1 original: ${Duration.ofNanos(measureNanoTime { solve(original, 64, 1000, defaultHasher(), ::defaultPromoter, ::defaultValidator) })}")
    println("Part 2 original: ${Duration.ofNanos(measureNanoTime { solve(original, 64, 1000, stretchingHasher(2016), ::defaultPromoter, ::defaultValidator) })}")
    val wideningTheRanges = "yjdafjpo"
    println("Part 1 WideningTheRanges: ${Duration.ofNanos(measureNanoTime { solve(wideningTheRanges, 512, 100000, defaultHasher(), ::fourCharPromoter, ::sevenCharValidator) })}")
}