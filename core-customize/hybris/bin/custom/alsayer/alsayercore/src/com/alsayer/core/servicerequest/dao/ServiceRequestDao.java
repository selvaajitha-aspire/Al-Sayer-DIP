package com.alsayer.core.servicerequest.dao;

import com.alsayer.core.jalo.ServiceRequest;

import java.util.List;
import com.alsayer.core.model.ServiceRequestModel;

public interface ServiceRequestDao {

    public List<ServiceRequestModel> getAllServiceRequests();

}
