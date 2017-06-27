package org.tbm.client;

import org.tbm.common.AppContext;
import org.tbm.common.bean.MachineBinding;
import org.tbm.common.bean.vo.JvmBaseInfo;
import org.tbm.common.utils.StringUtils;

import java.util.Properties;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class ClientContext extends AppContext {

    public static long BINDING_ID;

    public static long SYSTEM_ID;

    public static String IP;

    public static int PORT;

    public static String OS;

    public static String ARCH;

    public static String VERSION;

    public static int AVAILABLE_PROCESSORS;

    public static long JVM_START;

    public static long JVM_UPTIME;

    public static String JVM_BOOTSTRAP;

    public static String JVM_CLASS_PATH;

    public static String JVM_LIB_PATH;

    public ClientContext() {
    }

    public static void load(String path) {
        AppContext.load(path);

        setSystemId();
    }

    public static void setProperties(Properties properties) {
        AppContext.setProperties(properties);

        setSystemId();
    }

    private static void setSystemId() {
        if (StringUtils.isEmpty(AppContext.getString("system.id"))) {
            throw new IllegalArgumentException("not found properties:system.id");
        }

        SYSTEM_ID = Long.valueOf(AppContext.getString("system.id"));
    }

    public static void initJvmBaseInfo(JvmBaseInfo jvmBaseInfo) {
        OS = jvmBaseInfo.getOs();
        ARCH = jvmBaseInfo.getArch();
        VERSION = jvmBaseInfo.getVersion();
        AVAILABLE_PROCESSORS = jvmBaseInfo.getAvailableProcessors();

        JVM_START = jvmBaseInfo.getStart();
        JVM_UPTIME = jvmBaseInfo.getUptime();
        JVM_BOOTSTRAP = jvmBaseInfo.getBootstrap();
        JVM_CLASS_PATH = jvmBaseInfo.getClassPath();
        JVM_LIB_PATH = jvmBaseInfo.getLibPath();
    }

    public static MachineBinding getMachineInfo() {
        MachineBinding info = new MachineBinding(SYSTEM_ID, IP, PORT);
        info.setBindingId(BINDING_ID);
        info.setOs(OS);
        info.setVersion(VERSION);
        info.setArch(ARCH);
        info.setAvailableProcessors(AVAILABLE_PROCESSORS);
        info.setJvmStart(JVM_START);
        info.setJvmUptime(JVM_UPTIME);
        info.setBootstrap(JVM_BOOTSTRAP);
        info.setClasspath(JVM_CLASS_PATH);
        info.setLibPath(JVM_LIB_PATH);
        return info;
    }
}
