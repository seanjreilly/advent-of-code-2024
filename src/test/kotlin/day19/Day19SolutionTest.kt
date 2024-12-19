package day19

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day19SolutionTest {

    private val sampleInput = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()

    private val solution = Day19Solution()
    
    @Test
    fun `parseTowelPatterns should return the set of towel patterns`() {
        val expectedTowelPatterns = setOf(
            "r",
            "wr",
            "b",
            "g",
            "bwu",
            "rb",
            "gb",
            "br"
        )

        val towelPatterns: Set<String> = parseTowelPatterns(sampleInput)

        assert(towelPatterns == expectedTowelPatterns)
    }

    @Test
    fun `parseDesiredDesigns should return the list of desired towel designs`() {
        val expectedDesigns = listOf(
            "brwrr",
            "bggr",
            "gbbr",
            "rrbgbr",
            "ubwu",
            "bwurrg",
            "brgr",
            "bbrgwb"
        )

        val desiredDesigns: List<String> = parseDesiredDesigns(sampleInput)

        assert(desiredDesigns == expectedDesigns)
    }

    @Test
    fun `isDesignPossible should return true if the design can be made with a combination of available towels`() {
        val towelPatterns = parseTowelPatterns(sampleInput)
        assert(isDesignPossible("brwrr", towelPatterns))
        assert(isDesignPossible("bggr", towelPatterns))
        assert(isDesignPossible("gbbr", towelPatterns))
        assert(isDesignPossible("rrbgbr", towelPatterns))
        assert(isDesignPossible("bwurrg", towelPatterns))
        assert(isDesignPossible("brgr", towelPatterns))
    }

    @Test
    fun `isDesignPossible should return false if the design cannot be made with a combination of available towels`() {
        val towelPatterns = parseTowelPatterns(sampleInput)
        assert(!isDesignPossible("ubwu", towelPatterns))
        assert(!isDesignPossible("bbrgwb", towelPatterns))

    }

    @Nested
    inner class TowelCombinationCounterTest {
        private val counter: TowelCombinationCounter = TowelCombinationCounter(parseTowelPatterns(sampleInput))

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'brwrr'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("brwrr")
            assert(combinations == 2L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'bggr'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("bggr")
            assert(combinations == 1L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'gbbr'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("gbbr")
            assert(combinations == 4L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'rrbgbr'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("rrbgbr")
            assert(combinations == 6L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'bwurrg'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("bwurrg")
            assert(combinations == 1L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'brgr'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("brgr")
            assert(combinations == 2L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'ubwu'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("ubwu")
            assert(combinations == 0L)
        }

        @Test
        fun `countPossibleTowelCombinations should return the count of possible towel combinations that can make 'bbrgwb'`() {
            val combinations: Long = counter.countPossibleTowelCombinations("bbrgwb")
            assert(combinations == 0L)
        }
    }

    @Test
    fun `part1 should count the number of designs that are possible`() {
        assert(solution.part1(sampleInput) == 6L)
    }
    
    @Test
    fun `part2 should return the sum of possible towel combinations for all the designs`() {
        assert(solution.part2(sampleInput) == 16L)
    }
}