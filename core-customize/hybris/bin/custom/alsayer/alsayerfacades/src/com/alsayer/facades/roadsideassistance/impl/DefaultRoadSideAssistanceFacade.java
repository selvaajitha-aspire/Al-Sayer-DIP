package com.alsayer.facades.roadsideassistance.impl;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.roadsideassistance.services.RoadSideAssistanceService;
import com.alsayer.facades.data.DriverDetailsData;
import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.roadsideassistance.RoadSideAssistanceFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;


public class DefaultRoadSideAssistanceFacade implements RoadSideAssistanceFacade {


    private RoadSideAssistanceService roadSideAssistanceService;

    private Converter<RsaRequestData, RsaRequestModel> rsaRequestReverseConverter;

    private Converter<DriverDetailsModel, DriverDetailsData> driverDetailsConverter;

    @Override
    public DriverDetailsData getDriverDetails(){
        return getDriverDetailsConverter().convert(getRoadSideAssistanceService().getDriverDetails());
    }

    @Override
    public void  storeServiceRequest(RsaRequestData data){
        final RsaRequestModel serviceRequestModel = getRsaRequestReverseConverter().convert(data);
       getRoadSideAssistanceService().saveServiceRequest(serviceRequestModel);
    }

    public Converter<RsaRequestData, RsaRequestModel> getRsaRequestReverseConverter() {
        return rsaRequestReverseConverter;
    }

    public void setRsaRequestReverseConverter(Converter<RsaRequestData, RsaRequestModel> rsaRequestReverseConverter) {
        this.rsaRequestReverseConverter = rsaRequestReverseConverter;
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
