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

    @Test
    fun `concatenate should combine the digits from its operands into a single number`() {
        val expectedResult = 12345L
        val result : Long = concatenate(12L, 345L)
        assert(result == expectedResult)
    }

    @Test
    fun `concatenate should return the correct answer for many examples`() {
        assert(concatenate(12L, 345L) == 12345L)
        assert(concatenate(12345L, 6789L) == 123456789L)
        assert(concatenate(87654321L, 12345678L) == 8765432112345678L)
    }

    @Test
    fun `numberOfDecimalDigits should return the correct number of decimal digits`() {
        assert(1L.numberOfDecimalDigits() == 1)
        assert(9L.numberOfDecimalDigits() == 1)
        assert(10L.numberOfDecimalDigits() == 2)
        assert(99L.numberOfDecimalDigits() == 2)
        assert(100L.numberOfDecimalDigits() == 3)
        assert(999L.numberOfDecimalDigits() == 3)
        assert(1000L.numberOfDecimalDigits() == 4)
        assert(123456789L.numberOfDecimalDigits() == 9)
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

        @Test
        fun `couldBeValid should return true for more calibration equations using the expanded set of operators in part 2`() {
            val viableEquations = mutableListOf(
                CalibrationEquation(190, 10, 19),
                CalibrationEquation(3267, 81, 40, 27),
                CalibrationEquation(292, 11, 6, 16, 20)
            )

            val additionalViableEquations = listOf (
                CalibrationEquation(156, 15, 6),
                CalibrationEquation(7290, 6, 8, 6, 15),
                CalibrationEquation(192, 17, 8, 14)
            )
            viableEquations += additionalViableEquations

            viableEquations.forEach { equation ->
                val result : Boolean = equation.couldBeValid(PART2_OPERATORS)
                assert(result)
            }
        }
    }

    @Test
    fun `part1 should return the sum of the test values for all CalibrationEquations that could be valid`() {
        assert(solution.part1(sampleInput) == 3749L)
    }

    @Test
    fun `part2 should return the sum of the test values for all CalibrationEquations that could be valid using the part 2 operators`() {
        assert(solution.part2(sampleInput) == 11387L)

    }

    @Test
    fun `count the combinatorials`() {
        println(parseInput(solution.readInput()).maxOf { equation -> equation.inputs.size })
    }
}