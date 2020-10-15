package com.alsayer.facades.roadsideassistance;

import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.ServiceRequestData;
import com.alsayer.facades.data.VehicleData;

import java.util.List;

public interface RoadSideAssistanceFacade {
    public void  storeServiceRequest(ServiceRequestData data);
    public DriverDetailsData getDriverDetails();
}

