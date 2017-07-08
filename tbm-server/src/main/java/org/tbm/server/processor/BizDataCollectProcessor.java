package org.tbm.server.processor;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.vo.BizData;


/**
 * Created by Jason.Xia on 17/6/1.
 */
public class BizDataCollectProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(BizDataCollectProcessor.class);

    @Override
    public PacketLite process(PacketLite packetLite) {
        BizData bizData = JSONObject.parseObject(packetLite.payload, BizData.class);
        System.out.println("received bizData:" + bizData);
        return null;
    }
}
