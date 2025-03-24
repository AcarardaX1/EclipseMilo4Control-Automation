package business.reader;

//import jdk.internal.icu.impl.StringPrepDataReader;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NodeReader {

    List<String> nodeIdentifiers;


    public NodeReader() {
        nodeIdentifiers = new ArrayList<>();
        nodeIdentifiers.add("APPL.system.sv_iProdCounterAct");
        nodeIdentifiers.add("APPL.system.sv_iShotCounterAct");
        nodeIdentifiers.add("APPL.system.sv_sMoldData");
        nodeIdentifiers.add("APPL.system.sv_dCycleTime");
        nodeIdentifiers.add("APPL.system.sv_dCycleTimeMachine");
        nodeIdentifiers.add("APPL.system.sv_dLastCycleTime");
        nodeIdentifiers.add("APPL.system.sv_dMaxCycleTime");
        nodeIdentifiers.add("APPL.system.sv_dRefCycleTime");
        nodeIdentifiers.add("APPL.system.sv_dShotTimeAct");
        nodeIdentifiers.add("APPL.OperationMode1.sv_bSetAlarmSignal");
        nodeIdentifiers.add("APPL.OperationMode1.sv_iSetAlarmSignalCount");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_iNumberOfZones");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp1");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain1.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp2");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain2.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp3");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain3.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp4");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain4.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp5");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain5.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp6");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain6.rSetValVis");
        nodeIdentifiers.add("APPL.HeatingNozzle1.ti_InTemp7");
        nodeIdentifiers.add("APPL.HeatingNozzle1.sv_ZoneRetain7.rSetValVis");
    }

    public void readNode(OpcUaClient client) throws ExecutionException, InterruptedException {
        List<ReadValueId> readValueIds = new ArrayList<>();

        for (int i = 0; i < nodeIdentifiers.size(); i++) {
            readValueIds.add(
                    new ReadValueId(
                            new NodeId(4, nodeIdentifiers.get(i)),
                            AttributeId.Value.uid(),
                            null, // indexRange
                            QualifiedName.NULL_VALUE
                    )
            );

        }
        ReadResponse readResponse = client.read(
                0.0, // maxAge
                TimestampsToReturn.Both,
                readValueIds
        ).get();
        DataValue[] results = readResponse.getResults();
        for (int i = 0; i < results.length; i++) {
            System.out.println(results[i].getStatusCode());
            System.out.println(results[i].getValue());

        }

    }

}
