package com.alsayer.facades.populators;

import com.alsayer.core.model.ServiceHistoryModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.facades.data.ServiceHistoryData;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class ServiceHistoryPopulator implements Populator<ServiceHistoryModel, ServiceHistoryData> {

    private Converter<VehicleModel, VehicleData> vehicleDataConverter;

    @Override
    public void populate(ServiceHistoryModel serviceHistoryModel, ServiceHistoryData serviceHistoryData) throws ConversionException {
        serviceHistoryData.setInvAmt(serviceHistoryModel.getInvAmt());
        serviceHistoryData.setLocation(serviceHistoryModel.getLocation());
        serviceHistoryData.setServiceDate(serviceHistoryModel.getServiceDate());
        serviceHistoryData.setLocationCode(serviceHistoryModel.getLocationCode());
        serviceHistoryData.setMileage(serviceHistoryModel.getMileage());
        serviceHistoryData.setServiceDesc(serviceHistoryModel.getServiceDesc());
        serviceHistoryData.setServiceType(serviceHistoryModel.getServiceType());

        if(null != serviceHistoryModel.getVehicle()
                && serviceHistoryModel.getVehicle() instanceof VehicleModel){
            VehicleData vehicle = getVehicleDataConverter().convert(serviceHistoryModel.getVehicle());
            serviceHistoryData.setVehicle(vehicle);
        }
    }

    public Converter<VehicleModel, VehicleData> getVehicleDataConverter() {
        return vehicleDataConverter;
    }

    public void setVehicleDataConverter(Converter<VehicleModel, VehicleData> vehicleDataConverter) {
        this.vehicleDataConverter = vehicleDataConverter;
    }
}
