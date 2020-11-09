package com.alsayer.core.roadsideassistance.services;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface RoadSideAssistanceService {
    public void saveServiceRequest(RsaRequestModel serviceRequestModel);
    public DriverDetailsModel getDriverDetails();
}

