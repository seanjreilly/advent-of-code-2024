package day15

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.CardinalDirection
import utils.CardinalDirection.*
import utils.Point

class Day15SolutionTest {
    private val largeSampleInput = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().lines()

    private val smallSampleInput = """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########

        <^^>>>vv<v>>v<<
    """.trimIndent().lines()

    private val largeWarehouseFinalPosition = """
        ##########
        #.O.O.OOO#
        #........#
        #OO......#
        #OO@.....#
        #O#.....O#
        #O.....OO#
        #O.....OO#
        #OO....OO#
        ##########
        
    """.trimIndent().lines()

    private val transformedLargeWarehouseLayout = """
        ####################
        ##....[]....[]..[]##
        ##............[]..##
        ##..[][]....[]..[]##
        ##....[]@.....[]..##
        ##[]##....[]......##
        ##[]....[]....[]..##
        ##..[][]..[]..[][]##
        ##........[]......##
        ####################
    """.trimIndent().lines()

    private val solution = Day15Solution()

    // region parseMove tests

    @Test
    fun `parseMove should return North given ^`() {
        val direction: CardinalDirection = parseMove('^')
        assert(direction == North)
    }

    @Test
    fun `parseMove should return East given greater than symbol`() {
        val direction: CardinalDirection = parseMove('>')
        assert(direction == East)
    }

    @Test
    fun `parseMove should return South given v`() {
        val direction: CardinalDirection = parseMove('v')
        assert(direction == South)
    }

    @Test
    fun `parseMove should return West given less than symbol`() {
        val direction: CardinalDirection = parseMove('<')
        assert(direction == West)
    }

    @Test
    fun `parseMove should throw an IllegalArgumentException given any other character`() {
        val exception = assertThrows<IllegalArgumentException> { parseMove(('x')) }
        assert(exception.message != null)
        assert(exception.message!!.isNotEmpty())
    }

    // endregion

    @Test
    fun `parseMoves should return the list of CardinalDirections given smallInput`() {
        val expectedMoves = listOf(
            West,
            North,
            North,
            East,
            East,
            East,
            South,
            South,
            West,
            South,
            East,
            East,
            South,
            West,
            West
        )

        val moves: List<CardinalDirection> = parseMoves(smallSampleInput)

        assert(moves.size == expectedMoves.size)
        expectedMoves.forEachIndexed { index, direction ->
            assert(moves[index] == direction)
        }
    }

    @Test
    fun `parseMoves should return the expected list of moves given move input on multiple lines`() {
        val moves: List<CardinalDirection> = parseMoves(largeSampleInput)
        val firstThreeMoves = moves.take(3)
        val lastThreeMoves = moves.takeLast(3)

        assert(moves.size == 700)
        assert(firstThreeMoves == listOf(West, South, South))
        assert(lastThreeMoves == listOf(West, West, North))
    }

