
class Main

fun main(args: Array<String>) {

    // エージェント数1000, 平均次数8のスケールフリーグラフを生成
    var simulation: Simulation = Simulation(1000, 8)

    var result: MutableList<Int> = simulation.executeEpisode(1)

    println(result.toString())
}