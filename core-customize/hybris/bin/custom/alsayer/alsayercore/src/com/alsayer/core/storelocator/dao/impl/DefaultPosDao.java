package com.alsayer.core.storelocator.dao.impl;

import com.alsayer.core.storelocator.dao.PosDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultPosDao extends AbstractItemDao implements PosDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultPosDao.class);

    private FlexibleSearchService flexibleSearchService;

    private static final String SERVICE_CENTER_QUERY= "SELECT {pos.pk}"
            + " FROM {" + PointOfServiceModel._TYPECODE + " as pos}"
            + " WHERE {pos."+PointOfServiceModel.ISSERVICECENTER + "} = ?isSeviceCenter";

    final StringBuilder builder = new StringBuilder(SERVICE_CENTER_QUERY);


    @Override
    public List<PointOfServiceModel> getAllServiceCenterPOS(){
        final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        query.addQueryParameter("isSeviceCenter", Boolean.TRUE);
        final SearchResult<PointOfServiceModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
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
