package com.alsayer.core.vehicles.daos.impl;

import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.vehicles.daos.MyVehiclesDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMyVehiclesDao extends AbstractItemDao implements MyVehiclesDao {

    final static Logger LOG = LoggerFactory.getLogger(DefaultMyVehiclesDao.class);

    private FlexibleSearchService flexibleSearchService;

    private static final String MY_VEHICLE_QUERY= "SELECT {v.pk}"
            + " FROM {" + VehicleModel._TYPECODE + " as v}"
            + " WHERE {v."+VehicleModel.UID + "} = ?uid";

    final StringBuilder builder = new StringBuilder(MY_VEHICLE_QUERY);

    /**
     * @param vehicleUid the UID of the Product
     * @return the Product
     * @throws NullPointerException if no Vehicle with the specified UID is found
     */
    @Override
    public VehicleModel getVehicle(final String vehicleUid){
        final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString());
        query.addQueryParameter("uid", vehicleUid);
        final SearchResult<VehicleModel> result = getFlexibleSearchService().search(query);
        return result.getResult()!=null?result.getResult().get(0):null;
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
