package com.alsayer.core.roadsideassistance.services;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface RoadSideAssistanceService {
    public List<VehicleModel> getVehiclesForCustomer();
    public void saveServiceRequest(ServiceRequestModel serviceRequestModel);
    public VehicleModel getVehicleByUID(String vehicle_uid);
}

