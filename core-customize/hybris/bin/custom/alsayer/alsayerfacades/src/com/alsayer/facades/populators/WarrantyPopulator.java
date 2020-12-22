package com.alsayer.facades.populators;

import com.alsayer.core.model.WarrantyModel;
import com.alsayer.facades.data.WarrantyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class WarrantyPopulator implements Populator<WarrantyModel, WarrantyData> {


    @Override
    public void populate(WarrantyModel source, WarrantyData target) throws ConversionException {
        if(null == source || null == target){
            return;
        }

        target.setWarrantyType(source.getWarrantyType());
        target.setWarrantyExpiryDate(source.getWarrantyExpiryDate());
        target.setDescription(source.getDescription());

    }
}
