package com.alsayer.facades.populators;

import com.alsayer.core.constants.GeneratedAlsayerCoreConstants;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.facades.data.ServiceRequestData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.UUID;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.facades.data.DriverDetailsData;

import javax.annotation.Resource;

public class ServiceRequestPopulator implements Populator<ServiceRequestModel,ServiceRequestData> {

    private I18NService i18nService;

    private Converter<VehicleModel,VehicleData> vehicleDataConverter;

    private Converter<DriverDetailsModel,DriverDetailsData> driverDetailsConverter;

    public Converter<VehicleModel, VehicleData> getVehicleDataConverter() {
        return vehicleDataConverter;
    }

    public void setVehicleDataConverter(Converter<VehicleModel, VehicleData> vehicleDataConverter) {
        this.vehicleDataConverter = vehicleDataConverter;
    }

    public Converter<DriverDetailsModel, DriverDetailsData> getDriverDetailsConverter() {
        return driverDetailsConverter;
    }

    public void setDriverDetailsConverter(Converter<DriverDetailsModel, DriverDetailsData> driverDetailsConverter) {
        this.driverDetailsConverter = driverDetailsConverter;
    }

    @Override
    public void populate(ServiceRequestModel serviceRequestModel, ServiceRequestData serviceRequestData) throws ConversionException {
        if(null != serviceRequestModel && null != serviceRequestData
                && serviceRequestModel instanceof ServiceRequestModel && serviceRequestData instanceof ServiceRequestData){
            if(null != serviceRequestModel.getLatitude()){
                serviceRequestData.setLatitude(serviceRequestModel.getLatitude());
            }
            if(null != serviceRequestModel.getLongitude()){
                serviceRequestData.setLongitude(serviceRequestModel.getLongitude());
            }
            if(null != serviceRequestModel.getNotes()){
                serviceRequestData.setNotes(serviceRequestModel.getNotes());
            }
            if(null != serviceRequestModel.getAttachments()){
                serviceRequestData.setAttachments(serviceRequestModel.getAttachments());
            }
            if(null != serviceRequestModel.getServiceDate()){
                serviceRequestData.setServiceDate(serviceRequestModel.getServiceDate());
            }
            if(null != serviceRequestModel.getMileage()){
                serviceRequestData.setMileage(serviceRequestModel.getMileage());
            }
            if(null != serviceRequestModel.getVehicle()
                    && serviceRequestModel.getVehicle() instanceof VehicleModel){
                VehicleData vehicle = getVehicleDataConverter().convert(serviceRequestModel.getVehicle());
                serviceRequestData.setVehicle(vehicle);
            }
            if(null != serviceRequestModel.getDriverDetails()
                    && serviceRequestModel.getDriverDetails() instanceof DriverDetailsModel){
                DriverDetailsData driverDetails = getDriverDetailsConverter().convert(serviceRequestModel.getDriverDetails());
                serviceRequestData.setDriverDetails(driverDetails);
            }
            if(null != serviceRequestModel.getStatus()){
                serviceRequestData.setStatus(serviceRequestModel.getStatus().getCode());
            }
            if(null != serviceRequestModel.getType()){
                serviceRequestData.setType(serviceRequestModel.getType());
            }

        }
    }

    public I18NService getI18nService() {
        return i18nService;
    }

    public void setI18nService(I18NService i18nService) {
        this.i18nService = i18nService;
    }
}
