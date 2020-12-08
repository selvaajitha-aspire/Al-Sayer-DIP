package com.alsayer.core.servicehistory.dao;

import com.alsayer.core.model.ServiceHistoryModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface ServiceHistoryDao {

    public List<ServiceHistoryModel> getServiceHistoryByChassisAndCustomer(String chassisNo, CustomerModel customer);
}
