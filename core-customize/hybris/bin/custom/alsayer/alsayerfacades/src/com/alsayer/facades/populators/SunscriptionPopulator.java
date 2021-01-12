package com.alsayer.facades.populators;

import com.alsayer.core.model.SubscriptionModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import com.alsayer.facades.data.SubscriptionData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class SunscriptionPopulator implements Populator<SubscriptionModel,SubscriptionData> {

    private Converter<CustomerModel, CustomerData> customerConverter;

    @Override
    public void populate(SubscriptionModel source, SubscriptionData target) throws ConversionException {
        if(null == source || null == target ){
            return;
        }

        target.setSubscriptionJSON(source.getSubscriptionJSON());

        if(null != source.getUser() && source.getUser() instanceof  CustomerModel ){
            target.setUser(customerConverter.convert(source.getUser()));
        }

    }

    public void setCustomerConverter(Converter<CustomerModel, CustomerData> customerConverter) {
        this.customerConverter = customerConverter;
    }
}
