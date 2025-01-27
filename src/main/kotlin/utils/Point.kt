package utils

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    fun north() = Point(x, y - 1)
    fun northEast() = Point(x + 1, y - 1)
    fun east() = Point(x + 1, y)
    fun southEast() = Point(x + 1, y + 1)
    fun south() = Point(x, y + 1)
    fun southWest() = Point(x - 1, y + 1)
    fun west() = Point(x - 1, y)
    fun northWest() = Point(x - 1, y - 1)

    fun getCardinalNeighbours() : Collection<Point> {
        return listOf(
            north(),
            south(),
            east(),
            west(),
        )
    }

    fun getCardinalAndDiagonalNeighbours() : Collection<Point> {
        return getCardinalNeighbours() + getDiagonalNeighbours()
    }

    fun getDiagonalNeighbours() : Collection<Point> {
        return listOf(
            northEast(),
            northWest(),
            southEast(),
            southWest()
        )
    }

    fun manhattanDistance(other: Point): Int {
        return abs(other.x - x) + abs(other.y - y)
    }

    fun pointsWithManhattanDistance(manhattanDistance: Int): Sequence<Point> {
        return (0..manhattanDistance)
            .asSequence()
            .flatMap { xAdjustment ->
                val yAdjustment = manhattanDistance - xAdjustment
                sequenceOf(
                    Point(this.x + xAdjustment, this.y + yAdjustment),
                    Point(this.x + xAdjustment, this.y - yAdjustment),
                    Point(this.x - xAdjustment, this.y + yAdjustment),
                    Point(this.x - xAdjustment, this.y - yAdjustment),
                )
            }
    }

    fun move(cardinalDirection: CardinalDirection): Point {
        return cardinalDirection.moveOperation.invoke(this)
    }

    fun move(direction: Direction): Point {
        return direction.moveOperation.invoke(this)
    }

    infix fun facing(direction: CardinalDirection) = PointAndDirection(this, direction)
}

enum class TurnDirection {
    Left, Right
}

enum class CardinalDirection(internal val moveOperation: (Point) -> Point) {
    North(Point::north),
    East(Point::east),
    South(Point::south),
    West(Point::west);

    fun turn(direction: TurnDirection): CardinalDirection {
        var index = entries.indexOf(this)
        when (direction) {
            TurnDirection.Left -> index--
            TurnDirection.Right -> index++
        }
        return entries[index.mod(entries.size)]
    }

    fun opposite(): CardinalDirection {
        val index = entries.indexOf(this) + 2
        return entries[index.mod(entries.size)]
    }
}

enum class DiagonalDirection(internal val moveOperation: (Point) -> Point) {
    NorthEast(Point::northEast),
    SouthEast(Point::southEast),
    SouthWest(Point::southWest),
    NorthWest(Point::northWest);

    fun opposite(): DiagonalDirection {
        val index = DiagonalDirection.entries.indexOf(this) + 2
        return DiagonalDirection.entries[index.mod(entries.size)]
    }
}

enum class Direction(internal val moveOperation: (Point) -> Point) {
    North(Point::north),
    NorthEast(Point::northEast),
    East(Point::east),
    SouthEast(Point::southEast),
    South(Point::south),
    SouthWest(Point::southWest),
    West(Point::west),
    NorthWest(Point::northWest);

    fun opposite(): Direction {
        val index = Direction.entries.indexOf(this) + 4
        return Direction.entries[index.mod(entries.size)]
    }
}

data class PointAndDirection(val point: Point, val direction: CardinalDirection) {
    fun move() : PointAndDirection = this.copy(point = this.point.move(direction))
}

data class Bounds(val validXCoordinates: IntRange, val validYCoordinates: IntRange) : Iterable<Point> {

    //convenience constructors
    constructor(data: Array<IntArray>): this(data.first().indices, data.indices)
    constructor(data: Array<CharArray>): this(data.first().indices, data.indices)
    constructor(data: Array<Array<*>>): this(data.first().indices, data.indices)
    constructor(data: Array<CharSequence>): this(data.first().indices, data.indices)
    constructor(data: List<CharSequence>): this(data.first().indices, data.indices)

    val lastX = validXCoordinates.last
    val lastY = validYCoordinates.last
    operator fun contains(point: Point) = point.x in validXCoordinates && point.y in validYCoordinates

    override fun iterator() = iterator {
        for (x in validXCoordinates) {
            for (y in validYCoordinates) {
                yield(Point(x, y))
            }
        }
    }

    fun shrink(): Bounds {
        val newXRange = (validXCoordinates.first + 1)..(validXCoordinates.last - 1)
        val newYRange = (validYCoordinates.first + 1)..(validYCoordinates.last - 1)
        return Bounds(newXRange, newYRange)
    }
}

/*
    Common extension functions for accessing a nested array with a point
 */
operator fun <T> Array<Array<T>>.get(p: Point) : T = this[p.y][p.x]
operator fun Array<CharArray>.get(p: Point) = this[p.y][p.x]
operator fun Array<IntArray>.get(p: Point) = this[p.y][p.x]
operator fun List<String>.get(p: Point) : Char = this[p.y][p.x]