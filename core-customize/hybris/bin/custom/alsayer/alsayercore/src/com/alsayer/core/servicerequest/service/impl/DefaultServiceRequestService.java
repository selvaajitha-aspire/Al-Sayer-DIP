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

    @Override
    public List<ServiceRequestData> getServiceRequestsByStatus(String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByStatus(status));
    }

    @Override
    public ServiceRequestData getServiceRequestByUID(String uid) {
        return convert(getServiceRequestDao().getServiceRequestByUID(uid));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByCustomerId(String customerID) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByCustomerId(customerID));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByCustomerIdAndStatus(String customerID, String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByCustomerIdAndStatus(customerID,status));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByVehicleId(String vehicleID) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByVehicleId(vehicleID));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByVehicleIdAndStatus(String vehicleID, String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByVehicleIdAndStatus(vehicleID,status));
    }
}
