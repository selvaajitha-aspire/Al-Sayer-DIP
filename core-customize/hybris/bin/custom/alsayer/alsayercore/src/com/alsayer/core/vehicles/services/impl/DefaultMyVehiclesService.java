package com.alsayer.core.vehicles.services.impl;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.vehicles.daos.MyVehiclesDao;
import com.alsayer.core.vehicles.daos.impl.DefaultMyVehiclesDao;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class DefaultMyVehiclesService implements MyVehiclesService {

    private UserService userService;

    private MyVehiclesDao myVehiclesDao;

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
    public VehicleModel getVehicleByChassisNo(String chassisNumber){
        return getMyVehiclesDao().getVehicle(chassisNumber);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MyVehiclesDao getMyVehiclesDao() {
        return myVehiclesDao;
    }

    public void setMyVehiclesDao(MyVehiclesDao myVehiclesDao) {
        this.myVehiclesDao = myVehiclesDao;
    }
}
