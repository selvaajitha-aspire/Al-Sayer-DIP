package com.alsayer.facades.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import com.alsayer.facades.data.SubscriptionData;
import com.alsayer.core.model.SubscriptionModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class SubscriptionReversePopulator implements Populator<SubscriptionData,SubscriptionModel> {

    private Converter<CustomerData,CustomerModel> customerReverseConverter;

    @Override
    public void populate(SubscriptionData source, SubscriptionModel target) throws ConversionException {
        if(null == source || null == target ){
            return;
        }

        target.setSubscriptionJSON(source.getSubscriptionJSON());

        if(null != source.getUser() && source.getUser() instanceof CustomerData){
            target.setUser(customerReverseConverter.convert(source.getUser()));
        }
    }

    public void setCustomerReverseConverter(Converter<CustomerData, CustomerModel> customerReverseConverter) {
        this.customerReverseConverter = customerReverseConverter;
    }
}
