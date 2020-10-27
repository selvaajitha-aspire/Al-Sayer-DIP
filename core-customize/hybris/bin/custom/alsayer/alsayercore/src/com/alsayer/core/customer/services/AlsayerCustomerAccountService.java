package com.alsayer.core.customer.services;

import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;

import java.text.ParseException;

public interface AlsayerCustomerAccountService {


    ECCCustomerWsDTO getCustomerECCDetails(String code);

    void sendOTP();

    void eccRecordSynchronization(RegisterData registerData);

    boolean getOTPForValidation(RegisterData registerData) throws ParseException;

    void activateUser(String token) throws TokenInvalidatedException;
}

