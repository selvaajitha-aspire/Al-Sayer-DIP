package com.alsayer.facades.populators;

import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.facades.data.DriverDetailsData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class DriverDetailsPopulator implements Populator<DriverDetailsModel, DriverDetailsData> {

    @Override
    public void populate(DriverDetailsModel source, DriverDetailsData target) throws ConversionException {
        if(source!=null){
            target.setName(source.getName());
            target.setLatitude(source.getLatitude());
            target.setLongitude(source.getLongitude());
        }
    }
}
