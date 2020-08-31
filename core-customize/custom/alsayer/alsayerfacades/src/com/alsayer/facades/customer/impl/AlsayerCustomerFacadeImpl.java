package com.alsayer.facades.customer.impl;

import com.alsayer.core.customer.AlsayerCustomerAccountService;
import com.alsayer.facades.customer.AlsayerCustomerFacade;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class AlsayerCustomerFacadeImpl  extends DefaultCustomerFacade implements AlsayerCustomerFacade{

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerFacadeImpl.class);

    @Resource(name = "alsayerCustomerAccountService")
    private AlsayerCustomerAccountService alsayerCustomerAccountService;



    public void activateUser(String token) throws TokenInvalidatedException {
        getAlsayerCustomerAccountService().activateUser(token);
    }

    @Override
    public void register(final RegisterData registerData) throws DuplicateUidException
    {
        validateParameterNotNullStandardMessage("registerData", registerData);
        Assert.hasText(registerData.getFirstName(), "The field [FirstName] cannot be empty");
        Assert.hasText(registerData.getLastName(), "The field [LastName] cannot be empty");
        Assert.hasText(registerData.getLogin(), "The field [Login] cannot be empty");

        final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
        newCustomer.setLoginDisabled(true);
        setCommonPropertiesForRegister(registerData, newCustomer);
        getCustomerAccountService().register(newCustomer, registerData.getPassword());
    }




    public AlsayerCustomerAccountService getAlsayerCustomerAccountService() {
        return alsayerCustomerAccountService;
    }

    public void setAlsayerCustomerAccountService(AlsayerCustomerAccountService alsayerCustomerAccountService) {
        this.alsayerCustomerAccountService = alsayerCustomerAccountService;
    }


}
