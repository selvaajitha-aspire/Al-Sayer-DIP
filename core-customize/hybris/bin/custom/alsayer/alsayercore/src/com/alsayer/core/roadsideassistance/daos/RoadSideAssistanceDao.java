package com.alsayer.core.roadsideassistance.daos;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;

import java.util.List;

public interface RoadSideAssistanceDao {
    public boolean saveServiceRequestinDB(RsaRequestModel serviceRequest);
    public DriverDetailsModel getDriverDeatailsFromServiceRequest();
}

