package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.data.WarrantyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.collections.CollectionUtils;

public class VehicleDataReversePopulator implements Populator<VehicleData, VehicleModel> {

    Converter<WarrantyData, WarrantyModel> warrantyReverseConverter;

    @Override
    public void populate(VehicleData vehicleData, VehicleModel vehicleModel) throws ConversionException {

        vehicleModel.setChassisNumber(vehicleData.getChassisNumber());
        vehicleModel.setStatus(vehicleData.getStatus());
        vehicleModel.setModline(vehicleData.getModline());
        vehicleModel.setModyear(vehicleData.getModyear());
        vehicleModel.setPlateNumber(vehicleData.getPlateNumber());
        vehicleModel.setBrand(vehicleData.getBrand());

        if(CollectionUtils.isNotEmpty(vehicleData.getWarranties())){
            vehicleModel.setWarranties(warrantyReverseConverter.convertAll(vehicleData.getWarranties()));
        }

    }

    public Converter<WarrantyData, WarrantyModel> getWarrantyReverseConverter() {
        return warrantyReverseConverter;
    }

    public void setWarrantyReverseConverter(Converter<WarrantyData, WarrantyModel> warrantyReverseConverter) {
        this.warrantyReverseConverter = warrantyReverseConverter;
    }
}
