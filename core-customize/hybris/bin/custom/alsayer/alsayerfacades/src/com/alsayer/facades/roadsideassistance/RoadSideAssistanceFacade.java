package com.alsayer.facades.roadsideassistance;

import com.alsayer.facades.data.ServiceRequestData;
import com.alsayer.facades.data.VehicleData;

import java.util.List;

public interface RoadSideAssistanceFacade {
    public List<VehicleData> getVehicles();
    public void  storeServiceRequest(ServiceRequestData data);
}

