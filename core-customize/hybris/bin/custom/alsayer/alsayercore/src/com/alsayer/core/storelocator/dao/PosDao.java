package com.alsayer.core.storelocator.dao;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

public interface PosDao {
    public List<PointOfServiceModel> getAllServiceCenterPOS();
}
