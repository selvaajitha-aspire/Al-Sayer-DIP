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
            "SELECT {A.pk} FROM {" + ServiceRequestModel._TYPECODE + " as A }";

    private String getBaseQuery(){
        return SERVICE_REQUEST_BASE_QUERY;
    }

    private String getServiceRequestsByStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {ServiceRequest as A ")
                .append(" JOIN ServiceStatus as B on {B.pk} = {A."+ServiceRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {B.code} = ?status");
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
        builder.append(" SELECT {A.pk} FROM {ServiceRequest as A ")
                .append(" JOIN Customer as C on {C.pk} = {A."+ServiceRequestModel.CUSTOMER+"}} ")
                .append(" WHERE ")
                .append(" {C.pk} = ?customerID");
        return builder.toString();
    }

    private String getServiceRequestsByCustomerIDAndStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {ServiceRequest as A ")
                .append(" JOIN Customer as B on {B.pk} = {A."+ServiceRequestModel.CUSTOMER+"} ")
                .append(" JOIN ServiceStatus as C on {C.pk} = {A."+ServiceRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {B.pk} = ?customerID")
                .append(" AND {C.code} = ?status");
        return builder.toString();
    }

    private String getServiceRequestsByVehicleIDQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {ServiceRequest as A ")
                .append(" JOIN Vehicle as B on {B.pk} = {A."+ServiceRequestModel.VEHICLE+"}} ")
                .append(" WHERE ")
                .append(" {B.pk} = ?vehicleID");
        return builder.toString();
    }

    private String getServiceRequestsByVehicleIDAndStatusQuery(){
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT {A.pk} FROM {ServiceRequest as A ")
                .append(" JOIN Vehicle as B on {B.pk} = {A."+ServiceRequestModel.VEHICLE+"} ")
                .append(" JOIN ServiceStatus as C on {C.pk} = {A."+ServiceRequestModel.STATUS+"}} ")
                .append(" WHERE ")
                .append(" {B.pk} = ?vehicleID")
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
    public List<ServiceRequestModel> getAllServiceRequests() {
        return getServiceRequests(getBaseQuery(),null);
    }

    @Override
    public List<ServiceRequestModel> getServiceRequestsByStatus(String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("status",status);
        return getServiceRequests(getServiceRequestsByStatusQuery(),params);
    }

    @Override
    public ServiceRequestModel getServiceRequestByUID(String pk) {
        Map<String,Object> params = new HashMap<>();
        params.put("pk",pk);
        return getServiceRequest(getServiceRequestsByUIDQuery(),params);
    }

    @Override
    public List<ServiceRequestModel> getServiceRequestsByCustomerId(String customerID) {
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customerID);
        return getServiceRequests(getServiceRequestsByCustomerIDQuery(),params);
    }

    @Override
    public List<ServiceRequestModel> getServiceRequestsByCustomerIdAndStatus(String customerID, String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("customerID",customerID);
        params.put("status",status);
        return getServiceRequests(getServiceRequestsByCustomerIDAndStatusQuery()
                ,params);
    }

    @Override
    public List<ServiceRequestModel> getServiceRequestsByVehicleId(String vehicleID) {
        Map<String,Object> params = new HashMap<>();
        params.put("vehicleID",vehicleID);
        return getServiceRequests(getServiceRequestsByVehicleIDQuery()
                ,params);
    }

    @Override
    public List<ServiceRequestModel> getServiceRequestsByVehicleIdAndStatus(String vehicleID, String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("vehicleID",vehicleID);
        params.put("status",status);
        return getServiceRequests(getServiceRequestsByVehicleIDAndStatusQuery()
                ,params);
    }

    private FlexibleSearchQuery getFlexiQueryInstance(String query, Map<String,Object> params){
        if(null == query || "".equals(query)){
            return null;
        }
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

    private List<ServiceRequestModel> getServiceRequests(String queryStr,Map<String,Object> params) {
        try{
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr,params);
            final SearchResult<ServiceRequestModel> result = getFlexibleSearchService().search(query);
            return (result.getResult()!=null && !result.getResult().isEmpty())?result.getResult():null;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private ServiceRequestModel getServiceRequest(String queryStr,Map<String,Object> params) {
        try {
            final FlexibleSearchQuery query = getFlexiQueryInstance(queryStr, params);
            final SearchResult<ServiceRequestModel> result = getFlexibleSearchService().search(query);
            return (result.getResult() != null && !result.getResult().isEmpty()) ? result.getResult().get(0) : null;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
