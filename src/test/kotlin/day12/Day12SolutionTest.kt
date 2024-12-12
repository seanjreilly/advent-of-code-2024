package day12

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.Bounds
import utils.get

class Day12SolutionTest {
    private val smallSampleInput = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()

    private val mediumSampleInput = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent().lines()

    private val largeSampleInput = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent().lines()

    private val solution = Day12Solution()

    @Nested
    inner class RegionTest {

        @Test
        fun `constructor should throw an exception given non-contiguous plots`() {
            val nonContiguousPlots = setOf(Plot(0,0), Plot(3,3))
            val expectedException: IllegalArgumentException = assertThrows { Region('A', nonContiguousPlots) }
            assert(expectedException.message!!.contains("Plots in a region must be contiguous"))
        }

        @Test
        fun `constructor should throw an exception given no plots`() {
            val expectedException: IllegalArgumentException = assertThrows { Region('A', emptySet<Plot>()) }
            assert(expectedException.message!!.contains("A region must contain at least one plot"))
        }

        @Test
        fun `area should return the number of plots in the region`() {
            assert(Region('A', Plot(0, 0)).area == 1)
            assert(Region('A', Plot(0, 0), Plot(1, 0)).area == 2)
            assert(Region('A', Plot(0, 0), Plot(1, 0), Plot(0, 1)).area == 3)
        }

        @Test
        fun `perimeter should return 4 given a region containing a single plot`() {
            assert(Region('A', Plot(0, 0)).perimeter() == 4)
        }

        @Test
        fun `perimeter should return 6 given a region containing 2 plots`() {
            assert(Region('A', Plot(0, 0), Plot(1, 0)).perimeter() == 6)
        }

        @Test
        fun `perimeter should return 10 given a region containing 4 plots in a line`() {
            val region = findRegionInSampleInput('A')

            assert(region.perimeter() == 10)
        }

        @Test
        fun `perimeter should return 8 given a region containing 4 plots in a square`() {
            val region = findRegionInSampleInput('B')

            assert(region.perimeter() == 8)
        }

        @Test
        fun `perimeter should return the appropriate results for the small sample`() {
            assert(findRegionInSampleInput('A').perimeter() == 10)
            assert(findRegionInSampleInput('B').perimeter() == 8)
            assert(findRegionInSampleInput('C').perimeter() == 10)
            assert(findRegionInSampleInput('D').perimeter() == 4)
            assert(findRegionInSampleInput('E').perimeter() == 8)
        }

        @Test
        fun `perimeter should consider the outer and inner borders for a region`() {
            val oRegion = findRegionInSampleInput('O', mediumSampleInput)
            assert(oRegion.perimeter() == 36)
        }
        
        @Test
        fun `fencePrice should return the area times the perimeter`() {
            assert(findRegionInSampleInput('A').fencePrice() == 40)
            assert(findRegionInSampleInput('B').fencePrice() == 32)
            assert(findRegionInSampleInput('C').fencePrice() == 40)
            assert(findRegionInSampleInput('D').fencePrice() == 4)
            assert(findRegionInSampleInput('E').fencePrice() == 24)

            assert(findRegionInSampleInput('O', mediumSampleInput).fencePrice() == 756)

            assert(findRegionInSampleInput('R', largeSampleInput).fencePrice() == 216)
        }
    }

    @Test
    fun `findRegions should return 5 regions given small sample input`() {
        val expectedResult = setOf(
            findRegionInSampleInput('A'),
            findRegionInSampleInput('B'),
            findRegionInSampleInput('C'),
            findRegionInSampleInput('D'),
            findRegionInSampleInput('E'),
        )

        val result = findRegions(smallSampleInput)

        val resultAsSet = result.toSet()
        assert(resultAsSet == expectedResult)
    }

    @Test
    fun `findRegions should return 5 regions given medium sample input`() {
        val expectedResult = setOf(
            findRegionInSampleInput('O', mediumSampleInput),
            Region('X', Plot(1, 1)),
            Region('X', Plot(1, 3)),
            Region('X', Plot(3, 1)),
            Region('X', Plot(3, 3))
        )

        val result = findRegions(mediumSampleInput)

        val resultAsSet = result.toSet()
        assert(resultAsSet == expectedResult)
    }

    @Test
    fun `findRegions should return 11 regions given large sample input`() {
        val result = findRegions(largeSampleInput)
        assert(result.size == 11)
    }

    @Test
    fun `part1 should find all of the regions and return the sum of their fence prices`() {
        assert(solution.part1(largeSampleInput) == 1930L)
    }

    private fun findRegionInSampleInput(plantType: Char, map: List<String> = smallSampleInput): Region {
        val bounds = Bounds(map)
        val plots = bounds.filter { point: Plot -> map[point] == plantType }.toSet()
        return Region(plantType, plots)
    }

}