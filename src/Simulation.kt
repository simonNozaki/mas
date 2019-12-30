
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator
import org.graphstream.algorithm.generator.Generator
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph
import java.util.*
import extention.ArrayExtension

/**
 * シミュレーションクラス。
 * @param population エージェントの数
 * @param averageDegree 一つの葉の平均次数
 */
class Simulation(private var population: Int, private  var averageDegree: Int) {

    private var agents: MutableList<Agent> = mutableListOf()
    private var initialCooperator: Int = selectInitialCooperators(population)



    init {

    }

    /**
     * シミュレーションを実行するエージェントを生成します
     */
    fun generateAgents(): MutableList<Agent> {

        var maxLinksPerSteps: Int = averageDegree / 2

        // -----------------------
        // グラフの初期化
        // -----------------------
        var baSfGraph: Graph = SingleGraph("ba-sf")
        var generator: Generator = BarabasiAlbertGenerator(maxLinksPerSteps)
        generator.addSink(baSfGraph)

        generator.begin() // 開始

        var agents: MutableList<Agent> = mutableListOf()

        for (i in 0..population) {
            generator.nextEvents()
            agents.add(Agent())
        }

        generator.end() // 終了

        baSfGraph.display()

        // -----------------------
        // エージェントの初期化
        // -----------------------
        for (i in 0..agents.size) {
            // エージェントiに隣接しているエージェントのインデックスリストを取得する
            var target: Node = baSfGraph.getNode<Node>(i)
            agents[i].neighborsId.add(i)
        }

        return agents
    }

    /**
     * 一階ゲームで協力戦略を選ぶエージェントを選ぶ。
     * @return 協力戦略をとるエージェントのインデックス
     */
    private fun selectInitialCooperators(population: Int): Int {
        return Random().nextInt(population)
    }

    /**
     * すべてのエージェントの一階ゲームの戦略を設定します。
     */
    private fun initializeStrategy(): Unit {
        for (i in 0..this.agents.size) {
            this.agents[i].strategy = if (i == initialCooperator) "C" else "D"
        }
    }

    /**
     * ゲームにより得られる利得を演算します。
     * @param dg
     * @param dr
     */
    private fun computePayoff(dg: Int, dr: Int): Unit {

        // 利得を定義
        var reward: Int = 1
        var sucker: Int = -dr
        var temptation: Int = 1+dg
        var punishment: Int = 0

        // 各プレイヤーの戦略を反映して、利得を決定する
        for (agent: Agent in this.agents) {
            for (id: Int in agent.neighborsId) {
                when {
                    agent.strategy == "C" && this.agents[id].strategy == "C" -> agent.point += reward
                    agent.strategy == "C" && this.agents[id].strategy == "D" -> agent.point += sucker
                    agent.strategy == "D" && this.agents[id].strategy == "C" -> agent.point += temptation
                    agent.strategy == "D" && this.agents[id].strategy == "D" -> agent.point += punishment
                }
            }
        }
    }

    /**
     * 各プレイヤーの次のゲームの戦略を更新します。
     */
    private fun updateStrategy(): Unit {

        this.agents.forEach{ agent: Agent -> agent.decideNextStrategy(this.agents) }

        this.agents.forEach { agent: Agent -> agent.updateStrategy() }
    }

    /**
     * ある時点での協調率を算出します。
     */
    private fun countCooperators(): Int {

        // 協調戦略をもつエージェントを集計します
        var counter: Int = this.agents.filter { agent: Agent -> agent.strategy == "C" }.count()

        return  counter/this.agents.size
    }

    /**
     * ゲームを試行します.
     * @param
     * @param
     * @param
     * @return
     */
    fun playGame(episode: Int, dg: Int, dr: Int): Int {
        // -----------------------
        // 実行コンテキスト初期化
        // -----------------------
        this.initializeStrategy()
        var initialRatio: Int = countCooperators()
        var focals: MutableList<Int> = mutableListOf(initialRatio)
        var tmax: Int = 3000 // 最大試行回数
        var convergedFocal: Int = 0 // 返却する、収束した対象のインデックス

        // -----------------------
        // 収束判定
        // -----------------------
        println("/_/_/_/_/_/_/_/_/_ Episode:$episode, Dg:$dg, Dr:$dr, Time:0, Fc:${initialRatio.toFloat()} /_/_/_/_/_/_/_/_/_")

        for (t in 0..tmax) {
            this.computePayoff(dg, dr) // 利得の計算
            this.updateStrategy() //戦略の更新
            var focal: Int = countCooperators() // 協調率を計算
            focals.add(focal)

            println("/_/_/_/_/_/_/_/_/_ Episode:$episode, Dg:$dg, Dr:$dr, Time:$t, Fc:${focal} /_/_/_/_/_/_/_/_/_")

            if (t >= 100 && absolute(sum(focals - focal)/100 - focal) < 0.01 || t == tmax - 1) {
                convergedFocal = sum(focals - focal) / 99
                break
            } else if (focal == 0 || focal == 1) {
                convergedFocal = focal
                break
            }

            println("/_/_/_/_/_/_/_/_/_ Episode:$episode, Dg:$dg, Dr:$dr, Time:$t, Fc:${focal} /_/_/_/_/_/_/_/_/_")

        }
        return convergedFocal

    }

    /**
     * 指定の範囲でパラメータを動かし、エピソードを実行します.
     */
    fun executeEpisode(episode: Int): MutableList<Int> {

        var result: MutableList<Int> = mutableListOf()

        for (dr in 0..10) {
            for (dg in 0..10) {
                result.add(this.playGame(episode, dg, dr))
            }
        }

        return result
    }

    private fun sum(list: List<Int>): Int {
        var result: Int = 0
        for (i in 0..list.size) result += i

        return result
    }

    private fun absolute(p: Int): Int {
        if (p < 0) return p * -1
        return p
    }

}