package com.alsayer.core.payload.dao;

import com.alsayer.core.model.NotificationPayloadModel;

import java.util.List;
import java.util.Map;

public interface NotificationPayloadDao {
    List<NotificationPayloadModel> getAllNotificationPayloads();

    NotificationPayloadModel getNotificationPayloadByCode(String templateCode);

    NotificationPayloadModel getNotificationPayloadByCode(Map<String, Object> params);
}
