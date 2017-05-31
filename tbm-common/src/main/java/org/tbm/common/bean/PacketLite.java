package org.tbm.common.bean;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class PacketLite extends Serialize {
    private int type;
    private String payload;

    public static PacketLite createHandshake(long systemId, String host, int port) {
        PacketLite packetLite = new PacketLite();
        packetLite.setType(PACKET_TYPE.HANDSHAKE);
        packetLite.setPayload(new HostInfo(systemId, host, port).toString());
        return packetLite;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public interface PACKET_TYPE {
        int HANDSHAKE = 1;
        int ACK = 2;
        int JVM_DATA = 3;
        int BIZ_DATA = 4;
    }
}
