package com.alsayer.core.servicerequest.dao;

import com.alsayer.core.jalo.ServiceRequest;

import java.util.List;
import com.alsayer.core.model.ServiceRequestModel;

public interface ServiceRequestDao {

    public List<ServiceRequestModel> getAllServiceRequests();

    public List<ServiceRequestModel> getServiceRequestsByStatus(String status);

    public ServiceRequestModel getServiceRequestByUID(String uid);

    public List<ServiceRequestModel> getServiceRequestsByCustomerId(String customerID);

    public List<ServiceRequestModel> getServiceRequestsByCustomerIdAndStatus(String customerID,String status);

    public List<ServiceRequestModel> getServiceRequestsByVehicleId(String vehicleID);

    public List<ServiceRequestModel> getServiceRequestsByVehicleIdAndStatus(String vehicleID,String status);
}
