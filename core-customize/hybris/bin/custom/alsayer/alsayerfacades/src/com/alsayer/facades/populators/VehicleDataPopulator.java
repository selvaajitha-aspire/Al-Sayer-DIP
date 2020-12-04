package com.alsayer.facades.populators;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.model.WarrantyModel;
import com.alsayer.facades.data.VehicleData;
import com.alsayer.facades.data.WarrantyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public class VehicleDataPopulator implements Populator<VehicleModel, VehicleData> {
    @Override
    public void populate(VehicleModel source, VehicleData target) throws ConversionException {
        if(source!=null){
            target.setPlateNumber(source.getPlateNumber());
            target.setModline(source.getModline());
            target.setModyear(source.getModyear());
            target.setStatus(source.getStatus());
            target.setChassisNumber(source.getChassisNumber());
            List<WarrantyData> warrantyDataList=new LinkedList<>();
            if(CollectionUtils.isNotEmpty(source.getWarranties())){
                for(WarrantyModel warranty: source.getWarranties()){
                    WarrantyData data=new WarrantyData();
                    data.setWarrantyType(warranty.getWarrantyType());
                    data.setWarrantyExpiryDate(warranty.getWarrantyExpiryDate());
                    warrantyDataList.add(data);
                }
                target.setWarranties(warrantyDataList);
            }
        }
    }
}
