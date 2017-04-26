// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import lombok.ToString;
import strickli.graph.Copyable;
import strickli.graph.Direction;
import strickli.graph.Edge;
import strickli.graph.Vertex;

public class XVertex extends XElement implements Vertex, Copyable<XVertex> {
    public static XVertex of(XGraph g, long id) {
        return new XVertex( g, id );
    }
    public static XVertex of(XGraph g, RawVertex rv) {
        return new XVertex( g, rv.id );
    }
    @Override
    public XVertex copy() {
        return new XVertex(this);
    }
    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        return null;
    }
    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return null;
    }
    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return null;
    }
    @Override
    public String toString() {
        return "V[" + id + "]";
    }
    private XVertex(XGraph g, long id) {
        super(g, id);
    }

    private XVertex(XVertex v) {
        super(v.graph, v.id);
    }
    // =================================
    @ToString(callSuper=true)
    static class RawVertex extends RawElement {
        public static RawVertex of(long id) {
            return new RawVertex( id );
        }
        public static RawVertex of(XVertex v) {
            return new RawVertex( v.id );
        }

        public RawVertex copy() {
            return new RawVertex( this );
        }
        private RawVertex(long id) {
            super(id);
            inEdges = newHashSet();
            outEdges = newHashSet();
        }
        private RawVertex(RawVertex other) {
            super(other);
            inEdges = newHashSet(other.inEdges);
            outEdges = newHashSet(other.outEdges);
        }
//        @Override
//        public String toString() {
//            return "V[" + id + "]";
//        }
        // =================================
        private final Set<Long> inEdges;
        private final Set<Long> outEdges;
    }
}
