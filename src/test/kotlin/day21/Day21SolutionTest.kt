package day21

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
    inner class DoorKeyboardTest {

        @Test
        fun `doorKeyboard should translate 029A into the correct sequence`() {
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
        fun `doorKeyboard should encode all transitions to and from A into the correct sequence`() {
            assert(doorKeyboard("AA") == "AA")
            assert(doorKeyboard("0A") == "<A>A")
            assert(doorKeyboard("1A") == "^<<A>>vA")
            assert(doorKeyboard("2A") == "^<A>vA")
            assert(doorKeyboard("3A") == "^AvA")
            assert(doorKeyboard("4A") == "^^<<A>>vvA")
            assert(doorKeyboard("5A") == "^^<A>vvA")
            assert(doorKeyboard("6A") == "^^AvvA")
            assert(doorKeyboard("7A") == "^^^<<A>>vvvA")
            assert(doorKeyboard("8A") == "^^^<A>vvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 0 into the correct sequence`() {
            assert(doorKeyboard("00A") == "<AA>A")
            assert(doorKeyboard("01A") == "<A^<A>>vA")
            assert(doorKeyboard("02A") == "<A^A>vA")
            assert(doorKeyboard("03A") == "<A^>AvA")
            assert(doorKeyboard("04A") == "<A^^<A>>vvA")
            assert(doorKeyboard("05A") == "<A^^A>vvA")
            assert(doorKeyboard("06A") == "<A^^>AvvA")
            assert(doorKeyboard("07A") == "<A^^^<A>>vvvA")
            assert(doorKeyboard("08A") == "<A^^^A>vvvA")
            assert(doorKeyboard("09A") == "<A^^^>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 1 into the correct sequence`() {
            assert(doorKeyboard("10A") == "^<<A>vA>A")
            assert(doorKeyboard("11A") == "^<<AA>>vA")
            assert(doorKeyboard("12A") == "^<<A>A>vA")
            assert(doorKeyboard("13A") == "^<<A>>AvA")
            assert(doorKeyboard("14A") == "^<<A^A>>vvA")
            assert(doorKeyboard("15A") == "^<<A^>A>vvA")
            assert(doorKeyboard("16A") == "^<<A^>>AvvA")
            assert(doorKeyboard("17A") == "^<<A^^A>>vvvA")
            assert(doorKeyboard("18A") == "^<<A^^>A>vvvA")
            assert(doorKeyboard("19A") == "^<<A^^>>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 2 into the correct sequence`() {
            assert(doorKeyboard("20A") == "^<AvA>A")
            assert(doorKeyboard("21A") == "^<A<A>>vA")
            assert(doorKeyboard("22A") == "^<AA>vA")
            assert(doorKeyboard("23A") == "^<A>AvA")
            assert(doorKeyboard("24A") == "^<A^<A>>vvA")
            assert(doorKeyboard("25A") == "^<A^A>vvA")
            assert(doorKeyboard("26A") == "^<A^>AvvA")
            assert(doorKeyboard("27A") == "^<A^^<A>>vvvA")
            assert(doorKeyboard("28A") == "^<A^^A>vvvA")
            assert(doorKeyboard("29A") == "^<A>^^AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 3 into the correct sequence`() {
            assert(doorKeyboard("30A") == "^Av<A>A")
            assert(doorKeyboard("31A") == "^A<<A>>vA")
            assert(doorKeyboard("32A") == "^A<A>vA")
            assert(doorKeyboard("33A") == "^AAvA")
            assert(doorKeyboard("34A") == "^A^<<A>>vvA")
            assert(doorKeyboard("35A") == "^A^<A>vvA")
            assert(doorKeyboard("36A") == "^A^AvvA")
            assert(doorKeyboard("37A") == "^A^^<<A>>vvvA")
            assert(doorKeyboard("38A") == "^A^^<A>vvvA")
            assert(doorKeyboard("39A") == "^A^^AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 4 into the correct sequence`() {
            assert(doorKeyboard("40A") == "^^<<A>vvA>A")
            assert(doorKeyboard("41A") == "^^<<AvA>>vA")
            assert(doorKeyboard("42A") == "^^<<A>vA>vA")
            assert(doorKeyboard("43A") == "^^<<A>>vAvA")
            assert(doorKeyboard("44A") == "^^<<AA>>vvA")
            assert(doorKeyboard("45A") == "^^<<A>A>vvA")
            assert(doorKeyboard("46A") == "^^<<A>>AvvA")
            assert(doorKeyboard("47A") == "^^<<A^A>>vvvA")
            assert(doorKeyboard("48A") == "^^<<A^>A>vvvA")
            assert(doorKeyboard("49A") == "^^<<A^>>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 5 into the correct sequence`() {
            assert(doorKeyboard("50A") == "^^<AvvA>A")
            assert(doorKeyboard("51A") == "^^<Av<A>>vA")
            assert(doorKeyboard("52A") == "^^<AvA>vA")
            assert(doorKeyboard("53A") == "^^<Av>AvA")
            assert(doorKeyboard("54A") == "^^<A<A>>vvA")
            assert(doorKeyboard("55A") == "^^<AA>vvA")
            assert(doorKeyboard("56A") == "^^<A>AvvA")
            assert(doorKeyboard("57A") == "^^<A^<A>>vvvA")
            assert(doorKeyboard("58A") == "^^<A^A>vvvA")
            assert(doorKeyboard("59A") == "^^<A^>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 6 into the correct sequence`() {
            assert(doorKeyboard("60A") == "^^A<vvA>A")
            assert(doorKeyboard("61A") == "^^A<<vA>>vA")
            assert(doorKeyboard("62A") == "^^A<vA>vA")
            assert(doorKeyboard("63A") == "^^AvAvA")
            assert(doorKeyboard("64A") == "^^A<<A>>vvA")
            assert(doorKeyboard("65A") == "^^A<A>vvA")
            assert(doorKeyboard("66A") == "^^AAvvA")
            assert(doorKeyboard("67A") == "^^A^<<A>>vvvA")
            assert(doorKeyboard("68A") == "^^A^<A>vvvA")
            assert(doorKeyboard("69A") == "^^A^AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 7 into the correct sequence`() {
            assert(doorKeyboard("70A") == "^^^<<A>vvvA>A")
            assert(doorKeyboard("71A") == "^^^<<AvvA>>vA")
            assert(doorKeyboard("72A") == "^^^<<A>vvA>vA")
            assert(doorKeyboard("73A") == "^^^<<A>>vvAvA")
            assert(doorKeyboard("74A") == "^^^<<AvA>>vvA")
            assert(doorKeyboard("75A") == "^^^<<Av>A>vvA")
            assert(doorKeyboard("76A") == "^^^<<Av>>AvvA")
            assert(doorKeyboard("77A") == "^^^<<AA>>vvvA")
            assert(doorKeyboard("78A") == "^^^<<A>A>vvvA")
            assert(doorKeyboard("79A") == "^^^<<A>>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 8 into the correct sequence`() {
            assert(doorKeyboard("80A") == "^^^<AvvvA>A")
            assert(doorKeyboard("81A") == "^^^<A<vvA>>vA")
            assert(doorKeyboard("82A") == "^^^<AvvA>vA")
            assert(doorKeyboard("83A") == "^^^<A>vvAvA")
            assert(doorKeyboard("84A") == "^^^<A<vA>>vvA")
            assert(doorKeyboard("85A") == "^^^<AvA>vvA")
            assert(doorKeyboard("86A") == "^^^<Av>AvvA")
            assert(doorKeyboard("87A") == "^^^<A<A>>vvvA")
            assert(doorKeyboard("88A") == "^^^<AA>vvvA")
            assert(doorKeyboard("89A") == "^^^<A>AvvvA")
        }

        @Test
        fun `doorKeyboard should encode all transitions from 9 into the correct sequence`() {
            assert(doorKeyboard("90A") == "^^^A<vvvA>A")
            assert(doorKeyboard("91A") == "^^^A<<vvA>>vA")
            assert(doorKeyboard("92A") == "^^^A<vvA>vA")
            assert(doorKeyboard("93A") == "^^^AvvAvA")
            assert(doorKeyboard("94A") == "^^^A<<vA>>vvA")
            assert(doorKeyboard("95A") == "^^^A<vA>vvA")
            assert(doorKeyboard("96A") == "^^^AvAvvA")
            assert(doorKeyboard("97A") == "^^^A<<A>>vvvA")
            assert(doorKeyboard("98A") == "^^^A<A>vvvA")
            assert(doorKeyboard("99A") == "^^^AAvvvA")
        }

        @Test
        fun `doorKeyboardTransitions should have complete mappings`() {
            assert(doorKeyboardTransitions.size == 121)
            "A0123456789".forEach { char ->
                assert(doorKeyboardTransitions[KeyTransition(char, char)] == "A")
            }
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
        fun `robotKeyboard should encode all transitions to and from A into the correct sequence`() {
            assert(robotKeyboard("AA") == "AA")
            assert(robotKeyboard("^A") == "<A>A")
            assert(robotKeyboard("<A") == "v<<A>>^A")
            assert(robotKeyboard("vA") == "<vA>^A")
            assert(robotKeyboard(">A") == "vA^A")
        }

        @Test
        fun `robotKeyboard should encode all transitions from ^ into the correct sequence`() {
            assert(robotKeyboard("^^A") == "<AA>A")
            assert(robotKeyboard("^<A") == "<Av<A>>^A")
            assert(robotKeyboard("^vA") == "<AvA>^A")
            assert(robotKeyboard("^>A") == "<Av>A^A")
        }

        @Test
        fun `robotKeyboard should encode all transitions from left arrow into the correct sequence`() {
            assert(robotKeyboard("<^A") == "v<<A>^A>A")
            assert(robotKeyboard("<<A") == "v<<AA>>^A")
            assert(robotKeyboard("<vA") == "v<<A>A>^A")
            assert(robotKeyboard("<>A") == "v<<A>>A^A")
        }

        @Test
        fun `robotKeyboard should encode all transitions from v into the correct sequence`() {
            assert(robotKeyboard("v^A") == "<vA^A>A")
            assert(robotKeyboard("v<A") == "<vA<A>>^A")
            assert(robotKeyboard("vvA") == "<vAA>^A")
            assert(robotKeyboard("v>A") == "<vA>A^A")
        }

        @Test
        fun `robotKeyboard should encode all transitions from right arrow into the correct sequence`() {
            assert(robotKeyboard(">^A") == "vA<^A>A")
            assert(robotKeyboard("><A") == "vA<<A>>^A")
            assert(robotKeyboard(">vA") == "vA<A>^A")
            assert(robotKeyboard(">>A") == "vAA^A")
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

