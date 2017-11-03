package org.tbm.common.bean;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.tbm.common.Serialize;
import org.tbm.common.util.Utils;

import java.util.List;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class PacketLite extends Serialize {
    public static final ByteBuf HEARTBEAT_PACKET = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new PacketLite
            (PACKET_TYPE.HEARTBEAT, Utils.getLocalAddress().toString(), Utils.getUUIDWithoutStrike()).toString() +
            "\r\n", CharsetUtil.UTF_8));

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

    public static PacketLite createHandshake(WorkNode machineInfo) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = machineInfo.toString();
        packetLite.seq = Utils.getUUIDWithoutStrike();
        return packetLite;
    }

    public static PacketLite createHandshakeAck(String seq, WorkNode hostInfo) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = hostInfo.toString();
        packetLite.seq = seq;
        return packetLite;
    }

    public static PacketLite createLogDataPackage(JSONObject data) {
        PacketLite lite = new PacketLite(PACKET_TYPE.LOG_DATA, Utils.getUUIDWithoutStrike());
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
        int LOG_DATA = 5;
    }
}
