package com.alsayer.facades.populators;

import com.alsayer.core.model.WarrantyModel;
import com.alsayer.facades.data.WarrantyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class WarrantyReversePopulator implements Populator<WarrantyData, WarrantyModel> {

    @Override
    public void populate(WarrantyData source, WarrantyModel target) throws ConversionException {

        if(null == source || null == target){
            return;
        }

        target.setWarrantyType(source.getWarrantyType());
        target.setWarrantyExpiryDate(source.getWarrantyExpiryDate());
        target.setDescription(source.getDescription());

    }
}
