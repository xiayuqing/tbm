package org.tbm.common.access;

import org.tbm.common.utils.JsonConfigReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class OperationManager {
    private static OperationManager operationManager;
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    //    private Map<String, Operation> operationMap = new HashMap<>();
    private Map<String/*baseTableName*/, Map<String/*operationName*/, Operation>> tableOperations = new HashMap<>();

    public static OperationManager getOperationManager() {
        if (null == operationManager) {
            init(OperationManager.class.getResource("/operation.json").getFile());
        }

        return operationManager;
    }

    public static OperationManager init(String path) {
        if (!initialized.compareAndSet(false, true)) {
            return operationManager;
        }

        operationManager = new OperationManager();
        if (null == path || 0 == path.length()) {
            throw new IllegalArgumentException("Not found config file path :" + path);
        }

        String file = path;
        if (!path.endsWith(".json")) {
            file = path + (path.endsWith("/") ? "operation.json" : "/operation.json");
        }

        try {
            List<Operation> list = JsonConfigReader.readerArray(file, Operation.class);
            for (Operation item : list) {
                Map<String, Operation> operations = operationManager.tableOperations.get(item.getBaseName());
                if (null == operations) {
                    Map<String, Operation> map = new HashMap<>();
                    map.put(item.getName(), item);
                    operationManager.tableOperations.put(item.getBaseName(), map);
                } else {
                    operations.put(item.getName(), item);
                }
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }

        return operationManager;
    }

    public Map<String/*operationName*/, Operation> getOperations(String baseName) {
        return tableOperations.get(baseName);
    }

//    public Operation getOperation(SqlTemplate key) {
//        return this.operationMap.get(key.toString());
//    }
//
//    public Operation getOperation(String key) {
//        return this.operationMap.get(key);
//    }
}
