// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package strickli.xgraph.cache;

public interface XCache<T> {
    void add(Long id, T t);
    T get(Long id);
    void remove(Long id);
    void reset();
    void dump();
}
