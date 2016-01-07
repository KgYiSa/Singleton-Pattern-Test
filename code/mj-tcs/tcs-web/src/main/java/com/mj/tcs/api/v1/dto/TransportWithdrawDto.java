package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Created by xiaobai on 2016/1/2.
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class TransportWithdrawDto {
    private String uuid;// transport order uuid
    private boolean force = false;
    private boolean disableVehicle = false;

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isDisableVehicle() {
        return disableVehicle;
    }

    public void setDisableVehicle(boolean disableVehicle) {
        this.disableVehicle = disableVehicle;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

}
