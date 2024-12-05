package day05

import org.junit.jupiter.api.Nested
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
            PageOrderingRule(97, 13),
            PageOrderingRule(97, 61)
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
            listOf(75, 47, 61, 53, 29),
            listOf(97, 61, 53, 29, 13)
        )

        val result: List<List<Int>> = parsePageUpdates(smallSampleInput)
        assert(result == expectedResult)
    }

    @Test
    fun `parsePageUpdates should work with larger input`() {
        val result = parsePageUpdates(sampleInput)
        assert(result.size == 6)
        assert(result[0] == listOf(75, 47, 61, 53, 29))
        assert(result[1] == listOf(97, 61, 53, 29, 13))
        assert(result[2] == listOf(75, 29, 13))
        assert(result[3] == listOf(75, 97, 47, 61, 53))
        assert(result[4] == listOf(61, 13, 29))
        assert(result[5] == listOf(97, 13, 75, 29, 47))
    }

    @Test
    fun `findMiddle should find the middle number in a list of odd length`() {
        val list = listOf(75, 47, 61, 53, 29)
        val result: Int = findMiddle(list)
        assert(result == 61)
    }

    @Test
    fun `findMiddle should throw an IllegalArgumentException given a list of even length`() {
        val list = listOf(1, 2, 3, 4)
        assertThrows<IllegalArgumentException> { findMiddle(list) }
    }

    @Nested
    inner class PageRuleComparatorTest {
        @Test
        fun `compare should follow the sort rules`() {
            val rules = listOf(
                PageOrderingRule(1, 2),
                PageOrderingRule(4, 3)
            )
            val comparator: Comparator<Int> = PageRuleComparator(rules)

            assert(comparator.compare(1, 2) == -1)
            assert(comparator.compare(2, 1) == 1)
            assert(comparator.compare(4, 3) == -1)
            assert(comparator.compare(3, 4) == 1)
        }

        @Test
        fun `compare should return zero if both arguments are equal`() {
            val comparator: Comparator<Int> = PageRuleComparator(emptyList())
            assert(comparator.compare(15, 15) == 0)
        }

        @Test
        fun `compare should return zero if neither page should be before the other`() {
            val rules = listOf(
                PageOrderingRule(1, 2)
            )
            val comparator: Comparator<Int> = PageRuleComparator(rules)

            assert(comparator.compare(3, 4) == 0)
            assert(comparator.compare(4, 3) == 0)
        }

        @Test
        fun `compare should return zero if no rule mentions both pages, even if a rule mentions either page`() {
            val rules = listOf(
                PageOrderingRule(1, 2)
            )
            val comparator: Comparator<Int> = PageRuleComparator(rules)

            assert(comparator.compare(1, 3) == 0)
            assert(comparator.compare(3, 1) == 0)
            assert(comparator.compare(2, 3) == 0)
            assert(comparator.compare(3, 2) == 0)
        }

        @Test
        fun `compare should return zero if either argument is null`() {
            val comparator: Comparator<Int> = PageRuleComparator(emptyList())
            assert(comparator.compare(1, null) == 0)
            assert(comparator.compare(null, 1) == 0)
        }

        @Test
        fun `isUpdateInRightOrder should return true if all of the PageOrderingRules that refer to pages in this update are followed`() {
            val pageOrderingRules = parsePageOrderingRules(sampleInput)
            val pageUpdate = parsePageUpdates(sampleInput)[0]
            val instance = PageRuleComparator(pageOrderingRules)

            val result: Boolean = instance.isUpdateInRightOrder(pageUpdate)

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
            val pageUpdate = listOf(1, 2, 4, 3)
            val instance = PageRuleComparator(pageOrderingRules)


            val result: Boolean = instance.isUpdateInRightOrder(pageUpdate)
            assert(!result)
        }

        @Test
        fun `isUpdateInRightOrder should return the expected answers given examples from the problem`() {
            val pageOrderingRules = parsePageOrderingRules(sampleInput)
            val pageUpdates = parsePageUpdates(sampleInput)
            val instance = PageRuleComparator(pageOrderingRules)

            assert(instance.isUpdateInRightOrder(pageUpdates[0]))
            assert(instance.isUpdateInRightOrder(pageUpdates[1]))
            assert(instance.isUpdateInRightOrder(pageUpdates[2]))
            assert(!instance.isUpdateInRightOrder(pageUpdates[3]))
            assert(!instance.isUpdateInRightOrder(pageUpdates[4]))
            assert(!instance.isUpdateInRightOrder(pageUpdates[5]))
        }

        @Test
        fun `fixPageOrders should use the page rules to fix the order of a badly ordered update`() {
            val comparator = PageRuleComparator(parsePageOrderingRules(sampleInput))

            assert(comparator.fixPageOrders(listOf(75, 97, 47, 61, 53)) == listOf(97, 75, 47, 61, 53))
            assert(comparator.fixPageOrders(listOf(61, 13, 29)) == listOf(61, 29, 13))
            assert(comparator.fixPageOrders(listOf(97, 13, 75, 29, 47)) == listOf(97, 75, 47, 29, 13))
        }
    }

    @Test
    fun `part1 should find all of the valid page updates and return the sum of their middle page numbers`() {
        val result = solution.part1(sampleInput)
        assert(result == 143)
    }

    @Test
    fun `part2 should find all of the invalid page updates, fix them, and return the sum of their middle page numbers`() {
        val result = solution.part2(sampleInput)
        assert(result == 123)
    }
}
