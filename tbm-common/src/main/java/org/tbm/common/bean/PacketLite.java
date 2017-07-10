package org.tbm.common.bean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.tbm.common.AppContext;
import org.tbm.common.bean.vo.BizData;
import org.tbm.common.bean.vo.JvmData;
import org.tbm.common.utils.DigestUtils;
import org.tbm.common.utils.NetUtils;

import java.util.List;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class PacketLite extends Serialize {
    public static final ByteBuf HEARTBEAT_PACKET = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new PacketLite
            (PACKET_TYPE.HEARTBEAT, NetUtils.getLocalAddress().toString() + AppContext.SYSTEM_ID, DigestUtils
                    .getUUIDWithoutStrike()).toString() + "\r\n", CharsetUtil.UTF_8));

    public int type;
    public String seq;
    public String payload;

    private PacketLite() {
    }

    private PacketLite(int type, String seq) {
        this.type = type;
        this.seq = seq;
    }

    private PacketLite(int type, String msg, String seq) {
        this.type = type;
        this.seq = seq;
        this.payload = msg;
    }

    public static PacketLite createJvmDataPackage(List<JvmData> data) {
        PacketLite packetLite = new PacketLite(PACKET_TYPE.JVM_DATA, DigestUtils.getUUIDWithoutStrike());
        packetLite.payload = data.toString();
        return packetLite;
    }

    public static PacketLite createHandshake(MachineBinding machineInfo) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = machineInfo.toString();
        packetLite.seq = DigestUtils.getUUIDWithoutStrike();
        return packetLite;
    }

    public static PacketLite createHandshakeAck(String seq, MachineBinding hostInfo) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = hostInfo.toString();
        packetLite.seq = seq;
        return packetLite;
    }

    public static PacketLite createBizDataPackage(List<BizData> data) {
        PacketLite lite = new PacketLite(PACKET_TYPE.BIZ_DATA, DigestUtils.getUUIDWithoutStrike());
        lite.payload = data.toString();
        return lite;
    }

    public static PacketLite createAck(String seq) {
        return new PacketLite(PACKET_TYPE.ACK, seq);
    }

    public static PacketLite createException(String msg, String seq) {
        return new PacketLite(PACKET_TYPE.EXCEPTION, msg, seq);
    }

    public static PacketLite createError(String msg, String seq) {
        return new PacketLite(PACKET_TYPE.ERROR, msg, seq);
    }

    public interface PACKET_TYPE {
        int HEARTBEAT = -1;
        int HANDSHAKE = 1;
        int ACK = 2;
        int EXCEPTION = 3;
        // 服务器错误
        int ERROR = 4;
        int JVM_DATA = 5;
        int BIZ_DATA = 6;
    }
}
