package com.business.communication.entity;

import com.business.entity.Machine;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpcuaDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private @NotNull Machine machine;

    private String ipAddress;
    private Integer tcpPort;

    private String deviceName;

    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "opcuaDevice", orphanRemoval = true)
    private List<OpcuaNode> opcuaNodes = new ArrayList<>();

    // Constructor
    public OpcuaDevice(@NotNull Machine machine, String ipAddress, Integer tcpPort, String deviceName) {
        this.machine = machine;
        this.ipAddress = ipAddress;
        this.tcpPort = tcpPort;
        this.deviceName = deviceName;
    }

    public List<OpcuaNode> getOpcuaNodes() {
        return Collections.unmodifiableList(opcuaNodes); // Dışarıdan değiştirilemez liste döndürülmesi
    }

    public void addOpcuaNode(OpcuaNode opcuaNode) {
        opcuaNodes.add(opcuaNode);
        opcuaNode.setOpcuaDevice(this);
    }

    public void removeOpcuaNode(OpcuaNode opcuaNode) {
        opcuaNodes.remove(opcuaNode);
        opcuaNode.setOpcuaDevice(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpcuaDevice that = (OpcuaDevice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
