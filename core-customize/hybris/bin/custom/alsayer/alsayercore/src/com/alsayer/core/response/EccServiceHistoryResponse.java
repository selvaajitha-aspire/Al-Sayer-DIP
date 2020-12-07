package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EccServiceHistoryResponse {
    @JsonProperty("E_ser_info")
    List<E_ser_info> e_ser_infos;

    @Override
    public String toString() {
        return "EccServiceHistoryResponse{" +
                "e_ser_infos=" + e_ser_infos +
                '}';
    }

    public List<E_ser_info> getE_ser_infos() {
        return e_ser_infos;
    }

    public void setE_ser_infos(List<E_ser_info> e_ser_infos) {
        this.e_ser_infos = e_ser_infos;
    }


}
