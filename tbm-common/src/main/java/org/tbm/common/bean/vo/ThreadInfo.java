package org.tbm.common.bean.vo;

import org.tbm.common.bean.vo.BaseInfo;

/**
 * Created by Jason.Xia on 16/4/1.
 */
public class ThreadInfo extends BaseInfo {
    private Integer activeCount;
    private Integer daemonCount;
    private Integer peakCount;
    private long[] lockedCount;

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Integer getDaemonCount() {
        return daemonCount;
    }

    public void setDaemonCount(Integer daemonCount) {
        this.daemonCount = daemonCount;
    }

    public long[] getLockedCount() {
        return lockedCount;
    }

    public void setLockedCount(long[] lockedCount) {
        this.lockedCount = lockedCount;
    }

    public Integer getPeakCount() {
        return peakCount;
    }

    public void setPeakCount(Integer peakCount) {
        this.peakCount = peakCount;
    }
}
