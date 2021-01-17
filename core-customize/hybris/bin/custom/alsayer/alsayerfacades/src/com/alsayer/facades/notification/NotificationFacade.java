package com.alsayer.facades.notification;

import com.alsayer.facades.data.SubscriptionData;
import nl.martijndwars.webpush.Subscription;

import java.util.List;

public interface NotificationFacade {

    void pushNotification(Subscription subscription, String payload);

    void pushNotification(List<Subscription> subscriptionList, String payload);

    void pushNotification(SubscriptionData subscription, String payload);

    void pushNotificationWithSubscriptionData(List<SubscriptionData> subscriptionList, String payload);

    void pushNotification(String subscription, String payload);

    void pushNotificationWithSubscriptionString(List<String> subscriptionList, String payload);
}