    @Nested
    inner class WarehouseTest {
        @Test
        fun `constructor should put the robot in the expected location given small sample input`() {
            val expectedRobotLocation = Point(2,2)
            val warehouse = Warehouse(smallSampleInput)

            val robotLocation = warehouse.robotLocation
            assert(robotLocation == expectedRobotLocation)
        }

        @Test
        fun `constructor should put the boxes in the expected locations given small sample input`() {
            val expectedBoxLocations = setOf(
                Point(3,1),
                Point(5,1),
                Point(4,2),
                Point(4,3),
                Point(4,4),
                Point(4,5),
            )
            val warehouse = Warehouse(smallSampleInput)

            val boxLocations:Set<Point> = warehouse.boxLocations
            assert(boxLocations == expectedBoxLocations)
        }

        @Test
        fun `constructor should build the walls correctly given small sample input`() {
            val warehouse = Warehouse(smallSampleInput)

            val wallLocations:Set<Point> = warehouse.wallLocations

            assert(wallLocations.size == 30)
            (0 until 8).forEach { x ->
                assert(Point(x, 0) in wallLocations) { "north wall should be present" }
                assert(Point(x, 7) in wallLocations) { "south wall should be present" }
            }

            (0 until 7).forEach { y ->
                assert(Point(0, y) in wallLocations) { "west wall should be present" }
                assert(Point(7, y) in wallLocations) { "east wall should be present" }
            }

            assert(Point(1, 2) in wallLocations) {" interior wall 1 "}
            assert(Point(2, 4) in wallLocations) {" interior wall 2 "}
        }

        @Test
        fun `constructor should put the robot in the expected location given large sample input`() {
            val expectedRobotLocation = Point(4,4)
            val warehouse = Warehouse(largeSampleInput)

            val robotLocation = warehouse.robotLocation
            assert(robotLocation == expectedRobotLocation)
        }

        // region move tests

        @Test
        fun `moveRobot should do nothing if there is a wall in this direction`() {
            val originalWarehouse = Warehouse(smallSampleInput)

            //there is a wall west of the robot
            val updatedWarehouse = Warehouse(smallSampleInput).moveRobot(West)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot if there's an empty space in that direction`() {
            val originalWarehouse = Warehouse(smallSampleInput)


            //there is an empty space south of the robot
            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse != originalWarehouse)
            assert(updatedWarehouse.robotLocation == originalWarehouse.robotLocation.south())
            assert(originalWarehouse.boxLocations == updatedWarehouse.boxLocations)
            assert(originalWarehouse.wallLocations == updatedWarehouse.wallLocations)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is next to a box and there's an empty space beyond the box`() {
            val originalWarehouse = Warehouse(smallSampleInput).moveRobot(East)

            val oldBoxLocation = Point(4, 2)
            val newBoxLocation = Point(5, 2)

            //there is a box east of the robot, and an empty space beyond that
            val updatedWarehouse = originalWarehouse.moveRobot(East)

            assert(updatedWarehouse != originalWarehouse)
            assert(updatedWarehouse.robotLocation == originalWarehouse.robotLocation.east())
            assert(updatedWarehouse.boxLocations.size == originalWarehouse.boxLocations.size)
            assert(oldBoxLocation !in updatedWarehouse.boxLocations)
            assert(newBoxLocation in updatedWarehouse.boxLocations)
            assert(originalWarehouse.wallLocations == updatedWarehouse.wallLocations)
        }

        @Test
        fun `moveRobot should not move anything if the robot is next to a box and there's a wall beyond the box`() {
            val originalWarehouse = Warehouse(smallSampleInput).moveRobot(East)

            //there is a box north of the robot, and a wall beyond that
            val updatedWarehouse = originalWarehouse.copy().moveRobot(North)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and all of the boxes if the robot is next to a row of boxes and there's an empty space beyond the boxes`() {
            val originalWarehouse = Warehouse(smallSampleInput).copy(robotLocation = Point(4, 1))

            //there is a row of 4 boxes south of the robot, and an empty space beyond that
            val updatedWarehouse = originalWarehouse.copy().moveRobot(South)

            assert(updatedWarehouse !== originalWarehouse)
            assert(updatedWarehouse.robotLocation == Point(4,2))
            assert(updatedWarehouse.robotLocation !in updatedWarehouse.boxLocations)
            assert(Point(4, 3) in updatedWarehouse.boxLocations)
            assert(Point(4, 4) in updatedWarehouse.boxLocations)
            assert(Point(4, 5) in updatedWarehouse.boxLocations)
            assert(Point(4, 6) in updatedWarehouse.boxLocations)
            assert(originalWarehouse.wallLocations == updatedWarehouse.wallLocations)
        }

        @Test
        fun `moveRobot should not move anything if the robot is next to a row of boxes and there's a wall beyond the boxes`() {
            val originalWarehouse = Warehouse(smallSampleInput).copy(robotLocation = Point(4, 1)).moveRobot(South)

            //there is a row of 4 boxes south of the robot, and a wall beyond that
            val updatedWarehouse = originalWarehouse.copy().moveRobot(South)

            assert(updatedWarehouse !== originalWarehouse)
            assert(updatedWarehouse == originalWarehouse)
        }

        // endregion

