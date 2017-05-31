package org.tbm.common.bean.vo;

/**
 * Created by Jason.Xia on 16/4/1.
 */
public class MemorySummaryInfo extends BaseInfo {
    private HeapInfo heapInfo;
    private NonHeapInfo nonHeapInfo;

    public HeapInfo getHeapInfo() {
        return heapInfo;
    }

    public void setHeapInfo(HeapInfo heapInfo) {
        this.heapInfo = heapInfo;
    }

    public NonHeapInfo getNonHeapInfo() {
        return nonHeapInfo;
    }

    public void setNonHeapInfo(NonHeapInfo nonHeapInfo) {
        this.nonHeapInfo = nonHeapInfo;
    }

}
