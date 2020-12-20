package com.alsayer.core.response.fsm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FSMTechnicianResponse {

    @Override
    public String toString() {
        return "FSMTechnicianResponse{" +
                "data=" + data +
                '}';
    }

    @JsonProperty("data")
    List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
