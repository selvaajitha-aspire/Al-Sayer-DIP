package com.alsayer.core.servicerequest.service;

import com.alsayer.facades.data.RsaRequestData;

import java.util.List;

public interface RsaRequestService
{

    public List<RsaRequestData> getAllRsaRequests();

    public List<RsaRequestData> getRsaRequestsByStatus(String status);

    public RsaRequestData getRsaRequestByUID(String uid);

    public List<RsaRequestData> getRsaRequestsByCustomerId();

    public List<RsaRequestData> getRsaRequestsByCustomerIdAndStatus(List<String> statuses);

    public List<RsaRequestData> getRsaRequestsByVehicleId(String vehicleID);

    public List<RsaRequestData> getRsaRequestsByVehicleIdAndStatus(String vehicleID,String status);

    public List<RsaRequestData> getRsaRequestsByChassisAndCustomer(String chassisNo);
}
