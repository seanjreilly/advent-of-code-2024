package day21

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.fail
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class Day21SolutionTest {
    private val sampleInput = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent().lines()

    private val sampleDecodeRounds = """
        <vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A
        v<<A>>^A<A>AvA<^AA>A<vAAA>^A
        <A^A>^^AvvvA
        029A
    """.trimIndent().lines()

    private val shortestPresses = mapOf(
        "029A" to "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A",
        "980A" to "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A",
        "179A" to "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A",
        "456A" to "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A",
        "379A" to "<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"
    )

    private val solution = Day21Solution()

    @Nested
    inner class DoorKeypadTest {

        @Test
        fun `immediateDoorKeypadTransitions should contain the correct values`() {
            assert(immediateDoorKeypadTransitions.size == 11)
            assert(immediateDoorKeypadTransitions['A'] == mapOf('0' to '<', '3' to '^'))
            assert(immediateDoorKeypadTransitions['0'] == mapOf('2' to '^', 'A' to '>'))
            assert(immediateDoorKeypadTransitions['1'] == mapOf('4' to '^', '2' to '>'))
            assert(immediateDoorKeypadTransitions['2'] == mapOf('1' to '<', '5' to '^', '3' to '>', '0' to 'v'))
            assert(immediateDoorKeypadTransitions['3'] == mapOf('2' to '<', '6' to '^', 'A' to 'v'))
            assert(immediateDoorKeypadTransitions['4'] == mapOf('7' to '^', '5' to '>', '1' to 'v'))
            assert(immediateDoorKeypadTransitions['5'] == mapOf('4' to '<', '8' to '^', '6' to '>', '2' to 'v'))
            assert(immediateDoorKeypadTransitions['6'] == mapOf('5' to '<', '9' to '^', '3' to 'v'))
            assert(immediateDoorKeypadTransitions['7'] == mapOf('8' to '>', '4' to 'v'))
            assert(immediateDoorKeypadTransitions['8'] == mapOf('7' to '<', '9' to '>', '5' to 'v'))
            assert(immediateDoorKeypadTransitions['9'] == mapOf('8' to '<', '6' to 'v'))
        }

        @Test
        fun `immediateDoorKeypadTransitions should have a reverse mapping for every mapping`() {
            val opposites = mapOf(
                '<' to '>',
                '>' to '<',
                '^' to 'v',
                'v' to '^'
            )

            immediateDoorKeypadTransitions.forEach { first, remainingMap ->
                remainingMap.forEach { second, value ->
                    try {
                        assert(immediateDoorKeypadTransitions[second]!![first]!! == opposites[value])
                    } catch (_: NullPointerException) {
                        fail("Could not find reverse mapping from ${second} to ${first} (expected '${opposites[value]}')")
                    }
                }
            }
        }

        @Test
        fun `immediateDoorKeypadTransitions should not map any key directly to itself`() {
            immediateDoorKeypadTransitions.forEach { first, remainingMap ->
                assert(!remainingMap.containsKey(first))
            }
        }
        
        @Test
        fun `generateAllShortestTransitions should map from every key to itself given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)
            "A0123456789".forEach {
                assert(shortestTransitions[KeyTransition(it, it)] == setOf("A"))
            }
        }

        @Test
        fun `generateAllShortestTransitions should generate basic transitions for neighbours given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)

            assert(shortestTransitions[KeyTransition('A', '0')] == setOf("<A"))
            assert(shortestTransitions[KeyTransition('A', '3')] == setOf("^A"))

            assert(shortestTransitions[KeyTransition('5', '4')] == setOf("<A"))
            assert(shortestTransitions[KeyTransition('5', '8')] == setOf("^A"))
            assert(shortestTransitions[KeyTransition('5', '6')] == setOf(">A"))
            assert(shortestTransitions[KeyTransition('5', '2')] == setOf("vA"))
        }

        @Test
        fun `generateAllShortestTransitions should not generate any forbidden transitions given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)

            assert(shortestTransitions[KeyTransition('1', '0')]!!.size == 1)
        }

        @Test
        fun `generateAllShortestTransitions should generate complete mappings given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)

            assert(shortestTransitions.size == 121)
        }

        @Test
        fun `generateAllShortestTransitions should generate all of the shortest paths from 0 to 7 given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)

            val expectedTransitions = setOf(
                "^^^<A",
                "^^<^A",
                "^<^^A"
            )

            val transitions = shortestTransitions[KeyTransition('0', '7')]
            assert(transitions == expectedTransitions)
        }

        @Test
        fun `generateAllShortestTransitions should generate all of the shortest paths from 2 to 9 given immediateDoorKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateDoorKeypadTransitions)

            val expectedTransitions = setOf(
                "^^>A",
                "^>^A",
                ">^^A"
            )

            val transitions = shortestTransitions[KeyTransition('2', '9')]
            assert(transitions == expectedTransitions)
        }

        @Test
        fun `doorKeypad should be a Keypad`() {
            val keypad: Keypad = ::doorKeypad
            val expectedOutput = "<A^A>^^AvvvA"
            val result: Set<String> = keypad("029A")
            assert(expectedOutput in result)
        }

        @Test
        fun `doorKeypad should translate 029A into the correct sequences`() {
            val expectedOutput = "<A^A>^^AvvvA"
            val result = doorKeypad("029A")
            assert(expectedOutput in result)
        }

        @Test
        fun `doorKeypad should translate 980A into the correct sequence`() {
            val expectedOutput = "^^^A<AvvvA>A"
            val result = doorKeypad("980A")
            assert(expectedOutput in result)
        }
    }

    @Nested
    inner class RobotKeyboardTest {
        @Test
        fun `immediateRobotKeypadTransitions should contain the correct values`() {
            assert(immediateRobotKeypadTransitions.size == 5)
            assert(immediateRobotKeypadTransitions['A'] == mapOf('^' to '<', '>' to 'v'))
            assert(immediateRobotKeypadTransitions['^'] == mapOf('A' to '>', 'v' to 'v'))
            assert(immediateRobotKeypadTransitions['>'] == mapOf('A' to '^', 'v' to '<'))
            assert(immediateRobotKeypadTransitions['v'] == mapOf('^' to '^', '<' to '<', '>' to '>'))
            assert(immediateRobotKeypadTransitions['<'] == mapOf('v' to '>'))
        }

        @Test
        fun `immediateRobotKeypadTransitions should have a reverse mapping for every mapping`() {
            val opposites = mapOf(
                '<' to '>',
                '>' to '<',
                '^' to 'v',
                'v' to '^'
            )

            immediateRobotKeypadTransitions.forEach { first, remainingMap ->
                remainingMap.forEach { second, value ->
                    try {
                        assert(immediateRobotKeypadTransitions[second]!![first]!! == opposites[value])
                    } catch (_: NullPointerException) {
                        fail("Could not find reverse mapping from ${second} to ${first} (expected '${opposites[value]}')")
                    }
                }
            }
        }

        @Test
        fun `immediateRobotKeypadTransitions should not map any key directly to itself`() {
            immediateRobotKeypadTransitions.forEach { first, remainingMap ->
                assert(!remainingMap.containsKey(first))
            }
        }

        @Test
        fun `generateAllShortestTransitions should generate basic transitions for neighbours given immediateRobotKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateRobotKeypadTransitions)

            assert(shortestTransitions[KeyTransition('A', '^')] == setOf("<A"))
            assert(shortestTransitions[KeyTransition('A', '>')] == setOf("vA"))

            assert(shortestTransitions[KeyTransition('v', '^')] == setOf("^A"))
            assert(shortestTransitions[KeyTransition('v', '<')] == setOf("<A"))
            assert(shortestTransitions[KeyTransition('v', '>')] == setOf(">A"))
        }

        @Test
        fun `generateAllShortestTransitions should not generate any forbidden transitions given immediateRobotKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateRobotKeypadTransitions)

            assert(shortestTransitions[KeyTransition('^', '<')]!!.size == 1)
        }

        @Test
        fun `generateAllShortestTransitions should generate complete mappings given immediateRobotKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateRobotKeypadTransitions)

            assert(shortestTransitions.size == 25)
        }

        @Test
        fun `generateAllShortestTransitions should generate all of the shortest paths from left arrow to A given immediateRobotKeypadTransitions`() {
            val shortestTransitions: Map<KeyTransition, Set<String>> = generateAllShortestTransitions(immediateRobotKeypadTransitions)

            val expectedTransitions = setOf(
                ">>^A",
                ">^>A",
            )

            val transitions = shortestTransitions[KeyTransition('<', 'A')]
            assert(transitions == expectedTransitions)
        }

        @Test
        fun `robotKeypad should translate ^A into the correct sequence`() {
            val expectedOutput = "<A>A"
            val result : Set<String> = robotKeypad("^A")
            assert(expectedOutput in result)
        }

        @Test
        fun `robotKeypad should translate vA into the correct sequence`() {
            val expectedOutput = "<vA>^A"
            val result = robotKeypad("vA")
            assert(expectedOutput in result)
        }

        @Test
        fun `robotKeypad should be a keypad`() {
            val keypad: Keypad = ::robotKeypad
            val expectedOutput = "<A>A"
            val result = keypad("^A")
            assert(expectedOutput in result)
        }
    }

    @Test
    fun `decoding should work with multiple rounds`() {
        val initialValue = sampleDecodeRounds.last()

        val firstIntermediateValues = doorKeypad(initialValue)
        assert(sampleDecodeRounds[2] in firstIntermediateValues)

        val secondIntermediateValues = robotKeypad(sampleDecodeRounds[2])
        assert(sampleDecodeRounds[1] in secondIntermediateValues)

        val finalValues = robotKeypad(sampleDecodeRounds[1])
        assert(sampleDecodeRounds[0] in finalValues)
    }
    
    @Test
    fun `calculateComplexity should return the product of the shortest sequence of button pushes for the code and the code`() {
        assert(calculateComplexity("029A", 2) == 68L * 29L)
        assert(calculateComplexity("980A", 2) == 60L * 980L)
        assert(calculateComplexity("179A", 2) == 68L * 179L)
        assert(calculateComplexity("456A", 2) == 64L * 456L)
        assert(calculateComplexity("379A", 2) == 64L * 379L)
    }
    
    @Test
    fun `dynamic programming approach should should translate through multiple robot keypads and a door keypad and return the length of the shortest sequence`() {
        assert(robotKeypad("029A", 2, 0) == 68L)
        assert(robotKeypad("980A", 2, 0) == 60L)
        assert(robotKeypad("179A", 2, 0) == 68L)
        assert(robotKeypad("456A", 2, 0) == 64L)
        assert(robotKeypad("379A", 2, 0) == 64L)
    }

    @Test
    fun `dynamic programming approach should return the shortest sequences of buttons presses for all sample inputs`() {
        shortestPresses.forEach { initialValue, expectedResult ->
            val result = robotKeypad(initialValue, 2, 0)
            assert(result == expectedResult.length.toLong())
        }
    }

    @Timeout(1, unit = TimeUnit.SECONDS)
    @Test
    fun `dynamic programming approach should should work with 25 rounds of robot keypads and a door keypad and return the length of the shortest sequence`() {
        assert(robotKeypad("029A", 25, 0) == 82050061710L)
        assert(robotKeypad("980A", 25, 0) == 72242026390L)
        assert(robotKeypad("179A", 25, 0) == 81251039228L)
        assert(robotKeypad("456A", 25, 0) == 80786362258L)
        assert(robotKeypad("379A", 25, 0) == 77985628636L)
    }

    @Test
    fun `calculateComplexity should return the product of the shortest sequence of button pushe with 25 rounds for the code and the code`() {
        assert(calculateComplexity("029A", 25) == 82050061710L * 29L)
        assert(calculateComplexity("980A", 25) == 72242026390L * 980L)
        assert(calculateComplexity("179A", 25) == 81251039228L * 179L)
        assert(calculateComplexity("456A", 25) == 80786362258L * 456L)
        assert(calculateComplexity("379A", 25) == 77985628636L * 379L)
    }

    @Test
    fun `part1 should return the sum of the complexities of the input with 2 robot keypads`() {
        assert(solution.part1(sampleInput) == 126384L)
    }

    @Test
    fun `part2 should return the sum of the complexities of the input with 25 robot keypads`() {
        assert(solution.part2(sampleInput) == 154115708116294L)
    }
}