package com.alsayer.facades.roadsideassistance.impl;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.roadsideassistance.services.impl.DefaultRoadSideAssistanceService;
import com.alsayer.facades.data.ServiceRequestData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;


public class DefaultRoadSideAssistanceFacade implements RoadSideAssistanceFacade {


    private DefaultRoadSideAssistanceService roadSideAssistanceService;

    private Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter;

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

    public DefaultRoadSideAssistanceService getRoadSideAssistanceService() {
        return roadSideAssistanceService;
    }

    public void setRoadSideAssistanceService(DefaultRoadSideAssistanceService roadSideAssistanceService) {
        this.roadSideAssistanceService = roadSideAssistanceService;
    }
}
