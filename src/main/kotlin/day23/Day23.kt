package day23

import Coord
import Direction
import day10.get
import day10.getOrNull
import java.lang.RuntimeException

fun main() {
    val forest = input.lines().map { it.toList() }
    val start = Coord(1, 0)
    val longestWay = findWay(forest, start, mutableListOf())
    longestWay?.let { printWay(forest, it, null)}
    println("Steps: ${longestWay!!.size - 1}")
}

private fun findWay(
    forest: List<List<Char>>,
    start: Coord,
    visited: MutableList<Coord>
): List<Coord>? {
    visited.add(start)
    val endY = forest.size - 1
    var position = start
    while (position.y != endY) {
        val nextOptions = findNextPositions(position, forest, visited)
        if (nextOptions.isEmpty()) {
            return null
        }
        if (nextOptions.size == 1) {
            position = nextOptions.first()
            visited.add(position)
        } else {
            return nextOptions.mapNotNull {next ->
                val optionalVisited = ArrayList(visited)
                findWay(forest, next, optionalVisited)
            }.sortedBy { it.size }.last()
        }
    }
    return visited
}

fun findNextPositions(source: Coord, forest: List<List<Char>>, visited: MutableList<Coord>): List<Coord> {
    val field = forest.get(source)
    return when (field) {
        '>' -> listOf(Direction.E.go(source))
        '<' -> listOf(Direction.W.go(source))
        '^' -> listOf(Direction.N.go(source))
        'v' -> listOf(Direction.S.go(source))
        '.' -> {
            Direction.straightEntries.map { dir ->
                dir.go(source)
            }.filter { next ->
                isDirectionPossible(next, forest, visited)
            }
        }
        else -> throw RuntimeException("Invalid coord $source")
    }.filter { !visited.contains(it) }
}

fun isDirectionPossible(next: Coord, forest: List<List<Char>>, steps: MutableList<Coord>): Boolean {
    val field = forest.getOrNull(next)
    return field != null && field != '#'
}


fun printWay(forest: List<List<Char>>, longestWay: List<Coord>, mark: Coord?) {
    val text = forest.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (Coord(x, y) == mark)
                "X"
            else if (longestWay.contains(Coord(x, y)))
                "O"
            else c
        }.joinToString("")
    }.joinToString("\n")
    println(text)
}


private val dummmy = """
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
""".trimIndent()

