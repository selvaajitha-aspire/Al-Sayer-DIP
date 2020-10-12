package com.alsayer.core.servicerequest.service.impl;

import com.alsayer.core.AbstractService;
import com.alsayer.core.servicerequest.dao.ServiceRequestDao;
import com.alsayer.core.servicerequest.service.ServiceRequestService;
import com.alsayer.facades.data.ServiceRequestData;

import java.util.List;

public class DefaultServiceRequestService extends AbstractService implements ServiceRequestService {

    private ServiceRequestDao serviceRequestDao;

    public ServiceRequestDao getServiceRequestDao() {
        return serviceRequestDao;
    }

    public void setServiceRequestDao(ServiceRequestDao serviceRequestDao) {
        this.serviceRequestDao = serviceRequestDao;
    }

    @Override
    public List<ServiceRequestData> getAllServiceRequests() {
        return convertAllModel(getServiceRequestDao().getAllServiceRequests());
    }
}
