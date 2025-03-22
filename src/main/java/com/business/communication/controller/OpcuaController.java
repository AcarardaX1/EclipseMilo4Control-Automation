package com.business.communication.controller;

import com.business.client.OpcuaClient;
import com.business.communication.boundary.OpcuaService;
import com.business.communication.entity.OpcuaDevice;
import com.business.communication.entity.OpcuaNode;
import com.business.entity.Machine;
import io.netty.channel.ConnectTimeoutException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.business.communication.controller.CategoryVariableProduction;

@Startup
@Singleton
public class OpcuaController {

    @Inject
    OpcuaService opcuaService;

    private Map<OpcUaClient, OpcuaDevice> clients;
    private Map<Machine, Integer> lastCycleCounters;  // lastCycleCounters adı yerine lastCycleTime kullanılabilir, bu senin ihtiyacına bağlı

    private List<ManagedDataItem> dataItems;

    @PostConstruct
    public void init() {
        createAndConnectToClientsFromDatabase();
    }

    private void createAndConnectToClientsFromDatabase() {
        clients = new HashMap<>();
        lastCycleCounters = new HashMap<>();
        List<OpcuaDevice> opcuaDevices = opcuaService.list();

        if (opcuaDevices != null && !opcuaDevices.isEmpty()) {
            createAndConnectToClients(opcuaDevices);
        } else {
            System.err.println("No OPC-UA devices found in the database.");
        }
    }

    private void createAndConnectToClients(@NotNull List<OpcuaDevice> opcuaDevices) {
        for (OpcuaDevice opcuaDevice : opcuaDevices) {
            try {
                List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://" + opcuaDevice.getIpAddress() + ":" + opcuaDevice.getTcpPort()).get();
                EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), opcuaDevice.getIpAddress(), opcuaDevice.getTcpPort());

                OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
                cfg.setEndpoint(configPoint)
                        .setApplicationName(LocalizedText.english("newstl opc-ua client"))
                        .setApplicationUri("newstl:spa:com")
                        .setRequestTimeout(Unsigned.uint(5000));

                OpcUaClient client = OpcUaClient.create(cfg.build());
                client.connect().get(5, TimeUnit.SECONDS);
                listen(opcuaDevice, client);
                clients.put(client, opcuaDevice);
            } catch (InterruptedException | ExecutionException | NumberFormatException | UaException |
                     TimeoutException ex) {
                if (!getCause(ex).getClass().equals(ConnectTimeoutException.class)) {
                    // Bu hata tipi, makinenin kapalı olmasından kaynaklanıyorsa log kaydı yazma
                    System.err.println("OpcuaController:init - Error: " + ex.getLocalizedMessage());
                }
            }
        }
    }

    private Throwable getCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }

        return result;
    }


    private void listen(@NotNull OpcuaDevice opcuaDevice, @NotNull OpcuaClient client) throws UaException {
        Optional<OpcuaNode> optionalOpcuaNode = opcuaDevice.getOpcuaNodes().stream().filter(n -> n.getCategoryVariableProduction() == CategoryVariableProduction.PieceCounter).findAny();
        if (optionalOpcuaNode.isPresent()) {
            OpcuaNode opcuaNode = optionalOpcuaNode.get();
            try {
                lastCycleCounters.put(
                        opcuaDevice.getMachine(), Integer.valueOf(String.valueOf(client.connect().getAddressSpace().getVariableNode(createNodeId(opcuaNode)).readValue().getValue().getValue())));

                dataItems = new ArrayList<>();
                client.getSubscriptionManager().addSubscriptionListener(new UaSubscriptionManager.SubscriptionListener() {
                    @Override public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode statusCode) {System.err.println("OpcuaController:onSubscriptionTransferFailed - Errore: " + statusCode);
                        try {
                            createSubscription(client, opcuaNode);
                        } catch (UaException e) {
                            System.err.println("OpcuaController:onSubscriptionTransferFailed - Errore: " + e);
                        }
                    }
                }
                ); createSubscription(client, opcuaNode);
            } catch (NumberFormatException e) {
                System.err.println("OpcuaController:listen - Errore: " + e.getLocalizedMessage());
            }
        }
    }

    /*TO BE UPDATED*/
