package com.alsayer.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EccWarrantyResponse {

    @JsonProperty("E_wty_info")
    E_wty_info eWtyInfo;

    @Override
    public String toString() {
        return "EccWarrantyResponse{" +
                "eWtyInfo=" + eWtyInfo +
                '}';
    }

    public E_wty_info geteWtyInfo() {
        return eWtyInfo;
    }

    public void seteWtyInfo(E_wty_info eWtyInfo) {
        this.eWtyInfo = eWtyInfo;
    }
}
