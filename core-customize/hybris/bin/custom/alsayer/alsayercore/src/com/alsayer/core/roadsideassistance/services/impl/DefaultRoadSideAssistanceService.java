package com.alsayer.core.roadsideassistance.services.impl;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.roadsideassistance.daos.impl.DefaultRoadSideAssistanceDao;
import com.alsayer.core.roadsideassistance.services.RoadSideAssistanceService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DefaultRoadSideAssistanceService implements RoadSideAssistanceService {

    private DefaultRoadSideAssistanceDao roadSideAssistanceDao;

    @Override
    public void saveServiceRequest(ServiceRequestModel serviceRequestModel){
         getRoadSideAssistanceDao().saveServiceRequestinDB(serviceRequestModel);
    }

    public DefaultRoadSideAssistanceDao getRoadSideAssistanceDao() {
        return roadSideAssistanceDao;
    }

    public void setRoadSideAssistanceDao(DefaultRoadSideAssistanceDao roadSideAssistanceDao) {
        this.roadSideAssistanceDao = roadSideAssistanceDao;
    }
}
