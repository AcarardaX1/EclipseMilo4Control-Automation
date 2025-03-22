package com.business;

import com.business.client.OpcuaClient;
import com.business.client.OpcuaNodeBrowser;
import com.business.client.OpcuaNodeReader;
import com.business.communication.controller.OpcuaController;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class Main {
    public static void main(String[] args) {

        OpcuaController controller = new OpcuaController();

        try {
            OpcuaClient opcuaClient = new OpcuaClient();
            var client = opcuaClient.connect();

            // Root node’dan başlayarak tüm node'ları listele
            System.out.println("Browsing nodes...");
            OpcuaNodeBrowser.browseNodes(client, NodeId.parse("ns=0;i=1000")); // Objects folder

            // Belirli bir node'un değerini oku (örnek: ns=2;s=ProdCounterAct)
            Object value = OpcuaNodeReader.readNodeValue(client, "ns=2;s=ProdCounterAct");
            System.out.println("Node Value: " + value);

            // Bağlantıyı kapat
            opcuaClient.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}