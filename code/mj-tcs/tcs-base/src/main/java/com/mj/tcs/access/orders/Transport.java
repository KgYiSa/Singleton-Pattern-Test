package com.mj.tcs.access.orders;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A transport order to be processed by the kernel.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@XmlType(propOrder={"deadline", "intendedVehicle", "destinations", "dependencies"})
public class Transport extends TCSOrder {

    /**
     * The deadling of this transport order.
     */
    private Date deadline;
    /**
     * The intended vehicle.
     */
    private String intendedVehicle;
    /**
     * The destinations.
     */
    private List<Destination> destinations = new LinkedList<>();
    /**
     * The dependencies.
     */
    private List<String> dependencies = new LinkedList<>();

    /**
     * Creates a new instance.
     */
    public Transport() {
        // Do nada.
    }

    /**
     * Returns the deadline.
     *
     * @return The deadline.
     */
    @XmlAttribute(name="deadline", required=false)
    public Date getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline.
     *
     * @param deadline The new deadline.
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * Returns the intended vehicle.
     *
     * @return The intended vehicle.
     */
    @XmlAttribute(name="intendedVehicle", required=false)
    public String getIntendedVehicle() {
        return intendedVehicle;
    }

    /**
     * Sets the intended vehicle.
     *
     * @param intendedVehicle The new intended vehicle.
     */
    public void setIntendedVehicle(String intendedVehicle) {
        this.intendedVehicle = intendedVehicle;
    }

    /**
     * Returns the destinations.
     *
     * @return The list of destinations.
     */
    @XmlElement(name="destination", required=true)
    public List<Destination> getDestinations() {
        return destinations;
    }

    /**
     * Sets the destinations.
     *
     * @param destinations The new list of destinations.
     */
    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    /**
     * Returns the dependencies.
     *
     * @return The list of dependencies.
     */
    @XmlElement(name="dependency", required=false)
    public List<String> getDependencies() {
        return dependencies;
    }

    /**
     * Sets the dependencies.
     *
     * @param dependencies The new list of dependencies.
     */
    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
}