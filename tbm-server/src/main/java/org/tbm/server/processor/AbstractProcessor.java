package org.tbm.server.processor;

import org.tbm.common.Processor;
import org.tbm.server.collect.BaseCollectorPool;

/**
 * Created by Jason.Xia on 17/6/2.
 */
@Deprecated
public abstract class AbstractProcessor implements Processor {
    protected final BaseCollectorPool collectorPool;

    public AbstractProcessor(BaseCollectorPool collectorPool) {
        this.collectorPool = collectorPool;
    }
}
