package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class VehicleDataPopulator implements Populator<VehicleModel, VehicleData> {
    @Override
    public void populate(VehicleModel source, VehicleData target) throws ConversionException {
        if(source!=null){
            target.setPlateNumber(source.getPlateNumber());
            target.setModline(source.getModline());
            target.setModyear(source.getModyear());
            target.setStatus(source.getStatus());
            target.setChassisNumber(source.getChassisNumber());
        }
    }
}
