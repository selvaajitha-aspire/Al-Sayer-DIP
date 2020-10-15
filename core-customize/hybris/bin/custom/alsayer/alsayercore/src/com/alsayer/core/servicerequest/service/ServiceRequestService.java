package com.alsayer.core.servicerequest.service;

import java.util.List;
import com.alsayer.facades.data.ServiceRequestData;

import com.alsayer.core.model.ServiceRequestModel;

public interface ServiceRequestService {

    public List<ServiceRequestData> getAllServiceRequests();

    public List<ServiceRequestData> getServiceRequestsByStatus(String status);

    public ServiceRequestData getServiceRequestByUID(String uid);

    public List<ServiceRequestData> getServiceRequestsByCustomerId(String customerID);

    public List<ServiceRequestData> getServiceRequestsByCustomerIdAndStatus(String customerID,String status);

    public List<ServiceRequestData> getServiceRequestsByVehicleId(String vehicleID);

    public List<ServiceRequestData> getServiceRequestsByVehicleIdAndStatus(String vehicleID,String status);
}
