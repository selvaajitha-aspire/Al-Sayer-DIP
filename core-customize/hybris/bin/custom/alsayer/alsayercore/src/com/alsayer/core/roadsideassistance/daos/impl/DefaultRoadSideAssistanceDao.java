package com.alsayer.core.roadsideassistance.daos.impl;

import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.roadsideassistance.daos.RoadSideAssistanceDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRoadSideAssistanceDao extends AbstractItemDao implements RoadSideAssistanceDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultRoadSideAssistanceDao.class);

    private ModelService modelService;

    @Override
    public boolean saveServiceRequestinDB(ServiceRequestModel serviceRequest) {
        Boolean saved=true;
        try {
           getModelService().save(serviceRequest);
        }
        catch (final Exception e)
        {
            saved=false;
            LOG.error("Exception in saving the Service Request", e);
            return saved;
        }
        return saved;
    }

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

}
