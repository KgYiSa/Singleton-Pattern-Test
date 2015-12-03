package com.mj.tcs.drivers;

import com.mj.tcs.LocalKernel;
import com.mj.tcs.data.model.Vehicle;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * A registry for all communication adapters in the system.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class CommunicationAdapterRegistry {

    /**
     * This class's Logger.
     */
    private static final Logger log = Logger.getLogger(CommunicationAdapterRegistry.class.getName());
    /**
     * The registered factories.
     */
    private final List<CommunicationAdapterFactory> factories;
    /**
     * A reference to the kernel.
     */
    private final LocalKernel kernel;

    /**
     * Creates a new registry.
     *
     * @param kernel A reference to the local kernel.
     */
    public CommunicationAdapterRegistry(LocalKernel kernel) {
        this.kernel = Objects.requireNonNull(kernel);

        // Auto-detect communication adapter factories.
        factories = new LinkedList<>();
        ServiceLoader<CommunicationAdapterFactory> factoryLoader =
                ServiceLoader.load(CommunicationAdapterFactory.class);
        for (CommunicationAdapterFactory factory : factoryLoader) {
            log.info("Setting up communication adapter factory: "
                    + factory.getClass().getName());
            factory.setKernel(kernel);
            factories.add(factory);
        }

        if (factories.isEmpty()) {
            throw new IllegalStateException(
                    "No communication adapter factories found.");
        }
    }

    /**
     * Returns a reference to the local kernel.
     *
     * @return A reference to the local kernel.
     * @deprecated Will be removed after MJTCS 2.7.
     */
    public LocalKernel getKernel() {
        return kernel;
    }

    /**
     * Returns all registered factories that can provide communication adapters.
     *
     * @return All registered factories that can provide communication adapters.
     */
    public List<CommunicationAdapterFactory> getFactories() {
        return new LinkedList<>(factories);
    }

    /**
     * Returns a set of factories that can provide communication adapters for the
     * given vehicle.
     *
     * @param vehicle The vehicle to find communication adapters/factories for.
     * @return A set of factories that can provide communication adapters for the
     * given vehicle.
     */
    public List<CommunicationAdapterFactory> findFactoriesFor(Vehicle vehicle) {
        if (vehicle == null) {
            throw new NullPointerException("vehicle is null");
        }
        List<CommunicationAdapterFactory> result = new LinkedList<>();
        for (CommunicationAdapterFactory curFactory : factories) {
            if (curFactory.providesAdapterFor(vehicle)) {
                result.add(curFactory);
            }
        }
        return result;
    }
}
