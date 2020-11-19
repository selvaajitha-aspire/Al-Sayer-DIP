package com.alsayer.core.servicerequest.service.impl;

import com.alsayer.core.servicerequest.dao.RsaRequestDao;
import com.alsayer.core.servicerequest.service.RsaRequestService;
import com.alsayer.facades.data.RsaRequestData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.alsayer.core.model.RsaRequestModel;
import de.hybris.platform.servicelayer.user.UserService;
import javax.annotation.Resource;
import java.util.List;

public class DefaultRsaRequestService implements RsaRequestService
{

    @Resource
    private Converter<RsaRequestModel,RsaRequestData> rsaRequestConverter;

    @Resource
    private Converter<RsaRequestData, RsaRequestModel> rsaRequestReverseConverter;

    private UserService userService;

    private RsaRequestDao rsaRequestDao;

    public RsaRequestDao getRsaRequestDao() {
        return rsaRequestDao;
    }

    public void setRsaRequestDao(RsaRequestDao rsaRequestDao) {
        this.rsaRequestDao = rsaRequestDao;
    }

    @Override
    public List<RsaRequestData> getAllRsaRequests() {
        return convertAllModel(getRsaRequestDao().getAllRsaRequests());
    }

    @Override
    public List<RsaRequestData> getRsaRequestsByStatus(String status) {
        return convertAllModel(getRsaRequestDao().getRsaRequestsByStatus(status));
    }

    @Override
    public RsaRequestData getRsaRequestByUID(String uid) {
        return convert(getRsaRequestDao().getRsaRequestByUID(uid));
    }

    @Override
    public List<RsaRequestData> getRsaRequestsByCustomerId()
    {
        final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
        return convertAllModel(getRsaRequestDao().getRsaRequestsByCustomerId(currentCustomer));
    }

    @Override
    public List<RsaRequestData> getRsaRequestsByCustomerIdAndStatus(List<String> statuses) {
        final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
        return convertAllModel(getRsaRequestDao().getRsaRequestsByCustomerIdAndStatus(currentCustomer,statuses));
    }

    @Override
    public List<RsaRequestData> getRsaRequestsByVehicleId(String vehicleID) {
        return convertAllModel(getRsaRequestDao().getRsaRequestsByVehicleId(vehicleID));
    }

    @Override
    public List<RsaRequestData> getRsaRequestsByVehicleIdAndStatus(String vehicleID, String status) {
        return convertAllModel(getRsaRequestDao().getRsaRequestsByVehicleIdAndStatus(vehicleID,status));
    }

    private Converter<RsaRequestModel, RsaRequestData> getRsaRequestConverter() {
        return rsaRequestConverter;
    }

    private void setRsaRequestConverter(Converter<RsaRequestModel, RsaRequestData> rsaRequestConverter) {
        this.rsaRequestConverter = rsaRequestConverter;
    }

    private Converter<RsaRequestData, RsaRequestModel> getRsaRequestReverseConverter() {
        return rsaRequestReverseConverter;
    }

    private void setRsaRequestReverseConverter(Converter<RsaRequestData, RsaRequestModel> rsaRequestReverseConverter) {
        this.rsaRequestReverseConverter = rsaRequestReverseConverter;
    }

    public RsaRequestModel convert(RsaRequestData data) {
        return getRsaRequestReverseConverter().convert(data);
    }

    public RsaRequestData convert(RsaRequestModel model) {
        return getRsaRequestConverter().convert(model);
    }

    public List<RsaRequestData> convertAllModel(List<RsaRequestModel> modelList) {
        return getRsaRequestConverter().convertAll(modelList);
    }

    public List<RsaRequestModel> convertAllDto(List<RsaRequestData> dataList) {
        return getRsaRequestReverseConverter().convertAll(dataList);
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }
}
