package com.mj.tcs.data;

import com.mj.tcs.KernelRuntimeException;

/**
 * @author Wang Zhen
 */
public class ObjectAccessViolationException extends KernelRuntimeException {

    public ObjectAccessViolationException(Long sceneId, Long elementId) {
        super(
                "The element is not belonging to the scene." +
                "sceneId: " + sceneId + "elementId: " + elementId
        );
    }
}
