package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class VehicleDataReversePopulator implements Populator<VehicleData, VehicleModel> {
    @Override
    public void populate(VehicleData vehicleData, VehicleModel vehicleModel) throws ConversionException {

        vehicleModel.setUid(vehicleData.getUid());
        vehicleModel.setModel(vehicleData.getModel());
        vehicleModel.setChassisno(vehicleData.getChassisno());
    }
}
