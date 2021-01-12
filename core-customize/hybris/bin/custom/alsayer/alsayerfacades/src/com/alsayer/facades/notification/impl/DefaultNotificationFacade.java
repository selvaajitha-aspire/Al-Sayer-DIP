package com.alsayer.facades.notification.impl;

import com.alsayer.facades.data.SubscriptionData;
import com.alsayer.facades.notification.NotificationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DefaultNotificationFacade implements NotificationFacade {

    final static Logger LOG = LoggerFactory.getLogger(DefaultNotificationFacade.class);
    
    final String PUBLIC_KEY = "BB1Mex4SWQ_ifDM9edDpq90UyvjZheWnwUM15qLcoOOb8HiLI-PuYCR42AQpNJlB76HKe_IQP9E6jzrkZEGzGag";
    final String PRIVATE_KEY = "PgzoT7acau8MHNSG_1eMRwz_2halkotYxFrrT_2R3Io";

    @Override
    public void pushNotification(Subscription subscription, String payload){
        try {
            Notification note = new Notification(subscription,payload);
            PushService ps = new PushService(PUBLIC_KEY,PRIVATE_KEY,"Test Notification");
            try {
                ps.send(note);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            } catch (ExecutionException e) {
                LOG.error(e.getMessage());
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
            }
        } catch (GeneralSecurityException | JoseException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void pushNotification(List<Subscription> subscriptionList, String payload){
        try {
            for(Subscription subscription : subscriptionList) {
                pushNotification(subscription,payload);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void pushNotification(SubscriptionData subscription, String payload){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Subscription sub = mapper.readValue(subscription.getSubscriptionJSON(),Subscription.class);
            pushNotification(sub,payload);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void pushNotificationWithSubscriptionData(List<SubscriptionData> subscriptionList, String payload){
        try {
            for(SubscriptionData subscription : subscriptionList) {
                pushNotification(subscription, payload);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void pushNotification(String subscription, String payload){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Subscription sub = mapper.readValue(subscription, Subscription.class);
            pushNotification(sub,payload);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void pushNotificationWithSubscriptionString(List<String> subscriptionList , String payload){
        try {
            for(String subscription : subscriptionList) {
                pushNotification(subscription, payload);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}
