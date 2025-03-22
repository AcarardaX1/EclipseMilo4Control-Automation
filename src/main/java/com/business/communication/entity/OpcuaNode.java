package com.business.communication.entity;


import com.business.communication.controller.CategoryVariableProduction;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
public class OpcuaNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nodeId;

    private String displayName;

    private Object Value;


    @ManyToOne(optional = false)
    private @NotNull OpcuaDevice opcuaDevice;

    @Enumerated
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull CategoryVariableProduction categoryVariableProduction;

    @Column(nullable = false)
    private @NotNull @DecimalMin(value = "0", inclusive = false) Integer nameSpaceIndex;

    @Column(nullable = false)
    private @NotNull String nodeIdentifier;

    public OpcuaNode() {
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

    public OpcuaDevice getOpcuaDevice() {
        return opcuaDevice;
    }

    public void setOpcuaDevice(OpcuaDevice opcuaDevice) {
        this.opcuaDevice = opcuaDevice;
    }

    public CategoryVariableProduction getCategoryVariableProduction() {
        return categoryVariableProduction;
    }

    public void setCategoryVariableProduction(CategoryVariableProduction categoryVariableProduction) {
        this.categoryVariableProduction = categoryVariableProduction;
    }

    public Integer getNameSpaceIndex() {
        return nameSpaceIndex;
    }

    public void setNameSpaceIndex(Integer nameSpaceIndex) {
        this.nameSpaceIndex = nameSpaceIndex;
    }

    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    public void setNodeIdentifier(String nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
    }
}




