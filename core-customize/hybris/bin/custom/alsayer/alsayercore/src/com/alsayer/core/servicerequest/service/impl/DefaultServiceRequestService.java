package com.alsayer.core.servicerequest.service.impl;

import com.alsayer.core.servicerequest.dao.ServiceRequestDao;
import com.alsayer.core.servicerequest.service.ServiceRequestService;
import com.alsayer.facades.data.ServiceRequestData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.alsayer.core.model.ServiceRequestModel;


import javax.annotation.Resource;
import java.util.List;

public class DefaultServiceRequestService implements ServiceRequestService {

    @Resource
    private Converter<ServiceRequestModel,ServiceRequestData> serviceRequestConverter;

    @Resource
    private Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter;

    private ServiceRequestDao serviceRequestDao;

    public ServiceRequestDao getServiceRequestDao() {
        return serviceRequestDao;
    }

    public void setServiceRequestDao(ServiceRequestDao serviceRequestDao) {
        this.serviceRequestDao = serviceRequestDao;
    }

    @Override
    public List<ServiceRequestData> getAllServiceRequests() {
        return convertAllModel(getServiceRequestDao().getAllServiceRequests());
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByStatus(String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByStatus(status));
    }

    @Override
    public ServiceRequestData getServiceRequestByUID(String uid) {
        return convert(getServiceRequestDao().getServiceRequestByUID(uid));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByCustomerId(String customerID) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByCustomerId(customerID));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByCustomerIdAndStatus(String customerID, String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByCustomerIdAndStatus(customerID,status));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByVehicleId(String vehicleID) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByVehicleId(vehicleID));
    }

    @Override
    public List<ServiceRequestData> getServiceRequestsByVehicleIdAndStatus(String vehicleID, String status) {
        return convertAllModel(getServiceRequestDao().getServiceRequestsByVehicleIdAndStatus(vehicleID,status));
    }

    private Converter<ServiceRequestModel, ServiceRequestData> getServiceRequestConverter() {
        return serviceRequestConverter;
    }

    private void setServiceRequestConverter(Converter<ServiceRequestModel, ServiceRequestData> serviceRequestConverter) {
        this.serviceRequestConverter = serviceRequestConverter;
    }

    private Converter<ServiceRequestData, ServiceRequestModel> getServiceRequestReverseConverter() {
        return serviceRequestReverseConverter;
    }

    private void setServiceRequestReverseConverter(Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter) {
        this.serviceRequestReverseConverter = serviceRequestReverseConverter;
    }

    public ServiceRequestModel convert(ServiceRequestData data) {
        return getServiceRequestReverseConverter().convert(data);
    }

    public ServiceRequestData convert(ServiceRequestModel model) {
        return getServiceRequestConverter().convert(model);
    }

    public List<ServiceRequestData> convertAllModel(List<ServiceRequestModel> modelList) {
        return getServiceRequestConverter().convertAll(modelList);
    }

    public List<ServiceRequestModel> convertAllDto(List<ServiceRequestData> dataList) {
        return getServiceRequestReverseConverter().convertAll(dataList);
    }
}
