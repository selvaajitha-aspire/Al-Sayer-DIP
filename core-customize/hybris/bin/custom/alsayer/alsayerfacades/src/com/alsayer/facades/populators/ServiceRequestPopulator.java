package com.alsayer.facades.populators;

import com.alsayer.core.constants.GeneratedAlsayerCoreConstants;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.facades.data.ServiceRequestData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.UUID;

public class ServiceRequestPopulator implements Populator<ServiceRequestModel,ServiceRequestData> {

    private I18NService i18nService;

    @Override
    public void populate(ServiceRequestModel serviceRequestModel, ServiceRequestData serviceRequestData) throws ConversionException {
        serviceRequestData.setLatitude(serviceRequestModel.getLatitude());
        serviceRequestData.setLongitude(serviceRequestModel.getLongitude());
        serviceRequestData.setNotes(serviceRequestModel.getNotes());
        serviceRequestData.setAttachments(serviceRequestModel.getAttachments());
    }

    public I18NService getI18nService() {
        return i18nService;
    }

    public void setI18nService(I18NService i18nService) {
        this.i18nService = i18nService;
    }
}
