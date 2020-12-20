package com.alsayer.core.actions.service;

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.definition.ActionDefinitionContext;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.task.RetryLaterException;

public class CreateCustomerInECC extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    private ConfigurationService configurationService;

    @Override
    public Transition executeAction(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel) throws RetryLaterException, Exception {
        CustomerModel cust = storeFrontCustomerProcessModel.getCustomer();
        System.out.println("Action getting Started" + CustomerType.REGISTERED.equals(cust.getType()));
        if(CustomerType.REGISTERED.equals(cust.getType())){
            System.out.println("Action getting executed");
        }
        System.out.println("Action getting Finished");
        return Transition.OK;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
