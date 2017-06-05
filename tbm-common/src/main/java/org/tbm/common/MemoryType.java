package org.tbm.common;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public interface MemoryType {
    int SUMMARY_HEAP = 100;
    int SUMMARY_NON_HEAP = 200;
    int POOL_NON_HEAP_CODE_CACHE = 201;
    int POOL_NON_HEAP_PS_PERM_GEN = 202;
    int POOL_HEAP_PS_EDEN_SPACE = 101;
    int POOL_HEAP_PS_SURVIVOR_SPACE = 102;
    int POOL_HEAP_PS_OLD_GEN = 103;

    String STR_SUMMARY_HEAP = "Heap memory";
    String STR_SUMMARY_NON_HEAP = "Non-heap memory";
    String STR_POOL_NON_HEAP_CODE_CACHE = "Code Cache";
    String STR_POOL_NON_HEAP_PS_PERM_GEN = "PS Perm Gen";
    String STR_POOL_HEAP_PS_EDEN_SPACE = "PS Eden Space";
    String STR_POOL_HEAP_PS_SURVIVOR_SPACE = "PS Survivor Space";
    String STR_POOL_HEAP_PS_OLD_GEN = "PS Old Gen";

    String MEMRORY_SUMMARY = "memory summary";
    String MEMORY_POOL = "memory pool";
    String CLASS_LOAD = "class load";
    String THREAD = "thread";
}
