package com.mj.tcs.access.orders;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A script to be processed by the kernel.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@XmlType(propOrder={"fileName"})
public class TransportScript
        extends TCSOrder {

    /**
     * The file name.
     */
    private String fileName = "";

    /**
     * Creates a new instance.
     */
    public TransportScript() {
        // Do nada.
    }

    /**
     * Returns the file name.
     *
     * @return The name.
     */
    @XmlAttribute(name="fileName", required=true)
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName The name.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
