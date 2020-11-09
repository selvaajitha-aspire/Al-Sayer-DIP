package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.facades.data.VehicleData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class VehicleDataReversePopulator implements Populator<VehicleData, VehicleModel> {
    @Override
    public void populate(VehicleData vehicleData, VehicleModel vehicleModel) throws ConversionException {

        vehicleModel.setChassisNumber(vehicleData.getChassisNumber());
        vehicleModel.setStatus(vehicleData.getStatus());
        vehicleModel.setModline(vehicleData.getModline());
        vehicleModel.setModyear(vehicleData.getModyear());
        vehicleModel.setPlateNumber(vehicleModel.getPlateNumber());
        vehicleModel.setWarrantyType(vehicleData.getWarrantyType());
        vehicleModel.setWarrantyExpiryDate(vehicleData.getWarrantyExpiryDate());
    }
}
