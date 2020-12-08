package com.alsayer.core.servicerequest.dao;


import com.alsayer.core.model.RsaRequestModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface RsaRequestDao
{

    public List<RsaRequestModel> getAllRsaRequests();

    public List<RsaRequestModel> getRsaRequestsByStatus(String status);

    public RsaRequestModel getRsaRequestByUID(String uid);

    public List<RsaRequestModel> getRsaRequestsByCustomerId(CustomerModel customerID);

    public List<RsaRequestModel> getRsaRequestsByCustomerIdAndStatus(CustomerModel customerID,List<String> statuses);

    public List<RsaRequestModel> getRsaRequestsByVehicleId(String vehicleID);

    public List<RsaRequestModel> getRsaRequestsByVehicleIdAndStatus(String vehicleID,String status);
}
