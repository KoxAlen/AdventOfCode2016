package aoc.day25

import aoc.utils.Utils

/**
 * Project: AdventOfCode2016
 * Created by KoxAlen on 27/12/2016.
 */

class AssembunnyVM(raw: List<String>) {
    class BadClock : Throwable()

    val registers = IntArray(4)
    private var pc = 0
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

    private abstract class Op {
        abstract operator fun invoke(vararg args: Value)
    }
    private inner class Copy : Op() {
        override fun invoke(vararg args: Value) {
            registers[args[1].register] = args[0].value
        }
    }
    private inner class Inc : Op() {
        override fun invoke(vararg args: Value) {
            registers[args[0].register]++
        }
    }
    private inner class Dec : Op() {
        override fun invoke(vararg args: Value) {
            registers[args[0].register]--
        }
    }
    private inner class JumpNotZero : Op() {
        override fun invoke(vararg args: Value) {
            if (args[0].value != 0) pc += args[1].value-1
        }
    }
    var clock = generateSequence(0) { when (it) { 0 -> 1 else -> 0 } }.iterator()
    private inner class Out : Op() {
        override fun invoke(vararg args: Value) {
            if (args[0].value != clock.next())
                throw BadClock()
        }
    }

    private val code: MutableList<Instruction>

    private val opMap = mapOf(
            "cpy" to Copy(),
            "inc" to Inc(),
            "dec" to Dec(),
            "jnz" to JumpNotZero(),
            "out" to Out()
    )

    init {
        code = raw.map { it.trim().split(' ') }.map { Instruction(opMap[it[0]]!!, parse(it.getOrNull(1)), parse(it.getOrNull(2))) }.toMutableList()
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
            try {
                execute()
            } catch(e: BadClock) {
                println("Error in the output")
                break
            }
        }
    }
    fun reset() {
        clock = generateSequence(0) { when (it) { 0 -> 1 else -> 0 } }.iterator()
        pc = 0
        for (n in 0..registers.size-1)
            registers[n] = 0
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(25)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val vm = AssembunnyVM(input.readLines())

    generateSequence(0,Int::inc).forEach {
        vm.reset()
        vm.registers[0] = it
        print("Runing vm for a=$it... ")
        vm.run()
    }
}