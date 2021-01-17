package com.alsayer.core.payload.service.impl;

import com.alsayer.core.model.NotificationPayloadModel;
import com.alsayer.core.payload.dao.NotificationPayloadDao;
import com.alsayer.core.payload.service.NotificationPayloadService;
import com.alsayer.facades.data.NotificationPayloadData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class DefaultNotificationPayloadService implements NotificationPayloadService {

    private NotificationPayloadDao notificationPayloadDao;

    private Converter<NotificationPayloadModel, NotificationPayloadData> notificationPayloadConverter;

    private Converter<NotificationPayloadData, NotificationPayloadModel> notificationPayloadReverseConverter;

    @Override
    public NotificationPayloadData getPayloadTemplateByCode(String templateCode){
        return notificationPayloadConverter.convert(notificationPayloadDao.getNotificationPayloadByCode(templateCode));
    }

    public void setNotificationPayloadDao(NotificationPayloadDao notificationPayloadDao) {
        this.notificationPayloadDao = notificationPayloadDao;
    }

    public void setNotificationPayloadConverter(Converter<NotificationPayloadModel, NotificationPayloadData> notificationPayloadConverter) {
        this.notificationPayloadConverter = notificationPayloadConverter;
    }

    public void setNotificationPayloadReverseConverter(Converter<NotificationPayloadData, NotificationPayloadModel> notificationPayloadReverseConverter) {
        this.notificationPayloadReverseConverter = notificationPayloadReverseConverter;
    }
}
