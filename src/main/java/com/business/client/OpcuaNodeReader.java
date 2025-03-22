package com.business.client;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.concurrent.ExecutionException;

public class OpcuaNodeReader {
    public static Object readNodeValue(OpcUaClient client, String nodeIdString) throws ExecutionException, InterruptedException {
        NodeId nodeId = NodeId.parse(nodeIdString);
        DataValue value = client.readValue(0, null, nodeId).get();
        return value.getValue().getValue();
    }
}
