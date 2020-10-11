package com.alsayer.core.vehicles.services;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface MyVehiclesService {
    public List<VehicleModel> getVehiclesForCustomer();
    public VehicleModel getVehicleByUID(String vehicle_uid);
}

