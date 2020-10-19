package com.alsayer.facades.roadsideassistance.impl;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.roadsideassistance.services.RoadSideAssistanceService;
import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.ServiceRequestData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;


public class DefaultRoadSideAssistanceFacade implements RoadSideAssistanceFacade {


    private RoadSideAssistanceService roadSideAssistanceService;

    private Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter;

    private Converter<DriverDetailsModel, DriverDetailsData> driverDetailsConverter;

    @Override
    public DriverDetailsData getDriverDetails(){
        return getDriverDetailsConverter().convert(getRoadSideAssistanceService().getDriverDetails());
    }

    @Override
    public void  storeServiceRequest(ServiceRequestData data){
        ServiceRequestModel serviceRequestModel=getServiceRequestReverseConverter().convert(data);
       getRoadSideAssistanceService().saveServiceRequest(serviceRequestModel);
    }

    public Converter<ServiceRequestData, ServiceRequestModel> getServiceRequestReverseConverter() {
        return serviceRequestReverseConverter;
    }

    public void setServiceRequestReverseConverter(Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter) {
        this.serviceRequestReverseConverter = serviceRequestReverseConverter;
    }

    public RoadSideAssistanceService getRoadSideAssistanceService() {
        return roadSideAssistanceService;
    }

    public void setRoadSideAssistanceService(RoadSideAssistanceService roadSideAssistanceService) {
        this.roadSideAssistanceService = roadSideAssistanceService;
    }

    public Converter<DriverDetailsModel, DriverDetailsData> getDriverDetailsConverter() {
        return driverDetailsConverter;
    }

    public void setDriverDetailsConverter(Converter<DriverDetailsModel, DriverDetailsData> driverDetailsConverter) {
        this.driverDetailsConverter = driverDetailsConverter;
    }
}
