package org.tbm.common.bean.vo;

/**
 * Created by Jason.Xia on 16/4/1.
 */
public class ClassLoadingInfo extends BaseInfo {
    private Integer loadedClassCount;
    private Long totalLoadedClassCount;
    private Long unloadedClassCount;

    public Integer getLoadedClassCount() {
        return loadedClassCount;
    }

    public void setLoadedClassCount(Integer loadedClassCount) {
        this.loadedClassCount = loadedClassCount;
    }

    public Long getTotalLoadedClassCount() {
        return totalLoadedClassCount;
    }

    public void setTotalLoadedClassCount(Long totalLoadedClassCount) {
        this.totalLoadedClassCount = totalLoadedClassCount;
    }

    public Long getUnloadedClassCount() {
        return unloadedClassCount;
    }

    public void setUnloadedClassCount(Long unloadedClassCount) {
        this.unloadedClassCount = unloadedClassCount;
    }
}
