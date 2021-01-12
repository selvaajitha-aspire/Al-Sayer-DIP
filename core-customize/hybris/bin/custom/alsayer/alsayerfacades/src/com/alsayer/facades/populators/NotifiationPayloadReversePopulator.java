package com.alsayer.facades.populators;

import com.alsayer.core.model.NotificationPayloadModel;
import com.alsayer.facades.data.NotificationPayloadData;
import de.hybris.platform.converters.Populator;
import org.apache.commons.beanutils.ConversionException;

public class NotifiationPayloadReversePopulator implements Populator<NotificationPayloadData, NotificationPayloadModel> {

@Override
public void populate(NotificationPayloadData source, NotificationPayloadModel target) throws ConversionException {
        if(null == source || null == target){
            return;
        }

        if(source instanceof  NotificationPayloadData){
            target.setTemplate(source.getTemplate());
            target.setTemplateCode(source.getTemplateCode());
        }
    }
}