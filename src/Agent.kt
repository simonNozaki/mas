/**
 * シミュレーションを実行するエージェントクラス。
 */
class Agent {
    var point: Float = 0.toFloat()

    /**
     * プレイヤーの戦略、C（協力）もしくはD（裏切り）を取る。
     */
    var strategy: String = ""
    /**
     * プレイヤーの次の戦略、C（協力）もしくはD（裏切り）を取る。
     */
    var nextStrategy: String = ""
    var neighborsId: MutableList<Int> = mutableListOf()

    /**
     * 次の戦略を決定します。
     */
    fun decideNextStrategy(agents: MutableList<Agent>): Unit {
        var opponentId: Int = this.neighborsId[0]
        var opponent: Agent = agents[opponentId]

        if (opponent.strategy != this.strategy) this.nextStrategy = opponent.strategy
    }

    /**
     * 戦略を更新します。
     */
    fun updateStrategy(): Unit {
        this.strategy = this.nextStrategy
    }
}