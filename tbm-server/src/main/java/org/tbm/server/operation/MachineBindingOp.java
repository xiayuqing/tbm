package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlOperations;
import org.tbm.common.bean.MachineBinding;
import org.tbm.server.executor.MachineInfoSqlExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class MachineBindingOp extends AbstractOp {

    private MachineInfoSqlExecutor executor;


    public MachineBindingOp(DataAccessor dataAccessor, Map<String, Operation> ops) {
        super(dataAccessor, ops);
        executor = new MachineInfoSqlExecutor();
    }

    public List<MachineBinding> SELECT_MACHINE_BINDING(long systemId, String ip) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(systemId);
        args.add(ip);

        return executor.setAndRun(args, getOp(SqlOperations.SELECT_MACHINE_BINDING), dataAccessor.getConnection());
    }

    public void UPDATE_MACHINE_BINDING_STATUS(long systemId, String ip) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(systemId);
        args.add(ip);

        executor.setAndRun(args, getOp(SqlOperations.UPDATE_MACHINE_BINDING_STATUS), dataAccessor.getConnection());
    }

    public void INSERT_MACHINE_BINDING(MachineBinding info) throws Exception {
        List<Object> args = new ArrayList<>();
        args.add(info.getSystemId());
        args.add(info.getBindingId());
        args.add(info.getIp());
        args.add(info.getOs());
        args.add(info.getVersion());
        args.add(info.getArch());
        args.add(info.getAvailableProcessors());
        args.add(info.getJvmStart());
        // status = online
        args.add(1);

        executor.setAndRun(args, getOp(SqlOperations.INSERT_MACHINE_BINDING), dataAccessor.getConnection());
    }
}
