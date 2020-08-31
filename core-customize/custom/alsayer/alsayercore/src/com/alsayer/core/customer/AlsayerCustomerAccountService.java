package com.alsayer.core.customer;

import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

public class AlsayerCustomerAccountService extends DefaultCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerAccountService.class);


    public void activateUser(@RequestParam(name = "token") String token) throws TokenInvalidatedException {
        final SecureToken data = getSecureTokenService().decryptData(token);
        if (getTokenValiditySeconds() > 0L)
        {
            final long delta = new Date().getTime() - data.getTimeStamp();
            if (delta / 1000 > getTokenValiditySeconds())
            {
                throw new IllegalArgumentException("token expired");
            }
        }

        final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
        if (customer == null)
        {
            throw new IllegalArgumentException("user for token not found");
        }
        if (!token.equals(customer.getToken()))
        {
            throw new TokenInvalidatedException();
        }
        customer.setToken(null);
        customer.setLoginDisabled(false);
        getModelService().save(customer);
    }
}
