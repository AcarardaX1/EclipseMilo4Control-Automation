package com.business.communication.controller;

public enum CategoryVariableProduction {

    PieceCounter(28),
    ProdCounterAct(28),
    ShotCounterAct(28),
    MoldData(28),
    CycleTime(28),
    CycleTimeMachine(28),           /*Idk what to write for an instance CycleTimeMachine(9) as an order?*/
    LastCycleTime(28),
    MaxCycleTime(35),
    RefCycleTime(28),
    ShotTimeAct(28),
    SetAlarmSignal(28),
    SetAlarmSignalCount(28),
    NumberofZones(28),
    ZoneRetain1_rSetValvis(28),
    ZoneRetain_InTemp1_value(28),
    ZoneRetain2_rSetValvis(28),
    ZoneRetain_InTemp2_value(28),
    ZoneRetain3_rSetValvis(28),
    ZoneRetain_InTemp3_value(28),
    ZoneRetain4_rSetValvis(28),
    ZoneRetain_InTemp4_value(28),
    ZoneRetain5_rSetValvis(28),
    ZoneRetain_InTemp5_value(28),
    ZoneRetain6_rSetValvis(28),
    ZoneRetain_InTemp6_value(28),
    ZoneRetain7_rSetValvis(28),
    ZoneRetain_InTemp7_value(28);

    private final int value;

    CategoryVariableProduction(int value) {
        this.value = value;
    }
}
