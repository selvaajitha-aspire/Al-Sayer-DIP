package com.alsayer.core.storelocator.service.impl;

import com.alsayer.core.storelocator.dao.PosDao;
import com.alsayer.core.storelocator.service.PosService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

public class DefaultPosService implements PosService {

    private PosDao posDao;

    public List<PointOfServiceModel> getServiceCenterPOS(){
        return getPosDao().getAllServiceCenterPOS();
    }

    public PosDao getPosDao() {
        return posDao;
    }

    public void setPosDao(PosDao posDao) {
        this.posDao = posDao;
    }
}
