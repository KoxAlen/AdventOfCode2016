package aoc.day17

import aoc.utils.toHex
import java.security.MessageDigest
import java.util.*

/**
* Project: AdventOfCode2016
* Created by KoxAlen on 17/12/2016.
*/

data class State(val path: String, val position: Pair<Int, Int>, private val hasher: (String) -> String) {
    private val hash = hasher(path)
    //doors: up, down, left, and right
    val doors = BooleanArray(4) { hash[it] in 'B'..'F' }

    enum class Directions(val dx: Int, val dy: Int) {
        UP(0,-1), DOWN(0,1), LEFT(-1,0), RIGHT(1, 0)
    }
    fun getNextStates(): List<State> {
        return Directions.values()
                .filterIndexed { i, _ -> doors[i] }
                .map { State(path+it.name[0], Pair(position.first+it.dx, position.second+it.dy), hasher) }
                .filter { (_, it) -> it.first in 0..3 && it.second in 0..3 }
    }
}
val getHasher = {
    seed: String ->
    val seed = seed.toByteArray()
    val md5 = MessageDigest.getInstance("MD5");
    { path: String -> md5.update(seed); md5.digest(path.toByteArray()).toHex() }
}

fun main(args: Array<String>) {
    val input = "qljzarfv"
    val target = Pair(3,3)

    val hasher = getHasher(input)
    val initialState = State("", Pair(0, 0), hasher)
    val queue = ArrayDeque<State>()
    queue.add(initialState)
    var found = false
    var last = initialState
    // BFS search
    while (queue.isNotEmpty()) {
        val currentState = queue.poll()
        if (currentState.position == target) {
            if (!found) {
                println("[Part 1] ${currentState.path}")
                found = true
            }
            last = currentState //BFS: so new paths will always be >= that the previous one
        } else
            currentState.getNextStates().forEach { queue.add(it) }
    }
    if (found)
        println("[Part 2] Max path length: ${last.path.length}")
    else
        println("There is not solution for seed: $input")
}
