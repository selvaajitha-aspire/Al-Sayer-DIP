package com.alsayer.facades.customer.impl;

import com.alsayer.core.customer.services.AlsayerCustomerAccountService;
import com.alsayer.facades.customer.AlsayerCustomerFacade;
import com.alsayer.occ.dto.ECCCustomerWsDTO;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.text.ParseException;
import java.util.UUID;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

public class AlsayerCustomerFacadeImpl extends DefaultCustomerFacade implements AlsayerCustomerFacade {

    private static final Logger LOG = Logger.getLogger(AlsayerCustomerFacadeImpl.class);

    @Resource(name = "alsayerCustomerAccountService")
    private AlsayerCustomerAccountService alsayerCustomerAccountService;


    public void activateUser(String token) throws TokenInvalidatedException {
        getAlsayerCustomerAccountService().activateUser(token);
    }


    @Override
    public void register(final RegisterData registerData) throws DuplicateUidException {
        validateParameterNotNullStandardMessage("registerData", registerData);
        Assert.hasText(registerData.getCivilId(), "The field [civilId] cannot be empty");
        Assert.hasText(registerData.getName(), "The field [name] cannot be empty");
        Assert.hasText(registerData.getArabicName(), "The field [arabicName] cannot be empty");
        Assert.hasText(registerData.getMobile(), "The field [mobile] cannot be empty");
        Assert.hasText(registerData.getUid(), "The field [uid] cannot be empty");


        final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
        newCustomer.setLoginDisabled(true);
        setCommonPropertiesForRegister(registerData, newCustomer);
        getCustomerAccountService().register(newCustomer, registerData.getPassword());
    }


    protected void setCommonPropertiesForRegister(final RegisterData registerData, final CustomerModel customerModel) {
        customerModel.setName(registerData.getName());
        // setTitleForRegister(registerData, customerModel);
        setUidForRegister(registerData, customerModel);
        customerModel.setCivilId(registerData.getCivilId());
        customerModel.setUid(registerData.getUid());
        customerModel.setArabicName(registerData.getArabicName());
        customerModel.setMobile(registerData.getMobile());
        customerModel.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
        customerModel.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
        customerModel.setEccCustId(registerData.getEccCustId());
    }

    protected void setUidForRegister(final RegisterData registerData, final CustomerModel customer)
    {
        customer.setUid(registerData.getUid().toLowerCase());
        customer.setOriginalUid(registerData.getUid());
    }


    @Override
    public ECCCustomerWsDTO getCustomerECCDetails(String code) {
        return getAlsayerCustomerAccountService().getCustomerECCDetails(code);
    }

    @Override
    public void sendOTP(String code, String mobile) {
        Assert.hasText(code, "The field [civilId] cannot be empty");
        Assert.hasText(mobile, "The field [mobile] cannot be empty");
        getAlsayerCustomerAccountService().sendOTP(code,mobile);


    }


    @Override
    public void eccRecordSynchronization(RegisterData registerData) {

        getAlsayerCustomerAccountService().eccRecordSynchronization(registerData);

    }

    @Override
    public boolean validateOTP(RegisterData registerData) throws ParseException {

        boolean result =  getAlsayerCustomerAccountService().getOTPForValidation(registerData);
        return result;
    }


    @Override
    public CustomerData nextDummyCustomerData(final RegisterData registerData) {
        final CustomerModel userModel = new CustomerModel();
        setCommonPropertiesForRegister(registerData, userModel);
        userModel.setCustomerID(UUID.randomUUID().toString());

        return getCustomerConverter().convert(userModel);
    }


    public AlsayerCustomerAccountService getAlsayerCustomerAccountService() {
        return alsayerCustomerAccountService;
    }

    public void setAlsayerCustomerAccountService(AlsayerCustomerAccountService alsayerCustomerAccountService) {
        this.alsayerCustomerAccountService = alsayerCustomerAccountService;
    }


}
