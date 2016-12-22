package aoc.day1

import aoc.utils.Utils
import io.kotlintest.specs.StringSpec
import java.io.ByteArrayInputStream

/**
 * Project: AdventOfCode2016
 * Created by KoxAlen on 22/12/2016.
 */

fun inputStream(src: String) = ByteArrayInputStream(src.toByteArray())

class Day1Test: StringSpec() {
    init {
        "Following R2, L3 leaves 5 blocks away." {
            val grid = GridMovement()
            val parser = MovementParser(grid)
            parser.parse(inputStream("R2, L3"))
            grid.totalDistance() shouldBe 5
        }
        "R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away." {
            val grid = GridMovement()
            val parser = MovementParser(grid)
            parser.parse(inputStream("R2, R2, R2"))
            grid.totalDistance() shouldBe 2
        }
        "R5, L5, R5, R3 leaves you 12 blocks away." {
            val grid = GridMovement()
            val parser = MovementParser(grid)
            parser.parse(inputStream("R5, L5, R5, R3"))
            grid.totalDistance() shouldBe 12
        }
        "R8, R4, R4, R8, the first location you visit twice is 4 blocks away" {
            val grid = GridMovement()
            val parser = MovementParser(grid)
            parser.parse(inputStream("R8, R4, R4, R8"))
            grid.distanceToHQ() shouldBe 4
        }
        "Puzzle: part 1 = 239, part 2 = 141" {
            val input = Utils.getInput(1)
            val gridMovement = GridMovement()
            val parser = MovementParser(gridMovement)
            parser.parse(input.inputStream())
            gridMovement.totalDistance() shouldBe 239
            gridMovement.distanceToHQ() shouldBe 141
        }
    }
}