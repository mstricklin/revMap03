// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph.cache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ForwardingXStore {
    public <T> void add(Long id, T t) {
        XCache<T> c = getCache( t );
        log.info("add cache {}", c);
        c.add( id, t );
    }
    public <T> T get(Long id, Class<T> t) {
        XCache<T> c = getCache( t );
        log.info( "get cache {}", c );
        return c.get( id );
    }
    public <T> void remove(Long id, Class<T> t) {
        XCache<T> c = getCache( t );
        log.info("rm cache {}", c);
        c.remove( id );
    }
    public abstract void reset();
    public abstract void dump();

    protected abstract <T> XCache<T> getCache(Class<T> clazz);
    protected abstract <T> XCache<T> getCache(T t);
}
