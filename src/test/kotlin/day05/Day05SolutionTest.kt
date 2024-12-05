package day05

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Day05SolutionTest {
    private val sampleInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()

    val smallSampleInput = """
        47|53
        97|13
        97|61

        75,47,61,53,29
        97,61,53,29,13
    """.trimIndent().lines()

    private val solution = Day05Solution()

    @Test
    fun `parsePageOrderingRules should return a list of ordering rules given small sample input, ignoring the page updates`() {
        val expectedResult = listOf(
            PageOrderingRule(47, 53),
            PageOrderingRule(97,13),
            PageOrderingRule(97,61)
        )

        val result: List<PageOrderingRule> = parsePageOrderingRules(smallSampleInput)

        assert(result == expectedResult)
    }

    @Test
    fun `parsePageOrderingRules should work with larger input`() {
        val result = parsePageOrderingRules(sampleInput)
        assert(result.size == 21)
    }

    @Test
    fun `parsePageUpdates should return a list of page updates, ignoring the page ordering rules`() {
        val expectedResult = listOf(
            listOf(75,47,61,53,29),
            listOf(97,61,53,29,13)
        )

        val result: List<List<Int>> = parsePageUpdates(smallSampleInput)
    }

    @Test
    fun `parsePageUpdates should work with larger input`() {
        val result = parsePageUpdates(sampleInput)
        assert(result.size == 6)
        assert(result[0] == listOf(75,47,61,53,29))
        assert(result[1] == listOf(97,61,53,29,13))
        assert(result[2] == listOf(75,29,13))
        assert(result[3] == listOf(75,97,47,61,53))
        assert(result[4] == listOf(61,13,29))
        assert(result[5] == listOf(97,13,75,29,47))
    }
    
    @Test
    fun `isUpdateInRightOrder should return true if all of the PageOrderingRules that refer to pages in this update are followed`() {
        val pageOrderingRules = parsePageOrderingRules(sampleInput)
        val pageUpdate = parsePageUpdates(sampleInput)[0]
        val result: Boolean = isUpdateInRightOrder(pageUpdate, pageOrderingRules)
        assert(result)
    }

    @Test
    fun `isUpdateInRightOrder should return false for an update with two pages in an order that violates any PageOrderingRule, given simple PageOrderingRules`() {
        val rulesDefinition = """
            1|2
            2|3
            3|4

        """.trimIndent().lines()
        val pageOrderingRules = parsePageOrderingRules(rulesDefinition)
        val pageUpdate = listOf(1,2,4,3)
        val result: Boolean = isUpdateInRightOrder(pageUpdate, pageOrderingRules)
        assert(!result)
    }

    @Test
    fun `isUpdateInRightOrder should return the expected answers given examples from the problem`() {
        val pageOrderingRules = parsePageOrderingRules(sampleInput)
        val pageUpdates = parsePageUpdates(sampleInput)

        assert(isUpdateInRightOrder(pageUpdates[0], pageOrderingRules))
        assert(isUpdateInRightOrder(pageUpdates[1], pageOrderingRules))
        assert(isUpdateInRightOrder(pageUpdates[2], pageOrderingRules))
        assert(!isUpdateInRightOrder(pageUpdates[3], pageOrderingRules))
        assert(!isUpdateInRightOrder(pageUpdates[4], pageOrderingRules))
        assert(!isUpdateInRightOrder(pageUpdates[5], pageOrderingRules))
    }

    @Test
    fun `findMiddle should find the middle number in a list of odd length`() {
        val list = listOf(75,47,61,53,29)
        val result: Int = findMiddle(list)
        assert(result == 61)
    }

    @Test
    fun `findMiddle should throw an IllegalArgumentException given a list of even length`() {
        val list = listOf(1,2,3,4)
        assertThrows<IllegalArgumentException> { findMiddle(list) }
    }

    @Test
    fun `part1 should find all of the valid page updates and return the sum of their middle page numbers`() {
        val result = solution.part1(sampleInput)
        assert(result == 143)
    }
}
