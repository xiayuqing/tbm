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

    @Column("port")
    private int port;

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

    @Column("jvm_uptime")
    private long jvmUptime;

    @Column("jvm_bootstrap")
    private String bootstrap;

    @Column("jvm_classpath")
    private String classpath;

    @Column("jvm_libpath")
    private String libPath;

    public MachineBinding() {
    }

    public MachineBinding(long systemId) {
        this.systemId = systemId;
        this.ip = ip;
        this.port = port;
    }

    public MachineBinding(long systemId, String ip, int port) {
        this.systemId = systemId;
        this.ip = ip;
        this.port = port;
    }

    public void setAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public long getJvmUptime() {
        return jvmUptime;
    }

    public void setJvmUptime(long jvmUptime) {
        this.jvmUptime = jvmUptime;
    }

    public String getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getLibPath() {
        return libPath;
    }

    public void setLibPath(String libPath) {
        this.libPath = libPath;
    }
}
