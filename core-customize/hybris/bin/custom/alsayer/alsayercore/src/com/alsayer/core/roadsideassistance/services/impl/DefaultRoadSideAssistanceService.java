package com.alsayer.core.roadsideassistance.services.impl;

import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.roadsideassistance.daos.RoadSideAssistanceDao;
import com.alsayer.core.roadsideassistance.services.RoadSideAssistanceService;

public class DefaultRoadSideAssistanceService implements RoadSideAssistanceService {

    private RoadSideAssistanceDao roadSideAssistanceDao;

    @Override
    public void saveServiceRequest(RsaRequestModel serviceRequestModel){
         getRoadSideAssistanceDao().saveServiceRequestinDB(serviceRequestModel);
    }

    public RoadSideAssistanceDao getRoadSideAssistanceDao() {
        return roadSideAssistanceDao;
    }

    public void setRoadSideAssistanceDao(RoadSideAssistanceDao roadSideAssistanceDao) {
        this.roadSideAssistanceDao = roadSideAssistanceDao;
    }
}
