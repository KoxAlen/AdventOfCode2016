package aoc.day2

import aoc.utils.Utils

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 02/12/2016.
*/

class Keypad(private val keyLayout: Array<Array<*>>, private var position: Pair<Int, Int>) {
    private val codeBuffer = StringBuilder()
    val code:String
        get() = codeBuffer.toString()

    fun move(commands: String) {
        var (x, y) = position
        commands.forEach {
            when (it.toUpperCase()) {
                'U' -> {
                    val nx = (x-1).coerceAtLeast(0)
                    if (keyLayout[nx][y] != null)
                        x = nx
                }
                'D' -> {
                    val nx = (x+1).coerceAtMost(keyLayout.size-1)
                    if (keyLayout[nx][y] != null)
                        x = nx
                }
                'L' -> {
                    val ny = (y-1).coerceAtLeast(0)
                    if (keyLayout[x][ny] != null)
                        y = ny
                }
                'R' -> {
                    val ny = (y+1).coerceAtMost(keyLayout[x].size-1)
                    if (keyLayout[x][ny] != null)
                        y = ny
                }
            }
        }
        position = Pair(x,y)
        codeBuffer.append(getKey(position))
    }

    private fun getKey(position: Pair<Int, Int>): Any {
        return keyLayout[position.first][position.second] ?: "*"
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(2)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val keyPadNormal = Keypad(arrayOf(
            arrayOf(1, 2, 3),
            arrayOf(4, 5, 6),
            arrayOf(7, 8, 9)
    ), Pair(1,1))

    val keyPadBathroomPremium = Keypad(arrayOf(
            arrayOf(null, null,   1, null,  null),
            arrayOf(null,    2,   3,    4,  null),
            arrayOf(   5,    6,   7,    8,     9),
            arrayOf(null,   'A', 'B',  'C', null),
            arrayOf(null, null,  'D', null, null)
    ), Pair(2,0))

    input.forEachLine {
        keyPadNormal.move(it.trim())
        keyPadBathroomPremium.move(it.trim())
    }

    println("{Part 1] The code is ${keyPadNormal.code}")
    println("{Part 2] The code is ${keyPadBathroomPremium.code}")
}