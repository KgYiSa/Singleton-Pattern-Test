package com.mj.tcs.access.status;

/**
 * A generic status message.
 *
 */
public class StatusMessage {
    private final long sceneId;

    /**
     * Creates a new StatusMessage.
     */
    public StatusMessage(long sceneId) {
        this.sceneId = sceneId;
    }

    public long getSceneId() {
        return sceneId;
    }
}