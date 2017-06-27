package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlOperations;
import org.tbm.common.bean.MachineBinding;
import org.tbm.server.executor.MachineInfoSqlExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class MachineBindingOp extends AbstractOp {

    private MachineInfoSqlExecutor executor;


    public MachineBindingOp(DataAccessor dataAccessor, Map<String, Operation> ops) {
        super(dataAccessor);
        executor = new MachineInfoSqlExecutor();
        operationMap = ops;
    }

    public List<MachineBinding> SELECT_MACHINE_BINDING(long systemId, String ip) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(systemId);
        args.add(ip);

        return executor.setAndRun(args, getOp(SqlOperations.SELECT_MACHINE_BINDING), dataAccessor.getConnection());
    }

    public void INSERT_MACHINE_BINDING(MachineBinding info) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(info.getSystemId());
        args.add(info.getBindingId());
        args.add(info.getIp());
        args.add(info.getPort());
        args.add(info.getOs());
        args.add(info.getVersion());
        args.add(info.getArch());
        args.add(info.getAvailableProcessors());
        args.add(info.getJvmStart());
        args.add(info.getJvmUptime());
        args.add(info.getBootstrap());
        args.add(info.getClasspath());
        args.add(info.getLibPath());
        // status = online
        args.add(1);

        executor.setAndRun(args, getOp(SqlOperations.INSERT_MACHINE_BINDING), dataAccessor.getConnection());
    }

    public List<MachineBinding> run(String operation) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method method = MachineBinding.class.getMethod(operation);
        Object invoke = method.invoke(this, null);
        return null;
    }
}
