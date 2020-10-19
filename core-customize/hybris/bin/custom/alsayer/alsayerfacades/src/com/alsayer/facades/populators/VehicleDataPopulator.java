package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class VehicleDataPopulator implements Populator<VehicleModel, VehicleData> {
    @Override
    public void populate(VehicleModel source, VehicleData target) throws ConversionException {
        if(source!=null){
            target.setUid(source.getUid());
            target.setModel(source.getModel());
            target.setChassisNumber(source.getChassisNumber());
        }
    }
}
