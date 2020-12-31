package com.alsayer.facades.populators;

import com.alsayer.core.model.InsuranceModel;
import com.alsayer.facades.data.InsuranceData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class InsuranceDataPopulator  implements Populator<InsuranceModel, InsuranceData> {
    @Override
    public void populate(InsuranceModel insuranceModel, InsuranceData insuranceData) throws ConversionException {

        if(insuranceModel!=null){
            insuranceData.setDateOfExpiry(insuranceModel.getDateOfExpiry());
            insuranceData.setDateOfIssue(insuranceModel.getDateOfIssue());
            insuranceData.setCoverageInfo(insuranceModel.getCoverageInfo());
            insuranceData.setPolicyNumber(insuranceModel.getPolicyNumber());
        }
    }
}
