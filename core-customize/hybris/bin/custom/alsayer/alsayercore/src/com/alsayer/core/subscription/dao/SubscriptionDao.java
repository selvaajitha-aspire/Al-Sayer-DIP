package com.alsayer.core.subscription.dao;

import com.alsayer.core.model.SubscriptionModel;

import java.util.List;

public interface SubscriptionDao {

    public boolean save(final SubscriptionModel subscription);

    public List<SubscriptionModel> getAllSubscriptions();

}
