// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;
import strickli.graph.Edge;
import strickli.graph.Graph;
import strickli.graph.TransactionalGraph;
import strickli.graph.Vertex;
import strickli.xgraph.cache.XStore;

@Slf4j
public class XGraph implements Graph, TransactionalGraph {

    public static XGraph of() {
        return new XGraph();
    }

    @Override
    public Vertex addVertex(Object id) {
        long vID = VERTEX_ID.getAndIncrement();
        XVertex.RawVertex rv = XVertex.RawVertex.of( vID );
        tx.get().add( vID, rv );
        return XVertex.of( this, vID );
    }
    @Override
    public Vertex getVertex(Object id) {
        log.trace( "getVertex {}", id );
        checkNotNull( id );
        try {
            final Long longID = (id instanceof Long) ? (Long)id : Long.valueOf( id.toString() );
            XVertex.RawVertex rv = tx.get().get( longID, XVertex.RawVertex.class );
            return (null != rv) ? XVertex.of(this, longID)
                   : null;
        } catch (NumberFormatException | ClassCastException e) {
            log.error( "could not find vertex id {}", id );
        }
        return null;
    }
    @Override
    public void removeVertex(Vertex v) {
        XVertex xv = (XVertex)v;
        tx.get().remove( xv.getRawId(), XVertex.RawVertex.class );
    }
    @Override
    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        return null;
    }
    @Override
    public Edge getEdge(Object id) {
        return null;
    }
    @Override
    public void removeEdge(Edge v) {

    }
    @Override
    public void dump() {
        baseline.dump();
        tx.get().dump();
    }
    @Override
    public void shutdown() {

    }
    @Override
    public void commit() {

    }
    @Override
    public void rollback() {

    }
    // =================================
    private final XStore baseline = new XStore();
    private ThreadLocal<XStore.XRevision> tx = new ThreadLocal<XStore.XRevision>() {
        @Override
        protected XStore.XRevision initialValue() {
            log.info("make new threadlocal store");
            return baseline.getRevision();
        }
    };

    private static AtomicLong EDGE_ID = new AtomicLong( 0L );
    private static AtomicLong VERTEX_ID = new AtomicLong( 0L );
}
