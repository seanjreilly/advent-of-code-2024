package day22

import org.junit.jupiter.api.Test

class Day22SolutionTest {
    private val sampleInput = """
        1
        10
        100
        2024
    """.trimIndent().lines()

    private val sampleInputPart2 = """
        1
        2
        3
        2024
    """.trimIndent().lines()

    private val nextTenSecretNumbersFor123 = """
        15887950
        16495136
        527345
        704524
        1553684
        12683156
        11100544
        12249484
        7753432
        5908254
    """.trimIndent().lines()

    private val sampleInitialAnd2000thSecretNumbers = mapOf(
        1 to 8685429L,
        10 to 4700978L,
        100 to 15273692L,
        2024 to 8667524L,
    )

    private val solution = Day22Solution()
    
    @Test
    fun `mix should calculate the bitwise XOR of a value and a secret number`() {
        val secretNumber = 42L
        val numberToMix = 15L
        val expectedNewSecretNumber = 37L

        val result: Long = mix(numberToMix, secretNumber)

        assert(result == expectedNewSecretNumber)
    }

    @Test
    fun `prune should calculate the secret number modulo 16777216`() {
        val secretNumber = 100000000L
        val expectedNewSecretNumber = 16113920L

        val result: Long = prune(secretNumber)

        assert(result == expectedNewSecretNumber)
    }

    @Test
    fun `nextSecretNumber should multiply by 64, mix & prune, divide by 32, mix & prune, and multiply by 2048, mix & prune`() {
        val secretNumber = 123L
        val expectedResult = nextTenSecretNumbersFor123.first().toLong()

        val result: Long = nextSecretNumber(secretNumber)

        assert(result == expectedResult)
    }

    @Test
    fun `nextSecretNumber should provide the correct results for the first 10 rounds given 123`() {
        val initialSecretNumber = 123L
        val expectedResults = listOf(initialSecretNumber) + nextTenSecretNumbersFor123.map { it.toLong() }

        expectedResults.windowed(2).forEach { (current, next) ->
            val result = nextSecretNumber(current)
            assert(result == next)
        }
    }

    @Test
    fun `nextSecretNumberAfterNRounds should calculate the secret number after 10 rounds given a starting secret`() {
        assert(nextSecretNumberAfterNRounds(123L, 10) == 5908254L)
    }

    @Test
    fun `nextSecretNumberAfterNRounds should calculate the secret number after 2000 rounds given a starting secret and a number of rounds`() {
        sampleInitialAnd2000thSecretNumbers.forEach { (initialValue, expectedResult) ->
            assert(nextSecretNumberAfterNRounds(initialValue.toLong(), 2000) == expectedResult)
        }
    }
    
    @Test
    fun `findChangesAndPrices should generate the expected changes and prices given a starting secret of 123 and 9 rounds`() {
        val expectedResults = listOf(
            PriceChangePattern(-3, 6, -1, -1) to 4L,
            PriceChangePattern(6, -1, -1, 0) to 4L,
            PriceChangePattern(-1, -1, 0, 2) to 6L,
            PriceChangePattern(-1, 0, 2, -2) to 4L,
            PriceChangePattern(0, 2, -2, 0) to 4L,
            PriceChangePattern(2, -2, 0, -2) to 2L,
            PriceChangePattern(-2, 0, -2, 2) to 4L,
        )

        val results: List<Pair<PriceChangePattern, Long>> = findChangesAndPrices(123L, 10)

        assert(results == expectedResults)
    }

    @Test
    fun `removeDuplicates should only preserve the first instance of each PriceChange given a list of PriceChanges and prices`() {
        val input = listOf(
            PriceChangePattern(-3, 6, -1, -1) to 4L,
            PriceChangePattern(6, -1, -1, 0) to 4L,
            PriceChangePattern(-1, -1, 0, 2) to 6L,
            PriceChangePattern(-3, 6, -1, -1) to 7L,
            PriceChangePattern(-1, -1, 0, 2) to 8L,
            PriceChangePattern(6, -1, -1, 0) to 9L,
        )
        val expectedResult = input.dropLast(3).toMap()

        val result: Map<PriceChangePattern, Long> = removeDuplicates(input)

        assert(result == expectedResult)
    }

    @Test
    fun `removeDuplicates should PriceChanges that don't earn bananas`() {
        val input = listOf(
            PriceChangePattern(-3, 6, -1, -1) to 4L,
            PriceChangePattern(6, -1, -1, 0) to 4L,
            PriceChangePattern(-1, -1, 0, 2) to 0L,
        )
        val expectedResult = input.take(2).toMap()

        val result: Map<PriceChangePattern, Long> = removeDuplicates(input)

        assert(result == expectedResult)
    }

    @Test
    fun `merge should combine two price maps into a single price map with the sum of bananas each price change would earn`() {
        val firstMap = mapOf(
            PriceChangePattern(1,2,3,4) to 4L,
            PriceChangePattern(2,3,4,5) to 5L,
            PriceChangePattern(3,4,5,6) to 6L
        )
        val secondMap = mapOf(
            PriceChangePattern(2,3,4,5) to 1L,
            PriceChangePattern(3,4,5,6) to 2L,
            PriceChangePattern(4,5,6,7) to 3L
        )
        val expectedResult = mapOf(
            PriceChangePattern(1,2,3,4) to 4L,
            PriceChangePattern(2,3,4,5) to 6L,
            PriceChangePattern(3,4,5,6) to 8L,
            PriceChangePattern(4,5,6,7) to 3L
        )

        val result: Map<PriceChangePattern, Long> = merge(firstMap, secondMap)

        assert(result == expectedResult)
    }

    @Test
    fun `findBestPriceChange should return the best price change and how many bananas it would earn given a list of secret numbers`() {
        val secretNumbers = sampleInputPart2.map { it.toLong() }
        val expectedResult: Pair<PriceChangePattern, Long> = PriceChangePattern(-2,1,-1,3) to 23

        val result : Pair<PriceChangePattern, Long> = findBestPriceChange(secretNumbers)
        assert(result == expectedResult)
    }

    @Test
    fun `part1 should calculate the sum of the 2000th secret number generated by each buyer`() {
        assert(solution.part1(sampleInput) == 37327623L)
    }

    @Test
    fun `part2 should calculate the largest number of bananas that can be earned with a sequence of four price changes`() {
        assert(solution.part2(sampleInputPart2) == 23L)
    }
}
