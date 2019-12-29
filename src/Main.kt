import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph

class Main

fun main(args: Array<String>) {
    var graph: Graph = SingleGraph("startup")

    graph.addNode<Node>("A")
    graph.addNode<Node>("B" );
    graph.addNode<Node>("C" );
    graph.addEdge<Edge>("AB", "A", "B");
    graph.addEdge<Edge>("BC", "B", "C");
    graph.addEdge<Edge>("CA", "C", "A");

    // graph.display()

    // エージェント数1000, 平均次数8のスケールフリーグラフを生成
    Simulation(1000, 8).generateAgents()
}