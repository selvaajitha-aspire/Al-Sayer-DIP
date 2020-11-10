package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EccVehicleDetailsResponse {

    @Override
    public String toString() {
        return "EccVehicleDetailsResponse{" +
                "e_vehicle_infos=" + e_vehicle_infos +
                '}';
    }

    @JsonProperty("E_Vehicle_Info")
    List<E_Vehicle_Info> e_vehicle_infos;

    public List<E_Vehicle_Info> getE_vehicle_infos() {
        return e_vehicle_infos;
    }

    public void setE_vehicle_infos(List<E_Vehicle_Info> e_vehicle_infos) {
        this.e_vehicle_infos = e_vehicle_infos;
    }
}
