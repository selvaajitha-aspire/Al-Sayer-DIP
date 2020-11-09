package com.alsayer.facades.roadsideassistance;

import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.data.VehicleData;

import java.util.List;

public interface RoadSideAssistanceFacade {
    public void  storeServiceRequest(RsaRequestData data);
    public DriverDetailsData getDriverDetails();
}

