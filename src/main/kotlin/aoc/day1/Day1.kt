package aoc.day1

import aoc.utils.Utils
import java.io.BufferedReader
import java.io.InputStream
import java.lang.Math.abs

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 01/12/2016.
*/

class GridMovement {
    private enum class Direction {
        NORTH, SOUTH, WEST, EAST;

        fun turn(turn: Turn): Direction {
            return when (this) {
                NORTH -> {
                    when (turn) {
                        Turn.LEFT -> WEST
                        Turn.RIGHT -> EAST
                    }
                }
                SOUTH -> {
                    when (turn) {
                        Turn.LEFT -> EAST
                        Turn.RIGHT -> WEST
                    }
                }
                WEST -> {
                    when (turn) {
                        Turn.LEFT -> SOUTH
                        Turn.RIGHT -> NORTH
                    }
                }
                EAST -> {
                    when (turn) {
                        Turn.LEFT -> NORTH
                        Turn.RIGHT -> SOUTH
                    }
                }
            }
        }
    }
    enum class Turn {
        RIGHT, LEFT
    }
    private var facing = Direction.NORTH
    private val accumulator = arrayOf(0, 0)

    private val visited = mutableSetOf(Pair(0, 0))
    private var HQlocation: Pair<Int, Int>? = null

    fun move(turn: Turn, steps: Int) {
        facing = facing.turn(turn)
        if (HQlocation == null) {
            when (facing) {
                Direction.NORTH -> {
                    HQlocation = checkPath((accumulator[0]..accumulator[0]+steps).drop(1).map { Pair(it, accumulator[1]) })
                }
                Direction.SOUTH -> {
                    HQlocation = checkPath((accumulator[0] downTo accumulator[0]-steps).drop(1).map { Pair(it, accumulator[1]) })
                }
                Direction.WEST -> {
                    HQlocation = checkPath((accumulator[1]..accumulator[1]+steps).drop(1).map { Pair(accumulator[0], it) })
                }
                Direction.EAST -> {
                    HQlocation = checkPath((accumulator[1] downTo accumulator[1]-steps).drop(1).map { Pair(accumulator[0], it) })
                }
            }
        }
        when (facing) {
            Direction.NORTH -> {
                accumulator[0] += steps
            }
            Direction.SOUTH -> {
                accumulator[0] -= steps
            }
            Direction.WEST -> {
                accumulator[1] += steps
            }
            Direction.EAST -> {
                accumulator[1] -= steps
            }
        }
    }

    private fun checkPath(path: List<Pair<Int, Int>>): Pair<Int, Int>? {
        path.forEach {
            if(!visited.add(it))
                return it
        }
        return null
    }

    fun totalDistance(): Int {
        return accumulator[0] + accumulator[1]
    }

    fun distanceToHQ(): Int {
        val location = HQlocation ?: Pair(0,0)
        return abs(location.first) + abs(location.second)
    }
}

class MovementParser(val gridMovement: GridMovement) {
    fun parse(inputStream: InputStream) {
        val reader = inputStream.bufferedReader()
        while(true) {
            when (reader.read()) {
                'R'.toInt() -> {
                    gridMovement.move(GridMovement.Turn.RIGHT, readSteps(reader))
                }
                'L'.toInt() -> {
                    gridMovement.move(GridMovement.Turn.LEFT, readSteps(reader))
                }
                -1 -> {
                    reader.close()
                    return@parse
                }
            }
        }
    }

    private fun readSteps(reader: BufferedReader): Int {
        val acc: StringBuilder = StringBuilder()
        var c = reader.read().toChar()
        while (c.isDigit()) {
            acc.append(c)
            c = reader.read().toChar()
        }
        return acc.toString().toInt()
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(1)
    require(input.exists(), {"${input.path} does not exists"})
    require(input.isFile, {"${input.path} should be a file"})
    val gridMovement = GridMovement()
    val parser = MovementParser(gridMovement)
    parser.parse(input.inputStream())
    println("[Part 1] You are ${gridMovement.totalDistance()} block away")
    println("[Part 2] HQ is ${gridMovement.distanceToHQ()} block away")
}