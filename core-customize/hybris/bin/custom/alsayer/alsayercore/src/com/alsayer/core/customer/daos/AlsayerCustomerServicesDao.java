package com.alsayer.core.customer.daos;

import com.alsayer.core.model.CustomerAuthenticationModel;


import java.util.List;

public interface AlsayerCustomerServicesDao {


    CustomerAuthenticationModel getSavedOtp(String civilId);
}

