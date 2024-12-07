package day07

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day07SolutionTest {
    private val sampleInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()

    private val solution = Day07Solution()

    @Test
    fun `parseInput should return a list of calibration Equations`() {
        val expectedResult = listOf(
            CalibrationEquation(190, 10, 19),
            CalibrationEquation(3267, 81, 40, 27),
            CalibrationEquation(83, 17, 5),
            CalibrationEquation(156, 15, 6),
            CalibrationEquation(7290, 6, 8, 6, 15),
            CalibrationEquation(161011, 16, 10, 13),
            CalibrationEquation(192, 17, 8, 14),
            CalibrationEquation(21037, 9, 7, 18, 13),
            CalibrationEquation(292, 11, 6, 16, 20)
        )

        val result : List<CalibrationEquation> = parseInput(sampleInput)

        assert (result.size == expectedResult.size)
        result.forEachIndexed { index, result -> assert(result == expectedResult[index]) }
    }

    @Nested
    inner class CalibrationEquationTest {
        @Test
        fun `constructor should return a Calibration Equation given a line`() {
            val line = sampleInput.first()
            val expectedResult = CalibrationEquation(190, 10, 19)

            val equation = CalibrationEquation(line)

            assert(equation == expectedResult)
        }

        @Test
        fun `couldBeValid should return true when a combination of multiplying and adding the input terms from left to right can compute the testValue`() {
            val viableEquations = listOf(
                CalibrationEquation(190, 10, 19),
                CalibrationEquation(3267, 81, 40, 27),
                CalibrationEquation(292, 11, 6, 16, 20)
            )

            viableEquations.forEach { equation ->
                val result : Boolean = equation.couldBeValid()
                assert(result)
            }
        }

        @Test
        fun `couldBeValid should return false when no combination of multiplying and adding the input terms from left to right can compute the testValue`() {
            val nonViableEquations = parseInput(sampleInput).drop(2).dropLast(1)

            nonViableEquations.forEach { equation ->
                val result : Boolean = equation.couldBeValid()
                assert(!result)
            }
        }
    }

    @Test
    fun `part1 should return the sum of the test values for all CalibrationEquations that could be valid`() {
        assert(solution.part1(sampleInput) == 3749L)
    }
}