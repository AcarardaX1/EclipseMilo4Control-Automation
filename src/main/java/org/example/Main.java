package org.example;

import business.client.OpcuaClient;
import business.reader.NodeReader;
import business.reader.SubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {


        OpcuaClient opcuaClient = new OpcuaClient();
        opcuaClient.connect();

        SubscriptionManager subscriptionManager = new SubscriptionManager();

        try{
            subscriptionManager.createSubscription(opcuaClient.getClient());
        } catch (UaException | InterruptedException | ExecutionException e) {
            System.err.println("Failed to read node: " + e.getMessage());
            e.printStackTrace();
        }

       /*NodeReader nodeReader = new NodeReader();
        try {
            nodeReader.readNode(opcuaClient.getClient());
        } catch (Exception e) {
            System.err.println("Failed to read node: " + e.getMessage());
            e.printStackTrace();
        }*/
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        scheduler.schedule(() -> {
//            System.out.println("30 seconds have passed. Continuing with next operations...");
//        }, 30, TimeUnit.SECONDS);

        System.out.println("Waiting for 30 seconds before continuing...");
        try {
            Thread.sleep(30000);  // Bekleme süresi
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("30 seconds have passed. Continuing with next operations...");

        opcuaClient.disconnect();
    }

}