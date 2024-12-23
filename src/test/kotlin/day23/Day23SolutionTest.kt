package day23

import org.junit.jupiter.api.Test

class Day23SolutionTest {
    private val sampleInput = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent().lines()

    private val sampleInterconnectedSetsOfThree = """
        aq,cg,yn
        aq,vc,wq
        co,de,ka
        co,de,ta
        co,ka,ta
        de,ka,ta
        kh,qp,ub
        qp,td,wh
        tb,vc,wq
        tc,td,wh
        td,wh,yn
        ub,vc,wq
    """.trimIndent().lines()

    private val sampleInterconnectedSetsOfThreeWithAtLeastOneNodeStartingWithT = """
        co,de,ta
        co,ka,ta
        de,ka,ta
        qp,td,wh
        tb,vc,wq
        tc,td,wh
        td,wh,yn
    """.trimIndent().lines()

    private val solution = Day23Solution()

    @Test
    fun `findSetsOfThree should return a set of sets of three interconnected nodes given a list of bidirectional edges`() {
        val expectedSetsOfThree = toSetOfSets(sampleInterconnectedSetsOfThree)

        val result: Set<Set<String>> = findSetsOfThree(sampleInput)

        assert(result == expectedSetsOfThree)
    }

    @Test
    fun `containNodeStartingWithT should return the sets containing at least one node starting with t given sets of nodes`() {
        val setsOfThree = toSetOfSets(sampleInterconnectedSetsOfThree)
        val expectedSetsWithTNodes = toSetOfSets(sampleInterconnectedSetsOfThreeWithAtLeastOneNodeStartingWithT)

        val result: Collection<Set<String>> = containNodeStartingWithT(setsOfThree).toSet()

        assert(result == expectedSetsWithTNodes)
    }

    @Test
    fun `part1 should return the number of connected sets of 3 nodes containing at least one node starting with t`() {
        assert(solution.part1(sampleInput) == 7L)
    }

    private fun toSetOfSets(input: List<String>): Set<Set<String>> {
        return input
            .map { it.split(',').toSet() }
            .toSet()
    }
}