        @Test
        fun `totalScore should return the sum of all boxes scores`() {
            val warehouse = Warehouse(largeWarehouseFinalPosition)

            val score : Int = warehouse.totalScore()

            assert(score == 10092)
        }

        @Test
        fun `processing all moves should yield the expected warehouse layout given the large sample input`() {
            val expectedFinalWarehouse = Warehouse(largeWarehouseFinalPosition)
            val originalWarehouse = Warehouse(largeSampleInput)
            val moves = parseMoves(largeSampleInput)

            val finalWarehouse = moves.fold(originalWarehouse) { warehouse, direction -> warehouse.moveRobot(direction) }

            assert(finalWarehouse == expectedFinalWarehouse)
        }

        @Test
        fun `processing all moves should yield the expected total score given the small sample input`() {
            val originalWarehouse = Warehouse(smallSampleInput)
            val moves = parseMoves(smallSampleInput)

            val finalWarehouse = moves.fold(originalWarehouse) { warehouse, direction -> warehouse.moveRobot(direction) }

            assert(finalWarehouse.totalScore() == 2028)
        }
    }

    @Nested
    inner class Warehouse2Test {
        //region constructor tests

        @Test
        fun `constructor should put the robot in the expected location given small sample input`() {
            val expectedRobotLocation = Point(4,2)
            val warehouse = Warehouse2(smallSampleInput.transformWarehouseLayout())

            val robotLocation = warehouse.robotLocation
            assert(robotLocation == expectedRobotLocation)
        }

        @Test
        fun `constructor should put the boxes in the expected locations given small sample input`() {
            val expectedBoxLeftSideLocations = setOf(
                Point(6,1),
                Point(10,1),
                Point(8,2),
                Point(8,3),
                Point(8,4),
                Point(8,5),
            )
            val expectedBoxRightSideLocations = setOf(
                Point(7,1),
                Point(11,1),
                Point(9,2),
                Point(9,3),
                Point(9,4),
                Point(9,5),
            )
            val warehouse = Warehouse2(smallSampleInput.transformWarehouseLayout())

            val boxLeftSideLocations:Set<Point> = warehouse.boxLeftSideLocations
            assert(boxLeftSideLocations == expectedBoxLeftSideLocations)

            val boxRightSideLocations:Set<Point> = warehouse.boxRightSideLocations
            assert(boxRightSideLocations == expectedBoxRightSideLocations)
        }

        @Test
        fun `constructor should build the walls correctly given small sample input`() {
            val warehouse = Warehouse2(smallSampleInput.transformWarehouseLayout())

            val wallLocations:Set<Point> = warehouse.wallLocations

            assert(wallLocations.size == 60)
            (0 until 15).forEach { x ->
                assert(Point(x, 0) in wallLocations) { "north wall should be present" }
                assert(Point(x, 7) in wallLocations) { "south wall should be present" }
            }

            (0 until 7).forEach { y ->
                assert(Point(0, y) in wallLocations) { "first west wall should be present" }
                assert(Point(1, y) in wallLocations) { "second west wall should be present" }
                assert(Point(15, y) in wallLocations) { "first east wall should be present" }
                assert(Point(15, y) in wallLocations) { "second east wall should be present" }
            }

            assert(Point(2, 2) in wallLocations) {" interior wall 1a "}
            assert(Point(3, 2) in wallLocations) {" interior wall 1b "}
            assert(Point(4, 4) in wallLocations) {" interior wall 2a "}
            assert(Point(5, 4) in wallLocations) {" interior wall 2b "}
        }

        @Test
        fun `constructor should put the robot in the expected location given large sample input`() {
            val expectedRobotLocation = Point(8,4)
            val warehouse = Warehouse2(largeSampleInput.transformWarehouseLayout())

            val robotLocation = warehouse.robotLocation
            assert(robotLocation == expectedRobotLocation)
        }

        //endregion

        //region move tests

        //region moving east
        @Test
        fun `moveRobot should not move the robot east if there is a wall in this direction`() {
            val input = """
                ############
                ##..[][].@##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            //there is a wall west of the robot
            val updatedWarehouse = Warehouse2(input).moveRobot(East)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot east if there's an empty space in that direction`() {
            val input = """
                ############
                ##@.[][]..##
                ############
            """.trimIndent().lines()

            val expectedResult = """
                ############
                ##.@[][]..##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(East)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is moving east into a box and there's an empty space beyond the box`() {
            val input = """
                ############
                ##.@[]....##
                ############
            """.trimIndent().lines()

            val expectedResult = """
                ############
                ##..@[]...##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(East)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and all the boxes if the robot is moving east into a row of boxes and there's an empty space beyond the boxes`() {
            val input = """
                ############
                ##.@[][]..##
                ############
            """.trimIndent().lines()
            val originalWarehouse = Warehouse2(input)

            val expectedResult = """
                ############
                ##..@[][].##
                ############
            """.trimIndent().lines()
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(East)
            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should not move anything if the robot is moving east into a row of boxes and there's a wall beyond the boxes`() {
            val input = """
                ############
                ##...@[][]##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            val updatedWarehouse = Warehouse2(input).moveRobot(East)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }


        //endregion

        //region moving west
        @Test
        fun `moveRobot should move the robot west if there's an empty space in that direction`() {
            val input = """
                ############
                ##.@[][]..##
                ############
            """.trimIndent().lines()

            val expectedResult = """
                ############
                ##@.[][]..##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(West)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should not move the robot west if there's a wall in that direction`() {
            val input = """
                ############
                ##@.[][]..##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            val updatedWarehouse = Warehouse2(input).moveRobot(West)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and box west if there's a box and an empty space in that direction`() {
            val input = """
                ############
                ##.[]@....##
                ############
            """.trimIndent().lines()

            val expectedResult = """
                ############
                ##[]@.....##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(West)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and boxes west if there are several box in that direction, followed by an empty space`() {
            val input = """
                ############
                ##.[][]@..##
                ############
            """.trimIndent().lines()

            val expectedResult = """
                ############
                ##[][]@...##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(West)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should not move anything if the robot is moving west into a row of boxes and there's a wall beyond the boxes`() {
            val input = """
                ############
                ##[][]@...##
                ############
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            val updatedWarehouse = Warehouse2(input).moveRobot(West)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }
        //endregion

        //region moving north
        @Test
        fun `moveRobot should not move the robot north if there is a wall in this direction`() {
            val input = """
                ######
                ##.@##
                ##..##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            //there is a wall west of the robot
            val updatedWarehouse = Warehouse2(input).moveRobot(North)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot north if there's an empty space in that direction`() {
            val input = """
                ######
                ##..##
                ##.@##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##.@##
                ##..##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is moving north into the left side of a box and there's an empty space beyond the box`() {
            val input = """
                ######
                ##..##
                ##[]##
                ##@.##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##[]##
                ##@.##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is moving north into the right side of a box and there's an empty space beyond the box`() {
            val input = """
                ######
                ##..##
                ##[]##
                ##.@##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##[]##
                ##.@##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and boxes if the robot is moving north into the left side of a column of boxes and there's empty spaces beyond the boxes`() {
            val input = """
                ######
                ##..##
                ##[]##
                ##[]##
                ##@.##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##[]##
                ##[]##
                ##@.##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and boxes if the robot is moving north into the right side of a column of boxes and there's empty spaces beyond the boxes`() {
            val input = """
                ######
                ##..##
                ##[]##
                ##[]##
                ##.@##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##[]##
                ##[]##
                ##.@##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and three boxes if the robot is moving north into the left bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##......##
                ##.[][].##
                ##..[]..##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##.[][].##
                ##..[]..##
                ##..@...##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and three boxes if the robot is moving north into the right bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##......##
                ##.[][].##
                ##..[]..##
                ##...@..##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##.[][].##
                ##..[]..##
                ##...@..##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and multiple boxes if the robot is moving north into the left bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##......##
                ##..[][]##
                ##...[].##
                ##..[]..##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##..[][]##
                ##...[].##
                ##..[]..##
                ##..@...##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and seven boxes if the robot is moving north into a big triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##......##
                ##[][][]##
                ##.[][].##
                ##..[]..##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##[][][]##
                ##.[][].##
                ##..[]..##
                ##..@...##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should not move anything when the robot is moving north into a bunch of boxes with a wall to the north`() {
            val input = """
                ##########
                ##....#.##
                ##[][][]##
                ##.[][].##
                ##..[]..##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val updatedWarehouse = Warehouse2(input).moveRobot(North)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should not move anything when the robot is moving north into a weird bunch of boxes with a wall`() {
            val input = """
                ##########
                ##......##
                ##[][][]##
                ##.[]#..##
                ##..[]..##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val updatedWarehouse = Warehouse2(input).moveRobot(North)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move without moving the box when the robot is moving north into a space beside a box`() {
            val input = """
                ##########
                ##...[].##
                ##..@...##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##..@[].##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(North)

            assert(updatedWarehouse == expectedWarehouse)
        }
        //endregion

        //region moving south
        @Test
        fun `moveRobot should not move the robot south if there is a wall in this direction`() {
            val input = """
                ######
                ##..##
                ##..##
                ##..##
                ##.@##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)

            //there is a wall west of the robot
            val updatedWarehouse = Warehouse2(input).moveRobot(South)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move the robot south if there's an empty space in that direction`() {
            val input = """
                ######
                ##..##
                ##..##
                ##.@##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##..##
                ##..##
                ##..##
                ##.@##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is moving south into the left side of a box and there's an empty space beyond the box`() {
            val input = """
                ######
                ##@.##
                ##[]##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##..##
                ##@.##
                ##[]##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move a robot and a box if the robot is moving south into the right side of a box and there's an empty space beyond the box`() {
            val input = """
                ######
                ##.@##
                ##[]##
                ##..##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##..##
                ##.@##
                ##[]##
                ##..##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and boxes if the robot is moving south into the left side of a column of boxes and there's empty spaces beyond the boxes`() {
            val input = """
                ######
                ##@.##
                ##[]##
                ##[]##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##..##
                ##@.##
                ##[]##
                ##[]##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and boxes if the robot is moving south into the right side of a column of boxes and there's empty spaces beyond the boxes`() {
            val input = """
                ######
                ##.@##
                ##[]##
                ##[]##
                ##..##
                ######
            """.trimIndent().lines()

            val expectedResult = """
                ######
                ##..##
                ##.@##
                ##[]##
                ##[]##
                ######
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and three boxes if the robot is moving south into the left bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##..@...##
                ##..[]..##
                ##.[][].##
                ##......##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##......##
                ##..@...##
                ##..[]..##
                ##.[][].##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and three boxes if the robot is moving south into the right bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##...@..##
                ##..[]..##
                ##.[][].##
                ##......##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##......##
                ##...@..##
                ##..[]..##
                ##.[][].##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and multiple boxes if the robot is moving south into the left bottom of a triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##..@...##
                ##..[]..##
                ##...[].##
                ##..[][]##
                ##......##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##......##
                ##..@...##
                ##..[]..##
                ##...[].##
                ##..[][]##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should move the robot and seven boxes if the robot is moving south into a big triangle of boxes with empty spaces beyond them`() {
            val input = """
                ##########
                ##..@...##
                ##..[]..##
                ##.[][].##
                ##[][][]##
                ##......##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##......##
                ##..@...##
                ##..[]..##
                ##.[][].##
                ##[][][]##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        @Test
        fun `moveRobot should not move anything when the robot is moving south into a bunch of boxes with a wall to the north`() {
            val input = """
                ##########
                ##..@...##
                ##..[]..##
                ##.[][].##
                ##[][][]##
                ##....#.##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val updatedWarehouse = Warehouse2(input).moveRobot(South)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should not move anything when the robot is moving south into a weird bunch of boxes with a wall`() {
            val input = """
                ##########
                ##..@...##
                ##..[]..##
                ##.[]#..##
                ##[][][]##
                ##......##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val updatedWarehouse = Warehouse2(input).moveRobot(South)

            assert(updatedWarehouse == originalWarehouse)
            assert(updatedWarehouse !== originalWarehouse)
        }

        @Test
        fun `moveRobot should move without moving the box when the robot is moving south into a space beside a box`() {
            val input = """
                ##########
                ##..@...##
                ##...[].##
                ##########
            """.trimIndent().lines()

            val expectedResult = """
                ##########
                ##......##
                ##..@[].##
                ##########
            """.trimIndent().lines()

            val originalWarehouse = Warehouse2(input)
            val expectedWarehouse = Warehouse2(expectedResult)

            val updatedWarehouse = originalWarehouse.moveRobot(South)

            assert(updatedWarehouse == expectedWarehouse)
        }

        //endregion

        //endregion

        @Test
        fun `processing all moves should yield the expected warehouse layout given part2 sample input`() {
            val input = """
                #######
                #...#.#
                #.....#
                #..OO@#
                #..O..#
                #.....#
                #######

                <vv<<^^<<^^
            """.trimIndent().lines()
            val warehouse = Warehouse2(input.transformWarehouseLayout())
            val moves = parseMoves(input)

            val expectedResultLayout = """
                ##############
                ##...[].##..##
                ##...@.[]...##
                ##....[]....##
                ##..........##
                ##..........##
                ##############
            """.trimIndent().lines()
            val expectedResult = Warehouse2(expectedResultLayout)

            val result = moves.fold(warehouse) { warehouse, direction -> warehouse.moveRobot(direction) }
            assert(result == expectedResult)
        }

        @Test
        fun `processing all moves should yield the expected warehouse layout given the large sample input`() {
            val expectedFinalWarehouseLayout = """
                ####################
                ##[].......[].[][]##
                ##[]...........[].##
                ##[]........[][][]##
                ##[]......[]....[]##
                ##..##......[]....##
                ##..[]............##
                ##..@......[].[][]##
                ##......[][]..[]..##
                ####################
            """.trimIndent().lines()
            val expectedFinalWarehouse = Warehouse2(expectedFinalWarehouseLayout)
            val originalWarehouse = Warehouse2(transformedLargeWarehouseLayout)
            val moves = parseMoves(largeSampleInput)

            val finalWarehouse = moves.fold(originalWarehouse) { warehouse, direction -> warehouse.moveRobot(direction) }

            assert(finalWarehouse == expectedFinalWarehouse)
        }

        @Test
        fun `totalScore should return total score for the expected final layout`() {
            val expectedFinalWarehouseLayout = """
                ####################
                ##[].......[].[][]##
                ##[]...........[].##
                ##[]........[][][]##
                ##[]......[]....[]##
                ##..##......[]....##
                ##..[]............##
                ##..@......[].[][]##
                ##......[][]..[]..##
                ####################
            """.trimIndent().lines()
            val finalWarehouse = Warehouse2(expectedFinalWarehouseLayout)
            val score = finalWarehouse.totalScore()

            assert(score == 9021)
        }
    }

    @Nested
    inner class PointTest {
        @Test
        fun `score should return 100 times y plus x`() {
            val point = Point(4,1)
            assert(point.score() == 104)
        }

        @Test
        fun `score should return 100 times y plus x given a different point`() {
            val point = Point(1,4)
            assert(point.score() == 401)
        }
    }

    @Test
    fun `part1 should parse the warehouse, apply all the moves and return the total score given the small sample input`() {
        assert(solution.part1(smallSampleInput) == 2028)
    }

    @Test
    fun `part1 should parse the warehouse, apply all the moves and return the total score given the large sample input`() {
        assert(solution.part1(largeSampleInput) == 10092)
    }

    @Test
    fun `part2 should parse the doubled warehouse, apply all the moves and return the total score given the large sample input`() {
        assert(solution.part2(largeSampleInput) == 9021)
    }

    @Test
    fun `List of String dot transformWarehouseLayout should double the width of everything`() {
        val result = largeSampleInput.transformWarehouseLayout()
        assert(result == transformedLargeWarehouseLayout)
    }

}