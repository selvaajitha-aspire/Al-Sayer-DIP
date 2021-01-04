package com.alsayer.core.roadsideassistance.daos.impl;

import com.alsayer.core.event.rsa.RsaRequestStartEvent;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.RsaRequestProcessModel;
import com.alsayer.core.roadsideassistance.daos.RoadSideAssistanceDao;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRoadSideAssistanceDao extends AbstractItemDao implements RoadSideAssistanceDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultRoadSideAssistanceDao.class);

    private ModelService modelService;

    private EventService eventService;

    private FlexibleSearchService flexibleSearchService;

    private static final String DRIVER_DETAILS_QUERY= "SELECT {s.pk}"
            + " FROM {" + RsaRequestModel._TYPECODE + " as s}"
            + " WHERE {s."+RsaRequestModel.UID + "} = ?uid";

    final StringBuilder builder = new StringBuilder(DRIVER_DETAILS_QUERY);



    @Override
    public boolean saveServiceRequestinDB(final RsaRequestModel serviceRequest) {
        boolean saved=true;
        try {
           getModelService().save(serviceRequest);
            final RsaRequestProcessModel process= new RsaRequestProcessModel();
            process.setRsaRequest(serviceRequest);
            process.setServiceState(serviceRequest.getStatus());
            getEventService().publishEvent(new RsaRequestStartEvent(process));
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
    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    @Override
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
