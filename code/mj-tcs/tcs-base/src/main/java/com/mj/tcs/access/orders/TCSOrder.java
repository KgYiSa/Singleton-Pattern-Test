package com.mj.tcs.access.orders;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An order to be processed by the kernel.
 *
 */
@XmlType(propOrder={"uuid"})
public class TCSOrder {

    /**
     * The UUID of this order.
     */
    private String uuid = "";

    /**
     * Creates a new instance.
     */
    public TCSOrder() {
    }

    /**
     * Returns the uuid of this order.
     *
     * @return The uuid.
     */
    @XmlAttribute(name="uuid", required=true)
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid of this order.
     *
     * @param uuid The new uuid.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}