//    private void createSubscription(@NotNull OpcUaClient client, @NotNull OpcuaNode opcuaNode) throws UaException {
//        ManagedSubscription subscription = ManagedSubscription.create(client);
//        subscription.addDataChangeListener((List<ManagedDataItem> items, List<DataValue> values) -> {
//            for (int i = 0; i < items.size(); i++) {
//                ManagedDataItem item = items.get(i);
//                DataValue value = values.get(i);
//                OpcuaDevice opcuaDeviceTemp = clients.get(item.getClient());
//                Machine machine = opcuaDeviceTemp.getMachine();
//
//                String CycleTime = String.valueOf(value.getValue().getValue());
//                Integer CycleTimeMachine = Integer.valueOf(CycleTime);
//                ProdCounterAct prodCounterAct = prodCounterAct.getLastProdCounterAct(machine.getId());
//                ShotCounterAct shotCounterAct = new shotCounterAct;
//                if (prodCounterAct != null)
//                    eventoProduzione.setNumeroOrdineDiProduzione(ordineDiProduzione.getNumeroOrdineDiProduzione());
//                eventoProduzione.setMacchina(opcuaDeviceTemp.getMacchina());
//                eventoProduzione.setTimestampProduzione(LocalDateTime.now(Clock.systemUTC()));
//                Integer previousCycleCounter = lastCycleCounters.getOrDefault(macchina, 0);
//                Integer quantitaProdotta;
//                if (currentCycleCounter.compareTo(previousCycleCounter) < 0)
//                    quantitaProdotta = currentCycleCounter;
//                else
//                    quantitaProdotta = currentCycleCounter - previousCycleCounter;
//                eventoProduzione.setQuantita(
//                        quantitaProdotta
//                );
//                lastCycleCounters.put(
//                        machine,
//                        currentCycleCounter
//                );
//
//                List<WriteValue> writeValues = new ArrayList<>();
//                for (OpcuaNode opcuaNodeTemp : opcuaDeviceTemp.getOpcuaNodes()) {
//                    Variant variant = null;





    @Schedule(minute = "*/5", hour = "*", persistent = false)
    protected void checkOpcuaDevicesLiveness() {


        List<OpcuaDevice> opcuaDevices = opcuaService.list();
        if (opcuaDevices != null) {
            opcuaDevices.removeIf(device -> clients.values().stream().filter(d -> d.getId().equals(device.getId())).findAny().isPresent());
            createAndConnectToClients(opcuaDevices);
        }
    }


    @PreDestroy
    public void destroy() {
        if (dataItems != null)
            for (ManagedDataItem dataItem : dataItems) {
                try {
                    dataItem.delete();
                } catch (UaException ex) {
                    System.err.println("OpcuaController:destroy - Errore: " + ex.getLocalizedMessage());
                }
            }
        if (clients != null)
            for (OpcUaClient client : clients.keySet()) {
                if (client != null)
                    try {
                        client.disconnect().get(5, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                        System.err.println("OpcuaController:destroy - Errore: " + ex.getLocalizedMessage());
                    }
            }
    }

    private NodeId createNodeId(@NotNull OpcuaNode opcuaNode) {

        Integer nodeIdentifierInt;

        try {
            nodeIdentifierInt = Integer.valueOf(opcuaNode.getNodeIdentifier());
        } catch (NumberFormatException e) {

            nodeIdentifierInt = null;
        }

        if (nodeIdentifierInt != null)
            return new NodeId(opcuaNode.getNameSpaceIndex(), nodeIdentifierInt);
        else
            return new NodeId(opcuaNode.getNameSpaceIndex(), opcuaNode.getNodeIdentifier());
    }


}
