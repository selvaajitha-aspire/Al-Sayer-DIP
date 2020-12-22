package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.data.WarrantyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class VehicleDataPopulator implements Populator<VehicleModel, VehicleData> {

    Converter<WarrantyModel,WarrantyData> warrantyConverter;

    @Override
    public void populate(VehicleModel source, VehicleData target) throws ConversionException {
        if(source!=null){
            target.setPlateNumber(source.getPlateNumber());
            target.setModline(source.getModline());
            target.setModyear(source.getModyear());
            target.setStatus(source.getStatus());
            target.setChassisNumber(source.getChassisNumber());
            if(CollectionUtils.isNotEmpty(source.getWarranties())){
                target.setWarranties(warrantyConverter.convertAll(source.getWarranties()));
            }
        }
    }

    public Converter<WarrantyModel, WarrantyData> getWarrantyConverter() {
        return warrantyConverter;
    }

    public void setWarrantyConverter(Converter<WarrantyModel, WarrantyData> warrantyConverter) {
        this.warrantyConverter = warrantyConverter;
    }
}
