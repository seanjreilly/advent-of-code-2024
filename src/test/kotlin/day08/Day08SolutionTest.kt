package day08

import org.junit.jupiter.api.Test
import utils.Bounds
import utils.Point
import utils.get

class Day08SolutionTest {
    private val sampleInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()

    private val sampleInputPart2 = """
        T....#....
        ...T......
        .T....#...
        .........#
        ..#.......
        ..........
        ...#......
        ..........
        ....#.....
        ..........
    """.trimIndent().lines()

    private val solution = Day08Solution()

    @Test
    fun `generateTwoElementPairs should generate all of the unique 2 element pairs of a Set in any order`() {
        val set = setOf("A", "B", "C", "D")
        val expectedResult = setOf(
            "A" to "B",
            "A" to "C",
            "A" to "D",
            "B" to "C",
            "B" to "D",
            "C" to "D"
        )

        val result : Sequence<Pair<String, String>> = set.generateTwoElementPairs()

        assert(result.toSet() == expectedResult) //convert the result to a set to ignore order
    }

    @Test
    fun `generateTwoElementPairs should work for Points`() {
        val p1 = Point(1, 1)
        val p2 = Point(2, 2)
        val p3 = Point(3, 3)
        val points = setOf(p1, p2, p3)

        val expectedResult = setOf(
            p1 to p2,
            p1 to p3,
            p2 to p3
        )

        val result : Sequence<Pair<Point, Point>> = points.generateTwoElementPairs()

        assert(result.toSet() == expectedResult)
    }

    @Test
    fun `parseAntennaLocations should return a map of antenna frequencies to antenna locations`() {
        val antennaLocations : AntennaMap = parseAntennaLocations(sampleInput)
        val expectedZeroLocations = setOf(
            Point(8,1),
            Point(5,2),
            Point(7,3),
            Point(4,4)
        )

        val expectedALocations = setOf(
            Point(6,5),
            Point(8,8),
            Point(9,9)
        )


        assert(antennaLocations.keys == setOf('A', '0'))
        assert(antennaLocations['0'] == expectedZeroLocations)
        assert(antennaLocations['A'] == expectedALocations)
    }

    @Test
    fun `findAntinodesBetween should return the points that are in line with both antennae and twice as far away from one as the other given antennae on a horizontal line`() {
        val input = """
            ..........
            ...A..A...
            ..........
        """.trimIndent().lines()

        val antennaMap = parseAntennaLocations(input)
        val aLocations = antennaMap['A']!!
        assert(aLocations == setOf(Point(3,1), Point(6,1))) { "precondition" }
        val aLocationList = aLocations.toList()

        val expectedResult = setOf(Point(0,1), Point(9,1))

        val result : Set<Point> = findAntinodesBetween(aLocationList[0], aLocationList[1], Bounds(input))

        assert(result == expectedResult)
    }

    @Test
    fun `findAntinodesBetween should return the points that are in line with both antennae and twice as far away from one as the other given antennae on a vertical line`() {
        val input = """
            ...
            ...
            ...
            .A.
            ...
            ...
            .A.
            ...
            ...
            ...
        """.trimIndent().lines()

        val antennaMap = parseAntennaLocations(input)
        val aLocations = antennaMap['A']!!
        assert(aLocations == setOf(Point(1,3), Point(1,6))) { "precondition" }
        val aLocationList = aLocations.toList()

        val expectedResult = setOf(Point(1, 0), Point(1, 9))

        val result : Set<Point> = findAntinodesBetween(aLocationList[0], aLocationList[1], Bounds(input))

        assert(result == expectedResult)
    }
    
    @Test
    fun `findAntinodesBetween should only return antinodes within the bounds of the grid`() {
        val input = """
            ..........
            ..........
            #.........
            ....a.....
            ........a.
            ..........
            ..........
            ..........
            ..........
            ..........
        """.trimIndent().lines()

        val antennaMap = parseAntennaLocations(input)
        val aLocations = antennaMap['a']!!
        assert(aLocations == setOf(Point(4,3), Point(8,4))) { "precondition" }
        val aLocationList = aLocations.toList()

        val expectedResult = setOf(Point(0, 2))

        val result : Set<Point> = findAntinodesBetween(aLocationList[0], aLocationList[1], Bounds(input))

        assert(result == expectedResult)
    }

    @Test
    fun `findAntinodesBetweenWithResonantHarmonics should return antinodes in line until the bounds of the map are reached`() {
        val input = """
            T.........
            ...T......
            ......#...
            .........#
        """.trimIndent().lines()
        val bounds = Bounds(input)

        val tFrequencyAntennae = bounds.filter { input[it] == 'T' }
        assert(tFrequencyAntennae.size == 2) { "precondition" }
        val expectedResult = bounds.filter { input[it] in setOf('T', '#') }.toSet()

        val result: Set<Point> = findAntinodesBetweenWithResonantHarmonics(tFrequencyAntennae[0], tFrequencyAntennae[1], bounds)

        assert(result == expectedResult)
    }

    @Test
    fun `findAntinodesBetween should return antinodes within the bounds of the grid when the x and y differences between the points have different sign`() {
        val input = """
            ..........
            ..........
            ..........
            ..........
            ........a.
            ....a.....
            #.........
            ..........
            ..........
            ..........
        """.trimIndent().lines()

        val antennaMap = parseAntennaLocations(input)
        val aLocations = antennaMap['a']!!
        assert(aLocations == setOf(Point(4,5), Point(8,4))) { "precondition" }
        val aLocationList = aLocations.toList()

        val expectedResult = setOf(Point(0, 6))

        val result : Set<Point> = findAntinodesBetween(aLocationList[0], aLocationList[1], Bounds(input))

        assert(result == expectedResult)
    }

    @Test
    fun `findAntinodes should return all antinodes between any two members of the set`() {
        val input = """
            ..........
            ...#......
            #.........
            ....a.....
            ........a.
            .....a....
            ..#.......
            ......A...
            ..........
            ..........
        """.trimIndent().lines()
        val bounds = Bounds(input)

        val antennaMap = parseAntennaLocations(input)
        val aLocations = antennaMap['a']!!
        val expectedResults = bounds. filter { input[it] in setOf('A', '#') }.toSet()

        val results : Set<Point> = findAntinodes(aLocations, bounds)

        assert(results == expectedResults)
    }

    @Test
    fun `findAntinodes should return all antinodes between any two members of the set with part2 semantics`() {
        val input = sampleInputPart2
        val bounds = Bounds(input)

        val tLocations = parseAntennaLocations(input)['T']!!
        val expectedResults = bounds. filter { input[it] in setOf('T', '#') }.toSet()

        val results : Set<Point> = findAntinodes(tLocations, bounds, ::findAntinodesBetweenWithResonantHarmonics)

        assert(results == expectedResults)
    }

    @Test
    fun `part1 should return the count of unique antinode locations`() {
        assert(solution.part1(sampleInput) == 14)
    }

    @Test
    fun `part2 should return the count of unique antinode locations including resonant harmonics`() {
        assert(solution.part2(sampleInput) == 34)
    }
}