private val input = """
#.###########################################################################################################################################
#.#####.....................#.............#.......#...#...###...###.....#.......#...###...#...#.....#...............#...#.........#...#...###
#.#####.###################.#.###########.#.#####.#.#.#.#.###.#.###.###.#.#####.#.#.###.#.#.#.#.###.#.#############.#.#.#.#######.#.#.#.#.###
#...###.#...#...............#...........#.#.#.....#.#.#.#.###.#.#...#...#...#...#.#.#...#...#.#...#.#.....#.........#.#...#.....#.#.#.#.#.###
###.###.#.#.#.#########################.#.#.#.#####.#.#.#.###.#.#.###.#####.#.###.#.#.#######.###.#.#####.#.#########.#####.###.#.#.#.#.#.###
###.....#.#.#.......###...#...#...#...#.#.#.#.......#.#.#...#.#.#...#...#...#...#.#.#.......#.#...#.>.>...#.....#...#.....#...#...#.#.#.#.###
#########.#.#######.###.#.#.#.#.#.#.#.#.#.#.#########.#.###.#.#.###.###.#.#####.#.#.#######.#.#.#####v#########.#.#.#####.###.#####.#.#.#.###
#.........#.........#...#.#.#.#.#.#.#...#...#.........#...#...#...#.#...#...#...#.#.###.....#.#.#.....#.........#.#.#...#.#...#...#.#...#...#
#.###################.###.#.#.#.#.#.#########.###########.#######.#.#.#####.#.###.#.###.#####.#.#.#####.#########.#.#.#.#.#.###.#.#.#######.#
#.#...#...#...###...#...#.#.#.#.#.#.....#...#.#...#.>.>...#...#...#.#...#...#...#.#...#.....#...#.....#...#.....#.#.#.#.#.#.....#.#.....#...#
#.#.#.#.#.#.#.###.#.###.#.#.#.#.#.#####.#.#.#.#.#.#.#v#####.#.#.###.###.#.#####.#.###.#####.#########.###.#.###.#.#.#.#.#.#######.#####.#.###
#...#...#...#...#.#...#.#.#.#.#.#.#...#...#.#.#.#.#.#.#...#.#...###...#.#.#.....#.#...#.....#...#.....###...#...#.#.#.#...#.......#####.#...#
###############.#.###.#.#.#.#.#.#.#.#.#####.#.#.#.#.#.#.#.#.#########.#.#.#.#####.#.###.#####.#.#.###########.###.#.#.#####.###########.###.#
#...............#.#...#.#.#.#...#.#.#.#.....#...#...#...#.#.#...#...#.#.#.#.>.>.#.#.#...#.....#.#.......#.....#...#.#.#.....###...###...#...#
#.###############.#.###.#.#.#####.#.#.#.#################.#.#.#.#.#.#.#.#.###v#.#.#.#.###.#####.#######.#.#####.###.#.#.#######.#.###.###.###
#...#...#...#...#.#.###.#.#.....#.#.#.#.......#...........#...#...#.#.#.#.#...#...#.#...#.#...#.........#.#...#...#.#.#.#...###.#.###.#...###
###.#.#.#.#v#.#.#.#.###.#.#####.#.#.#.#######.#.###################.#.#.#.#.#######.###.#.#.#.###########.#.#.###.#.#.#.#.#.###.#.###.#.#####
#...#.#...#.>.#...#...#.#...#...#...#...>.>.#.#.#.................#.#.#.#.#.....###.....#...#...#.......#.#.#.#...#.#.#.#.#.#...#.#...#...###
#.###.#####v#########.#.###.#.###########v#.#.#.#.###############.#.#.#.#.#####.###############.#.#####.#.#.#.#.###.#.#.#.#.#.###.#.#####.###
#.....#...#.#...#...#.#...#...#...#.......#...#...###.............#...#.#.....#...#...#...#####...#.....#...#.#...#...#.#.#.#...#...#####...#
#######.#.#.#.#.#.#.#.###.#####.#.#.#################.#################.#####.###.#.#.#.#.#########.#########.###.#####.#.#.###.###########.#
#.......#...#.#...#.#.#...#.....#.#.............#...#...........#...#...#.....###...#...#.......#...#...###...#...#.....#.#.....#.....#...#.#
#.###########.#####.#.#.###.#####.#############.#.#.###########.#.#.#.###.#####################.#.###.#.###.###.###.#####.#######.###.#.#.#.#
#.....#.......#...#.#...###.....#.#...#.....#...#.#...#.........#.#.#...#.#.....................#...#.#...#...#.###.......#...###...#...#...#
#####.#.#######.#.#.###########.#.#.#.#.###.#.###.###.#.#########.#.###.#.#.#######################.#.###.###.#.###########.#.#####.#########
#...#...#...#...#...###...#.....#...#.#...#.#.###...#.#.....#.....#.###...#.............#...#######...###...#.#.#...#.......#.#...#.........#
#.#.#####.#.#.#########.#.#.#########.###.#.#.#####.#.#####.#.#####.###################.#.#.###############.#.#.#.#.#.#######.#.#.#########.#
#.#.#.....#.#.......#...#.#.........#...#.#.#...#...#.......#.#.....#.......#...........#.#...#.....#...###.#...#.#.#.......#.#.#.....#...#.#
#.#.#.#####.#######.#.###.#########.###.#.#.###.#.###########.#.#####.#####.#.###########.###.#.###.#.#.###.#####.#.#######.#.#v#####.#.#.#.#
#.#...#...#...#.....#...#.#.........###.#.#.#...#.#...........#.....#.....#.#...#.........#...#...#...#.....#...#.#...#...#.#.>.#...#...#.#.#
#.#####.#.###.#.#######.#.#.###########.#.#.#.###.#.###############.#####.#.###.#.#########.#####.###########.#.#.###.#.#.#.###v#.#.#####.#.#
#.......#...#...#.......#.#...........#...#...#...#.#...............#...#.#.###...#.........#...#...........#.#...###...#.#.###...#.....#...#
###########.#####.#######.###########.#########.###.#.###############.#.#.#.#######.#########.#.###########.#.###########.#.###########.#####
#...........#...#.....#...###.........###...###.....#...........#.....#.#.#.#.....#...#...###.#.#...#.......#.........#...#.#...........#...#
#.###########.#v#####.#.#####v###########.#.###################.#.#####.#.#.#.###.###.#.#.###.#.#.#.#.###############.#.###.#.###########.#.#
#.#...#...#...#.>...#.#.#...>.>.###.....#.#.###...#...#.........#...#...#.#.#.#...#...#.#...#.#.#.#.#.......#.........#.#...#.#...........#.#
#.#.#.#.#.#.###v###.#.#.#.###v#.###.###.#.#.###.#.#.#.#.###########.#.###.#.#.#.###v###.###.#.#.#.#.#######.#.#########.#.###.#.###########.#
#...#...#.#...#...#...#...###.#...#.#...#.#.#...#.#.#.#.......#...#.#...#.#.#.#.#.>.>.#.#...#.#.#.#.#...###.#.........#.#.#...#.#...........#
#########.###.###.###########.###.#.#.###.#.#.###.#.#.#######.#.#.#.###.#.#.#.#.#.#v#.#.#.###.#.#.#.#.#.###v#########.#.#.#.###.#.###########
#...#.....#...###...###.......###.#.#.###.#.#...#.#.#.#...#...#.#.#...#.#.#...#...#.#.#.#...#.#.#.#.#.#...>.>.#.....#.#...#.....#...........#
#.#.#.#####.#######.###.#########.#.#.###.#.###.#.#.#.#.#.#v###.#.###.#.#.#########.#.#.###.#.#.#.#.#.#####v#.#.###.#.#####################.#
#.#...#.....#...###...#.......#...#.#...#.#.#...#.#.#.#.#.>.>.#.#.#...#...#.......#.#.#.###.#.#.#.#...###...#.#...#.#.#...................#.#
#.#####.#####.#.#####.#######.#.###.###.#.#.#.###.#.#.#.###v#.#.#.#.#######.#####.#.#.#.###.#.#.#.#######.###.###.#.#.#.#################.#.#
#.......###...#.......#.......#.....#...#.#...#...#.#.#.#...#...#.#.......#.....#...#.#...#.#.#...###.....###.....#...#.................#...#
###########.###########.#############.###.#####.###.#.#.#.#######.#######.#####.#####.###.#.#.#######.#################################.#####
#.........#.........#...#...........#...#.....#...#.#...#.......#.#.......#...#.....#.#...#.#.###...#...........#...###...#...#.........#...#
#.#######.#########.#.###.#########.###.#####.###.#.###########.#.#.#######.#.#####.#.#.###.#.###.#.###########.#.#.###.#.#.#.#.#########.#.#
#.......#...........#...#.#.........#...#.....#...#.#...#.......#...#.......#.......#...#...#.#...#.....#.......#.#.###.#.#.#.#...........#.#
#######.###############.#.#.#########.###.#####.###.#.#.#.###########.###################.###.#.#######.#.#######.#.###.#.#.#.#############.#
#.......#...#...#.....#.#.#...#...###...#.#...#.....#.#.#...........#...#.............###.....#.......#...###.....#...#.#...#.#.........#...#
#.#######.#.#.#.#.###.#.#.###.#.#.#####.#.#.#.#######.#.###########.###.#.###########.###############.#######.#######.#.#####.#.#######.#.###
#.........#...#...#...#...###.#.#.#...#...#.#.#...#...#.....#.......###...#...........#...#...#####...#.....#.....#...#...#...#.....###.#.###
###################.#########.#.#.#.#.#####.#.#.#.#.#######.#.#############.###########.#.#.#.#####.###.###.#####.#.#####.#.#######v###.#.###
#.................#...#...#...#.#.#.#.#...#.#.#.#.#.....#...#.###.........#.#.....#...#.#...#.#.....###...#...#...#.#...#.#...#...>.###...###
#.###############.###.#.#.#.###.#.#.#.#.#.#.#.#.#.#####.#.###v###.#######.#.#.###.#.#.#.#####.#.#########.###.#.###.#.#.#.###.#.###v#########
#...............#.....#.#.#...#.#.#.#.#.#.#.#...#...#...#...>.>.#.#.......#...#...#.#.#...#...#.....###...###.#...#.#.#.#...#.#.#...###.....#
###############.#######.#.###.#.#.#.#.#.#.#.#######.#.#######v#.#.#.###########.###.#.###.#.#######v###.#####.###.#.#.#.###.#.#.#.#####.###.#
#.....#...#.....#...###.#...#.#.#.#.#.#.#.#.....#...#.....###.#...#.#...#.......#...#.....#...#...>.>.#.....#.#...#.#.#.#...#.#.#.....#.#...#
#.###.#.#.#.#####.#v###.###.#.#.#.#.#.#.#.#####.#.#######.###.#####.#.#.#.#######.###########.#.###v#.#####.#.#.###.#.#.#.###.#.#####.#.#.###
#...#...#.#.....#.#.>...#...#.#.#.#.#.#.#.#...#.#...#.....#...#.....#.#.#.......#.........#...#.#...#.......#.#...#.#.#.#...#...#...#...#...#
###.#####.#####.#.#v#####.###v#.#.#.#.#.#.#.#.#.###.#.#####.###.#####.#.#######v#########.#.###.#.###########.###.#.#.#.###.#####.#.#######.#
###...#...#...#...#.#.....#.>.>.#.#.#.#.#.#.#.#.###...#...#...#.......#.#.....>.>...#...#.#...#.#.....###...#.#...#.#.#.#...#.....#.#.......#
#####.#.###.#.#####.#.#####.#v###.#.#.#.#.#.#.#.#######.#.###.#########.#.#####v###.#.#.#.###.#.#####.###.#.#.#.###.#.#.#.###.#####.#.#######
###...#...#.#.###...#.#...#.#...#.#.#.#.#.#.#.#.#.......#.###...###.....#.....#.###...#...#...#.....#...#.#.#.#.###...#.#.###.....#...###...#
###.#####.#.#.###.###.#.#.#.###.#.#.#.#.#.#.#.#.#.#######.#####.###.#########.#.###########.#######.###.#.#.#.#.#######.#.#######.#######.#.#
#...#...#...#...#.#...#.#...#...#...#.#.#...#...#.......#.#...#.#...#...#.....#...#...#...#.#...#...#...#.#.#...#.....#...#...###.......#.#.#
#.###.#.#######.#.#.###.#####.#######.#.###############.#.#.#.#.#.###.#.#.#######.#.#.#.#.#.#.#.#.###.###.#.#####.###.#####.#.#########.#.#.#
#...#.#.......#...#.....#.....#...###...#...#...#.......#...#.#.#...#.#.#...#...#...#.#.#.#...#...###...#.#...###.#...#.....#...........#.#.#
###.#.#######.###########.#####.#.#######.#.#.#.#.###########.#.###.#.#.###.#.#.#####.#.#.#############.#.###.###.#.###.#################.#.#
###.#.###...#...........#.......#.....#...#.#.#.#.#.......###.#...#.#.#...#.#.#.......#.#.#.........#...#.###...#.#.###.................#.#.#
###.#.###.#.###########.#############.#.###.#.#.#.#.#####.###.###.#.#.###.#.#.#########.#.#.#######.#.###.#####.#.#.###################.#.#.#
#...#.#...#.............###...........#.#...#.#...#.#.....#...#...#.#.#...#.#...........#.#.....#...#.....#...#...#.......#...###.......#.#.#
#.###.#.###################.###########.#.###.#####.#.#####.###.###.#.#.###.#############.#####.#.#########.#.###########.#.#.###.#######.#.#
#.....#...#...............#.#.....#.....#...#.......#...###.....###...#...#.#.............#...#.#.###...###.#.#...........#.#.#...#...###.#.#
#########.#.#############.#.#.###v#.#######.###########.#################.#.#.#############.#.#.#.###.#.###.#.#.###########.#.#.###.#.###.#.#
#...#...#...#...#.......#.#...#.>.>.#.......#...#...###.................#...#.........#.....#...#...#.#.....#...###...#...#.#.#...#.#...#.#.#
#.#.#.#.#####.#.#.#####.#.#####.#v###.#######.#.#.#.###################.#############.#.###########.#.#############.#.#.#.#.#.###v#.###.#.#.#
#.#...#.......#.#.....#...#...#.#...#.....###.#.#.#.#.....#.........#...###...........#.........#...#...........#...#.#.#...#...>.#...#.#.#.#
#.#############.#####.#####.#.#.###.#####.###.#.#.#.#.###.#.#######.#.#####.###################.#.#############v#.###.#.#########v###.#.#.#.#
#.............#.......#...#.#...#...#.....#...#...#.#...#...###...#...###...#...#...###.....#...#.#.....#.....>.>.#...#.#.......#.....#...#.#
#############.#########.#.#.#####.###.#####.#######.###.#######.#.#######.###.#.#.#.###.###.#.###.#.###.#.#####v###.###.#.#####.###########.#
#.............#...###...#.#.....#.#...#...#.....#...###...#...#.#.#.....#.....#...#...#...#...###.#...#...###...###.....#...###.#.....#...#.#
#.#############.#.###.###.#####.#.#.###.#.#####.#.#######.#.#.#.#.#.###.#############.###.#######.###.#######.#############.###.#.###.#.#.#.#
#.#...#...#.....#.....#...###...#.#...#.#.#...#.#.#...#...#.#.#.#.#.#...#...#...#...#...#.......#.#...#.......#...#...#####...#.#.#...#.#.#.#
#.#.#.#.#.#.###########.#####.###.###.#.#.#.#.#.#.#.#.#v###.#.#.#.#.#.###.#.#.#.#.#.###.#######.#.#.###.#######.#.#.#.#######.#.#.#.###.#.#.#
#...#...#...#...........#...#...#.###...#...#.#.#.#.#.>.>...#.#.#.#.#.#...#.#.#.#.#.###...#.....#...###.........#...#.#.......#...#.#...#.#.#
#############.###########.#.###.#.###########.#.#.#.###v#####.#.#.#.#.#.###.#.#.#.#.#####.#.#########################.#.###########.#.###.#.#
###...#...#...#...###...#.#.#...#.#...#.......#.#.#.###.....#.#.#.#.#.#...#.#.#.#.#.#...#.#.......#...#...#.....#...#.#.....#...###...###.#.#
###.#.#.#.#.###.#v###.#.#.#.#.###.#.#.#.#######.#.#.#######.#.#.#.#.#.###.#.#.#.#.#.#.#.#v#######.#.#.#.#.#.###.#.#.#.#####.#.#.#########.#.#
#...#...#.#.....#.>.#.#.#.#.#...#...#.#.......#.#.#.#.......#.#.#.#.#.#...#.#.#.#.#.#.#.>.>.#.....#.#.#.#...###.#.#...#...#...#.......###.#.#
#.#######.#######v#.#.#.#.#.###.#####.#######.#.#.#.#.#######.#.#.#.#.#.###.#.#.#.#.#.###v#.#.#####.#.#.#######.#.#####.#.###########.###.#.#
#.......#.#.....#.#.#.#...#.....#.....###.....#.#...#...#####...#...#.#...#.#.#.#.#...#...#.#.....#.#.#.......#.#.....#.#.............#...#.#
#######.#.#.###.#.#.#.###########.#######.#####.#######.#############.###.#.#.#.#.#####.###.#####.#.#.#######.#.#####.#.###############.###.#
#.......#.#.#...#.#.#.......#.....###...#.......###...#...#...#.....#...#.#.#.#.#.#.....###.......#.#...#.....#.#.....#...........#...#.....#
#.#######.#.#.###.#.#######.#.#######.#.###########.#.###.#.#.#.###.###.#.#.#.#.#.#.###############.###.#.#####.#.###############.#.#.#######
#.......#.#.#.###.#.#.......#.........#.....#.......#.....#.#...#...###...#...#...#...#####...#...#...#.#.....#...#...............#.#.......#
#######.#.#.#.###.#.#.#####################.#.#############.#####.###################.#####.#.#.#.###.#.#####.#####.###############.#######.#
#...#...#.#.#...#.#.#...#.....#.....#.....#.#.............#.....#...#...#.....#...###...###.#.#.#.#...#.#...#.....#.....#.....#.....#.......#
#.#.#v###.#.###.#.#.###.#.###.#.###.#.###.#.#############.#####.###.#.#.#.###.#.#.#####.###.#.#.#.#v###.#.#.#####.#####.#.###.#.#####.#######
#.#.#.>.#...#...#.#.#...#...#.#...#.#.###...#.............#...#.#...#.#.#.#...#.#.###...#...#.#.#.>.>.#...#.......#...#...#...#.#.....#...###
#.#.#v#.#####.###.#.#.#####.#.###.#.#.#######.#############.#.#.#.###.#.#.#.###.#.###v###.###.#.###v#.#############.#.#####.###.#.#####.#.###
#.#...#.....#.#...#...###...#...#.#...#...###...........#...#.#.#...#.#...#.#...#.#.>.>.#...#.#.#...#.........#...#.#...###.....#.......#...#
#.#########.#.#.#########.#####.#.#####.#.#############.#.###.#.###.#.#####.#.###.#.#v#.###.#.#.#.###########.#.#.#.###.###################.#
#.....#.....#...###.....#.....#.#.#...#.#.#...###...###.#...#.#.#...#...#...#...#...#.#.#...#...#.....#.......#.#.#...#.......#...#.........#
#####.#.###########.###.#####.#.#.#.#.#.#.#.#.###.#.###v###.#.#.#.#####.#.#####.#####.#.#.###########.#.#######.#.###.#######.#.#.#.#########
#.....#...#...#...#.#...#...#.#.#.#.#.#.#.#.#.#...#.#.>.>...#...#...#...#.......#.....#...#...........#...#.....#.#...#.....#...#...#####...#
#.#######.#.#.#.#.#.#.###.#.#.#.#v#.#.#.#.#.#.#.###.#.#v###########.#.###########.#########.#############.#.#####.#.###.###.#############.#.#
#.......#.#.#.#.#.#.#.#...#.#.#.>.>.#...#.#.#.#...#.#.#.#.........#.#...#...#...#.........#.............#.#.#.....#.....#...#...#.........#.#
#######.#.#.#.#.#.#.#.#.###.#.###v#######.#.#.###.#.#.#.#.#######.#.###.#.#.#.#.#########.#############.#.#.#.###########.###.#.#.#########.#
###.....#.#.#.#.#.#.#...#...#.###.#.....#.#.#.#...#.#.#...#.......#...#.#.#...#...........###.....###...#...#...#...#...#.....#...#.....#...#
###.#####.#.#.#.#.#.#####.###.###.#.###.#.#.#.#.###.#.#####.#########.#.#.###################.###.###.#########.#.#.#.#.###########.###.#.###
#...#...#...#.#.#.#.....#...#.###...#...#.#.#.#.#...#.#.....###...###...#...................#...#.....#.........#.#.#.#.###...#.....###...###
#.###.#.#####.#.#.#####.###.#.#######.###.#.#.#.#.###.#.#######.#.#########################.###.#######.#########.#.#.#.###v#.#.#############
#.....#...###...#.#...#.#...#.#.......###.#.#.#.#...#.#.......#.#.#.................#.......###.......#.....#...#.#...#...>.#.#.............#
#########.#######.#.#.#.#.###.#.#########.#.#.#.###.#.#######.#.#.#.###############.#.###############.#####.#.#.#.#########v#.#############.#
#.........#.....#...#.#.#...#.#.........#...#...###...#.......#.#.#.....#.........#.#.#...#...###...#.....#...#.#...#.......#...............#
#.#########.###.#####.#.###.#.#########.###############.#######.#.#####.#.#######.#.#.#.#.#.#.###.#.#####.#####.###.#.#######################
#...........#...#...#...###...#.........#...........#...#...###.#...###...###.....#...#.#.#.#.###.#.#.....#...#.#...#.......................#
#############.###.#.###########.#########.#########.#.###.#.###.###.#########.#########.#.#.#.###.#.#.#####.#.#.#.#########################.#
#.............#...#...#.........#...#...#...#.......#.....#.....#...#...#...#.....#.....#...#.#...#.#.......#.#...#.............#...#.......#
#.#############.#####.#.#########.#.#.#.###.#.###################.###.#.#.#.#####.#.#########.#.###.#########.#####.###########.#.#.#.#######
#.............#.#.....#.........#.#...#.....#.....###...#.........###.#.#.#.###...#.......#...#...#.#.........#...#.........###...#...#...###
#############.#.#.#############.#.###############.###.#.#.###########.#.#.#.###v#########.#.#####.#.#.#########.#.#########.###########.#.###
#.............#.#...#...#.....#.#.#.........#.....#...#.#.....#.....#.#...#.#.>.>...#.....#...#...#...#####...#.#.###.....#.....#...#...#...#
#.#############.###.#.#.#.###.#.#.#.#######.#.#####.###.#####.#.###.#.#####.#.#####.#.#######.#.###########.#.#.#.###.###.#####.#.#.#.#####.#
#.......###...#.#...#.#.#.#...#...#.......#.#.....#...#.......#.#...#.....#.#...###.#...#.....#...........#.#.#.#...#...#.....#...#.#.#.....#
#######.###.#.#.#.###.#.#.#.#############.#.#####.###.#########.#.#######.#.###.###.###.#.###############.#.#.#.###.###.#####.#####.#.#.#####
#.....#...#.#.#.#.#...#.#.#.#...#...#.....#.#.....#...#.........#...#.....#.....#...#...#...#.....#.......#.#.#.#...###.....#.......#.#.#...#
#.###.###.#.#.#.#.#.###.#.#.#.#.#.#.#.#####.#.#####.###.###########.#.###########.###.#####.#.###.#.#######.#.#.#.#########v#########.#.#.#.#
#...#.....#.#.#.#.#.#...#.#.#.#.#.#.#...###...#...#.....#...........#...........#.....#####...#...#.....#...#.#.#.....#...>.#...#...#.#...#.#
###.#######.#.#.#.#.#.###.#.#.#.#.#.###v#######.#.#######.#####################.###############.#######.#.###.#.#####.#.###v#.#.#.#.#.#####.#
###.......#.#.#.#.#.#.#...#.#.#.#.#...>.>.#...#.#.#.....#...#.....#...#.....#...#...#...........#...#...#.#...#.....#.#.#...#.#.#.#.#.#.....#
#########.#.#.#.#.#.#.#.###.#.#.#.#######.#.#.#.#.#.###.###.#.###.#.#.#.###.#.###.#.#.###########.#.#v###.#.#######.#.#.#.###.#.#.#.#.#.#####
#.........#.#...#.#.#.#.###...#.#.......#.#.#.#.#.#...#.###.#.#...#.#.#...#.#.....#.#.......#...#.#.>.>.#.#.......#.#...#...#.#.#.#...#.#...#
#.#########.#####.#.#.#.#######.#######.#.#.#.#.#.###.#.###v#.#.###.#.###.#.#######.#######.#.#.#.#####.#.#######.#.#######.#.#.#.#####.#.#.#
#.#.......#.#.....#.#.#.......#...#.....#...#.#.#...#.#.#.>.>.#...#.#.....#...#...#.###.....#.#.#...#...#.......#...###...#...#...#.....#.#.#
#.#.#####.#.#.#####.#.#######.###.#.#########.#.###.#.#.#.#######.#.#########.#.#.#.###.#####.#.###.#.#########.#######.#.#########.#####.#.#
#.#.#.....#.#.#...#.#...#...#.#...#...#.......#.#...#.#.#...#.....#.........#.#.#.#...#...#...#.#...#...#...#...#.......#...........#...#.#.#
#.#.#.#####.#.#.#.#.###.#.#.#.#.#####.#.#######.#.###.#.###.#.#############.#.#.#.###.###.#.###.#.#####.#.#.#.###.###################.#.#.#.#
#...#.......#...#...###...#...#.......#.........#.....#.....#...............#...#.....###...###...#####...#...###.....................#...#.#
###########################################################################################################################################.#
""".trimIndent()