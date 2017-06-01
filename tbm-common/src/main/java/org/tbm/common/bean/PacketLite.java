package org.tbm.common.bean;

import java.util.UUID;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class PacketLite extends Serialize {
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

    public static PacketLite createHandshake(long systemId, String host, int port) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = new HostInfo(systemId, host, port).toString();
        packetLite.seq = UUID.randomUUID().toString().replace("-", "");
        return packetLite;
    }

    public static PacketLite createHandshakeAck(String seq, HostInfo hostInfo) {
        PacketLite packetLite = new PacketLite();
        packetLite.type = PACKET_TYPE.HANDSHAKE;
        packetLite.payload = hostInfo.toString();
        packetLite.seq = seq;
        return packetLite;
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
        int HANDSHAKE = 1;
        int ACK = 2;
        int EXCEPTION = 3;
        // 服务器错误
        int ERROR = 4;
        int JVM_DATA = 5;
        int BIZ_DATA = 6;
    }
}
