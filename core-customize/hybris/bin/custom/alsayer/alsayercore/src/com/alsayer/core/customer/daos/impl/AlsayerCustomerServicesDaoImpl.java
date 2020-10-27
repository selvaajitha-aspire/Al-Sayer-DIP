package com.alsayer.core.customer.daos.impl;

import com.alsayer.core.customer.daos.AlsayerCustomerServicesDao;
import com.alsayer.core.model.CustomerAuthenticationModel;
import com.alsayer.core.model.VehicleModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AlsayerCustomerServicesDaoImpl extends AbstractItemDao implements AlsayerCustomerServicesDao {

    final static Logger LOG = LoggerFactory.getLogger(AlsayerCustomerServicesDaoImpl.class);

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;


    private AlsayerCustomerServicesDao alsayerCustomerServicesDao;

    private static final String CUSTOMER_REQUEST_QUERY = "SELECT {p.pk}"
            + " FROM {" + CustomerAuthenticationModel._TYPECODE + " as p}"
            + " WHERE {p."+ CustomerAuthenticationModel.JSESSIONID+ "} = ?uid";

    final StringBuilder builder = new StringBuilder(CUSTOMER_REQUEST_QUERY);



    @Override
    public CustomerAuthenticationModel getSavedOtp(String civilId) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        query.addQueryParameter("uid", "s4229810032448");
        System.out.println(query);
        final SearchResult<CustomerAuthenticationModel> result = getFlexibleSearchService().search(query);
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
