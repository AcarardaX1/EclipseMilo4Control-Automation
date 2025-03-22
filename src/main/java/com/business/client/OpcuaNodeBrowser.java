package com.business.client;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OpcuaNodeBrowser {
    public static void browseNodes(OpcUaClient client, NodeId nodeId) throws ExecutionException, InterruptedException {
        List<ReferenceDescription> references = client.getAddressSpace().browse(NodeId nodeId);

        for (ReferenceDescription ref : references) {
            System.out.println("Node: " + ref.getBrowseName().getName() + " (" + ref.getNodeId().toParseableString() + ")");

            // Eğer node'un altında başka node'lar varsa, bunları da gez
            ref.getNodeId().local().ifPresent(childNodeId -> {
                try {
                    browseNodes(client, nodeId);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };

        }
    }
}
