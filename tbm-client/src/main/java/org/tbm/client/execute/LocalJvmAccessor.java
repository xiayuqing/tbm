package org.tbm.client.execute;

import org.tbm.client.ClientContext;
import org.tbm.common.bean.vo.*;
import org.tbm.common.bean.vo.ThreadInfo;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class LocalJvmAccessor {
    private static final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    private static final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();

    public JvmBaseInfo getJvmInfo() {
        JvmBaseInfo result = new JvmBaseInfo();
        result.setStart(runtimeMXBean.getStartTime());
        result.setUptime(runtimeMXBean.getUptime());
        result.setBootstrap(runtimeMXBean.getBootClassPath());
        result.setClassPath(runtimeMXBean.getClassPath());
        result.setLibPath(runtimeMXBean.getLibraryPath());
//        result.setTimestamp(System.currentTimeMillis());

        result.setArch(osMXBean.getArch());
        result.setVersion(osMXBean.getVersion());
        result.setAvailableProcessors(osMXBean.getAvailableProcessors());
        result.setSystemLoadAverage(osMXBean.getSystemLoadAverage());
        return result;
    }

    public MemorySummaryInfo getMemorySummaryInfo() {
        MemorySummaryInfo result = new MemorySummaryInfo();
        result.setHeapInfo(getHeapInfo());
        result.setNonHeapInfo(getNonHeapInfo());
//        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public List<MemoryPoolInfo> getMemoryPoolsInfo() {
        List<MemoryPoolInfo> result = new ArrayList<>();
        for (MemoryPoolMXBean item : memoryPoolMXBeans) {
            MemoryPoolInfo poolInfo = new MemoryPoolInfo();
            poolInfo.setName(item.getName());
            poolInfo.setMemoryType(item.getType().toString());
            if (item.isUsageThresholdSupported()) {
                poolInfo.setUsageThreshold(item.getUsageThreshold());
            }

            // usage
            MemoryUsage usage = item.getUsage();
            poolInfo.setUsage(new MemoryUsageInfo(usage.getMax(), usage.getInit(), usage.getCommitted(), usage
                    .getUsed()));
            // peak usage
            MemoryUsage peakUsage = item.getPeakUsage();
            poolInfo.setPeakUsage(new MemoryUsageInfo(peakUsage.getMax(), peakUsage.getInit(), peakUsage.getCommitted
                    (), peakUsage.getUsed()));

//            poolInfo.setTimestamp(System.currentTimeMillis());
            result.add(poolInfo);
        }

        return result;
    }

    public ClassLoadingInfo getClassLoadingInfo() {
        ClassLoadingInfo result = new ClassLoadingInfo();
        result.setLoadedClassCount(classLoadingMXBean.getLoadedClassCount());
        result.setTotalLoadedClassCount(classLoadingMXBean.getTotalLoadedClassCount());
        result.setUnloadedClassCount(classLoadingMXBean.getUnloadedClassCount());
//        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public ThreadInfo getThreadInfo() {
        ThreadInfo result = new ThreadInfo();
        result.setActiveCount(threadMXBean.getThreadCount());
        result.setDaemonCount(threadMXBean.getDaemonThreadCount());
        result.setPeakCount(threadMXBean.getPeakThreadCount());
        result.setLockedCount(threadMXBean.findDeadlockedThreads());
//        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public HeapInfo getHeapInfo() {
        HeapInfo result = new HeapInfo();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        result.setMax(heapMemoryUsage.getMax());
        result.setInit(heapMemoryUsage.getInit());
        result.setCommitted(heapMemoryUsage.getCommitted());
        result.setUsed(heapMemoryUsage.getUsed());
        return result;
    }

    public NonHeapInfo getNonHeapInfo() {
        NonHeapInfo result = new NonHeapInfo();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        result.setMax(nonHeapMemoryUsage.getMax());
        result.setInit(nonHeapMemoryUsage.getInit());
        result.setCommitted(nonHeapMemoryUsage.getCommitted());
        result.setUsed(nonHeapMemoryUsage.getUsed());
        return result;
    }

    public JvmData fullPackageData() {
        JvmData result = new JvmData();
        if (0 == ClientContext.BINDING_ID) {
            throw new IllegalStateException("client have no binding id");
        }

        result.setBindingId(ClientContext.BINDING_ID);
        result.setMemorySummaryInfo(getMemorySummaryInfo());
        result.setMemoryPoolInfo(getMemoryPoolsInfo());
        result.setClassLoadingInfo(getClassLoadingInfo());
        result.setThreadInfo(getThreadInfo());

        return result;
    }
}
