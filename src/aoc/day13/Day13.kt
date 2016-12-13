package aoc.day13

import java.util.*

/**
 * Created by KoxAlen on 13/12/2016.
 */

class Maze(val designersNumber: Int) {
    fun isWall(position: Pair<Int, Int>) = isWall(position.first, position.second)
    fun isWall(x: Int, y: Int): Boolean {
        val c = (x*x + 3*x + 2*x*y + y + y*y)+designersNumber
        val bitCount = Integer.bitCount(c)
        return bitCount % 2 != 0
    }

    fun toString(maxX: Int, maxY: Int): String {
        val builder = StringBuilder()
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                builder.append(if (isWall(x,y)) '#' else '.')
            }
            builder.append('\n')
        }
        return builder.toString()
    }

    data class State(val steps: Int, val position: Pair<Int, Int>) {
        fun getNextStates(): List<State> {
            return listOf(Pair(1,0), Pair(-1,0), Pair(0,1), Pair(0,-1)).map {
                (dX, dY) ->
                State(steps+1, Pair(position.first+dX, position.second+dY))
            }.filter { it.position.first >= 0 && it.position.second >= 0 }
        }
    }

    fun findPath(from: Pair<Int, Int>, to: Pair<Int, Int>): Int {
        val initialState = State(0, from)
        val queue = ArrayDeque<State>()
        queue.add(initialState)
        val seen = mutableSetOf(initialState.position)
        //BFS Search
        while (queue.isNotEmpty()) {
            val currentState = queue.poll()
            if (currentState.position == to)
                return currentState.steps
            currentState.getNextStates()
                    .filterNot { isWall(it.position) }
                    .filter { it.position !in seen }.forEach { queue.add(it); seen.add(it.position) }
        }
        return -1
    }

    fun flood(from: Pair<Int, Int>, maxSteps: Int): Int {
        val initialState = State(0, from)
        val queue = ArrayDeque<State>()
        queue.add(initialState)
        val seen = mutableSetOf(initialState.position)
        //BFS Search
        while (queue.isNotEmpty()) {
            val currentState = queue.poll()
            currentState.getNextStates()
                    .filterNot { isWall(it.position) }
                    .filter { it.steps <= maxSteps }
                    .filter { it.position !in seen }.forEach { queue.add(it); seen.add(it.position) }
        }
        return seen.size
    }
}

fun main(args: Array<String>) {
    val input = 1350 //Puzzle input

    val maze = Maze(input)

    val p1 = maze.findPath(Pair(1,1), Pair(31,39))
    println("[Part 1] Steps to 31,39: $p1")

    val p2 = maze.flood(Pair(1,1), 50)
    println("[Part 2] Reachable coordinates: $p2")
}