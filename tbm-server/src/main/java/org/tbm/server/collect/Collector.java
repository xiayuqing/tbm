package org.tbm.server.collect;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/26.
 */
public class Collector<T> {
    //    private final JvmStash stash = StashFactory.get(Collector.class);
    private T[] bufArr;
    private AtomicInteger capacity;
    private AtomicBoolean enable = new AtomicBoolean(false);

    public void Collector(T[] bufArr) {
        this.bufArr = bufArr;
        this.capacity = new AtomicInteger(bufArr.length);
    }

    public void add(T data) {
        if (!enable.get()) {
            throw new IllegalStateException("too long bufArr");
        }

        synchronized (bufArr) {
            bufArr[bufArr.length - capacity.getAndDecrement()] = data;
            if (0 == capacity.get()) {
                enable.set(false);
                flush();
            }
        }
    }

    private void flush() {
//        stash
        enable.set(true);
    }
}
