package sample

import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph

class GraphStreamSample {

    fun run() {
        var graph: Graph = SingleGraph("startup")

        graph.addNode<Node>("A")
        graph.addNode<Node>("B" );
        graph.addNode<Node>("C" );
        graph.addEdge<Edge>("AB", "A", "B");
        graph.addEdge<Edge>("BC", "B", "C");
        graph.addEdge<Edge>("CA", "C", "A");

        graph.display()
    }

}