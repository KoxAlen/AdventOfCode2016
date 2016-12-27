package aoc.utils

import java.io.File

/**
* Created by KoxAlen on 05/12/2016.
*/

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
private val LOWER_HEX_CHARS = "0123456789abcdef".toCharArray()

fun ByteArray.toHex(): String{
    val retval = StringBuilder(size*2)

    forEach {
        val octet = it.toInt()
        retval.append(HEX_CHARS[octet ushr 4 and 0x0F])
        retval.append(HEX_CHARS[octet and 0x0F])
    }

    return retval.toString()
}

fun ByteArray.toLowerHex(): String{
    val retval = StringBuilder(size*2)

    forEach {
        val octet = it.toInt()
        retval.append(LOWER_HEX_CHARS[octet ushr 4 and 0x0F])
        retval.append(LOWER_HEX_CHARS[octet and 0x0F])
    }

    return retval.toString()
}

fun <T> List<T>.permutations(groupSize: Int = 0): Iterable<List<T>> {
    val r = if (groupSize < 1) this.size else groupSize
    return object : Iterable<List<T>> {
        override fun iterator(): Iterator<List<T>>{
            return object : Iterator<List<T>> {
                val pool = this@permutations
                val indices = IntArray(pool.size) { it }
                val cycles = IntArray(r) { pool.size - it }
                var next: Array<Any?> = kotlin.arrayOfNulls(r)
                var done = false

                override fun hasNext(): Boolean = !done

                override fun next(): List<T> {
                    if (next.any { it == null }) {
                        for (i in 0..r - 1) {
                            val index = indices[i]
                            next[i] = pool[index]
                        }
                    } else {
                        for (i in (r - 1) downTo 0) {
                            cycles[i]--
                            if (cycles[i] == 0) {
                                val index = indices[i]
                                for (j in i..pool.size - 2)
                                    indices[j] = indices[j + 1]
                                indices[pool.size - 1] = index
                                cycles[i] = pool.size - i
                            } else {
                                val cycle = cycles[i]
                                val tmp = indices[i]
                                indices[i] = indices[pool.size - cycle]
                                indices[pool.size - cycle] = tmp
                                for (k in i..r - 1) {
                                    val index = indices[k]
                                    next[k] = pool[index]
                                }
                                break
                            }
                            if (i == 0)
                                done = true
                        }
                    }
                    @Suppress("UNCHECKED_CAST")
                    return next.toList() as List<T>
                }
            }
        }
    }
}
object Utils {
    fun getInput(day: Int, name: String = "input"): File {
        return File(javaClass.getResource("/aoc/day$day/$name")?.file ?: name)
    }
}