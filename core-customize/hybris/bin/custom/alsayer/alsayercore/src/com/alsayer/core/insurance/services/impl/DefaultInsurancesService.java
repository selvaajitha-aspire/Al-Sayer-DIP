package com.alsayer.core.insurance.services.impl;

import com.alsayer.core.insurance.daos.InsurancesDao;
import com.alsayer.core.insurance.services.InsurancesService;
import com.alsayer.core.model.InsuranceModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

public class DefaultInsurancesService implements InsurancesService {

    private InsurancesDao insurancesDao;

    private UserService userService;

    @Override
    public List<InsuranceModel> getInsurancesByChassisNo(String chassisNumber){
       final CustomerModel customer= (CustomerModel) getUserService().getCurrentUser();
        return getInsurancesDao().getInsurances(chassisNumber,customer);
    }

    public InsurancesDao getInsurancesDao() {
        return insurancesDao;
    }

    public void setInsurancesDao(InsurancesDao insurancesDao) {
        this.insurancesDao = insurancesDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
