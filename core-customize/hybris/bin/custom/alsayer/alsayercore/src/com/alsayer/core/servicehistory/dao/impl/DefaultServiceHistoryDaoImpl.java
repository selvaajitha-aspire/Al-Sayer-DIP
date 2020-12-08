package com.alsayer.core.servicehistory.dao.impl;

import com.alsayer.core.jalo.ServiceHistory;
import com.alsayer.core.model.ServiceHistoryModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.servicehistory.dao.ServiceHistoryDao;
import com.alsayer.core.servicerequest.dao.impl.DefaultRsaRequestDao;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultServiceHistoryDaoImpl implements ServiceHistoryDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultRsaRequestDao.class);

    private ModelService modelService;

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    private FlexibleSearchService flexibleSearchService;

    private String getServiceHistoryByChassisAndCustomerQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {" + ServiceHistoryModel._TYPECODE + " as A ")
                .append(" JOIN "+ VehicleModel._TYPECODE +" as B on {B.pk} = {A."+ ServiceHistoryModel.VEHICLE+"} } ")
                .append(" WHERE ")
                .append(" {B." + VehicleModel.CHASSISNUMBER + "} = ?chassisNo")
                .append(" AND {B.").append(VehicleModel.CUSTOMER).append("} = ?customerID");
        return builder.toString();
    }
    
    @Override
    public List<ServiceHistoryModel> getServiceHistoryByChassisAndCustomer(String chassisNo, CustomerModel customer) {
        Map<String,Object> params = new HashMap<>();
        params.put("chassisNo",chassisNo);
        params.put("customerID",customer);
        return getServiceHistoryList(getServiceHistoryByChassisAndCustomerQuery()
                ,params);
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

    private List<ServiceHistoryModel> getServiceHistoryList(String queryStr, Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<ServiceHistoryModel> result = getFlexibleSearchService().search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ServiceHistoryModel getServiceHistory(String queryStr,Map<String,Object> params) {
        try {
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr, params);
            final SearchResult<ServiceHistoryModel> result = getFlexibleSearchService().search(query);
            return (result.getResult() != null && !result.getResult().isEmpty()) ? result.getResult().get(0) : null;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
