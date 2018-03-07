package com.jingl.cluster;

import com.jingl.common.exceptions.ConnectionFailedException;
import com.jingl.transfer.Transfer;

/**
 * Created by Ben on 13/02/2018.
 */
public interface Cluster {
    Transfer getTransfer(Class clazz) throws ConnectionFailedException;
}
