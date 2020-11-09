package com.alsayer.core.servicerequest.service;

import java.util.List;
import com.alsayer.facades.data.RsaRequestData;

import com.alsayer.core.model.RsaRequestModel;

public interface RsaRequestService
{

    public List<RsaRequestData> getAllRsaRequests();

    public List<RsaRequestData> getRsaRequestsByStatus(String status);

    public RsaRequestData getRsaRequestByUID(String uid);

    public List<RsaRequestData> getRsaRequestsByCustomerId();

    public List<RsaRequestData> getRsaRequestsByCustomerIdAndStatus(String status);

    public List<RsaRequestData> getRsaRequestsByVehicleId(String vehicleID);

    public List<RsaRequestData> getRsaRequestsByVehicleIdAndStatus(String vehicleID,String status);
}
