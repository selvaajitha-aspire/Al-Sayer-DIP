package com.alsayer.core.servicerequest.dao;


import com.alsayer.core.model.RsaRequestModel;

import java.util.List;

public interface RsaRequestDao
{

    public List<RsaRequestModel> getAllRsaRequests();

    public List<RsaRequestModel> getRsaRequestsByStatus(String status);

    public RsaRequestModel getRsaRequestByUID(String uid);

    public List<RsaRequestModel> getRsaRequestsByCustomerId(String customerID);

    public List<RsaRequestModel> getRsaRequestsByCustomerIdAndStatus(String customerID,String status);

    public List<RsaRequestModel> getRsaRequestsByVehicleId(String vehicleID);

    public List<RsaRequestModel> getRsaRequestsByVehicleIdAndStatus(String vehicleID,String status);
}
