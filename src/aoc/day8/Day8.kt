package aoc.day8

import aoc.utils.Utils

/**
 * Created by KoxAlen on 08/12/2016.
 */

class LittleScreen(private val width: Int, private val height: Int) {
    private val rawScreen = Array(height) { Array(width) { false }}
    val screen: Array<Array<Boolean>>
        get() = rawScreen.copyOf()

    fun rect(A: Int, B: Int): LittleScreen {
        require(A < width)
        require(B < height)
        for (r in 0..B-1)
            for (c in 0..A-1)
                rawScreen[r][c] = true
        return this
    }

    fun rotateRow(A: Int, B: Int): LittleScreen {
        require(A < height)
        (0..width-1).map { rawScreen[A][it] }.forEachIndexed{ i, b -> rawScreen[A][(i+B)%width] = b }
        return this
    }

    fun rotateColumn(A: Int, B: Int): LittleScreen {
        require(A < width)
        (0..height-1).map { rawScreen[it][A] }.forEachIndexed { i, b -> rawScreen[(i+B)%height][A] = b }
        return this
    }

    override fun toString(): String {
        return rawScreen.joinToString(separator = "\n") {it.joinToString(separator = "") { if (it) "\u2588" else "\u2592" }}
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(8)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val lcd = LittleScreen(50, 6)

    input.forEachLine {
        val command = it.trim().split(' ')
        when (command[0]) {
            "rect" -> {
                val ab = command[1].split('x').map(String::toInt)
                lcd.rect(ab[0], ab[1])
            }
            "rotate" -> {
                val A = command[2].split('=')[1].toInt()
                val B = command[4].toInt()
                when (command[1]) {
                    "row" -> lcd.rotateRow(A, B)
                    "column" -> lcd.rotateColumn(A, B)
                }
            }
        }
    }
    println("[Part 1] Lit pixels: ${lcd.screen.fold(0) { acc, it -> acc + it.count { it == true } }}")
    //Eyeball part 2
    println(lcd)
}