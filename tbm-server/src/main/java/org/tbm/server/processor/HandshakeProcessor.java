package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Processor;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.bean.PacketLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class HandshakeProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeProcessor.class);
    private DataAccessor dataAccessor = DataAccessorFactory.getInstance();

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
        args.add(hostInfo.getPort());
        List<Object> select;
        try {
            select = dataAccessor.select(SqlTemplate.SELECT_MACHINE_INFO, args, HostInfo.class);
            if (null != select && 1 < select.size()) {
                logger.error("duplicate host info with :{}", hostInfo);
                return PacketLite.createException("duplicate host info with system:" + hostInfo.getSystemId() + ",ip:" +
                        hostInfo.getIp() + ",port:" + hostInfo.getPort(), packetLite.seq);
            }

            if (null == select || 0 == select.size()) { // 新系统上线,换了机器/端口需要重新添加绑定信息
                hostInfo.setBindingId(System.currentTimeMillis());
                List<Object> obj = new ArrayList<>();
                obj.add(hostInfo.getSystemId());
                obj.add(hostInfo.getIp());
                obj.add(hostInfo.getPort());
                obj.add(hostInfo.getBindingId());
                dataAccessor.insert(SqlTemplate.INSERT_MACHINE_INFO, obj);
            } else {
                hostInfo = (HostInfo) select.get(0);
            }

            return PacketLite.createHandshakeAck(packetLite.seq, hostInfo);
        } catch (Exception e) {
            logger.error("select machine info error.msg:{},trace:{}", e.getMessage(), e.getStackTrace());

            return PacketLite.createException(e.getMessage(), packetLite.seq);
        }
    }
}
