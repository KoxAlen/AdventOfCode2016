package aoc.day21

import aoc.utils.Utils
import java.util.*

/**
 * Created by KoxAlen on 21/12/2016.
 */

abstract class Instruction {
    data class SwapByPosition internal constructor(val from: Int, val to: Int) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            input.swap(from, to)
        }

        override fun reverse(): Instruction {
            return copy()
        }
    }

    data class SwapByChar internal constructor(val from: Char, val to: Char) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            input.swap(input.indexOf(from), input.indexOf(to))
        }

        override fun reverse(): Instruction {
            return copy()
        }
    }

    internal enum class Direction {
        LEFT {
            override fun opposite() = RIGHT
        },
        RIGHT {
            override fun opposite() = LEFT
        };
        abstract fun opposite(): Direction
        companion object {
            fun parse(raw: String): Direction {
                return when (raw) {
                    "left" -> LEFT
                    "right" -> RIGHT
                    else -> error("$raw is not a valid direction")
                }
            }
        }
    }
    data class RotateByPosition internal constructor(val direction: Direction, val distance: Int) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            when (direction) {
                Direction.LEFT -> input.rotateLeft(distance)
                Direction.RIGHT -> input.rotateRight(distance)
            }
        }

        override fun reverse(): Instruction {
            return copy(direction = direction.opposite())
        }
    }

    data class RotateByChar internal constructor(val char: Char) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            val steps = input.indexOf(char) +1
            input.rotateRight(if (steps > 4) steps+1 else steps)
        }

        override fun reverse(): Instruction {
            return object : Instruction() {
                override fun invoke(input: MutableList<Char>) {
                    require(input.size == 8) {"RotateByChar reverse only works for input size of 8"}
                    val indexOf = input.indexOf(char)
                    /*
                        pos shift newpos
                        0   1     1
                        1   2     3
                        2   3     5
                        3   4     7
                        4   6     2
                        5   7     4
                        6   8     6
                        7   9     0
                        You can also iterate all possibles rotations of input and look for it, but
                        for some input sizes the solution is not unique (it is for 8 thought).
                     */
                    input.rotateLeft(intArrayOf(9, 1, 6, 2, 7, 3, 8, 4)[indexOf])
                }

                override fun reverse(): Instruction {
                    return this@RotateByChar.copy()
                }
            }
        }
    }

    data class Reverse internal constructor(val from: Int, val to: Int) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            input.subList(from, to+1).reverse()
        }

        override fun reverse(): Instruction {
            return copy()
        }

    }

    data class Move internal constructor(val from: Int, val to: Int) : Instruction() {
        override fun invoke(input: MutableList<Char>) {
            input.add(to, input.removeAt(from))
        }

        override fun reverse(): Instruction {
            return copy(to, from)
        }
    }

    /* exposed methods */
    abstract operator fun invoke(input: MutableList<Char>)
    abstract fun reverse(): Instruction

    /* Constructor */
    companion object {
        private val reSwapPos    = Regex("""swap position (\d+) with position (\d+)""")
        private val reSwapChar   = Regex("""swap letter (\p{Alpha}) with letter (\p{Alpha})""")
        private val reRotate     = Regex("""rotate (left|right) (\d+) step[s]?""")
        private val reRotateChar = Regex("""rotate based on position of letter (\p{Alpha})""")
        private val reReverse    = Regex("""reverse positions (\d+) through (\d+)""")
        private val reMove       = Regex("""move position (\d+) to position (\d+)""")
        fun parse(raw: String): Instruction {
            return when {
                reSwapPos.matches(raw) -> {
                    val (from, to) = reSwapPos.find(raw)!!.groupValues.let { Pair(it[1].toInt(), it[2].toInt()) }
                    SwapByPosition(from, to)
                }
                reSwapChar.matches(raw) -> {
                    val (from, to) = reSwapChar.find(raw)!!.groupValues.let { Pair(it[1][0], it[2][0]) }
                    SwapByChar(from, to)
                }
                reRotate.matches(raw) -> {
                    val (direction, steps) = reRotate.find(raw)!!.groupValues.let { Pair(Direction.parse(it[1]), it[2].toInt()) }
                    RotateByPosition(direction, steps)
                }
                reRotateChar.matches(raw) -> {
                    RotateByChar(reRotateChar.find(raw)!!.groupValues[1][0])
                }
                reReverse.matches(raw) -> {
                    val (from, to) = reReverse.find(raw)!!.groupValues.let { Pair(it[1].toInt(), it[2].toInt()) }
                    Reverse(from, to)
                }
                reMove.matches(raw) -> {
                    val (from, to) = reMove.find(raw)!!.groupValues.let { Pair(it[1].toInt(), it[2].toInt()) }
                    Move(from, to)
                }
                else -> error("$raw is not a valid instruction")
            }
        }
    }
}

fun <T> MutableList<T>.swap(i: Int, j: Int): MutableList<T> {
    Collections.swap(this, i, j)
    return this
}
fun <T> MutableList<T>.rotateRight(distance: Int): MutableList<T> {
    Collections.rotate(this, distance)
    return this
}
fun <T> MutableList<T>.rotateLeft(distance: Int): MutableList<T> {
    return this.rotateRight(-1*distance)
}

fun apply(instructions: List<Instruction>, input: String): String {
    return instructions.fold(input.toMutableList()) { input, instruction -> instruction(input); input }.joinToString("")
}

fun main(args: Array<String>) {
    val input = Utils.getInput(21)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val instructions = input.useLines { it.map { Instruction.parse(it.trim()) }.toList() }

    val p1 = "abcdefgh" //Input for part 1
    println("[Part 1] '$p1' -{scramble}-> '${apply(instructions, p1)}'")
    val p2 = "fbgdceah" //Input for part 2
    println("[Part 2] '$p2' -{reverse}-> '${apply(instructions.reversed().map { it.reverse() }, p2)}'")
}