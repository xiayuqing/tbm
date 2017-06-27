package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Processor;
import org.tbm.common.bean.MachineBinding;
import org.tbm.common.bean.PacketLite;
import org.tbm.server.operation.MachineBindingOp;
import org.tbm.server.operation.OpsFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class HandshakeProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeProcessor.class);

    private MachineBindingOp machineBindingOp = (MachineBindingOp) OpsFactory.get(MachineBinding.class);

    @Override
    public PacketLite process(PacketLite packetLite) {
        if (null == packetLite.payload) {
            return PacketLite.createException("payload no data", packetLite.seq);
        }

        MachineBinding machineInfo;
        try {
            machineInfo = JSON.parseObject(packetLite.payload, MachineBinding.class);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("handshake json parse error.{}", e);
            }

            return PacketLite.createException("payload format illegal", packetLite.seq);
        }


        List<MachineBinding> select;
        try {
            select = machineBindingOp.SELECT_MACHINE_BINDING(machineInfo.getSystemId(), machineInfo.getIp());
            if (null != select && 1 < select.size()) {
                logger.error("duplicate host info with :{}", machineInfo);
                return PacketLite.createException("duplicate host info with system:" + machineInfo.getSystemId() + "," +
                        "ip:" +
                        machineInfo.getIp() + ",port:" + machineInfo.getPort(), packetLite.seq);
            }

            if (null == select || 0 == select.size()) { // 新系统上线/换机器需要重新添加绑定信息
                machineInfo.setBindingId(System.currentTimeMillis());
                machineBindingOp.INSERT_MACHINE_BINDING(machineInfo);
            } else {
                machineInfo = select.get(0);
                machineBindingOp.UPDATE_MACHINE_BINDING_STATUS(machineInfo.getSystemId(), machineInfo.getIp());
            }

            return PacketLite.createHandshakeAck(packetLite.seq, machineInfo);
        } catch (Exception e) {
            logger.error("select machine info error.msg:{},trace:{}", e.getMessage(), e.getStackTrace());

            return PacketLite.createException(e.getMessage(), packetLite.seq);
        }
    }
}
