package day13

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day13SolutionTest {
    private val sampleInput = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
        """.trimIndent().lines()

    private val solution = Day13Solution()

    @Test
    fun `parseClawMachines should return a list of ClawMachines`() {
        val first = ClawMachine(Prize(8400, 5400), Button(94, 34), Button(22, 67))
        val second = ClawMachine(Prize(12748, 12176), Button(26, 66), Button(67, 21))
        val third = ClawMachine(Prize(7870, 6450), Button(17, 86), Button(84, 37))
        val fourth = ClawMachine(Prize(18641, 10279), Button(69, 23), Button(27, 71))

        val result : List<ClawMachine> = parseClawMachines(sampleInput)

        assert(result.size == 4)
        assert(result[0] == first)
        assert(result[1] == second)
        assert(result[2] == third)
        assert(result[3] == fourth)
    }

    @Nested
    inner class ClawMachineTest {
        @Test
        fun `constructor should return a ClawMachine given three lines of input`() {
            val (a, b, c) = sampleInput.take(3)
            val result = ClawMachine(a,b,c)

            assert(result.prize == Prize(8400, 5400))
            assert(result.buttonA == Button(94, 34))
            assert(result.buttonB == Button(22, 67))
        }

        @Test
        fun `findWayToWin should return the combination of A presses and B presses that will win the prize`() {
            val machine: ClawMachine = parseClawMachines(sampleInput).first()
            val result = machine.findWayToWin()

            val expectedBestWayToWin = WayToWin(80, 40)

            assert(expectedBestWayToWin == result)
        }

        @Test
        fun `findWayToWin should return the combination of A presses and B presses that will win the prize given a different claw machine`() {
            val machine: ClawMachine = parseClawMachines(sampleInput)[2]
            val result = machine.findWayToWin()

            val expectedBestWayToWin = WayToWin(38, 86)

            assert(expectedBestWayToWin == result)
        }

        @Test
        fun `findWayToWin should return null if no combination of A presses and B presses will win the prize`() {
            val clawMachines = parseClawMachines(sampleInput)

            assert(clawMachines[1].findWayToWin() == null)
            assert(clawMachines[3].findWayToWin() == null)
        }
        
        @Test
        fun `toPart2 should add a huge number to the prize`() {
            val expectedPrize = Prize(10000000008400, 10000000005400)
            val clawMachine = parseClawMachines(sampleInput).first()
            val part2Machine : ClawMachine = clawMachine.toPart2()

            assert(part2Machine.buttonA == clawMachine.buttonA)
            assert(part2Machine.buttonB == clawMachine.buttonB)
            assert(part2Machine.prize == expectedPrize)
        }

        @Test
        fun `findWayToWin should not return an answer for the first machine in part2`() {
            val machine = parseClawMachines(sampleInput)[0].toPart2()
            val result = machine.findWayToWin()
            assert(result == null)
        }

        @Test
        fun `findWayToWin should return an answer for the second machine in part2`() {
            val machine = parseClawMachines(sampleInput)[1].toPart2()
            val result = machine.findWayToWin()
            assert(result != null)
            println(result)
            println(result!!.cost)
        }

        @Test
        fun `findWayToWinPart2 should not return an answer for the third machine in part2`() {
            val machine = parseClawMachines(sampleInput)[2].toPart2()
            val result = machine.findWayToWin()
            assert(result == null)
        }

        @Test
        fun `findWayToWinPart2 should return an answer for the last machine in part2`() {
            val machine = parseClawMachines(sampleInput)[3].toPart2()
            val result = machine.findWayToWin()
            assert(result != null)
            println(result)
            println(result!!.cost)
        }
    }

    @Nested
    inner class WayToWinTest {
        @Test
        fun `cost should return 3 times aPresses plus bPresses`() {
            val wayToWin = WayToWin(80, 40)

            assert(wayToWin.cost == 280L)
        }
    }

    @Test
    fun `part1 should return the fewest number of tokens needed to win all possible prizes`() {
        assert(solution.part1(sampleInput) == 480L)
    }

    @Test
    fun `part2 should return the fewest number of tokens needed to win all possible part2 prizes`() {
        assert(solution.part2(sampleInput) == 459236326669L + 416082282239L)
    }
}