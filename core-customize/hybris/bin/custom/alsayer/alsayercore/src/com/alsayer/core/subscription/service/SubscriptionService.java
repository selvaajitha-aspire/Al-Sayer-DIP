package com.alsayer.core.subscription.service;

import com.alsayer.core.model.SubscriptionModel;
import com.alsayer.facades.data.SubscriptionData;

import java.util.List;

public interface SubscriptionService {

    public boolean save(SubscriptionData subscription);

    public List<SubscriptionData> getAllSubscriptions();

}
