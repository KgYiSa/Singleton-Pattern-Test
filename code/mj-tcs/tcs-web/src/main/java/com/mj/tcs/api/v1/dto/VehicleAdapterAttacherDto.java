package com.mj.tcs.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaobai on 2016/1/2.
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
public class VehicleAdapterAttacherDto {
    private String vehicleUUID;
    private String adapterName;
    private String initialPositionUUID;// valid only for SimCommunicationAdapter
    private boolean enable;

    // TODO:[TBD] Append specific adapter here???


    public String getVehicleUUID() {
        return vehicleUUID;
    }

    public void setVehicleUUID(String vehicleUUID) {
        this.vehicleUUID = vehicleUUID;
    }

    public String getAdapterName() {
        return adapterName;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public String getInitialPositionUUID() {
        return initialPositionUUID;
    }

    public void setInitialPositionUUID(String initialPositionUUID) {
        this.initialPositionUUID = initialPositionUUID;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

