package com.alsayer.facades.insurance.impl;

import com.alsayer.core.insurance.services.InsurancesService;
import com.alsayer.core.model.InsuranceModel;
import com.alsayer.facades.data.InsuranceData;
import com.alsayer.facades.insurance.InsurancesFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;


public class DefaultInsurancesFacade implements InsurancesFacade {


    private InsurancesService insurancesService;

    private Converter<InsuranceModel, InsuranceData> insuranceDataConverter;

    @Override
    public List<InsuranceData> getInsurance(){
        List<InsuranceModel> vehicles  = getInsurancesService().getInsurances();
        return Converters.convertAll(vehicles, getInsuranceDataConverter());

    }

    public InsurancesService getInsurancesService() {
        return insurancesService;
    }

    public void setInsurancesService(InsurancesService insurancesService) {
        this.insurancesService = insurancesService;
    }

    public Converter<InsuranceModel, InsuranceData> getInsuranceDataConverter() {
        return insuranceDataConverter;
    }

    public void setInsuranceDataConverter(Converter<InsuranceModel, InsuranceData> insuranceDataConverter) {
        this.insuranceDataConverter = insuranceDataConverter;
    }
}
