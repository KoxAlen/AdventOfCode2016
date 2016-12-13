package aoc.day7

import aoc.utils.Utils
/**
 * Created by KoxAlen on 07/12/2016.
 */

class IPv7(raw: String) {
    val tlsSupport: Boolean
    val sslSupport: Boolean

    init {
        val ip = raw.split('[', ']').foldIndexed(Array(2) { mutableListOf<String>() }) {
            idx, acc, it ->
                acc[idx%2].add(it)
            acc
        }
        val address = ip[0]
        val hypernet = ip[1]

        tlsSupport = address.any { hasABBA(it) } && hypernet.none { hasABBA(it) }

        val ABAs = address.flatMap { getABA(it) }
        val BABs = hypernet.flatMap { getABA(it) }
        sslSupport = ABAs.map { "${it[1]}${it[0]}${it[1]}" }.any { it in BABs }
    }

    private fun getABA(it: String): List<String> {
        return (0..it.length-3)
                .filter { i -> it[i] != it[i+1] && it[i] == it[i+2] }
                .map { i -> it.substring(i, i+3) }
    }

    private fun hasABBA(it: String): Boolean {
        return (0..it.length-4)
                .firstOrNull { i -> it[i] != it[i+1] && (it[i] == it[i+3] && it[i+1] == it[i+2]) } != null
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(7)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val ips = input.useLines {
        it.map(::IPv7).toList()
    }
    println("[Part 1] IPs with TLS support: ${ips.count(IPv7::tlsSupport)}")
    println("[Part 2] IPs with SSL support: ${ips.count(IPv7::sslSupport)}")
}