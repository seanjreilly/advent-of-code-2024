package day14

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.Bounds
import utils.Point

private val TEST_BOUNDS = Bounds(0..11, 0..7)
private val NOT_MOVING = Velocity(0, 0)

class Day14SolutionTest {
    private val sampleInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
        """.trimIndent().lines()

    private val solution = Day14Solution(TEST_BOUNDS)



    @Nested
    inner class RobotTest {
        @Test
        @Suppress("USELESS_IS_CHECK")
        fun `constructor should return a valid robot given a line of input`() {
            val inputLine = sampleInput.first()
            val robot = Robot(inputLine)

            assert(robot.position is Point)
            assert(robot.position.x == 0)
            assert(robot.position.y == 4)
            assert(robot.velocity is Velocity)
            assert(robot.velocity.dX == 3)
            assert(robot.velocity.dY == -3)
        }

        @Test
        fun `constructor should work when all velocity values are negative`() {
            val input = "p=3,0 v=-2,-2"
            val expectedRobot = Robot(Point(3,0), Velocity(-2, -2))
            val robot = Robot(input)

            assert(robot == expectedRobot)
        }

        @Test
        fun `constructor should work when all velocity values are positive`() {
            val input = "p=9,3 v=2,3"
            val expectedRobot = Robot(Point(9,3), Velocity(2, 3))
            val robot = Robot(input)

            assert(robot == expectedRobot)
        }
        
        @Test
        fun `move should update the robot's position by adding the robot's velocity`() {
            val robot = Robot("p=2,4 v=2,-3")
            val expectedPosition = Point(4,1)

            robot.move(TEST_BOUNDS)

            assert(robot.position == expectedPosition)
        }

        @Test
        fun `move should wrap within the bounds on the y axis`() {
            val robot = Robot("p=2,4 v=2,-3")

            val expectedPositions = listOf(
                Point(4,1),
                Point(6,5),
                Point(8,2)
            )

            robot.move(TEST_BOUNDS)
            assert(robot.position == expectedPositions[0])

            robot.move(TEST_BOUNDS)
            assert(robot.position == expectedPositions[1])

            robot.move(TEST_BOUNDS)
            assert(robot.position == expectedPositions[2])
        }

        @Test
        fun `move should wrap within the bounds on the x axis`() {
            val robot = Robot("p=0,0 v=-1,0")
            val expectedPosition = Point(10, 0)
            robot.move(TEST_BOUNDS)

            assert(robot.position == expectedPosition)
        }

        @Test
        fun `quadrant should return TOP_LEFT if the robot is in the top left quadrant of the bounds`() {
            assert(testRobot(0, 0).quadrant(TEST_BOUNDS) == Quadrant.TOP_LEFT)
            assert(testRobot(4, 0).quadrant(TEST_BOUNDS) == Quadrant.TOP_LEFT)
            assert(testRobot(0, 2).quadrant(TEST_BOUNDS) == Quadrant.TOP_LEFT)
            assert(testRobot(4, 2).quadrant(TEST_BOUNDS) == Quadrant.TOP_LEFT)
            assert(testRobot(1, 1).quadrant(TEST_BOUNDS) == Quadrant.TOP_LEFT)
        }

        @Test
        fun `quadrant should return TOP_RIGHT if the robot is in the top right quadrant of the bounds`() {
            assert(testRobot(6, 0).quadrant(TEST_BOUNDS) == Quadrant.TOP_RIGHT)
            assert(testRobot(10, 0).quadrant(TEST_BOUNDS) == Quadrant.TOP_RIGHT)
            assert(testRobot(6, 2).quadrant(TEST_BOUNDS) == Quadrant.TOP_RIGHT)
            assert(testRobot(10, 2).quadrant(TEST_BOUNDS) == Quadrant.TOP_RIGHT)
            assert(testRobot(7, 1).quadrant(TEST_BOUNDS) == Quadrant.TOP_RIGHT)
        }

        @Test
        fun `quadrant should return BOTTOM_LEFT if the robot is in the bottom left quadrant of the bounds`() {
            assert(testRobot(0, 4).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_LEFT)
            assert(testRobot(4, 4).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_LEFT)
            assert(testRobot(0, 6).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_LEFT)
            assert(testRobot(4, 6).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_LEFT)
            assert(testRobot(1, 5).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_LEFT)
        }

        @Test
        fun `quadrant should return BOTTOM_RIGHT if the robot is in the top right quadrant of the bounds`() {
            assert(testRobot(6, 4).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_RIGHT)
            assert(testRobot(10, 4).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_RIGHT)
            assert(testRobot(6, 6).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_RIGHT)
            assert(testRobot(10, 6).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_RIGHT)
            assert(testRobot(7, 5).quadrant(TEST_BOUNDS) == Quadrant.BOTTOM_RIGHT)
        }

        @Test
        fun `quadrant should return null if the robot is in the center column`() {
            assert(testRobot(5, 0).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 1).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 2).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 4).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 5).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 6).quadrant(TEST_BOUNDS) == null)
        }

        @Test
        fun `quadrant should return null if the robot is in the center row`() {
            assert(testRobot(0, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(1, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(2, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(3, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(4, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(5, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(6, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(7, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(8, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(9, 3).quadrant(TEST_BOUNDS) == null)
            assert(testRobot(10, 3).quadrant(TEST_BOUNDS) == null)
        }
    }

    @Test
    fun `parseRobots should return a list of Robots`() {
        val expectedRobots = listOf(
            Robot(Point(0, 4), Velocity(3,-3)),
            Robot(Point(6, 3), Velocity(-1,-3)),
            Robot(Point(10, 3), Velocity(-1,2)),
            Robot(Point(2, 0), Velocity(2,-1)),
            Robot(Point(0, 0), Velocity(1,3)),
            Robot(Point(3, 0), Velocity(-2,-2)),
            Robot(Point(7, 6), Velocity(-1,-3)),
            Robot(Point(3, 0), Velocity(-1,-2)),
            Robot(Point(9, 3), Velocity(2,3)),
            Robot(Point(7, 3), Velocity(-1,2)),
            Robot(Point(2, 4), Velocity(2,-3)),
            Robot(Point(9, 5), Velocity(-3,-3))
        )

        val robots : List<Robot> = parseRobots(sampleInput)

        assert(robots == expectedRobots)
    }

    @Test
    fun `List of Robot dot safetyFactor should count how many robots are in each quadrant and return the product, given a bounds`() {
        val robots = listOf(
            testRobot(6, 0),
            testRobot(6, 0),
            testRobot(9, 0),

            testRobot(0, 2),


            testRobot(1, 3),
            testRobot(2, 3),


            testRobot(5, 4),

            testRobot(3, 5),
            testRobot(4, 5),
            testRobot(4, 5),

            testRobot(1, 6),
            testRobot(6, 6)
        )

        val safetyFactor: Int = robots.safetyFactor(TEST_BOUNDS)
        assert(safetyFactor == 12)
    }
    
    @Test
    fun `bounds should return test bounds when provided to solution constructor`() {
        val solution = Day14Solution(TEST_BOUNDS)
        assert(solution.bounds == TEST_BOUNDS)
    }

    @Test
    fun `bounds should return prod bounds when solution constructor is called without bounds`() {
        val solution = Day14Solution()
        assert(solution.bounds == PROD_BOUNDS)
    }

    @Test
    fun `findAdjacentPoints should return distinct regions of adjacent points`() {
        val firstSquare = listOf(
            Point(0,0),
            Point(1,0),
            Point(0,1),
            Point(1,1),
        )

        val secondSquare = listOf(
            Point(9,9),
            Point(10,9),
            Point(9,10),
            Point(10,10),
        )

        val soloPoint = Point(5,5)

        val points = (firstSquare + secondSquare + soloPoint).shuffled()

        val result : Collection<AdjacentRobots> = findAdjacentRobots(points)

        assert(result.size == 3)
        assert(result.count { it.size == 1 } == 1)
        assert(result.count { it.size == 4 } == 2)
    }

    @Test
    fun `part1 should parse the robots, move each one 100 times, and compute the total safety factor`() {
        assert(solution.part1(sampleInput) == 12)
    }

    @Test
    fun `part2 should parse the robots, move each one 7500 times, and find the round with the biggest clump of robots`() {
        assert(solution.part2(sampleInput) == 6)
    }

    private fun testRobot(x: Int, y: Int): Robot = Robot(Point(x, y), NOT_MOVING)
}