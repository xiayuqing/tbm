package org.tbm.common.bean.vo;

/**
 * Created by Jason.Xia on 16/4/6.
 */
public class MemoryUsageInfo extends BaseInfo {
    private Long max;
    private Long init;
    private Long committed;
    private Long used;

    public MemoryUsageInfo() {
    }

    public MemoryUsageInfo(Long max, Long init, Long committed, Long used) {
        this.max = max;
        this.init = init;
        this.committed = committed;
        this.used = used;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getInit() {
        return init;
    }

    public void setInit(Long init) {
        this.init = init;
    }

    public Long getCommitted() {
        return committed;
    }

    public void setCommitted(Long committed) {
        this.committed = committed;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }
}
