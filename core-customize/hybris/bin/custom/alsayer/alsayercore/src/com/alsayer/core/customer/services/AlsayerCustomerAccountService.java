package com.alsayer.core.customer.services;

import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.ParseException;

public interface AlsayerCustomerAccountService {


    ECCCustomerWsDTO getCustomerECCDetails(String code);

    void sendOTP(String code,String mobile);

    void eccRecordSynchronization(RegisterData registerData);

    boolean getOTPForValidation(RegisterData registerData) throws ParseException;

    boolean validateOtp(String civilId,String otp) throws ParseException;

    void activateUser(String token) throws TokenInvalidatedException;

    boolean updateProfilePhoto(CustomerModel customer, MediaFileDto profilePhoto) throws ParseException;

}

