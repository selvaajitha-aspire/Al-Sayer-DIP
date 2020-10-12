package com.alsayer.core.servicerequest.service;

import java.util.List;
import com.alsayer.facades.data.ServiceRequestData;

import com.alsayer.core.model.ServiceRequestModel;

public interface ServiceRequestService {

    public List<ServiceRequestData> getAllServiceRequests();
}
