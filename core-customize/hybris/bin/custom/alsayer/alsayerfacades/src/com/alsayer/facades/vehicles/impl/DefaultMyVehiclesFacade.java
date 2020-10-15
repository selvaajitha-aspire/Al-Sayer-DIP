package com.alsayer.facades.vehicles.impl;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.vehicles.MyVehiclesFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;


public class DefaultMyVehiclesFacade implements MyVehiclesFacade {


    private MyVehiclesService myVehiclesService;

    private Converter<VehicleModel, VehicleData> vehicleDataConverter;

    @Override
    public List<VehicleData> getVehicles(){
        List<VehicleModel> vehicles  = getMyVehiclesService().getVehiclesForCustomer();
        return Converters.convertAll(vehicles, getVehicleDataConverter());

    }

    public MyVehiclesService getMyVehiclesService() {
        return myVehiclesService;
    }

    public void setMyVehiclesService(MyVehiclesService myVehiclesService) {
        this.myVehiclesService = myVehiclesService;
    }

    public Converter<VehicleModel, VehicleData> getVehicleDataConverter() {
        return vehicleDataConverter;
    }

    public void setVehicleDataConverter(Converter<VehicleModel, VehicleData> vehicleDataConverter) {
        this.vehicleDataConverter = vehicleDataConverter;
    }
}
