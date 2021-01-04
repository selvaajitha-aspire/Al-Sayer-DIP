package com.alsayer.core.insurance.daos.impl;

import com.alsayer.core.insurance.daos.InsurancesDao;
import com.alsayer.core.model.InsuranceModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultInsurancesDao extends AbstractItemDao implements InsurancesDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultInsurancesDao.class);

    private FlexibleSearchService flexibleSearchService;

    private String getInsuranceByChassisAndCustomerQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {" + InsuranceModel._TYPECODE + " as A ")
                .append(" JOIN "+ CustomerModel._TYPECODE +" as B on {B.pk} = {A."+ InsuranceModel.USER+"} } ")
                .append(" WHERE ")
                .append(" {B.").append(CustomerModel.PK).append("} = ?customer");
        return builder.toString();
    }

    @Override
    public List<InsuranceModel> getInsurancesByCustomer(CustomerModel customer) {
        Map<String,Object> params = new HashMap<>();
        params.put("customer",customer);
        return getInsuranceList(getInsuranceByChassisAndCustomerQuery()
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

    private List<InsuranceModel> getInsuranceList(String queryStr, Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<InsuranceModel> result = getFlexibleSearchService().search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
