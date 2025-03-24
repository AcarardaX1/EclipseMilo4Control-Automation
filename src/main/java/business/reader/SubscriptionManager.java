package business.reader;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SubscriptionManager {

    public void createSubscription(OpcUaClient client) throws ExecutionException, InterruptedException, UaException {

        ManagedSubscription subscription = ManagedSubscription.create(client);

        ManagedDataItem dataItem = subscription.createDataItem(new NodeId(4, "APPL.HeatingNozzle1.ti_InTemp1"));

        subscription.addChangeListener(new ManagedSubscription.ChangeListener() {

            @Override
            public void onDataReceived(List<ManagedDataItem> dataItems, List<DataValue> dataValues) {

                for(int i = 0; i < dataValues.size(); i++) {
                    System.out.println("Value: " + dataValues.get(i).getValue());
                }
            }
        });
        if (!dataItem.getStatusCode().isGood()) {
            throw new RuntimeException("uh oh!");
        }

    }
}
