package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.MemoryType;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.vo.*;
import org.tbm.common.utils.CollectionUtils;
import org.tbm.server.operation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class JvmDataCollectProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(JvmDataCollectProcessor.class);

    private MemoryPoolOp memoryPoolOp = (MemoryPoolOp) OpsFactory.get(MemoryPoolOp.class);

    private MemorySummaryOp memorySummaryOp = (MemorySummaryOp) OpsFactory.get(MemorySummaryOp.class);

    private ThreadOp threadOp = (ThreadOp) OpsFactory.get(ThreadOp.class);

    private ClassLoadOp classLoadOp = (ClassLoadOp) OpsFactory.get(ClassLoadOp.class);

    public JvmDataCollectProcessor() {
    }

    @Override
    public PacketLite process(PacketLite packetLite) {
        List<JvmData> jvmData = JSON.parseArray(packetLite.payload, JvmData.class);
        if (null == jvmData || 0 == jvmData.size()) {
            return PacketLite.createAck(packetLite.seq);
        }

        try {
            Map<String, List<Object>> extract = extract(jvmData);
            List<Object> summary = extract.get(MemoryType.MEMORY_SUMMARY);
            if (!CollectionUtils.isEmpty(summary)) {
                memorySummaryOp.INSERT_MEMORY_SUMMARY(summary);
            }

            List<Object> pool = extract.get(MemoryType.MEMORY_POOL);
            if (!CollectionUtils.isEmpty(pool)) {
                memoryPoolOp.INSERT_MEMORY_POOL(pool);
            }

            List<Object> classLoad = extract.get(MemoryType.CLASS_LOAD);
            if (!CollectionUtils.isEmpty(classLoad)) {
                classLoadOp.INSERT_CLASS_LOAD(classLoad);
            }

            List<Object> thread = extract.get(MemoryType.THREAD);
            if (!CollectionUtils.isEmpty(thread)) {
                threadOp.INSERT_THREAD(thread);
            }
        } catch (Exception e) {
            logger.error("insert jvm statistic data error.seq:{},msg:{},trace:{}", packetLite.seq, e
                    .getMessage(), e.getStackTrace());
            PacketLite.createError(e.getMessage(), packetLite.seq);
        }

        return PacketLite.createAck(packetLite.seq);
    }

    private Map<String/*memory type*/, List<Object>> extract(List<JvmData> params) {
        if (null == params) {
            return null;
        }

        Map<String, List<Object>> map = new HashMap<>();
        List<Object> memorySummaryData = new ArrayList<>();
        List<Object> memoryPoolData = new ArrayList<>();
        List<Object> classLoadData = new ArrayList<>();
        List<Object> threadData = new ArrayList<>();
        for (JvmData item : params) {
            MemorySummaryInfo memorySummaryInfo = item.getMemorySummaryInfo();
            // heap summary
            memorySummaryData.addAll(getMemoryUsageInfo(memorySummaryInfo.getHeapInfo(), item.getBindingId(), MemoryType
                    .SUMMARY_HEAP));
            // non-heap summary
            memorySummaryData.addAll(getMemoryUsageInfo(memorySummaryInfo.getNonHeapInfo(), item.getBindingId(),
                    MemoryType
                            .SUMMARY_NON_HEAP));
            // memory pool
            for (MemoryPoolInfo info : item.getMemoryPoolInfo()) {
                memoryPoolData.addAll(getMemoryPool(info, item.getBindingId()));
            }

            // class load

            classLoadData.addAll(getClassLoad(item.getClassLoadingInfo(), item.getBindingId()));
            // thread
            threadData.addAll(getThread(item.getThreadInfo(), item.getBindingId()));
        }

        map.put(MemoryType.MEMORY_SUMMARY, memorySummaryData);
        map.put(MemoryType.MEMORY_POOL, memoryPoolData);
        map.put(MemoryType.CLASS_LOAD, classLoadData);
        map.put(MemoryType.THREAD, threadData);
        return map;
    }

    private List<Object> getMemoryUsageInfo(MemoryUsageInfo usageInfo, long bindingId, int memoryType) {
        List<Object> result = new ArrayList<>();
        result.add(bindingId);
        result.add(memoryType);
        result.add(usageInfo.getTimestamp());
        result.add(usageInfo.getMax());
        result.add(usageInfo.getInit());
        result.add(usageInfo.getCommitted());
        result.add(usageInfo.getUsed());
        return result;
    }

    private List<Object> getMemoryPool(MemoryPoolInfo poolInfo, long bindingId) {
        List<Object> result = new ArrayList<>();
        result.add(bindingId);
        switch (poolInfo.getName()) {
            case MemoryType.STR_POOL_NON_HEAP_CODE_CACHE:
                result.add(MemoryType.SUMMARY_NON_HEAP);
                result.add(MemoryType.POOL_NON_HEAP_CODE_CACHE);
                break;
            case MemoryType.STR_POOL_HEAP_PS_EDEN_SPACE:
                result.add(MemoryType.SUMMARY_HEAP);
                result.add(MemoryType.POOL_HEAP_PS_EDEN_SPACE);
                break;
            case MemoryType.STR_POOL_HEAP_PS_SURVIVOR_SPACE:
                result.add(MemoryType.SUMMARY_HEAP);
                result.add(MemoryType.POOL_HEAP_PS_SURVIVOR_SPACE);
                break;
            case MemoryType.STR_POOL_HEAP_PS_OLD_GEN:
                result.add(MemoryType.SUMMARY_HEAP);
                result.add(MemoryType.POOL_HEAP_PS_OLD_GEN);
                break;
            case MemoryType.STR_POOL_NON_HEAP_PS_PERM_GEN:
                result.add(MemoryType.SUMMARY_NON_HEAP);
                result.add(MemoryType.POOL_NON_HEAP_PS_PERM_GEN);
                break;
            default:
                result.add(0);
                result.add(0);
        }

        result.add(poolInfo.getName());
        result.add(poolInfo.getTimestamp());
        MemoryUsageInfo peakUsage = poolInfo.getPeakUsage();
        if (null == peakUsage) {
            result.add(0);
            result.add(0);
            result.add(0);
            result.add(0);
        } else {
            result.add(peakUsage.getMax());
            result.add(peakUsage.getInit());
            result.add(peakUsage.getCommitted());
            result.add(peakUsage.getUsed());
        }

        MemoryUsageInfo usage = poolInfo.getUsage();
        if (null == usage) {
            result.add(0);
            result.add(0);
            result.add(0);
            result.add(0);
        } else {
            result.add(usage.getMax());
            result.add(usage.getInit());
            result.add(usage.getCommitted());
            result.add(usage.getUsed());
        }

        Long usageThreshold = poolInfo.getUsageThreshold();
        result.add(null == usageThreshold ? 0 : usageThreshold);

        return result;
    }

    private List<Object> getClassLoad(ClassLoadingInfo loadingInfo, long bindingId) {
        List<Object> result = new ArrayList<>();
        result.add(bindingId);
        result.add(loadingInfo.getTimestamp());
        result.add(loadingInfo.getLoadedClassCount());
        result.add(loadingInfo.getTotalLoadedClassCount());
        result.add(loadingInfo.getUnloadedClassCount());
        return result;
    }

    private List<Object> getThread(ThreadInfo threadInfo, long bindingId) {
        List<Object> result = new ArrayList<>();
        result.add(bindingId);
        result.add(threadInfo.getTimestamp());
        result.add(threadInfo.getActiveCount());
        result.add(threadInfo.getDaemonCount());
        result.add(threadInfo.getPeakCount());
        // TODO locked thread
        result.add(0);
        return result;
    }
}
