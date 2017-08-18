package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.CollectorPool;
import org.tbm.common.RedisPoolManager;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlOperations;
import org.tbm.common.bean.vo.BizData;
import org.tbm.common.utils.CollectionUtils;
import org.tbm.server.executor.LogDataSqlExecutor;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Jason.Xia on 17/7/10.
 */
public class BizOp extends AbstractOp {
    private final CollectorPool collectorPool;

    public BizOp(CollectorPool collectorPool, DataAccessor dataAccessor, Map<String, Operation> operationMap) {
        super(dataAccessor, operationMap);
        this.collectorPool = collectorPool;
    }

    public void INSERT_INTO_CACHE(List<BizData> data) {
        Jedis jedis = RedisPoolManager.getJedis();
        String[] arr = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            arr[i] = data.get(i).toString();
        }

        jedis.rpush(String.valueOf(data.get(0).getBindingId()), arr);
        RedisPoolManager.returnJedis(jedis);
    }

    public Future INSERT_BIZ(List<BizData> data) throws Exception {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }

        List<Object> args = new ArrayList<>();
        for (BizData item : data) {
            args.add(item.getBindingId());
            args.add(item.getTime());
            args.add(item.getLevel());
            args.add(item.getTrace());
            args.add(item.getClazz());
            args.add(item.getMethod());
            args.add(item.getLine());
            args.add(item.getContent());
        }

        return collectorPool.add(new LogDataSqlExecutor(dataAccessor.getConnection(), getOp(SqlOperations
                .INSERT_BIZ), args));
    }
}
