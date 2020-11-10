package com.alsayer.core.vehicles.services;

import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface MyVehiclesService {
    public List<VehicleModel> getVehiclesForCustomer();
    public VehicleModel getVehicleByChassisNo(String chassisNumber);

}

