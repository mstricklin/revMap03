// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph.cache;

import static com.google.common.collect.Queues.newArrayDeque;

import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XStore extends ForwardingXStore {

    public void reset() {
        for (XCache<?> c : baselineCaches.asMap().values()) {
            c.reset();
        }
    }
    @Override
    public void dump() {
        log.info("Baseline");
        for (Map.Entry<Class<?>, XBaseline> e : baselineCaches.asMap().entrySet()) {
            log.info("    Cache {}", e.getKey());
            e.getValue().dump();
        }
    }
    protected <T> XCache<T> getCache(Class<T> clazz) {
        return baselineCaches.getUnchecked( clazz );
    }
    protected <T> XCache<T> getCache(T t) {
        return baselineCaches.getUnchecked( t.getClass() );
    }

    // TODO: make thread-local
    public XRevision getRevision() {
        return new XRevision( this );
    }
    // =================================
    // per-thread
    public static class XRevision extends ForwardingXStore {
        private XRevision(XStore baselineStore) {
            this.baselineStore = baselineStore;
        }
        public <T> T getForMutation(Long id, Class<T> t) {
            XBaseline.XRevision<T> c = revisionCaches.getUnchecked( t );
            log.info( "getForMutation cache {}", c );
            return c.getForMutation( id );
        }
        public void reset() {
            for (XCache<?> c : revisionCaches.asMap().values()) {
                c.reset();
            }
        }
        @Override
        public void dump() {
            log.info("Revision");
            for (Map.Entry<Class<?>, XBaseline.XRevision> e : revisionCaches.asMap().entrySet()) {
                log.info("    Cache {}", e.getKey());
                e.getValue().dump();
            }
        }
        protected <T> XCache<T> getCache(Class<T> clazz) {
            return revisionCaches.getUnchecked( clazz );
        }
        protected <T> XCache<T> getCache(T t) {
            return revisionCaches.getUnchecked( t.getClass() );
        }
        // =================================
        LoadingCache<Class<?>, XBaseline.XRevision> revisionCaches
                = CacheBuilder.newBuilder()
                              .build( new CacheLoader<Class<?>, XBaseline.XRevision>() {
                                  @Override
                                  public XBaseline.XRevision load(Class<?> key) throws Exception {
                                      log.info("Make new revision cache {}", key);
                                      XBaseline<?> bl = baselineStore.baselineCaches.getUnchecked( key );
                                      return new XBaseline.XRevision( bl );
                                  }
                              } );
        private final XStore baselineStore;
    }

    // =================================

    LoadingCache<Class<?>, XBaseline> baselineCaches
            = CacheBuilder.newBuilder()
                          .build( new CacheLoader<Class<?>, XBaseline>() {
                              @Override
                              public XBaseline load(Class<?> key) throws Exception {
                                  log.info("Make new baseline cache {}", key);

                                  return new XBaseline();
                              }
                          } );

}
