package org.tbm.common.bean;

/**
 * Created by Jason.Xia on 17/5/31.
 */
public class ValuePair<K, V> {
    private K k;
    private V v;

    public ValuePair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public void set(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }
}
