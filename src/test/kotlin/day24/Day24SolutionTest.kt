package day24

import day24.Operation.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day24SolutionTest {
    private val sampleInput = """
        x00: 1
        x01: 1
        x02: 1
        y00: 0
        y01: 1
        y02: 0

        x00 AND y00 -> z00
        x01 XOR y01 -> z01
        x02 OR y02 -> z02
    """.trimIndent().lines()

    private val largerSampleInput = """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1

        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
    """.trimIndent().lines()

    private val solution = Day24Solution()

    @Test
    fun `parseInitialWireValues should return a MutableMap of wires with inputs`() {
        val expectedResult: WireMap = mutableMapOf(
            "x00" to 1,
            "x01" to 1,
            "x02" to 1,
            "y00" to 0,
            "y01" to 1,
            "y02" to 0,
        )

        val result: WireMap = parseInitialWireValues(sampleInput)

        assert(result == expectedResult)
    }

    @Nested
    inner class GateTest {
        @Test
        fun `parseGate should return a Gate given a single line of valid input`() {
            //x02 OR y02 -> z02
            val input = sampleInput.last()
            val expectedResult = Gate("x02", "y02", OR, "z02")

            val result: Gate = parseGate(input)

            assert(result == expectedResult)
        }

        @Test
        fun `parseGates should result a collection of Gate instances`() {

            val expectedResult: List<Gate> = listOf(
                Gate("x00", "y00", AND, "z00"),
                Gate("x01", "y01", XOR, "z01"),
                Gate("x02", "y02", OR, "z02")
            )

            val result: List<Gate> = parseGates(sampleInput)

            assert(result == expectedResult)
        }

        @Test
        fun `calculate should and the two values when operation is AND`() {
            val gate = Gate("x00", "y00", AND, "z00")
            assert(gate.calculate(0,0) == 0)
            assert(gate.calculate(0,1) == 0)
            assert(gate.calculate(1,0) == 0)
            assert(gate.calculate(1,1) == 1)
        }

        @Test
        fun `calculate should or the two values when operation is OR`() {
            val gate = Gate("x00", "y00", OR, "z00")
            assert(gate.calculate(0,0) == 0)
            assert(gate.calculate(0,1) == 1)
            assert(gate.calculate(1,0) == 1)
            assert(gate.calculate(1,1) == 1)
        }

        @Test
        fun `calculate should xor the two values when operation is XOR`() {
            val gate = Gate("x00", "y00", XOR, "z00")
            assert(gate.calculate(0,0) == 0)
            assert(gate.calculate(0,1) == 1)
            assert(gate.calculate(1,0) == 1)
            assert(gate.calculate(1,1) == 0)
        }

        @Test
        fun `waitForInput should return the correct output value if both inputs are satisfied`() {
            val wires: WireMap = mutableMapOf(
                "x00" to 1,
                "y00" to 1
            )
            val gate = Gate("x00", "y00", AND, "z00")

            val result: Int? = gate.waitForInput(wires)

            assert(result == 1)
        }

        @Test
        fun `waitForInput should return null if wire1 is not in the wire map`() {
            val wires: WireMap = mutableMapOf(
                "y00" to 1
            )
            val gate = Gate("x00", "y00", AND, "z00")

            val result: Int? = gate.waitForInput(wires)

            assert(result == null)
        }

        @Test
        fun `waitForInput should return null if wire2 is not in the wire map`() {
            val wires: WireMap = mutableMapOf(
                "x00" to 1
            )
            val gate = Gate("x00", "y00", AND, "z00")

            val result: Int? = gate.waitForInput(wires)

            assert(result == null)
        }

        @Test
        fun `waitForInput should return null if neither input wire is in the wire map`() {
            val wires: WireMap = mutableMapOf()
            val gate = Gate("x00", "y00", AND, "z00")

            val result: Int? = gate.waitForInput(wires)

            assert(result == null)
        }
    }

    @Test
    fun `findZGateOutputs should process until all z gates have output and then return the values as a number `() {
        val result: Long = findZGateOutputs(sampleInput)

        assert(result == 4L)
    }

    @Test
    fun `findZGateOutputs should process multiple rounds for larger input`() {
        val result: Long = findZGateOutputs(largerSampleInput)

        assert(result == 2024L)
    }

    @Test
    fun `findZGateOutputs should terminate if there are unsatisfied gates that aren't z gates`() {
        val inputWithInfiniteLoop = largerSampleInput + "x99 OR y99 -> qqq"

        val result: Long = findZGateOutputs(inputWithInfiniteLoop)

        assert(result == 2024L)
    }
    
    @Test
    fun `part1 should calculate the decimal result of the z wires`() {
        assert(solution.part1(largerSampleInput) == 2024L)
    }
}
