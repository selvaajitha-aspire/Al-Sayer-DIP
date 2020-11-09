package com.alsayer.occ.Reaponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EccVehicleDetailsResponse {

    @JsonProperty("E_Vehicle_Info")
    E_Vehicle_Info e_vehicle_info;

    public E_Vehicle_Info getE_vehicle_info() {
        return e_vehicle_info;
    }

    public void setE_vehicle_info(E_Vehicle_Info e_vehicle_info) {
        this.e_vehicle_info = e_vehicle_info;
    }
}
