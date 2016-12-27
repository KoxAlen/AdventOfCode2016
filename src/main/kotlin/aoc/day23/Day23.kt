package aoc.day23

import aoc.utils.Utils

/**
 * Project: AdventOfCode2016
 * Created by KoxAlen on 23/12/2016.
 */

class AssembunnyVM(raw: List<String>) {
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
    private inner class Toggle : Op() {
        override fun invoke(vararg args: Value) {
            val address = pc+args[0].value
            code.getOrNull(address)?.let {
                instruction ->
                code.set(address, when (instruction.op) {
                    is Inc -> instruction.copy(op = Dec())
                    is Dec -> instruction.copy(op = Inc())
                    is Toggle -> instruction.copy(op = Inc())
                    is JumpNotZero -> instruction.copy(op = Copy())
                    is Copy -> instruction.copy(op = JumpNotZero())
                    else -> error("Invalid op")
                })
            }
        }
    }

    private val code: MutableList<Instruction>

    private val opMap = mapOf(
            "cpy" to Copy(),
            "inc" to Inc(),
            "dec" to Dec(),
            "jnz" to JumpNotZero(),
            "tgl" to Toggle()
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
            execute()
        }
    }
}

fun main(args: Array<String>) {
    val input = Utils.getInput(23)
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val vm = AssembunnyVM(input.readLines())

    vm.registers[0] = 7 //Input p1
    vm.run()
    println("[Part 1] Register a: ${vm.registers[0]}")

    //TODO: It only takes ~40s, but maybe apply the multiplication optimization
    val vm2 = AssembunnyVM(input.readLines())
    vm2.registers[0] = 12 //Input p2
    vm2.run()
    println("[Part 2] Register a: ${vm2.registers[0]}")
}