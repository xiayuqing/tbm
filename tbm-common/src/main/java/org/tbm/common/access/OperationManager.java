package org.tbm.common.access;

import org.tbm.common.utils.JsonConfigReader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class OperationManager {
    private static OperationManager operationManager;
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    private Map<String, Operation> operationMap = new HashMap<>();

    public static OperationManager getOperationManager() {
        if (null == operationManager) {
            init(OperationManager.class.getResource("/operation.json").getFile());
        }

        return operationManager;
    }

    public static void init(String path) {
        if (!initialized.compareAndSet(false, true)) {
            return;
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
            operationManager.operationMap = JsonConfigReader.readerObject(file, Operation.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public Operation getOperation(SqlTemplate key) {
        return this.operationMap.get(key.toString());
    }

    public Operation getOperation(String key) {
        return this.operationMap.get(key);
    }
}
