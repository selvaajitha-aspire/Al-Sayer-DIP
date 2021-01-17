package com.alsayer.core.subscription.service.impl;

import com.alsayer.core.model.SubscriptionModel;
import com.alsayer.core.subscription.dao.SubscriptionDao;
import com.alsayer.core.subscription.service.SubscriptionService;
import com.alsayer.facades.data.SubscriptionData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

public class DefaultSubscriptionService implements SubscriptionService {

    private SubscriptionDao subscriptionDao;

    private Converter<SubscriptionModel,SubscriptionData> subscriptionConverter;

    private Converter<SubscriptionData,SubscriptionModel> subscriptionReverseConverter;

    private UserService userService;

    @Override
    public boolean save(SubscriptionData subscription) {
        final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
        SubscriptionModel subModel = subscriptionReverseConverter.convert(subscription);
        subModel.setUser(currentCustomer);
        return subscriptionDao.save(subModel);
    }

    @Override
    public List<SubscriptionData> getAllSubscriptions() {

        return subscriptionConverter.convertAll(subscriptionDao.getAllSubscriptions());
    }

    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public void setSubscriptionConverter(Converter<SubscriptionModel, SubscriptionData> subscriptionConverter) {
        this.subscriptionConverter = subscriptionConverter;
    }

    public void setSubscriptionReverseConverter(Converter<SubscriptionData, SubscriptionModel> subscriptionReverseConverter) {
        this.subscriptionReverseConverter = subscriptionReverseConverter;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
