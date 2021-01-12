package com.alsayer.core.payload.service;

import com.alsayer.facades.data.NotificationPayloadData;

public interface NotificationPayloadService {
    NotificationPayloadData getPayloadTemplateByCode(String templateCode);
}
