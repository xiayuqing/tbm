package org.tbm.server.processor;

import org.tbm.common.Processor;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.bean.PacketLite;
import org.tbm.server.collect.CollectorPool;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class BizDataCollectProcessor  extends AbstractProcessor{
    public BizDataCollectProcessor(DataAccessor dataAccessor, CollectorPool collectorPool) {
        super(dataAccessor, collectorPool);
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        return null;
    }
}