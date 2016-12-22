package aoc.day4

import aoc.utils.Utils

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 04/12/2016.
*/

class Room(str: String) {
    val sectorId: Int
    val name: String
    val isValid: Boolean

    private fun shift(c: Char, delta: Int): Char {
        if (c == '-')
            return ' '
        val shift = (c-'a'+delta)%26
        return (shift+'a'.toInt()).toChar()
    }

    init {
        val s = str.split('[')
        val par = s[0].partition(Char::isDigit)
        val encodedName = par.second.dropLast(1)
        val checksum = s[1].dropLast(1)
        val frequency = encodedName.filter { it != '-' }.groupBy { it }.mapValues { it.value.count() }.toSortedMap()
        val calcChecksum = frequency.keys.sortedByDescending { frequency[it] }.take(5).joinToString(separator = "")

        sectorId = par.first.toInt()
        name = encodedName.map { shift(it, sectorId) }.joinToString(separator = "")
        isValid = checksum == calcChecksum
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(4)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val rooms = input.useLines {
        it.map(::Room).filter(Room::isValid).toList()
    }
    println("[Part 1]Sum of sectorIDs = ${rooms.sumBy(Room::sectorId)}")

    val sectorId = rooms.find { it.name == "northpole object storage" }?.sectorId ?: 0
    println("[Part 2] northpole object storage is on sector id $sectorId")
}