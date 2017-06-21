package org.tbm.common;

/**
 * Created by Jason.Xia on 17/6/21.
 */
public interface ConnectionState {
    int DISCONNECT = 0;
    int CONNECTING = 1;
    int CONNECTED = 2;
    int RECONNCTING = 3;
}
