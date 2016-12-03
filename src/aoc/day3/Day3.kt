package aoc.day3

import java.io.File

/**
 * Created by KoxAlen on 03/12/2016.
 */

fun validTriangle(sides: List<Int>): Boolean {
    return sides[0]+sides[1] > sides[2]
}

fun main(args: Array<String>) {
    assert(args.size == 1, { "Pass the input file as argument" })
    val input = File(args[0])
    assert(input.exists(), { "${input.path} does not exists" })
    assert(input.isFile, { "${input.path} should be a file" })
    val p1 = input.useLines {
        it.map { it.split(' ').filter(String::isNotBlank).map(String::toInt).sorted() }.count(::validTriangle)
    }
    println("[Part 1] There are $p1 valid triangles")

    val input2 = File(args[0]).bufferedReader()
    val p2 = takeTriangles(input2.lineSequence()).count(::validTriangle)
    input2.close()
    println("[Part 2] There are $p2 valid triangles")
}

fun  takeTriangles(sequence: Sequence<String>): Sequence<List<Int>> {
    return object : Sequence<List<Int>> {
        override fun iterator(): Iterator<List<Int>> {
            return object : Iterator<List<Int>> {
                val sourceIterator = sequence.iterator()
                val buffer = arrayOfNulls<List<Int>>(3)
                var index = 3
                override fun next(): List<Int> {
                    if(index >= 3) {
                        buffer[0] = sourceIterator.next().split(' ').filter(String::isNotBlank).map(String::toInt)
                        buffer[1] = sourceIterator.next().split(' ').filter(String::isNotBlank).map(String::toInt)
                        buffer[2] = sourceIterator.next().split(' ').filter(String::isNotBlank).map(String::toInt)
                        index = 0
                    }
                    val retval = listOf(buffer[0]!![index], buffer[1]!![index], buffer[2]!![index])
                    index++
                    return retval.sorted()
                }

                override fun hasNext(): Boolean {
                    return index < 3 || sourceIterator.hasNext()
                }
            }
        }
    }
}
