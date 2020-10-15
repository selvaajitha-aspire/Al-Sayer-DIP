package com.alsayer.core.roadsideassistance.daos.impl;

import com.alsayer.core.event.rsa.ServiceRequestStartEvent;
import com.alsayer.core.model.DriverDetailsModel;
import com.alsayer.core.model.ServiceRequestModel;
import com.alsayer.core.model.ServiceRequestProcessModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.roadsideassistance.daos.RoadSideAssistanceDao;
import de.hybris.platform.servicelayer.event.EventService;
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

    private EventService eventService;

    private String lastServiceRequestUid;

    private FlexibleSearchService flexibleSearchService;

    private static final String DRIVER_DETAILS_QUERY= "SELECT {s.pk}"
            + " FROM {" + ServiceRequestModel._TYPECODE + " as s}"
            + " WHERE {s."+ServiceRequestModel.UID + "} = ?uid";

    final StringBuilder builder = new StringBuilder(DRIVER_DETAILS_QUERY);



    @Override
    public boolean saveServiceRequestinDB(ServiceRequestModel serviceRequest) {
        Boolean saved=true;
        try {
            lastServiceRequestUid=serviceRequest.getUid();
           getModelService().save(serviceRequest);
            ServiceRequestProcessModel process= new ServiceRequestProcessModel();
            process.setServiceRequest(serviceRequest);
            process.setServiceState(serviceRequest.getStatus());
            getEventService().publishEvent(new ServiceRequestStartEvent(process));
        }
        catch (final Exception e)
        {
            saved=false;
            LOG.error("Exception in saving the Service Request", e);
            return saved;
        }
        return saved;
    }


    /**
     * @return the Driver Details
     * @throws NullPointerException if no Service Request with the specified UID is found
     */
    @Override
    public DriverDetailsModel getDriverDeatailsFromServiceRequest(){
        final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        query.addQueryParameter("uid", lastServiceRequestUid);
        final SearchResult<ServiceRequestModel> result = getFlexibleSearchService().search(query);
        return result.getResult()!=null?result.getResult().get(0).getDriverDetails():null;
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
