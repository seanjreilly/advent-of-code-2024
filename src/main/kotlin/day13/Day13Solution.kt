package day13

import utils.LongSolution

const val PART2_CRAZY_FACTOR = 10000000000000L

fun main() = Day13Solution().run()
class Day13Solution : LongSolution() {
    override fun part1(input: List<String>): Long {
        return parseClawMachines(input)
            .map { machine -> machine.findWaysToWin().sortedBy { it.cost } }
            .filter { it.isNotEmpty() }
            .sumOf { it.first().cost }
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
        return (0 until 100L)
            .map { aPresses ->
                val bPresses = findBPresses(aPresses, prize, buttonA, buttonB) ?: return@map null
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
            val (dX,dY) = BUTTON_REGEX.matchEntire(line)!!.destructured.toList().map { it.toLong() }
            return Button(dX ,dY)
        }
        fun parsePrize(line: String) : Prize {
            val (x,y) = PRIZE_REGEX.matchEntire(line)!!.destructured.toList().map { it.toLong() }
            return Prize(x, y)
        }
    }
}

internal fun findBPresses(aPresses: Long, thePrize: Prize, buttonA: Button, buttonB: Button) : Long? {
    val xValue = buttonA.dX * aPresses
    val yValue = buttonA.dY * aPresses

    if (xValue > thePrize.x) {
        return null
    }
    if (yValue > thePrize.y) {
        return null
    }

    val remainingX = thePrize.x - xValue
    val remainingY = thePrize.y - yValue

    if (remainingX % buttonB.dX != 0L) {
        return null
    }

    val bPresses = remainingX / buttonB.dX

    //validate
    if (buttonB.dY * bPresses != remainingY) {
        return null
    }

    return bPresses
}

internal data class WayToWin(val buttonAPresses: Long, val buttonBPresses: Long) {
    val cost = (3 * buttonAPresses) + buttonBPresses
}

internal data class Prize(val x: Long, val y: Long)
internal data class Button(val dX: Long, val dY: Long)
