package aoc.day9

import java.io.File
import java.io.Reader

/**
 * Created by KoxAlen on 09/12/2016.
 */

private tailrec fun decompressV1(content: List<Char>, count: Long = 0L): Long {
    if (content.isEmpty())
        return count
    if (content[0] == '(') {
        val markerIndex = content.indexOf(')')
        val marker = content.subList(1, markerIndex)
        val index = marker.indexOf('x')
        val len = marker.subList(0, index).joinToString(separator = "").toInt()
        val times = marker.subList(index+1, marker.size).joinToString(separator = "").toInt()
        return decompressV1(content.subList(markerIndex+len+1, content.size), count + (times * len))
    } else {
        return decompressV1(content.subList(1, content.size), count+1)
    }
}

fun decompressV1(input: Reader): Long {
    return input.use {
        decompressV1(it.readText().toList().filterNot(Char::isWhitespace))
    }
}

private tailrec fun decompressV2(content: List<Char>, count: Long = 0L): Long {
    if (content.isEmpty())
        return count
    if (content[0] == '(') {
        val markerIndex = content.indexOf(')')
        val marker = content.subList(1, markerIndex)
        val index = marker.indexOf('x')
        val len = marker.subList(0, index).joinToString(separator = "").toInt()
        val times = marker.subList(index+1, marker.size).joinToString(separator = "").toInt()
        return decompressV2(content.subList(markerIndex+len+1, content.size), count + (times * decompressV2(content.subList(markerIndex+1, markerIndex+len+1))))
    } else {
        return decompressV2(content.subList(1, content.size), count+1)
    }
}

fun decompressV2(input: Reader): Long {
    return input.use {
        decompressV2(it.readText().toList().filterNot(Char::isWhitespace))
    }
}

fun main(args: Array<String>) {
    require(args.size == 1, { "Pass the input file as argument" })
    val input = File(args[0])
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    println("[Part 1] Output length: ${decompressV1(input.bufferedReader())}")
    println("[Part 1] Output length: ${decompressV2(input.bufferedReader())}")
}
