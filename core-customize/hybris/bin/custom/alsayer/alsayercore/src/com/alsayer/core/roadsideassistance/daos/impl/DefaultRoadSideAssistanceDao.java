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

    private FlexibleSearchService flexibleSearchService;

    private static final String SERVICE_REQUEST_QUERY= "SELECT {v.pk}"
            + " FROM {" + VehicleModel._TYPECODE + " as v}"
            + " WHERE {v."+VehicleModel.UID + "} = ?uid";

    final StringBuilder builder = new StringBuilder(SERVICE_REQUEST_QUERY);

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

    /**
     * @param vehicleUid the UID of the Product
     * @return the Product
     * @throws NullPointerException if no Vehicle with the specified UID is found
     */
    public VehicleModel getVehicle(final String vehicleUid){
        final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        query.addQueryParameter("uid", vehicleUid);
        final SearchResult<VehicleModel> result = getFlexibleSearchService().search(query);
        return result.getResult()!=null?result.getResult().get(0):null;
    }

    @Override
    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    @Override
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
