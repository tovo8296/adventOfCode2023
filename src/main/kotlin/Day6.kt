// Raced 1
// https://www.wolframalpha.com/input?i=0+%3D+-x%5E2+%2B+40*x-219
//=> 27 options

// race 2
// https://www.wolframalpha.com/input?i=0+%3D+-x%5E2+%2B+81*x-1012
// zero at: 81/2 +- sqrt(2513) / 2  => +- 25.06
// => 50 options

// race 3
// https://www.wolframalpha.com/input?i=0+%3D+-x%5E2+%2B+77*x-1365
// zero at 77/2 +- sqrt(469) / 2  => +- 10.8
// => 22 options

// race 3
// https://www.wolframalpha.com/input?i=0+%3D+-x%5E2+%2B+72*x-1089
// zero at 36 +- 3*sqrt(23)  =>  +- 14.3
// => 29 options

//=> part1: 26 * 50 * 22 * 28 => 800800

val times = listOf(40, 81, 77, 72)
val distance = listOf(219, 1012, 1365, 1089)

fun main() {
    var mult = 1
    times.forEachIndexed { index, raceTime ->
        val options = calcWinOptions(raceTime.toLong(), distance[index].toLong())
        mult *= options
    }
    println("Total Mult: $mult")

    val oneTime = 40817772L
    val oneDist = 219101213651089L
    val options = calcWinOptions(oneTime, oneDist)
    println("One Race options: $options")
}

fun calcWinOptions(raceTime: Long, winDistance: Long): Int =
    (0L..raceTime).count { pressTime ->
        val dist = pressTime * (raceTime - pressTime)
        dist > winDistance
    }

