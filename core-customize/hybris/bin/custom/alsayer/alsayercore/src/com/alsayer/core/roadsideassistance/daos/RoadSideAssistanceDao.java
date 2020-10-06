package com.alsayer.core.roadsideassistance.daos;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface RoadSideAssistanceDao {

    public boolean saveServiceRequestinDB(ServiceRequestModel serviceRequest);
    public VehicleModel getVehicle(String vehicle_uid);
}

