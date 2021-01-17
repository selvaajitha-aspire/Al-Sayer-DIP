package com.alsayer.facades.populators;

import com.alsayer.core.model.NotificationPayloadModel;
import com.alsayer.facades.data.NotificationPayloadData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class NotificationPayloadPopulator implements Populator<NotificationPayloadModel, NotificationPayloadData> {

    @Override
    public void populate(NotificationPayloadModel source, NotificationPayloadData target) throws ConversionException {
        if(null == source || null == target){
            return;
        }

        if(source instanceof  NotificationPayloadModel){
            target.setTemplate(source.getTemplate());
            target.setTemplateCode(source.getTemplateCode());
        }
    }
}
