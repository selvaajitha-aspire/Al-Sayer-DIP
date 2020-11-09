package com.alsayer.facades.populators;

import com.alsayer.core.enums.IssueType;
import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.vehicles.services.MyVehiclesService;
import com.alsayer.facades.data.RsaRequestData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.UUID;

public class RsaRequestReversePopulator implements Populator<RsaRequestData, RsaRequestModel> {

    private UserService userService;

    private MyVehiclesService myVehiclesService;

    private I18NService i18nService;


    @Override
    public void populate(RsaRequestData serviceRequestData, RsaRequestModel serviceRequestModel) throws ConversionException {

        String chassisNumber=serviceRequestData.getVehicle().getChassisNumber();
        String issueType=serviceRequestData.getIssue();
        serviceRequestModel.setUid(UUID.randomUUID().toString());
        serviceRequestModel.setCustomer((CustomerModel) userService.getCurrentUser());
        serviceRequestModel.setVehicle(getVehicleByChassis(chassisNumber));
        serviceRequestModel.setStatus(ServiceStatus.STARTED);
        serviceRequestModel.setType(issueType.equalsIgnoreCase("RSA")?"RSA":"MUSAADA");

        if(issueType.equalsIgnoreCase("FLAT_TYRE")){
            serviceRequestModel.setIssue(IssueType.FLAT_TYRE);

        }else if(issueType.equalsIgnoreCase("OUT_OF_FUEL")){
            serviceRequestModel.setIssue((IssueType.OUT_OF_FUEL));
        }else{
            serviceRequestModel.setIssue((IssueType.DEAD_BATTERY));
        }
        serviceRequestModel.setLatitude(serviceRequestData.getLatitude());
        serviceRequestModel.setLongitude(serviceRequestData.getLongitude());
        serviceRequestModel.setNotes(serviceRequestData.getNotes(), getI18nService().getCurrentLocale());
        serviceRequestModel.setAttachments(serviceRequestData.getAttachments());
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
}
