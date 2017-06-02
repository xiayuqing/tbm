package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.JvmDataSqlExecutor;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.vo.JvmData;
import org.tbm.server.collect.CollectorPool;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class JvmDataCollectProcessor extends AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(JvmDataCollectProcessor.class);

    public JvmDataCollectProcessor(DataAccessor dataAccessor, CollectorPool collectorPool) {
        super(dataAccessor, collectorPool);
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        List<JvmData> jvmData = JSON.parseArray(packetLite.payload, JvmData.class);
        if (null == jvmData || 0 == jvmData.size()) {
            return PacketLite.createAck(packetLite.seq);
        }


        // TODO
        try {
            Future future = collectorPool.add(new JvmDataSqlExecutor(dataAccessor.getConnection(), SqlTemplate
                    .INSERT_MEMORY_SUMMARY, null));
            while (future.isDone()) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
