package com.alsayer.core;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.facades.data.ServiceRequestData;

import javax.annotation.Resource;

public class AbstractService {

    @Resource
    private Converter<ServiceRequestModel,ServiceRequestData> serviceRequestConverter;

    @Resource
    private Converter<ServiceRequestData, ServiceRequestModel> serviceRequestReverseConverter;

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
