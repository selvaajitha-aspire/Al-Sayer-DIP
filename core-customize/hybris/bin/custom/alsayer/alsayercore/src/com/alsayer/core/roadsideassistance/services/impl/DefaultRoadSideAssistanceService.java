package com.alsayer.core.roadsideassistance.services.impl;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.roadsideassistance.daos.RoadSideAssistanceDao;
import com.alsayer.core.roadsideassistance.services.RoadSideAssistanceService;

public class DefaultRoadSideAssistanceService implements RoadSideAssistanceService {

    private RoadSideAssistanceDao roadSideAssistanceDao;

    @Override
    public void saveServiceRequest(ServiceRequestModel serviceRequestModel){
         getRoadSideAssistanceDao().saveServiceRequestinDB(serviceRequestModel);
    }

    @Override
    public DriverDetailsModel getDriverDetails(){
       return getRoadSideAssistanceDao().getDriverDeatailsFromServiceRequest();
    }

    public RoadSideAssistanceDao getRoadSideAssistanceDao() {
        return roadSideAssistanceDao;
    }

    public void setRoadSideAssistanceDao(RoadSideAssistanceDao roadSideAssistanceDao) {
        this.roadSideAssistanceDao = roadSideAssistanceDao;
    }
}
