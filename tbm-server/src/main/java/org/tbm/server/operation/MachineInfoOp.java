package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.MachineInfo;
import org.tbm.server.executor.MachineInfoSqlExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class MachineInfoOp extends AbstractOp {

    private MachineInfoSqlExecutor executor;
    private Map<String, Operation> operationMap;

    public MachineInfoOp(DataAccessor dataAccessor, List<Operation> ops) {
        super(dataAccessor);
        executor = new MachineInfoSqlExecutor();
        operationMap = new HashMap<>();
        for (Operation item : ops) {
            operationMap.put(item.getName(), item);
        }
    }

    public List<MachineInfo> SELECT_MACHINE_BINDING(long systemId, String ip) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(systemId);
        args.add(ip);
        return executor.setAndRun(args, operationMap.get(SqlTemplate.SELECT_MACHINE_BINDING), dataAccessor
                .getConnection());
    }

    public List<MachineInfo> run(String operation) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method method = MachineInfo.class.getMethod(operation);
        Object invoke = method.invoke(this, null);
        return null;
    }
}
