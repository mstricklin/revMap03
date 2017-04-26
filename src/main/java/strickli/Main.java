// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli;

import lombok.extern.slf4j.Slf4j;
import strickli.graph.Vertex;
import strickli.xgraph.XGraph;

@Slf4j
public class Main {
    public static void main( String[] args ) {
        log.info("Fooooo!");
        XGraph xg = XGraph.of();
        Vertex v0 = xg.addVertex( null );
        Vertex v1 = xg.addVertex( null );

        xg.dump();
        xg.removeVertex( v0 );
        log.info("");
        xg.dump();
        log.info("");

        Vertex v0a = xg.getVertex( 1L );
        log.info("v0a {}", v0a);
    }
}
