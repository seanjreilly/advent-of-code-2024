package day21

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.lang.NullPointerException

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
                    } catch (e: NullPointerException) {
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
        fun `doorKeyboard should translate 029A into the correct sequences`() {
            val expectedOutput = "<A^A>^^AvvvA"
            val result = doorKeyboard("029A")
            assert(result == expectedOutput)
        }

        @Test
        fun `doorKeyboard should translate 980A into the correct sequence`() {
            val expectedOutput = "^^^A<AvvvA>A"
            val result = doorKeyboard("980A")
            assert(result == expectedOutput)
        }

        @Test
        fun `doorKeyboard should be a keyboard`() {
            val keyboard: Keyboard = ::doorKeyboard
            val expectedOutput = "<A^A>^^AvvvA"
            val result = keyboard("029A")
            assert(result == expectedOutput)
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
                    } catch (e: NullPointerException) {
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
        fun `robotKeyboard should translate ^A into the correct sequence`() {
            val expectedOutput = "<A>A"
            val result :String = robotKeyboard("^A")
            assert(result == expectedOutput)
        }


        @Test
        fun `robotKeyboard should translate vA into the correct sequence`() {
            val expectedOutput = "<vA>^A"
            val result = robotKeyboard("vA")
            assert(result == expectedOutput)
        }

        @Test
        fun `robotKeyboard should be a keyboard`() {
            val keyboard: Keyboard = ::robotKeyboard
            val expectedOutput = "<A>A"
            val result = keyboard("^A")
            assert(result == expectedOutput)
        }

        @Test
        fun `robotKeyboardTransitions should have complete mappings`() {
            assert(robotKeyboardTransitions.size == 25)
            "A^v<>".forEach { char ->
                assert(robotKeyboardTransitions[KeyTransition(char, char)] == "A")
            }
        }
    }

    @Test
    fun `decoding should work with multiple rounds`() {
        val initialValue = sampleDecodeRounds.last()

        val firstIntermediateValue = doorKeyboard(initialValue)
        assert(firstIntermediateValue == sampleDecodeRounds[2])

        val secondIntermediateValue = robotKeyboard(firstIntermediateValue)
        assert(secondIntermediateValue == sampleDecodeRounds[1])

        val finalValue = robotKeyboard(secondIntermediateValue)
        assert(finalValue == sampleDecodeRounds[0].replace("A<v<A", "Av<<A")) //sample uses a different final translation than I do
    }

    @Test
    fun `translateAllKeyboards should translate through two robot keyboards and a door keyboard`() {
        val initialValue = sampleDecodeRounds.last()
        val expectedResult = sampleDecodeRounds[0].replace("A<v<A", "Av<<A") //sample uses a different final translation than I do

        val result: String = translateAllKeyboards(initialValue)

        assert(result == expectedResult)
    }
}

