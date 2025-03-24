package business.client;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

public class OpcuaClient {

    private static final String ENDPOINT_URL = "opc.tcp://192.168.0.15:4842"; // OPC-UA sunucu adresi
    private OpcUaClient client;

    public OpcUaClient getClient() {
        return client;
    }

    public void setClient(OpcUaClient client) {
        this.client = client;
    }

    public OpcUaClient connect() {
        try {
            client = OpcUaClient.create(ENDPOINT_URL);
            client.connect().get();
            System.out.println("Connected to OPC-UA server: " + ENDPOINT_URL);
            return client;
        } catch (Exception e) {
            System.err.println("Failed to connect to OPC-UA server: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        }


        public void disconnect() {
            try {
                if (client != null) {
                    client.disconnect().get();
                    System.out.println("Disconnected from OPC-UA server");
                }
            } catch (Exception e) {
                System.err.println("Failed to disconnect from OPC-UA server: " + e.getMessage());
                e.printStackTrace();
            }


        }


}
