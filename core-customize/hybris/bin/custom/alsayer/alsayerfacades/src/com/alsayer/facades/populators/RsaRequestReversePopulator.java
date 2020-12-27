package com.alsayer.facades.populators;

import com.alsayer.core.enums.IssueType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import com.alsayer.facades.data.RsaRequestData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cmsfacades.dto.MediaFileDto;
import de.hybris.platform.cmsfacades.media.impl.DefaultMediaFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

public class RsaRequestReversePopulator implements Populator<RsaRequestData, RsaRequestModel> {

    private final Logger LOG = Logger.getLogger(DefaultMediaFacade.class);
    private UserService userService;

    private PersistentKeyGenerator rsaRequestCodeGenerator;

    private ModelService modelService;
    private MyVehiclesService myVehiclesService;

    private I18NService i18nService;
    private CatalogVersionService catalogVersionService;
    public static final String CONTENT_CATALOG="alsayer-spaContentCatalog";
    public static final String ONLINE = "Online";
    public MediaService mediaService;

    private Converter<AddressData, AddressModel> addressReverseConverter;



    @Override
    public void populate(RsaRequestData serviceRequestData, RsaRequestModel serviceRequestModel) throws ConversionException {

        final Date currentDate=java.util.Calendar.getInstance().getTime();
        final String chassisNumber=serviceRequestData.getVehicle().getChassisNumber();
        final String issueType=serviceRequestData.getIssue();
        serviceRequestModel.setUid(getRsaRequestCodeGenerator().generate().toString());
        serviceRequestModel.setCustomer((CustomerModel) userService.getCurrentUser());
        final VehicleModel vehicleModel=getVehicleByChassis(chassisNumber);
        serviceRequestModel.setVehicle(vehicleModel);
        serviceRequestModel.setStatus(ServiceStatus.STARTED);

        if(CollectionUtils.isNotEmpty(vehicleModel.getWarranties())){
            for(final WarrantyModel warrantyModel: vehicleModel.getWarranties()) {
                if(warrantyModel.getWarrantyType().contains("14")&& warrantyModel.getWarrantyExpiryDate().after(currentDate)) {


                    if (issueType.equalsIgnoreCase("FLAT_TYRE")) {
                        serviceRequestModel.setType("MUSAADA");
                        serviceRequestModel.setIssue(IssueType.FLAT_TYRE);

                    } else if (issueType.equalsIgnoreCase("OUT_OF_FUEL")) {
                        serviceRequestModel.setType("MUSAADA");
                        serviceRequestModel.setIssue((IssueType.OUT_OF_FUEL));
                    } else if (issueType.equalsIgnoreCase("DEAD_BATTERY")) {
                        serviceRequestModel.setType("MUSAADA");
                        serviceRequestModel.setIssue((IssueType.DEAD_BATTERY));
                    } else if(issueType.equalsIgnoreCase("OTHERS_MUSAADA")){
                        serviceRequestModel.setType("MUSAADA");
                        serviceRequestModel.setIssue((IssueType.OTHERS_MUSAADA));
                    }
                    else if(issueType.equalsIgnoreCase("RSA")){
                        serviceRequestModel.setType("RSA");
                        serviceRequestModel.setIssue((IssueType.RSA));
                    }
                    break;
                }else {
                    serviceRequestModel.setType("RSA");
                    serviceRequestModel.setIssue(IssueType.RSA);
                }
            }
        }else {
            serviceRequestModel.setType("RSA");
            serviceRequestModel.setIssue(IssueType.RSA);
        }
        serviceRequestModel.setLatitude(serviceRequestData.getLatitude());
        serviceRequestModel.setLongitude(serviceRequestData.getLongitude());
        serviceRequestModel.setNotes(serviceRequestData.getNotes(), getI18nService().getCurrentLocale());

        if (CollectionUtils.isNotEmpty(serviceRequestData.getAttachments()))
        {
            for (final MediaFileDto media : serviceRequestData.getAttachments())
            {
                final MediaModel mediaModel = getModelService().create(MediaModel.class);
                createMediaFilePopulator(media, mediaModel);
                serviceRequestModel.setAttachments(Collections.singletonList(mediaModel));
            }
        }
        serviceRequestModel.setAddress(getAddressReverseConverter().convert(serviceRequestData.getAddress()));
        serviceRequestModel.getAddress().setOwner(serviceRequestModel);
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

    public PersistentKeyGenerator getRsaRequestCodeGenerator() {
        return rsaRequestCodeGenerator;
    }

    public void setRsaRequestCodeGenerator(PersistentKeyGenerator rsaRequestCodeGenerator) {
        this.rsaRequestCodeGenerator = rsaRequestCodeGenerator;
    }

    public Converter<AddressData, AddressModel> getAddressReverseConverter() {
        return addressReverseConverter;
    }

    public void setAddressReverseConverter(Converter<AddressData, AddressModel> addressReverseConverter) {
        this.addressReverseConverter = addressReverseConverter;
    }
}


