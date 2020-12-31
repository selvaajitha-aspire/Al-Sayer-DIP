package com.alsayer.core.insurance.services;

import com.alsayer.core.model.InsuranceModel;

import java.util.List;

public interface InsurancesService {
    public List<InsuranceModel> getInsurancesByChassisNo(String chassisNumber);

}

