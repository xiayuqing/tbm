package org.tbm.common.bean.vo;

/**
 * Created by Jason.Xia on 16/4/6.
 */
public class MemoryPoolInfo extends BaseInfo {
    private String name;
    private String memoryType;
    private Long usageThreshold;
    private MemoryUsageInfo usage;
    private MemoryUsageInfo peakUsage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public MemoryUsageInfo getUsage() {
        return usage;
    }

    public void setUsage(MemoryUsageInfo usage) {
        this.usage = usage;
    }

    public MemoryUsageInfo getPeakUsage() {
        return peakUsage;
    }

    public void setPeakUsage(MemoryUsageInfo peakUsage) {
        this.peakUsage = peakUsage;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public Long getUsageThreshold() {
        return usageThreshold;
    }

    public void setUsageThreshold(Long usageThreshold) {
        this.usageThreshold = usageThreshold;
    }
}
