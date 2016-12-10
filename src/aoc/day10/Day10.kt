package aoc.day10

import java.io.File

/**
 * Created by KoxAlen on 10/12/2016.
 */

class BotController {
    val bots = mutableMapOf<Int, Bot>()
    val ops = mutableMapOf<Int, (Bot) -> Unit>()

    val outputs = mutableMapOf<Int, List<Int>>()

    private val value = Regex("""value (\d+) goes to bot (\d+)""")
    private val op = Regex("""bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""")

    fun parseOp(raw: String) {
        when {
            value.matches(raw) -> {
                val (rawChip, rawBot) = value.find(raw)?.destructured!!
                val botN = rawBot.toInt()
                val chip = rawChip.toInt()
                val bot = bots.getOrDefault(botN, Bot.emptyBot)
                bots[botN] = bot.give(chip)
            }
            op.matches(raw) -> {
                val (rawBotFrom, targetLow, rawLow, targetUp, rawUp) = op.find(raw)?.destructured!!
                val lowId = rawLow.toInt()
                val upId = rawUp.toInt()

                ops[rawBotFrom.toInt()] = {
                    (lower, upper) ->
                    when (targetLow) {
                        "bot" ->  bots[lowId] = bots.getOrDefault(lowId, Bot.emptyBot).give(lower)
                        "output" -> outputs[lowId] = outputs.getOrDefault(lowId, emptyList()) + lower
                    }
                    when (targetUp) {
                        "bot" ->  bots[upId] = bots.getOrDefault(upId, Bot.emptyBot).give(upper)
                        "output" -> outputs[upId] = outputs.getOrDefault(lowId, emptyList()) + upper
                    }
                }
            }
        }
    }

    fun trace(lower: Int, upper: Int): Int {
        while (true) {
            val target = bots.filter { it.value.lower == lower && it.value.upper == upper }
            if (target.isNotEmpty())
                return target.keys.first()
            val botsReady = bots.filter { it.value.isReady }
            if (botsReady.isEmpty())
                break
            botsReady.forEach { id, bot -> ops[id]?.invoke(bot); bots[id] = Bot.emptyBot }
        }
        return -1
    }

    fun run() {
        while (true) {
            val botsReady = bots.filter { it.value.isReady }
            if (botsReady.isEmpty())
                break
            botsReady.forEach { id, bot -> ops[id]?.invoke(bot); bots[id] = Bot.emptyBot }
        }
    }
}

data class Bot(val lower: Int, val upper: Int) {
    companion object {
        val emptyBot = Bot(-1, -1)
    }
    val isReady: Boolean
        get() = lower != -1
    fun give(chip: Int): Bot {
        if (chip > upper)
            return Bot(upper, chip)
        else
            return Bot(chip, upper)
    }
}


fun main(args: Array<String>) {
    require(args.size == 1, { "Pass the input file as argument" })
    val input = File(args[0])
    require(input.exists(), { "${input.path} does not exists" })
    require(input.isFile, { "${input.path} should be a file" })

    val controller = BotController()

    input.forEachLine {
        controller.parseOp(it.trim())
    }

    println("[Part 1] value 61, 17 bot: ${controller.trace(17, 61)}")

    controller.run()
    val p2 = (0..2).map { controller.outputs[it]?.get(0) ?: 1 }.reduce { acc, it ->  acc*it}
    println("[Part 2] multiply together the values of one chip in each of outputs 0, 1, and 2: $p2")
}