package com.business.client;

import com.business.communication.entity.OpcuaNode;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.reactivestreams.Subscriber;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class OpcuaClient {
    private static final String ENDPOINT_URL = "opc.tcp://localhost:4840"; // OPC-UA sunucu adresi
    private OpcUaClient client;
    private ManagedSubscription subscriptionManager;

    public OpcUaClient connect() throws Exception {
        client = OpcUaClient.create(ENDPOINT_URL);
        client.connect().get();
        System.out.println("Connected to OPC-UA server: " + ENDPOINT_URL);
        return client;
    }

    public void disconnect() throws Exception {
        if (client != null) {
            client.disconnect().get();
            System.out.println("Disconnected from OPC-UA server.");
        }
    }

    public ManagedSubscription getSubscriptionManager() {
        return subscriptionManager;
    }

    // SubscriptionManager için setter (opsiyonel)
    public void setSubscriptionManager(ManagedSubscription subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }



    public void createSubscription(OpcuaClient client, OpcuaNode opcuaNode) {
        try {
            // Eğer subscriptionManager null ise, yeni bir tane oluştur
            if (subscriptionManager == null) {
                subscriptionManager = ManagedSubscription.create(client);
                subscriptionManager.setPublishingInterval(1000.0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

    }

}

