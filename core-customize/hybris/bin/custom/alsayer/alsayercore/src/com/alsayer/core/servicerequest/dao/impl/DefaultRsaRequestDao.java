package com.alsayer.core.servicerequest.dao.impl;

import com.alsayer.core.enums.ServiceStatus;
import com.alsayer.core.model.RsaRequestModel;
import com.alsayer.core.model.VehicleModel;
import com.alsayer.core.servicerequest.dao.RsaRequestDao;
import de.hybris.platform.core.model.user.CustomerModel;
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

public class DefaultRsaRequestDao extends AbstractItemDao implements RsaRequestDao
{

    final static Logger LOG = LoggerFactory.getLogger(DefaultRsaRequestDao.class);

    private ModelService modelService;

    private FlexibleSearchService flexibleSearchService;



    private static final String SERVICE_REQUEST_BASE_QUERY=
            "SELECT {A.pk} FROM {" + RsaRequestModel._TYPECODE + " as A }";

    private String getBaseQuery(){
        return SERVICE_REQUEST_BASE_QUERY;
    }

    private String getServiceRequestsByStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {RsaRequest as A ")
                .append(" JOIN "+ ServiceStatus._TYPECODE +" as C on {C.pk} = {A."+RsaRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {C.code} = ?status");
        return builder.toString();
    }

    private String getServiceRequestsByUIDQuery(){
        StringBuilder builder = new StringBuilder(getBaseQuery());
        builder.append(" WHERE ")
                .append(" {A.pk} = ?pk");
        return builder.toString();
    }

    private String getServiceRequestsByCustomerIDQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {" + RsaRequestModel._TYPECODE + " as A} ")
                .append(" WHERE ")
                .append(" {").append(RsaRequestModel.CUSTOMER).append("} = ?customerID")
                .append(" ORDER BY {").append(RsaRequestModel.CREATIONTIME).append("} DESC");
        return builder.toString();
    }

    private String getServiceRequestsByCustomerIDAndStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {" + RsaRequestModel._TYPECODE + " as A ")
                .append(" JOIN "+ ServiceStatus._TYPECODE +" as C on {C.pk} = {A."+RsaRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {A.").append(RsaRequestModel.CUSTOMER).append("} = ?customerID")
                .append(" AND {C.code} IN (?statuses)");
        return builder.toString();
    }

    private String getServiceRequestsByVehicleIDQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {A.pk} FROM {RsaRequest as A ")
                .append(" JOIN "+ VehicleModel._TYPECODE +" as B on {B.pk} = {A."+RsaRequestModel.VEHICLE+"} ")
                .append(" WHERE ")
                .append(" {B.pk} = ?vehicleID");
        return builder.toString();
    }

    private String getServiceRequestsByVehicleIDAndStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {RsaRequest as A ")
                .append(" JOIN "+ VehicleModel._TYPECODE +" as B on {B.pk} = {A."+RsaRequestModel.VEHICLE+"} ")
                .append(" JOIN "+ ServiceStatus._TYPECODE +" as C on {C.pk} = {A."+RsaRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {B." + VehicleModel.PK + "} =?vehicleID")
                .append(" AND {C.code} = ?status");
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
    public List<RsaRequestModel> getAllRsaRequests() {
        return getRsaRequests(getBaseQuery(),null);
    }

    @Override
    public List<RsaRequestModel> getRsaRequestsByStatus(String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("status",status);
        return getRsaRequests(getServiceRequestsByStatusQuery(),params);
    }

    @Override
    public RsaRequestModel getRsaRequestByUID(String pk) {
        Map<String,Object> params = new HashMap<>();
        params.put("pk",pk);
        return getRsaRequest(getServiceRequestsByUIDQuery(),params);
    }

    @Override
    public List<RsaRequestModel> getRsaRequestsByCustomerId(CustomerModel customer) {
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customer);
        return getRsaRequests(getServiceRequestsByCustomerIDQuery(),params);
    }

    @Override
    public List<RsaRequestModel> getRsaRequestsByCustomerIdAndStatus(CustomerModel customer, List<String> statuses) {
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customer);
        params.put("statuses",statuses);
        return getRsaRequests(getServiceRequestsByCustomerIDAndStatusQuery()
                ,params);
    }

    @Override
    public List<RsaRequestModel> getRsaRequestsByVehicleId(String vehicleID) {
        Map<String,Object> params = new HashMap<>();
        params.put("vehicleID",vehicleID);
        return getRsaRequests(getServiceRequestsByVehicleIDQuery()
                ,params);
    }

    @Override
    public List<RsaRequestModel> getRsaRequestsByVehicleIdAndStatus(String vehicleID, String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("vehicleID",vehicleID);
        params.put("status",status);
        return getRsaRequests(getServiceRequestsByVehicleIDAndStatusQuery()
                ,params);
    }

    private FlexibleSearchQuery getFlexiQueryInstance(String query, Map<String,Object> params){
        if(null == query || "".equals(query)){
            return null;
        }
        final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
        if( null != params && !params.isEmpty()){
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                String key = entry.getKey();
//                Object value = entry.getValue();
//                flexiQuery.addQueryParameter(key,value);
//            }
            flexiQuery.addQueryParameters(params);
        }
        return flexiQuery;
    }

    private List<RsaRequestModel> getRsaRequests(String queryStr,Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<RsaRequestModel> result = getFlexibleSearchService().search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            LOG.debug(ex.getMessage());
            return null;
        }
    }

    private RsaRequestModel getRsaRequest(String queryStr,Map<String,Object> params) {
        try {
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr, params);
            final SearchResult<RsaRequestModel> result = getFlexibleSearchService().search(query);
            return (result.getResult() != null && !result.getResult().isEmpty()) ? result.getResult().get(0) : null;
        }catch (Exception ex){
            LOG.debug(ex.getMessage());
            return null;
        }
    }
}
