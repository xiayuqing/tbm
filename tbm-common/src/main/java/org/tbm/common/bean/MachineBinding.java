package org.tbm.common.bean;

import org.tbm.common.annotation.Column;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class MachineBinding extends Serialize {
    @Column("system_id")
    private long systemId;

    @Column("binding_id")
    private long bindingId;

    @Column("ip")
    private String ip;

    @Column("os")
    private String os;

    @Column("version")
    private String version;

    @Column("arch")
    private String arch;

    @Column("available_processors")
    private int availableProcessors;

    @Column("jvm_start")
    private long jvmStart;

    public MachineBinding() {
    }

    public MachineBinding(long systemId) {
        this.systemId = systemId;
    }

    public void setAddress(String ip, int port) {
        this.ip = ip;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getBindingId() {
        return bindingId;
    }

    public void setBindingId(long bindingId) {
        this.bindingId = bindingId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public long getJvmStart() {
        return jvmStart;
    }

    public void setJvmStart(long jvmStart) {
        this.jvmStart = jvmStart;
    }

}
