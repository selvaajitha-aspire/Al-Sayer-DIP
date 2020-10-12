package com.alsayer.core.vehicles.daos;

import com.alsayer.core.model.VehicleModel;

public interface MyVehiclesDao {

    public VehicleModel getVehicle(final String vehicleUid);
}

