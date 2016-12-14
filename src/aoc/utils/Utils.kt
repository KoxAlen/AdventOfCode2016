package aoc.utils

import java.io.File

/**
 * Created by KoxAlen on 05/12/2016.
 */

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
private val LOWER_HEX_CHARS = HEX_CHARS.map(Char::toLowerCase)

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

object Utils {
    fun getInput(day: Int, name: String = "input"): File {
        return File(javaClass.getResource("/aoc/day$day/$name")?.file ?: name)
    }
}