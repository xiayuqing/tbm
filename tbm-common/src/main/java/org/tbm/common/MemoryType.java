package org.tbm.common;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public interface MemoryType {
    int SUMMARY_HEAP=100;
    int SUMMARY_NON_HEAP=200;
    int POOL_NON_HEAP_CODE_CACHE=201;
    int POOL_NON_HEAP_PS_PERM_GEN=202;
    int POOL_HEAP_PS_EDEN_SPACE=101;
    int POOL_HEAP_PS_SURVIVOR_SPACE=102;
    int POOL_HEAP_PS_OLD_GEN=103;
}
