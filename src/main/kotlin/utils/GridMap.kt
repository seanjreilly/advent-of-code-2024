package utils

abstract class GridMap<T>(protected val data : Array<Array<T>>, private val getNeighboursMethod: (Point) -> Collection<Point>) : Iterable<Point> {
    val bounds = Bounds(data.first().indices, data.indices)
    val bottomRightCorner = Point(bounds.validXCoordinates.last, bounds.validYCoordinates.last)

    init {
        //ensure the map is rectangular
        check(data.all { it.size == data.first().size }) {"every row must be the same size"}
    }

    operator fun get(point: Point): T = data[point]

    open fun getNeighbours(point: Point): Collection<Point> {
        return getNeighboursMethod(point)
            .filter { contains(it) }
    }

    operator fun contains(point: Point): Boolean  = point in bounds

    override fun iterator() = bounds.iterator()
}

fun List<String>.toCharGrid() : Array<Array<Char>> = this.map { line -> line.toCharArray().toTypedArray() }.toTypedArray()

inline fun <reified T> List<String>.toGrid(transform: (Char) -> T) : Array<Array<T>> {
    return this
        .map { line -> line.map(transform).toTypedArray() }
        .toTypedArray()
}