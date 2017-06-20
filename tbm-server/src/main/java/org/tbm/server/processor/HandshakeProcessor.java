package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.server.collect.CollectorPool;
import org.tbm.server.collect.MachineInfoSqlExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class HandshakeProcessor extends AbstractProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeProcessor.class);

    public HandshakeProcessor(DataAccessor dataAccessor) {
        super(dataAccessor, null);
    }

    public HandshakeProcessor(DataAccessor dataAccessor, CollectorPool collectorPool) {
        super(dataAccessor, collectorPool);
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        if (null == packetLite.payload) {
            return PacketLite.createException("payload no data", packetLite.seq);
        }

        HostInfo hostInfo;
        try {
            hostInfo = JSON.parseObject(packetLite.payload, HostInfo.class);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("handshake json parse error.{}", e);
            }

            return PacketLite.createException("payload format illegal", packetLite.seq);
        }

        List<Object> args = new ArrayList<>();
        args.add(hostInfo.getSystemId());
        args.add(hostInfo.getIp());
        List<HostInfo> select;
        try {
            select = new MachineInfoSqlExecutor(dataAccessor.getConnection(), SqlTemplate.SELECT_MACHINE_INFO, args)
                    .run();
//            select = dataAccessor.select(SqlTemplate.SELECT_MACHINE_INFO.sql, args, HostInfo.class);
            if (null != select && 1 < select.size()) {
                logger.error("duplicate host info with :{}", hostInfo);
                return PacketLite.createException("duplicate host info with system:" + hostInfo.getSystemId() + ",ip:" +
                        hostInfo.getIp() + ",port:" + hostInfo.getPort(), packetLite.seq);
            }

            if (null == select || 0 == select.size()) { // 新系统上线/换机器需要重新添加绑定信息
                hostInfo.setBindingId(System.currentTimeMillis());
                List<Object> obj = new ArrayList<>();
                obj.add(hostInfo.getSystemId());
                obj.add(hostInfo.getIp());
                obj.add(hostInfo.getBindingId());
                new MachineInfoSqlExecutor(dataAccessor.getConnection(), SqlTemplate.INSERT_MACHINE_INFO, obj)
                        .run();
//                dataAccessor.insert(SqlTemplate.INSERT_MACHINE_INFO.sql, obj);
            } else {
                hostInfo = select.get(0);
            }

            return PacketLite.createHandshakeAck(packetLite.seq, hostInfo);
        } catch (Exception e) {
            logger.error("select machine info error.msg:{},trace:{}", e.getMessage(), e.getStackTrace());

            return PacketLite.createException(e.getMessage(), packetLite.seq);
        }
    }
}
