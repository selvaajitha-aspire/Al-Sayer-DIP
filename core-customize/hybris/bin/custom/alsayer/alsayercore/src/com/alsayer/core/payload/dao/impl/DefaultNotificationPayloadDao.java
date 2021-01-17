package com.alsayer.core.payload.dao.impl;

import com.alsayer.core.model.NotificationPayloadModel;
import com.alsayer.core.payload.dao.NotificationPayloadDao;
import com.alsayer.core.roadsideassistance.daos.impl.DefaultRoadSideAssistanceDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNotificationPayloadDao implements NotificationPayloadDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultRoadSideAssistanceDao.class);

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;

    private String getAllNotificationPayloadsQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM { "+NotificationPayloadModel._TYPECODE+" as A } ");
        return builder.toString();
    }

    private String getAllNotificationPayloadByCodeQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM { "+NotificationPayloadModel._TYPECODE+" as A } ")
        .append(" WHERE {A."+ NotificationPayloadModel.TEMPLATECODE +"} = ?templateCode");
        return builder.toString();
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public List<NotificationPayloadModel> getAllNotificationPayloads(){
        return getNotificationPayloads(getAllNotificationPayloadsQuery(),null);
    }

    @Override
    public NotificationPayloadModel getNotificationPayloadByCode(String templateCode){
        Map<String,Object> params = new HashMap<>();
        params.put("templateCode",templateCode);
        return getNotificationPayloadByCode(params);
    }

    @Override
    public NotificationPayloadModel getNotificationPayloadByCode(Map<String,Object> params){
        return getNotificationPayload(getAllNotificationPayloadByCodeQuery(),params);
    }

    private FlexibleSearchQuery getFlexiQueryInstance(String query, Map<String,Object> params){
        if(null == query || "".equals(query)){
            return null;
        }
        final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
        if( null != params && !params.isEmpty()){
            flexiQuery.addQueryParameters(params);
        }
        return flexiQuery;
    }

    private List<NotificationPayloadModel> getNotificationPayloads(String queryStr, Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<NotificationPayloadModel> result = flexibleSearchService.search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            LOG.debug(ex.getMessage());
            return null;
        }
    }

    private NotificationPayloadModel getNotificationPayload(String queryStr,Map<String,Object> params) {
        try {
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr, params);
            final SearchResult<NotificationPayloadModel> result = flexibleSearchService.search(query);
            return (result.getResult() != null && !result.getResult().isEmpty()) ? result.getResult().get(0) : null;
        }catch (Exception ex){
            LOG.debug(ex.getMessage());
            return null;
        }
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
    
}
