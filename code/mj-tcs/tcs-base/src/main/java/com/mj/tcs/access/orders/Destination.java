package com.mj.tcs.access.orders;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A destination of a transport.
 *
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@XmlType(propOrder={"locationUUID", "operation"})
public class Destination {

    /**
     * The location name.
     */
    private String locationUUID = "";
    /**
     * The operation.
     */
    private String operation = "";

    /**
     * Creates a new instance.
     */
    public Destination() {
        // Do nada.
    }

    /**
     * Returns the location UUID.
     *
     * @return The location UUID
     */
    @XmlAttribute(name = "locationUUID", required = true)
    public String getLocationUUID() {
        return locationUUID;
    }

    /**
     * Sets the location UUID.
     *
     * @param locationUUID The new UUID
     */
    public void setLocationUUID(String locationUUID) {
        this.locationUUID = locationUUID;
    }

    /**
     * Returns the operation.
     *
     * @return The operation
     */
    @XmlAttribute(name = "operation", required = true)
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation.
     *
     * @param operation The new operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
}
