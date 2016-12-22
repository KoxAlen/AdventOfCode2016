package aoc.day22

import aoc.utils.Utils

/**
 * Created by KoxAlen on 22/12/2016.
 */

data class Disk(val x: Int, val y: Int, val size: Int, var used: Int) {
    val avail: Int
        get() = size-used
    val isEmpty: Boolean
        get() = used == 0

    companion object {
        private val reDisk = Regex("""/dev/grid/node-x(\d+)-y(\d+)\s+(\d+)T\s+(\d+)T\s+\d+T\s+\d+%""")
        fun parse(raw: String): Disk? {
            return reDisk.find(raw)?.let {
                val (x, y, size, used) = it.destructured
                Disk(x.toInt(), y.toInt(), size.toInt(), used.toInt())
            }
        }
    }
}

private fun <E> List<E>.pairs(): List<Pair<E, E>> {
    return (0..size-1).flatMap { a -> (a+1..size-1).flatMap { b -> listOf(Pair(this[a], this[b]), Pair(this[b], this[a])) } }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(22)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val diskGrid = mutableListOf<MutableList<Disk>>()
    input.forEachLine {
        Disk.parse(it.trim())?.let {
            disk ->
            val row = diskGrid.getOrNull(disk.x) ?: mutableListOf<Disk>()
            if (row.isEmpty())
                diskGrid.add(row)
            row.add(disk)
        }
    }

    val viablePairs = diskGrid.flatMap { it }.pairs().filter { (a,b) -> !a.isEmpty && a.used <= b.avail }
    println("[Part 1] Viable pairs: ${viablePairs.size}")

    val maxX = diskGrid.size - 1
//    Pretty print the grid, initial p2 solution doing manually from this
//    println(diskGrid.joinToString("\n") { it.joinToString("") {
//        if (it.isEmpty) " _ "
//        else if (it.x == 0 && it.y == 0) "(.)"
//        else if (it.x == maxX && it.y == 0) " G "
//        else " . "
//    }})

    /*
    The following algorithm is derived from the "manual" solution of the grid print
    it follows the same idea gave by the Part 2 example
     */
    val emptyDisk = diskGrid.flatMap { it }.filter(Disk::isEmpty)
    require(emptyDisk.size == 1) { "The following algorithm requires one, and only one 'hole' to move around" }
    val (holeX, holeY) = emptyDisk[0]
    val solution = holeX+holeY+maxX+(maxX-1)*5
    println("[Part 2] Moves: $solution")
}