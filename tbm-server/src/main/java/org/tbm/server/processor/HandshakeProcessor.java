package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.OperationManager;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.MachineInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.server.collect.CollectorPool;
import org.tbm.server.executor.MachineInfoSqlExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class HandshakeProcessor extends AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeProcessor.class);

    public HandshakeProcessor(DataAccessor dataAccessor, OperationManager om) {
        super(dataAccessor, null, om);
    }

    public HandshakeProcessor(DataAccessor dataAccessor, CollectorPool collectorPool, OperationManager
            om) {
        super(dataAccessor, collectorPool, om);
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        if (null == packetLite.payload) {
            return PacketLite.createException("payload no data", packetLite.seq);
        }

        MachineInfo machineInfo;
        try {
            machineInfo = JSON.parseObject(packetLite.payload, MachineInfo.class);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("handshake json parse error.{}", e);
            }

            return PacketLite.createException("payload format illegal", packetLite.seq);
        }


        List<MachineInfo> select;
        try {
            select = new MachineInfoSqlExecutor(dataAccessor.getConnection(), om.getOperation(SqlTemplate
                    .SELECT_MACHINE_BINDING), args)
                    .run();
            if (null != select && 1 < select.size()) {
                logger.error("duplicate host info with :{}", machineInfo);
                return PacketLite.createException("duplicate host info with system:" + machineInfo.getSystemId() + ",ip:" +
                        machineInfo.getIp() + ",port:" + machineInfo.getPort(), packetLite.seq);
            }

            if (null == select || 0 == select.size()) { // 新系统上线/换机器需要重新添加绑定信息
                machineInfo.setBindingId(System.currentTimeMillis());
                List<Object> obj = new ArrayList<>();
                obj.add(machineInfo.getSystemId());
                obj.add(machineInfo.getIp());
                obj.add(machineInfo.getBindingId());
                new MachineInfoSqlExecutor(dataAccessor.getConnection(), om.getOperation(SqlTemplate
                        .INSERT_MACHINE_BINDING), obj).run();
            } else {
                machineInfo = select.get(0);
            }

            return PacketLite.createHandshakeAck(packetLite.seq, machineInfo);
        } catch (Exception e) {
            logger.error("select machine info error.msg:{},trace:{}", e.getMessage(), e.getStackTrace());

            return PacketLite.createException(e.getMessage(), packetLite.seq);
        }
    }
}
