package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EccWarrantyResponse {

    @JsonProperty("E_wty_info")
    List<E_wty_info> eWtyInfo;

    @Override
    public String toString() {
        return "EccWarrantyResponse{" +
                "eWtyInfo=" + eWtyInfo +
                '}';
    }

    public List<E_wty_info> geteWtyInfo() {
        return eWtyInfo;
    }

    public void seteWtyInfo(List<E_wty_info> eWtyInfo) {
        this.eWtyInfo = eWtyInfo;
    }
}
