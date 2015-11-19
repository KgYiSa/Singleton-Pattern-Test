package com.mj.tcs.service;

/**
 * @author Wang Zhen
 */
public abstract class ServiceState {
    /**
     * A global object to be used within the kernel.
     */
    protected final Object globalSyncObject;

    /**
     * The buffer for all messages published.
     */
//    protected final MessageBuffer messageBuffer;

    /**
     * Creates a new state.
     *
     */
    public ServiceState() {
        globalSyncObject = new Object();
    }

    /**
     * Initializes this kernel state.
     * (Allocates resources, starts kernel extensions etc.)
     */
    public abstract void initialize();

    /**
     * Terminates this kernel state.
     * (Frees resources, stops kernel extensions etc.)
     */
    public abstract void terminate();


    public abstract State getState(long sceneId);

    /**
     * The various states an scene may be running in.
     */
    public enum State {

        /**
         * The state in which the model/topology is created and parameters are set.
         */
        MODELLING,
        /**
         * The normal mode of operation in which transport orders may be accepted
         * and dispatched to vehicles.
         */
        OPERATING,
        /**
         * A transitional state the kernel is in while shutting down.
         */
        // SHUTDOWN
    }
}
