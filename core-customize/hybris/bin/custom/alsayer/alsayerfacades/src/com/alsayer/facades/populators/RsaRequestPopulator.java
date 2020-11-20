package com.alsayer.facades.populators;

import com.alsayer.core.constants.GeneratedAlsayerCoreConstants;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.cmsfacades.media.impl.DefaultMediaFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.facades.data.RsaRequestData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.facades.data.DriverDetailsData;
import de.hybris.platform.servicelayer.media.MediaService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

public class RsaRequestPopulator implements Populator<RsaRequestModel,RsaRequestData> {
    private final Logger LOG = Logger.getLogger(DefaultMediaFacade.class);

    private I18NService i18nService;

    private  MediaService mediaService;

    private Converter<VehicleModel,VehicleData> vehicleDataConverter;

    private Converter<DriverDetailsModel,DriverDetailsData> driverDetailsConverter;

    public Converter<VehicleModel, VehicleData> getVehicleDataConverter() {
        return vehicleDataConverter;
    }

    public void setVehicleDataConverter(Converter<VehicleModel, VehicleData> vehicleDataConverter) {
        this.vehicleDataConverter = vehicleDataConverter;
    }

    public Converter<DriverDetailsModel, DriverDetailsData> getDriverDetailsConverter() {
        return driverDetailsConverter;
    }

    public void setDriverDetailsConverter(Converter<DriverDetailsModel, DriverDetailsData> driverDetailsConverter) {
        this.driverDetailsConverter = driverDetailsConverter;
    }

    @Override
    public void populate(RsaRequestModel serviceRequestModel, RsaRequestData serviceRequestData) throws ConversionException {
        if(null != serviceRequestModel && null != serviceRequestData
                && serviceRequestModel instanceof RsaRequestModel && serviceRequestData instanceof RsaRequestData){
            if(null != serviceRequestModel.getLatitude()){
                serviceRequestData.setLatitude(serviceRequestModel.getLatitude());
            }
            if(null != serviceRequestModel.getLongitude()){
                serviceRequestData.setLongitude(serviceRequestModel.getLongitude());
            }
            if(null != serviceRequestModel.getNotes()){
                serviceRequestData.setNotes(serviceRequestModel.getNotes());
            }
            if(null != serviceRequestModel.getAttachments()){
             for( MediaModel media: serviceRequestModel.getAttachments()){
                 MediaFileDto mediaFileDto =new MediaFileDto();
                 InputStream data =getMediaService().getStreamFromMedia(media);
                 mediaFileDto.setInputStream(data);
                 mediaFileDto.setName(media.getRealFileName());
                 mediaFileDto.setSize(media.getSize());
                 mediaFileDto.setMime(media.getMime());
                 serviceRequestData.setAttachments(Collections.singletonList(mediaFileDto));
             }
            }

            if(null != serviceRequestModel.getVehicle()
                    && serviceRequestModel.getVehicle() instanceof VehicleModel){
                VehicleData vehicle = getVehicleDataConverter().convert(serviceRequestModel.getVehicle());
                serviceRequestData.setVehicle(vehicle);
            }
            if(null != serviceRequestModel.getDriverDetails()
                    && serviceRequestModel.getDriverDetails() instanceof DriverDetailsModel){
                DriverDetailsData driverDetails = getDriverDetailsConverter().convert(serviceRequestModel.getDriverDetails());
                serviceRequestData.setDriverDetails(driverDetails);
            }
            if(null != serviceRequestModel.getStatus()){
                serviceRequestData.setStatus(serviceRequestModel.getStatus().getCode());
            }
            if(null != serviceRequestModel.getType()){
                serviceRequestData.setType(serviceRequestModel.getType());
            }

        }
    }


    public I18NService getI18nService() {
        return i18nService;
    }

    public void setI18nService(I18NService i18nService) {
        this.i18nService = i18nService;
    }

    public MediaService getMediaService() {
        return mediaService;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }
}
