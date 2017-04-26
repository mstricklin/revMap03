// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph.cache;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XBaseline<T> implements XCache<T> {
    public static <T> XBaseline<T> newRevCache() {
        return new XBaseline<>();
    }
    @Override
    public void add(Long id, T t) {
        store.put(id, t);
    }
    @Override
    public T get(Long id) {
        return store.get( id );
    }
    @Override
    public void remove(Long id) {
        store.remove( id );
    }
    @Override
    public void reset() {
        store.clear();
    }
    @Override
    public void dump() {
        if ( ! store.isEmpty() ) {
            log.info( "    = store =" );
            for (Map.Entry<Long, T> e : store.entrySet()) {
                log.info( "    {} => {}", e.getKey(), e.getValue() );
            }
        }
    }
    public XCache<T> getRevision() {
        return new XRevision<>( this );
    }
    Map<Long, T> store = newHashMap();
    // =================================
    public static class XRevision<T> implements XCache<T> {
        XRevision(XBaseline<T> baseline) {
            this.baseline = baseline;
        }

        @Override
        public void add(Long id, T t) {
            revised.put( id, t );
        }
        @Override
        public T get(Long id) {
            if (removed.contains( id ))
                return null;
            T t = revised.get( id );
            return (null != t) ? t
                               : baseline.get( id );
        }
        public T getForMutation(Long id) {
            if (removed.contains( id ))
                return null;
            T t = revised.get( id );
            if (null != t)
                return t;
            t = baseline.get( id );
            if (null != t) {
                // TODO
//                t = t.copy();
                revised.put(id, t);
            }
            return t;
        }
        @Override
        public void remove(Long id) {
            removed.add( id );
            revised.remove( id );
        }
        @Override
        public void reset() {
            revised.clear();
            removed.clear();
        }
        @Override
        public void dump() {
            if ( ! revised.isEmpty() ) {
                log.info( "    = revised =" );
                for (Map.Entry<Long, T> e : revised.entrySet()) {
                    log.info( "    {} => {}", e.getKey(), e.getValue() );
                }
            }
            if ( ! removed.isEmpty() ) {
                log.info( "    = removed =" );
                log.info( "    {}", removed );
            }
        }
        // =================================
        final XBaseline<T> baseline;
        private final Map<Long, T> revised = newHashMap();
        private final Set<Long> removed = newHashSet();
    }
}
