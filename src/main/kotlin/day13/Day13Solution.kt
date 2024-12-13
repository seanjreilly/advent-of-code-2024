package day13

import utils.LongSolution

fun main() = Day13Solution().run()
class Day13Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return parseClawMachines(input)
            .map { machine -> machine.findWaysToWin().sortedBy { it.cost } }
            .filter { it.isNotEmpty() }
            .map { it.first() }
            .sumOf { it.cost.toLong() }
    }

    override fun part2(input: List<String>) = 0L
}

internal fun parseClawMachines(input: List<String>): List<ClawMachine> {
    var list = input
    var result = mutableListOf<ClawMachine>()
    while (list.isNotEmpty()) {
        val (a,b,c) = list
        result += ClawMachine(a,b,c)
        list = list.dropWhile { it.isNotBlank() }.drop(1)
    }
    return result
}

internal data class ClawMachine(val prize: Prize, val buttonA: Button, val buttonB: Button) {

    fun findWaysToWin() : Collection<WayToWin> {
        return (0 until 100)
            .map { aPresses ->
                val xValue = buttonA.dX * aPresses
                val yValue = buttonA.dY * aPresses

                if (xValue > prize.x) {
                    return@map null
                }
                if (yValue > prize.y) {
                    return@map null
                }

                val remainingX = prize.x - xValue
                val remainingY = prize.y - yValue

                if (remainingX % buttonB.dX != 0) {
                    return@map null
                }

                val bPresses = remainingX / buttonB.dX

                //validate
                if (buttonB.dY * bPresses != remainingY) {
                    return@map null
                }
                WayToWin(aPresses, bPresses)
            }
            .filterNotNull()
    }

    companion object {
        val BUTTON_REGEX = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
        val PRIZE_REGEX = """Prize: X=(\d+), Y=(\d+)""".toRegex()

        operator fun invoke(line1: String, line2: String, line3: String): ClawMachine {
            val buttonA = parseButton(line1)
            val buttonB = parseButton(line2)
            val prize = parsePrize(line3)
            return ClawMachine(prize, buttonA, buttonB)
        }

        fun parseButton(line: String) : Button {
            val (dX,dY) = BUTTON_REGEX.matchEntire(line)!!.destructured.toList().map { it.toInt() }
            return Button(dX ,dY)
        }
        fun parsePrize(line: String) : Prize {
            val (x,y) = PRIZE_REGEX.matchEntire(line)!!.destructured.toList().map { it.toInt() }
            return Prize(x.toInt(), y.toInt())
        }
    }
}

internal data class WayToWin(val buttonAPresses: Int, val buttonBPresses: Int) {
    val cost = (3 * buttonAPresses) + buttonBPresses
}

internal data class Prize(val x: Int, val y: Int)
internal data class Button(val dX: Int, val dY: Int)
