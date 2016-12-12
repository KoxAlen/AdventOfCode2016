package aoc.day12

import java.io.File

/**
 * Created by KoxAlen on 12/12/2016.
 */

class AssembunnyVM(raw: List<String>) {
    val registers = IntArray(4)
    private var pc = 0
    private typealias Op = (Value, Value) -> Unit
    private abstract class Value {
        abstract val value: Int
        abstract val register: Int
    }
    private class Literal(override val value: Int) : Value() {
        override val register: Int
            get() = error("Wrong value passed to op")
    }
    private inner class Register(override val register: Int) : Value() {
        override val value: Int
            get() = this@AssembunnyVM.registers[register]
    }
    private data class Instruction(val op: Op, val arg1: Value, val arg2: Value) {
        fun execute() {
            op(arg1, arg2)
        }
    }

    private val code: List<Instruction>

    private val opMap = mapOf<String, Op>(
            "cpy" to { x, y -> registers[y.register] = x.value },
            "inc" to { x, _ -> registers[x.register]++ },
            "dec" to { x, _ -> registers[x.register]-- },
            "jnz" to { x, y -> if (x.value != 0) pc += y.value-1 }
    )

    init {
        code = raw.map { it.trim().split(' ') }.map { Instruction(opMap[it[0]]!!, parse(it.getOrNull(1)), parse(it.getOrNull(2))) }
    }

    private fun execute() {
        code[pc].execute()
        pc++
    }

    private fun parse(arg: String?): Value {
        return arg?.let {
            try {
                Literal(arg.toInt())
            } catch (e: NumberFormatException) {
                val c = arg[0] - 'a'
                Register(c)
            }
        } ?: Register(-1) // This rise and exception if used
    }

    fun run() {
        while (pc < code.size) {
            execute()
        }
    }

    fun reset() {
        pc = 0
        for (n in 0..registers.size-1)
            registers[n] = 0
    }
}

fun main(args: Array<String>) {
    require(args.size == 1, { "Pass the input file as argument" })
    val input = File(args[0])
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val vm = AssembunnyVM(input.readLines())

    vm.run()
    println("[Part 1] Register a: ${vm.registers[0]}")

    vm.reset()
    vm.registers[2] = 1
    vm.run()
    println("[Part 2] Register a: ${vm.registers[0]}")
}