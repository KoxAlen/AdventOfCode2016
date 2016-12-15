package aoc.day15

import aoc.utils.Utils
import java.time.Duration
import kotlin.system.measureNanoTime

/**
 * Created by KoxAlen on 15/12/2016.
 */

/*
Implementation of the Chinese remainder theorem using extended euclidean algorithm to calculate the multiplicative inverse
 */
fun crt(n: List<Long>, a: List<Long>): Long {
    val N = n.fold(1L) { acc, it -> acc*it}
    val sum = n.foldIndexed(0L) {
        i, acc, ni ->
        val Nni = N / ni
        val multInv = multiplicativeInverse(Nni, ni)
        acc + (a[i] * multInv * Nni)
    }
    return leastPositiveModule(sum,N)
}

fun  leastPositiveModule(a: Long, n: Long): Long {
    return if (a < 0) {
        -(-a%n)+n
    } else a % n
}

fun multiplicativeInverse(a: Long, n: Long): Long {
    var t = 0L
    var nt = 1L
    var r = n
    var nr = a
    while (nr != 0L) {
        val quotient = r / nr
        val ot = t
        t = nt
        nt = ot - quotient * nt
        val or = r
        r = nr
        nr = or - quotient * nr
    }
    if (r > 1) error("$a is not invertible")
    if (t < 0) return t+n
    return t
}

/*
For https://www.reddit.com/r/adventofcode/comments/5ifvyc/2016_day_15_part_3_our_discs_got_larger/
 */
fun main(args: Array<String>) {
    val file = Utils.getInput(15, "UTA")
    val reInput = Regex("""has (\d+) positions; at time=0, it is at position (\d+).""")
    val input = file.useLines { it.mapNotNull { reInput.find(it)?.let { val (n, a) = it.destructured; Pair(n.toLong(), a.toLong()) } }.toList() }
    val n = input.map { it.first }
    val a = input.map { it.second }.mapIndexed { idx, it -> -it-(idx+1) } //Transform from our problem to the generic CRT

    println("Part 1: ${Duration.ofNanos(measureNanoTime { println(crt(n, a)) })}")

    val file2 = Utils.getInput(15, "UTA2")
    val input2 = file2.useLines { it.mapNotNull { reInput.find(it)?.let { val (n, a) = it.destructured; Pair(n.toLong(), a.toLong()) } }.toList() }
    val n2 = input2.map { it.first }
    val a2 = input2.map { it.second }.mapIndexed { idx, it -> -it-(idx+1) } //Transform from our problem to the generic CRT

    println("Part 1: ${Duration.ofNanos(measureNanoTime { println(crt(n2, a2)) })}")
}