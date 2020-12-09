package com.alsayer.core.servicehistory.service;

import com.alsayer.facades.data.RsaRequestData;
import com.alsayer.facades.data.ServiceHistoryData;

import java.util.List;

public interface ServiceHistoryService {
    List<ServiceHistoryData> getMyServiceHistoryByChassisNo(String chassisNo);
}
