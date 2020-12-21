package com.alsayer.facades.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.io.InputStream;

public class CusomerProfilePhotoPopulator implements Populator<CustomerModel, CustomerData> {

    private final Logger LOG = Logger.getLogger(CusomerProfilePhotoPopulator.class);

    private MediaService mediaService;

    private Converter<MediaModel, ImageData> imageConverter;

    @Override
    public void populate(CustomerModel source, CustomerData target) throws ConversionException {
            if(null == source || null== target){
                return;
            }

            if(null != source.getProfilePicture()  && source.getProfilePicture() instanceof MediaModel){
                target.setProfilePicture(getImageConverter().convert(source.getProfilePicture()));
                target.setProfilePhotoUrl(source.getProfilePicture().getURL());
            }
    }

    public MediaService getMediaService() {
        return mediaService;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public Converter<MediaModel, ImageData> getImageConverter() {
        return imageConverter;
    }

    public void setImageConverter(Converter<MediaModel, ImageData> imageConverter) {
        this.imageConverter = imageConverter;
    }
}
