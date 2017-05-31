package org.tbm.common.bean.vo;

import org.tbm.common.bean.Serialize;

import java.util.List;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class PackageData extends Serialize {
    private long bindingId;
    private MemorySummaryInfo memorySummaryInfo;
    private List<MemoryPoolInfo> memoryPoolInfo;
    private ClassLoadingInfo classLoadingInfo;
    private ThreadInfo threadInfo;

    public PackageData() {
    }

    public PackageData(long bindingId) {
        this.bindingId = bindingId;
    }

    public MemorySummaryInfo getMemorySummaryInfo() {
        return memorySummaryInfo;
    }

    public void setMemorySummaryInfo(MemorySummaryInfo memorySummaryInfo) {
        this.memorySummaryInfo = memorySummaryInfo;
    }

    public List<MemoryPoolInfo> getMemoryPoolInfo() {
        return memoryPoolInfo;
    }

    public void setMemoryPoolInfo(List<MemoryPoolInfo> memoryPoolInfo) {
        this.memoryPoolInfo = memoryPoolInfo;
    }

    public ClassLoadingInfo getClassLoadingInfo() {
        return classLoadingInfo;
    }

    public void setClassLoadingInfo(ClassLoadingInfo classLoadingInfo) {
        this.classLoadingInfo = classLoadingInfo;
    }

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public long getBindingId() {
        return bindingId;
    }

    public PackageData setBindingId(long bindingId) {
        this.bindingId = bindingId;
        return this;
    }
}
