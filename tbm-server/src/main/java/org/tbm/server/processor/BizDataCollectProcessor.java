package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.vo.BizData;
import org.tbm.server.operation.BizOp;
import org.tbm.server.operation.OpsFactory;

import java.util.List;


/**
 * Created by Jason.Xia on 17/6/1.
 */
public class BizDataCollectProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(BizDataCollectProcessor.class);

    private BizOp bizOp = (BizOp) OpsFactory.get(BizOp.class);

    private boolean redisEnable = true;

    public BizDataCollectProcessor(boolean redisEnable) {
        this.redisEnable = redisEnable;
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        List<BizData> bizData = JSON.parseArray(packetLite.payload, BizData.class);
        if (null == bizData || 0 == bizData.size()) {
            return PacketLite.createAck(packetLite.seq);
        }

        try {
            if (redisEnable) {
                bizOp.INSERT_INTO_CACHE(bizData);
            }else {
                bizOp.INSERT_BIZ(bizData);
            }
        } catch (Exception e) {
            logger.error("insert biz data error.seq:{},msg:{},trace:{}", packetLite.seq, e.getMessage(), e
                    .getStackTrace());
            PacketLite.createError(e.getMessage(), packetLite.seq);
        }

        return PacketLite.createAck(packetLite.seq);
    }
}
