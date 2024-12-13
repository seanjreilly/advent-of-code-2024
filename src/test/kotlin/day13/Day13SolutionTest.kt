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
        fun `findWaysToWin should return combinations of A presses and B presses that will win the prize`() {
            val machine: ClawMachine = parseClawMachines(sampleInput).first()
            val result : Collection<WayToWin> = machine.findWaysToWin()

            val expectedBestWayToWin = WayToWin(80, 40)

            assert(expectedBestWayToWin in result)
        }

        @Test
        fun `findWaysToWin should return combinations of A presses and B presses that will win the prize given a different claw machine`() {
            val machine: ClawMachine = parseClawMachines(sampleInput)[2]
            val result : Collection<WayToWin> = machine.findWaysToWin()

            val expectedBestWayToWin = WayToWin(38, 86)

            assert(expectedBestWayToWin in result)
        }

        @Test
        fun `findWaysToWin should return an empty collection if no combination of A presses and B presses will win the prize`() {
            val clawMachines = parseClawMachines(sampleInput)

            assert(clawMachines[1].findWaysToWin().isEmpty())
            assert(clawMachines[3].findWaysToWin().isEmpty())
        }
        
        @Test
        fun `toPart2 `() {
            
        }
    }

    @Test
    fun `part1 should return the fewest number of tokens needed to win all possible prizes`() {
        assert(solution.part1(sampleInput) == 480L)
    }

    @Nested
    inner class WayToWinTest {
        @Test
        fun `cost should return 3 times aPresses plus bPresses`() {
            val wayToWin = WayToWin(80, 40)

            assert(wayToWin.cost == 280L)
        }
    }
}