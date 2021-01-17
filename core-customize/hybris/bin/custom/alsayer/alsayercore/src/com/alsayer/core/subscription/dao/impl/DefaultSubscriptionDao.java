package com.alsayer.core.subscription.dao.impl;

import com.alsayer.core.model.SubscriptionModel;
import com.alsayer.core.roadsideassistance.daos.impl.DefaultRoadSideAssistanceDao;
import com.alsayer.core.subscription.dao.SubscriptionDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DefaultSubscriptionDao implements SubscriptionDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultRoadSideAssistanceDao.class);

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;

    private String getAllSubscriptionsQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM { Subscription as A } ");
        return builder.toString();
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public boolean save(final SubscriptionModel subscription){
        boolean saved = true;
        try{
            modelService.save(subscription);
        }catch(Exception ex){
            saved=false;
            LOG.error(ex.getMessage());
        }
        return saved;
    }

    @Override
    public List<SubscriptionModel> getAllSubscriptions(){
        return getSubscriptions(getAllSubscriptionsQuery(),null);
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

    private List<SubscriptionModel> getSubscriptions(String queryStr, Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<SubscriptionModel> result = flexibleSearchService.search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            LOG.debug(ex.getMessage());
            return null;
        }
    }

    private SubscriptionModel getSubscription(String queryStr,Map<String,Object> params) {
        try {
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr, params);
            final SearchResult<SubscriptionModel> result = flexibleSearchService.search(query);
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
