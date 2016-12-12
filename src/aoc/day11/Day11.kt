package aoc.day11

import java.io.File
import java.util.*

/**
 * Created by KoxAlen on 11/12/2016.
 */

data class Floor(val things: List<String>) {
    val isEmpty: Boolean
        get() = things.isEmpty()
    val isValid: Boolean
        get() {
            var (gens, chips) = things.partition { it.startsWith("g") }
            gens = gens.map { it.substring(1) }
            chips = chips.map { it.substring(1) }
            return gens.isEmpty() || chips.all { it in gens }
        }
}

data class State(var steps: Int, var elevator: Int, var floors: List<Floor>) {
    //Equivalent states will produce the same WeakState
    fun toWeakState(): WeakState {
        val elements = floors.flatMap { it.things.map { it.substring(1) } }.distinct()
        val pairs = elements.map {
            element ->
            val chipFloor = floors.indexOfFirst { (things) -> things.contains("c$element") }
            val genFloor = floors.indexOfFirst { (things) -> things.contains("g$element") }
            Pair(chipFloor, genFloor)
        }.sortedBy { it.first+it.second*10 }
        return WeakState(elevator, pairs)
    }

    val currentFloor: Floor
        get() = floors[elevator]

    fun getNextStates(): List<State> {
        return listOf(elevator-1, elevator+1).filter { it >= 0 && it < floors.size }.flatMap {
            moveTo ->
            currentFloor.things.mapNotNull {
                thing ->
                val newFloors = floors.map { it.things }.toTypedArray()
                newFloors[elevator] = newFloors[elevator].filter { it != thing }
                newFloors[moveTo] = newFloors[moveTo] + thing
                val floors = newFloors.map(::Floor)
                if (floors.all(Floor::isValid))
                    State(steps + 1, moveTo, floors)
                else
                    null
            } + currentFloor.things.pairs().mapNotNull {
                (a, b) ->
                val newFloors = floors.map { it.things }.toTypedArray()
                newFloors[elevator] = newFloors[elevator].filter { it != a && it != b }
                newFloors[moveTo] = newFloors[moveTo] + a + b
                val floors = newFloors.map(::Floor)
                if (floors.all(Floor::isValid))
                    State(steps + 1, moveTo, floors)
                else
                    null
            }
        }
    }

    val isTarget: Boolean
        get() = (0..floors.size-2).all { floors[it].isEmpty }
}

private fun <E> List<E>.pairs(): List<Pair<E, E>> {
    return (0..size-1).flatMap { a -> (a+1..size-1).map { b -> Pair(this[a], this[b]) } }
}

data class WeakState(val elevator: Int, var elements: List<Pair<Int, Int>>)

private val reGenerator = Regex("""an? (\w+) generator""")
private val reChip = Regex("""an? (\w+)-compatible microchip""")
fun parseFloor(raw: String): Floor {
    val chips = reChip.findAll(raw).map { "c${it.groupValues[1]}" }.toList()
    val generators = reGenerator.findAll(raw).map { "g${it.groupValues[1]}" }.toList()
    return Floor(chips+generators)
}

fun simulate(initialFloors: List<Floor>): Int {
    val initialState = State(0, 0, initialFloors)
    val queue = ArrayDeque<State>()
    queue.add(initialState)
    val seen = mutableSetOf(initialState.toWeakState())
    //BFS search
    while (queue.isNotEmpty()) {
        val currentState = queue.poll()
        if (currentState.isTarget)
            return currentState.steps
        currentState.getNextStates().filter { it.toWeakState() !in seen }.forEach { queue.add(it); seen.add(it.toWeakState()) }
    }
    return -1
}

fun main(args: Array<String>) {
    require(args.size == 1, { "Pass the input file as argument" })
    val input = File(args[0])
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val initialFloors = input.useLines { it.map(String::trim).map(::parseFloor).toList() }

    val p1 = simulate(initialFloors)
    println("[Part 1] Steps: $p1")

    val floorsP2 = initialFloors.toTypedArray()
    floorsP2[0] = Floor(floorsP2[0].things+"gelerium"+"celerium"+"gdilithium"+"cdilithium")
    val p2 = simulate(floorsP2.toList())
    println("[Part 2] Steps: $p2")
}
