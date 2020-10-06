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


    private UserService userService;

    private DefaultRoadSideAssistanceDao roadSideAssistanceDao;

    @Override
    public List<VehicleModel> getVehiclesForCustomer(){

    final CustomerModel customer = (CustomerModel) userService.getCurrentUser();

        if(CollectionUtils.isNotEmpty(customer.getVehicles()))
        {
            return customer.getVehicles();
        }
        return Collections.emptyList();
    }

    @Override
    public void saveServiceRequest(ServiceRequestModel serviceRequestModel){
         getRoadSideAssistanceDao().saveServiceRequestinDB(serviceRequestModel);
    }

    public VehicleModel getVehicleByUID(String vehicle_uid){
       return roadSideAssistanceDao.getVehicle(vehicle_uid);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public DefaultRoadSideAssistanceDao getRoadSideAssistanceDao() {
        return roadSideAssistanceDao;
    }

    public void setRoadSideAssistanceDao(DefaultRoadSideAssistanceDao roadSideAssistanceDao) {
        this.roadSideAssistanceDao = roadSideAssistanceDao;
    }
}
