package utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CombinationsTest {

    @Test
    fun `twoElementCombinations should generate all of the unique 2 element pairs of a Set in any order`() {
        val set = setOf("A", "B", "C", "D")
        val expectedResult = setOf(
            "A" to "B",
            "A" to "C",
            "A" to "D",
            "B" to "C",
            "B" to "D",
            "C" to "D"
        )

        val result : Sequence<Pair<String, String>> = set.twoElementCombinations()

        assert(result.toSet() == expectedResult) //convert the result to a set to ignore order
    }

    @Test
    fun `twoElementCombinations should generate all of the unique 2 element pairs of a List in any order`() {
        val input : Iterable<String> = listOf("A", "B", "C", "D")
        val expectedResult = setOf(
            "A" to "B",
            "A" to "C",
            "A" to "D",
            "B" to "C",
            "B" to "D",
            "C" to "D"
        )

        val result : Sequence<Pair<String, String>> = input.twoElementCombinations()

        assert(result.toSet() == expectedResult) //convert the result to a set to ignore order
    }

    @Test
    fun `twoElementCombinations should work for Points`() {
        val p1 = Point(1, 1)
        val p2 = Point(2, 2)
        val p3 = Point(3, 3)
        val points : Iterable<Point> = listOf(p1, p2, p3)

        val expectedResult = setOf(
            p1 to p2,
            p1 to p3,
            p2 to p3
        )

        val result : Sequence<Pair<Point, Point>> = points.twoElementCombinations()

        assert(result.toSet() == expectedResult)
    }

    @Test
    fun `twoElementCombinations should throw an exception given an iterable with one element`() {
        val invalidInput : Iterable<String> = listOf("A")

        val exception : IllegalArgumentException = assertThrows { invalidInput.twoElementCombinations() }

        assert(exception.message == "must have at least 2 elements to generate 2 element combinations")
    }
}