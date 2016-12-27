package aoc.day24

import aoc.utils.Utils
import aoc.utils.permutations
import java.util.*

/**
 * Project: AdventOfCode2016
 * Created by KoxAlen on 27/12/2016.
 */



fun main(args: Array<String>) {
    val input = Utils.getInput(24)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })
    val map = input.readLines().map(String::toCharArray).toTypedArray()

    val mapPoints = map.withIndex().flatMap { (x, l) -> l.withIndex().filter { it.value.isDigit() }.map { (y, v) -> Pair(v-'0', Pair(x, y)) } }
    val distances = calculateDistances(map, mapPoints)

    val locations =  mapPoints.map { it.first }
    val initial = locations.find { it == 0 }!!
    val toVisit = locations - initial

    var best = Int.MAX_VALUE
    for (permutation in toVisit.permutations()) {
        best = best.coerceAtMost(solve(distances, listOf(initial)+permutation))
    }
    println("[Part 1] Distance: $best")

    best = Int.MAX_VALUE
    for (permutation in toVisit.permutations()) {
        best = best.coerceAtMost(solve(distances, listOf(initial)+permutation+initial))
    }
    println("[Part 2] Distance: $best")
}

fun solve(distances: Array<IntArray>, path: List<Int>): Int {
    return path.drop(1).fold(Pair(path[0], 0)) {
        (from, dist), to ->
        Pair(to, dist+distances[from][to])
    }.second
}

fun calculateDistances(map: Array<CharArray>, locations: List<Pair<Int, Pair<Int, Int>>>): Array<IntArray> {
    val distances = Array(locations.size) { IntArray(locations.size) }
    locations.parallelStream().forEach {
        (c, from) ->
        val queue = ArrayDeque<Pair<Int,Pair<Int,Int>>>()
        queue.add(Pair(0, from))
        val seen = mutableSetOf(from)
        while (queue.isNotEmpty()) {
            val (steps, current) = queue.poll()
            map[current.first][current.second].let {
                if (it.isDigit()) {
                    distances[c][it - '0'] = steps
                }
            }
            listOf(Pair(1,0), Pair(-1,0), Pair(0,1), Pair(0,-1)) //Movements
                    .map { (dx,dy) -> Pair(current.first+dx, current.second+dy) } //Possible positions
                    .filter { (x,y) -> map[x][y] != '#' } //Not In wall
                    .filter { it !in seen } //Prune repeated positions
                    .forEach { queue.add(Pair(steps+1, it)); seen.add(it) }
        }
    }
    return distances
}
