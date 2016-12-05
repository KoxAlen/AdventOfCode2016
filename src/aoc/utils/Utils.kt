package aoc.utils

/**
 * Created by KoxAlen on 05/12/2016.
 */

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHex() : String{
    val retval = StringBuilder(size*2)

    forEach {
        val octet = it.toInt()
        retval.append(HEX_CHARS[octet ushr 4 and 0x0F])
        retval.append(HEX_CHARS[octet and 0x0F])
    }

    return retval.toString()
}