package com.alsayer.facades.populators;

import com.alsayer.core.enums.IssueType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.core.utils.DateUtil;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import com.alsayer.facades.data.RsaRequestData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.cmsfacades.media.impl.DefaultMediaFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class RsaRequestReversePopulator implements Populator<RsaRequestData, RsaRequestModel> {

    private final Logger LOG = Logger.getLogger(DefaultMediaFacade.class);
    private UserService userService;

    private ModelService modelService;
    private MyVehiclesService myVehiclesService;

    private I18NService i18nService;
    private CatalogVersionService catalogVersionService;
    public static final String CONTENT_CATALOG="alsayer-spaContentCatalog";
    public static final String ONLINE = "Online";
    public MediaService mediaService;




    @Override
    public void populate(RsaRequestData serviceRequestData, RsaRequestModel serviceRequestModel) throws ConversionException {

        Date currentDate=java.util.Calendar.getInstance().getTime();
        String chassisNumber=serviceRequestData.getVehicle().getChassisNumber();
        String issueType=serviceRequestData.getIssue();
        serviceRequestModel.setUid(UUID.randomUUID().toString());
        serviceRequestModel.setCustomer((CustomerModel) userService.getCurrentUser());
        VehicleModel vehicleModel=getVehicleByChassis(chassisNumber);
        serviceRequestModel.setVehicle(vehicleModel);
        serviceRequestModel.setStatus(ServiceStatus.STARTED);

        for(WarrantyModel warrantyModel: vehicleModel.getWarranties()) {
            if(warrantyModel.getWarrantyType().contains("14")&& warrantyModel.getWarrantyExpiryDate().after(currentDate)) {

                serviceRequestModel.setType("MUSAADA");
                if (issueType.equalsIgnoreCase("FLAT_TYRE")) {
                    serviceRequestModel.setIssue(IssueType.FLAT_TYRE);

                } else if (issueType.equalsIgnoreCase("OUT_OF_FUEL")) {
                    serviceRequestModel.setIssue((IssueType.OUT_OF_FUEL));
                } else if (issueType.equalsIgnoreCase("DEAD_BATTERY")) {
                    serviceRequestModel.setIssue((IssueType.DEAD_BATTERY));
                } else {
                    serviceRequestModel.setIssue((IssueType.OTHERS_MUSAADA));
                }
                break;
            }else {
                serviceRequestModel.setType("RSA");
                serviceRequestModel.setIssue(IssueType.RSA);
            }
        }
        serviceRequestModel.setLatitude(serviceRequestData.getLatitude());
        serviceRequestModel.setLongitude(serviceRequestData.getLongitude());
        serviceRequestModel.setNotes(serviceRequestData.getNotes(), getI18nService().getCurrentLocale());

        final MediaModel mediaModel = getModelService().create(MediaModel.class);

        final Collection<MediaFileDto> attachments = serviceRequestData.getAttachments();
        if (attachments != null) {
            for (final MediaFileDto media : attachments) {
                createMediaFilePopulator(media, mediaModel);

            }
            serviceRequestModel.setAttachments(Collections.singletonList(mediaModel));
        }

    }

    private void createMediaFilePopulator(MediaFileDto media, MediaModel mediaModel) {
        mediaModel.setCode(UUID.randomUUID().toString());
        mediaModel.setRealFileName(media.getName());
        mediaModel.setSize(media.getSize());
        mediaModel.setMime(media.getMime());
        mediaModel.setCatalogVersion(getCatalogVersionService().getCatalogVersion(CONTENT_CATALOG, ONLINE));
        getModelService().save(mediaModel);
        populateStream(media, mediaModel);

    }

    private void populateStream(MediaFileDto mediaFile, MediaModel mediaModel) {
        try (InputStream inputStream = mediaFile.getInputStream())
        {
            getMediaService().setStreamForMedia(mediaModel, inputStream);
            getModelService().save(mediaModel);
        }
        catch (final IOException e)
        {
            LOG.error(e);
        }
    }


    private VehicleModel getVehicleByChassis(String chassisNumber) {
        return  getMyVehiclesService().getVehicleByChassisNo(chassisNumber);
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MyVehiclesService getMyVehiclesService() {
        return myVehiclesService;
    }

    public void setMyVehiclesService(MyVehiclesService myVehiclesService) {
        this.myVehiclesService = myVehiclesService;
    }

    public I18NService getI18nService() {
        return i18nService;
    }

    public void setI18nService(I18NService i18nService) {
        this.i18nService = i18nService;
    }


    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    public MediaService getMediaService() {
        return mediaService;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }
}


