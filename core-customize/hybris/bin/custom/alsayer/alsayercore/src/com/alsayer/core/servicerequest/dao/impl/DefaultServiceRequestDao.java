package com.alsayer.core.servicerequest.dao.impl;

import com.alsayer.core.jalo.ServiceRequest;
import com.alsayer.core.roadsideassistance.daos.impl.DefaultRoadSideAssistanceDao;
import com.alsayer.core.servicerequest.dao.ServiceRequestDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alsayer.core.model.ServiceRequestModel;

public class DefaultServiceRequestDao extends AbstractItemDao implements ServiceRequestDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultServiceRequestDao.class);

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;

    private static final String SERVICE_REQUEST_BASE_QUERY=
            "SELECT * FROM {" + ServiceRequestModel._TYPECODE + " as E }";

    private String getBaseQuery(){
        return SERVICE_REQUEST_BASE_QUERY;
    }

    private String getServiceRequestsByStatusQuery(){
        StringBuilder builder = new StringBuilder(getBaseQuery());
        builder.append(" WHERE ")
                .append(" E."+ServiceRequestModel.STATUS+" = ?status");
        return builder.toString();
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

    @Override
    public List<ServiceRequestModel> getAllServiceRequests() {
        return getServiceRequests(getBaseQuery());
    }

    private FlexibleSearchQuery getFlexiQueryInstance(String query, Map<String,Object> params){
        final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
        if( null != params && !params.isEmpty()){
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                flexiQuery.addQueryParameter(key,value);
            }
        }
        return flexiQuery;
    }

    private List<ServiceRequestModel> getServiceRequests(String queryStr) {
        final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,null);
        final SearchResult<ServiceRequestModel> result = getFlexibleSearchService().search(query);
        return result.getResult()!=null?result.getResult():null;
    }

    private ServiceRequestModel getServiceRequest(String queryStr,Map<String,Object> params) {
        final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
        final SearchResult<ServiceRequestModel> result = getFlexibleSearchService().search(query);
        return result.getResult()!=null?result.getResult().get(0):null;
    }
}
