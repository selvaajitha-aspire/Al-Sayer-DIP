package com.alsayer.core.vehicles.services.impl;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.vehicles.daos.impl.DefaultMyVehiclesDao;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import java.util.Collections;
import java.util.List;

public class DefaultMyVehiclesService implements MyVehiclesService {

    private UserService userService;

    private DefaultMyVehiclesDao myVehiclesDao;

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
    public VehicleModel getVehicleByUID(String vehicle_uid){
        return getMyVehiclesDao().getVehicle(vehicle_uid);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public DefaultMyVehiclesDao getMyVehiclesDao() {
        return myVehiclesDao;
    }

    public void setMyVehiclesDao(DefaultMyVehiclesDao myVehiclesDao) {
        this.myVehiclesDao = myVehiclesDao;
    }
}
