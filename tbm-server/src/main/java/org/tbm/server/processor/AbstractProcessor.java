package org.tbm.server.processor;

import org.tbm.common.Processor;
import org.tbm.server.collect.CollectorPool;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public abstract class AbstractProcessor implements Processor {
    protected final CollectorPool collectorPool;

    public AbstractProcessor(CollectorPool collectorPool) {
        this.collectorPool = collectorPool;
    }
}
