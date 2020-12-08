package com.alsayer.core.servicehistory.service.impl;

import com.alsayer.core.jalo.ServiceHistory;
import com.alsayer.core.model.ServiceHistoryModel;
import com.alsayer.core.servicehistory.dao.ServiceHistoryDao;
import com.alsayer.core.servicehistory.service.ServiceHistoryService;
import com.alsayer.facades.data.ServiceHistoryData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import java.util.List;

public class DefaultServiceHistoryService implements ServiceHistoryService {

    @Resource
    private ServiceHistoryDao serviceHistoryDao;

    public Converter<ServiceHistoryModel, ServiceHistoryData> getServiceHistoryConverter() {
        return serviceHistoryConverter;
    }

    public void setServiceHistoryConverter(Converter<ServiceHistoryModel, ServiceHistoryData> serviceHistoryConverter) {
        this.serviceHistoryConverter = serviceHistoryConverter;
    }

    @Resource
    private Converter<ServiceHistoryModel,ServiceHistoryData> serviceHistoryConverter;

    private UserService userService;

    @Override
    public List<ServiceHistoryData> getMyServiceHistoryByChassisNo(String chassisNo) {
        final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
        return getServiceHistoryConverter().convertAll(getServiceHistoryDao().getServiceHistoryByChassisAndCustomer(chassisNo,currentCustomer));
    }

    public ServiceHistoryDao getServiceHistoryDao() {
        return serviceHistoryDao;
    }

    public void setServiceHistoryDao(ServiceHistoryDao serviceHistoryDao) {
        this.serviceHistoryDao = serviceHistoryDao;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }
}
