data class Coord(val x: Int, val y: Int) {
    inline fun forEachAdjacent(block: (coord: Coord) -> Unit) {
        (x - 1..x + 1).forEach { i ->
            block(Coord(i, y - 1))
            block(Coord(i, y + 1))
        }
        block(Coord(x - 1, y))
        block(Coord(x + 1, y))
    }
}


enum class Direction(val dx: Int, val dy: Int) {
    N(0, -1),
    S(0, 1),
    W(-1, 0),
    E(1, 0),

    NW(-1, -1),
    NE(1, -1),
    SW(-1, 1),
    SE(1, 1);

    companion object {
        val straightEntries = listOf(N, W, S, E)
    }

    fun go(source: Coord): Coord = Coord(source.x + dx, source.y+dy)

}