package org.tbm.common;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public interface State {
    int STOP = 0;
    int STARTING = 1;
    int STARTED = 2;
    int SHUTDOWN = 3;
}
