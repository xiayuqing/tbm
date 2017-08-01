package org.tbm.common;

import org.tbm.common.access.SqlExecutor;

import java.util.concurrent.Future;

/**
 * Created by Jason.Xia on 17/8/1.
 */
public interface CollectorPool {
    void run();

     void init() ;

    // 添加一个工作线程,单独负责一个队列
     void addScheduleJob(String bindingId);

     void removeScheduleJob(String bindingId) ;

     Future add(final SqlExecutor sqlExecutor) ;

    void stop() ;
}
