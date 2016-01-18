package com.mj.tcs.exception;

/**
 * @author Wang Zhen
 */
public class ObjectAccessViolationException extends TCSServerRuntimeException {

    public ObjectAccessViolationException(Long sceneId, Long elementId) {
        super(
                "The element is not belonging to the scene." +
                "sceneId: " + sceneId + "elementId: " + elementId
        );
    }
}
