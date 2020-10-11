package com.alsayer.facades.customer;

import com.alsayer.facades.data.RegisterData;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

public interface AlsayerCustomerFacade extends CustomerFacade {


    /**
     * @param token
     * @throws TokenInvalidatedException
     */
    void activateUser(String token) throws TokenInvalidatedException;


    /**
     * Register a user with given parameters
     *
     * @param registerData
     * 		the user data the user will be registered with
     * @throws IllegalArgumentException
     * 		if required data is missing
     * @throws UnknownIdentifierException
     * 		if the title code is invalid
     * @throws DuplicateUidException
     * 		if the login is not unique
     */
    void register(final RegisterData registerData) throws DuplicateUidException;


    CustomerData nextDummyCustomerData(final RegisterData registerData);


    ECCCustomerWsDTO getCustomerECCDetails(String code);

    void sendOTP();

    void eccRecordSynchronization(RegisterData registerData);


    void fetchECCCustomerRecord(RegisterData civilId);
}

