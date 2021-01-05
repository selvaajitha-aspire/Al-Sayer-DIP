package com.alsayer.core.insurance.daos;

import com.alsayer.core.model.InsuranceModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

public interface InsurancesDao {

    public List<InsuranceModel> getInsurancesByCustomer(final CustomerModel customer);
}

